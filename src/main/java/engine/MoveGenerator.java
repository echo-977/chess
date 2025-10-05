import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;

public class MoveGenerator {
    long checkRay;
    long pinRay;
    boolean doubleCheck;

    public MoveGenerator() {}

    /**
     * Generates all the moves for a given colour.
     *
     * @param position the position to generate moves in.
     * @return an integer array list of all the moves the colour can do.
     */
    public IntArrayList generateLegalMoves(Position position) {
        computeRays(position);
        King king = position.getBoard().findKing(position.getGameState().getTurn());
        if (doubleCheck) {
            IntArrayList legalMoves = new IntArrayList();
            king.generateMoves(position, legalMoves);
            return legalMoves;
        }
        int kingSquare = king.getSquare();
        IntArrayList moves = generatePseudoLegalMoves(position);
        int knightCheckSquare = knightCheckSearch(position);
        int pawnCheckSquare = pawnCheckSearch(position);
        boolean check = ((checkRay >> kingSquare) & 1) == 1 || knightCheckSquare != pawnCheckSquare;
        boolean pin = ((pinRay >> kingSquare) & 1) == 1;
        int enPassantTarget = position.getGameState().getEnPassantTarget();
        if (!check && !pin) {
            if (enPassantTarget != Squares.NONE) {
                for (IntIterator iterator = moves.iterator(); iterator.hasNext();) {
                    int move = iterator.nextInt();
                    int moveFlag = move >> MoveFlags.FLAG_SHIFT;
                    if ((moveFlag & MoveFlags.PROMOTION_BIT) !=  MoveFlags.PROMOTION_BIT &&
                            (moveFlag & MoveFlags.CAPTURE_BIT) == MoveFlags.CAPTURE_BIT &&
                            (moveFlag & MoveFlags.EN_PASSANT) == MoveFlags.EN_PASSANT) {
                        if (enPassantPinned(position, king, move & MoveFlags.SOURCE_MASK)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            return moves;
        }
        for (IntIterator iterator = moves.iterator(); iterator.hasNext();) {
            int move = iterator.nextInt();
            int source = (move & MoveFlags.SOURCE_MASK);
            int moveFlag = move >> MoveFlags.FLAG_SHIFT;
            if ((moveFlag & MoveFlags.PROMOTION_BIT) !=  MoveFlags.PROMOTION_BIT &&
                    (moveFlag & MoveFlags.EN_PASSANT) == MoveFlags.EN_PASSANT &&
            (moveFlag & MoveFlags.CAPTURE_BIT) == MoveFlags.CAPTURE_BIT){
                if (enPassantPinned(position, king, source)) {
                    iterator.remove();
                    continue;
                }
            }
            int destination = (move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT;
            if (pin && ((pinRay >> source) & 1) == 1) { //pin exists and the piece is pinned
                if (source == kingSquare) {
                    continue;
                }
                if (check) {  //there is a check so piece cannot move
                    iterator.remove();
                    continue;
                }
                if (((pinRay >> destination) & 1) == 0) { //destination is not on a pin ray so move is illegal
                    iterator.remove();
                    continue;
                }
                int sourceDirection = SquareMapUtils.findDirection(source, kingSquare);
                if (sourceDirection == 0 | sourceDirection != SquareMapUtils.findDirection(destination, kingSquare)) {
                    iterator.remove(); //destination square doesn't exist within the same ray so move is illegal
                    continue;
                }
            }
            if (check) {
                if ((destination == knightCheckSquare || destination == pawnCheckSquare)) {
                    continue; //move will capture the pawn or knight giving check and isn't pinned so is legal
                }
                if (position.getBoard().pieceSearch(source).getType() == PieceType.PAWN) {
                    int direction = (king.getColour() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
                    if (pawnCheckSquare + direction == enPassantTarget && destination == enPassantTarget) {
                        continue; //move is an en passant capture of the checking pawn
                    }
                }
                if (((checkRay >> destination) & 1L) == 0 && source != kingSquare) {
                    iterator.remove(); //there is a check in the position and the move doesn't block it so the move isn't legal
                }
            }
        }
        return moves;
    }

    public IntArrayList generatePseudoLegalMoves(Position position) {
        PieceColour turn = position.getGameState().getTurn();
        Piece[] pieces = position.getBoard().getPieces();
        IntArrayList moves = new IntArrayList();
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (pieces[squareIndex] != null && pieces[squareIndex].getColour() == turn) {
                pieces[squareIndex].generateMoves(position, moves);
            }
        }
        return moves;
    }

    /**
     * Computes check and pin rays, stored as a 64 bit long.
     * For the respective rays each bit refers to whether that square is along a check to the king or a pin
     * @param position the position to calculate the check and pin rays in.
     */
    public void computeRays(Position position) {
        checkRay = 0L;
        pinRay = 0L;
        doubleCheck = false;
        Board board = position.getBoard();
        PieceColour colour = position.getGameState().getTurn();
        long ray;
        int kingSquare = board.findKing(colour).getSquare();
        int currentSquare;
        Piece piece;
        boolean friendlyAlongRay;
        boolean check = false;
        for (int i = 0; i < ChessConstants.NUM_DIRECTIONS; i++) {
            friendlyAlongRay = false;
            ray = 1L << kingSquare;
            for (int j = 1; j <= MoveTables.DISTANCE_TO_EDGE[kingSquare][i]; j++) {
                currentSquare = kingSquare + j * ChessDirections.ALL[i];
                ray |= 1L << currentSquare;
                piece = board.pieceSearch(currentSquare);
                if (piece != null) {
                    if (piece.getColour() == colour) {
                        if (!friendlyAlongRay) {
                            friendlyAlongRay = true;
                        } else { //already a friendly piece in this direction so the second means there is no pin
                            break;
                        }
                    } else {
                        if (!piece.getType().isLinear()) {
                            break; //enemy pawn or knight so blocks all checks and pins in that direction
                        }
                        if (piece.isLegalMove(kingSquare)) {
                            if (friendlyAlongRay) { //we have a friendly piece then an enemy piece so a pin exists here
                                pinRay |= ray;
                            } else { //enemy piece without friendly piece means there is a check currently
                                checkRay |= ray;
                                doubleCheck = check;
                                check = true;
                                break; //stop checking in this direction now that there is a check
                            }
                        } else {
                            break; //enemy piece blocks any existing pin in this direction
                        }
                    }
                }
            }
        }
    }

    /**
     * Searches for where there could be a pawn checking the king.
     * @param position the position to search for pawn checks in.
     * @return the index of a pawn checking the king, or -1 if there is no pawn check.
     */
    public static int pawnCheckSearch(Position position) {
        Board board = position.getBoard();
        King king = board.findKing(position.getGameState().getTurn());
        int kingSquare = king.getSquare();
        PieceColour colour = king.getColour();
        int pawnDirection = (colour == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
        if (SquareMapUtils.getFileContribution(kingSquare) > Files.A) {
            int pawnSquare = kingSquare + pawnDirection + ChessDirections.LEFT;
            Piece piece = board.pieceSearch(pawnSquare);
            if (piece != null && piece.getType() == PieceType.PAWN && colour != piece.getColour()) { //pawn that can check the king exists
                return pawnSquare;
            }
        }
        if (SquareMapUtils.getFileContribution(kingSquare) < Files.H) {
            int pawnSquare = kingSquare + pawnDirection + ChessDirections.RIGHT;
            Piece piece = board.pieceSearch(pawnSquare);
            if (piece != null && piece.getType() == PieceType.PAWN && colour != piece.getColour()) { //pawn that can check the king exists
                return pawnSquare;
            }
        }
        return Squares.NONE;
    }

    /**
     * Searches for where there could be a knight checking the king.
     * @param position the position to search for knight checks in.
     * @return the index of a knight checking the king, or -1 if there is no knight check.
     */
    public static int knightCheckSearch(Position position) {
        Board board = position.getBoard();
        King king = board.findKing(position.getGameState().getTurn());
        int kingSquare = king.getSquare();
        PieceColour colour = king.getColour();
        Piece piece;
        for (int knightSquare : MoveTables.knightMoves[kingSquare]) {
            piece = board.pieceSearch(knightSquare);
            if (piece != null && piece.getType() == PieceType.KNIGHT && colour != piece.getColour()) {
                return knightSquare;
            }
        }
        return Squares.NONE;
    }

    /**
     * Searches along the king's rank to verify if an en passant capture is legal or pinned.
     * @param position the position to verify en passant in.
     * @param king the king of the side to move.
     * @param moveSource where the pawn capturing by en passant is.
     * @return true if the pawn is pinned from an en passant capture, false otherwise.
     */
    public static boolean enPassantPinned(Position position, King king, int moveSource) {
        int rank = SquareMapUtils.getRankContribution(moveSource);
        int kingSquare = king.getSquare();
        int kingRank = SquareMapUtils.getRankContribution(kingSquare);
        if (rank != kingRank) {
            return false;
        }
        int searchDirection;
        if (SquareMapUtils.getFileContribution(kingSquare) > SquareMapUtils.getFileContribution(moveSource)) {
            searchDirection = ChessDirections.LEFT_INDEX;
        } else {
            searchDirection = ChessDirections.RIGHT_INDEX;
        }
        int pawnCount = 0;
        Piece piece;
        Board board = position.getBoard();
        for (int i = 1; i <= MoveTables.DISTANCE_TO_EDGE[kingSquare][searchDirection]; i++) {
            piece = board.pieceSearch(kingSquare + i * ChessDirections.ALL[searchDirection]);
            if (piece != null) {
                PieceType pieceType = piece.getType();
                switch (pieceType) {
                    case PAWN:
                        pawnCount++;
                        if (pawnCount > 2) {
                            return false; //third pawn would block any pin
                        }
                        break;
                    case ROOK, QUEEN:
                        if (pawnCount < 2) {
                            return false; //there is a pawn then a gap then the en passanting pawn so the first pawn blocks any pin
                        } else {
                            return piece.getColour() != king.getColour();
                        }
                    default:
                        return false; //piece blocks a pin
                }
            }
        }
        return false;
    }

    /**
     * Simple getter for the check ray.
     * @return the check ray long.
     */
    public long getCheckRay() {
        return checkRay;
    }

    /**
     * Simple getter for the pin ray.
     * @return the pin ray long.
     */
    public long getPinRay() {
        return pinRay;
    }

    /**
     * Simple getter for double check.
     * @return the double check boolean.
     */
    public boolean isDoubleCheck() {
        return doubleCheck;
    }
}
