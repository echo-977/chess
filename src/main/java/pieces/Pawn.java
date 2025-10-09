import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Pawn extends Piece{
    private final int moveDirection;
    private final int sourceRank;

    /**
     * Constructs a pawn with the specified name, color, rank, and file.
     * Stores the direction the pawn moves in.
     *
     * @param colour the colour of the pawn ("White" or "Black")
     * @param square the square the pawn is on.
     */
    public Pawn(PieceColour colour, int square) {
        super(PieceType.PAWN, colour, square);
        if (colour == PieceColour.WHITE) {
            this.sourceRank = Ranks.TWO;
            this.moveDirection = ChessDirections.UP;
        } else {
            this.sourceRank = Ranks.SEVEN;
            this.moveDirection = ChessDirections.DOWN;
        }
    }

    /**
     * Generates all the legal moves the pawn can do.
     * @param position the board that we are searching for moves on.
     * @param moves array list legal moves are to be added to.
     */
    @Override
    public void generateMoves(Position position, IntArrayList moves) {
        int square = getSquare();
        int candidateMove = square;
        for (int i = 0; i < 2; i++) { //pawns can move forward either 1 or 2 squares
            candidateMove += moveDirection;
            if (isLegalMove(candidateMove) && position.getBoard().pieceSearch(candidateMove) == null) {
                if (i == 1) { //this is a double pawn push
                    moves.add(Move.encodeMove(position, candidateMove, square) |
                            MoveFlags.DOUBLE_PAWN_PUSH << MoveFlags.FLAG_SHIFT);
                } else {
                    addMove(position, moves, candidateMove);
                }
            } else {
                break; //if single push is blocked then double push is blocked
            }
        }
        int captureTarget;
        if (SquareMapUtils.getFileContribution(square) > Files.A) { //pawns on the a-file cannot capture to the left
            captureTarget = square + ChessDirections.LEFT + moveDirection;
            addCaptureMove(position, captureTarget, moves);
        }
        if (SquareMapUtils.getFileContribution(square) < Files.H) { //pawns on the h-file cannot capture to the right
            captureTarget = square + ChessDirections.RIGHT + moveDirection;
            addCaptureMove(position, captureTarget, moves);
        }
    }

    /**
     * Helper function for adding capture moves on either side of the pawn.
     *
     * @param position the position we are looking for moves in.
     * @param captureTarget the square the pawn can capture.
     * @param moves array list legal moves are to be added to.
     */
    private void addCaptureMove(Position position, int captureTarget, IntArrayList moves) {
        Piece piece = position.getBoard().pieceSearch(captureTarget);
        if (piece != null && piece.getColour() != getColour()) {
            addMove(position, moves, captureTarget);
            return;
        }
        int enPassantTarget = position.getGameState().getEnPassantTarget();
        if (enPassantTarget != Squares.NONE && enPassantTarget == captureTarget) {
            int moveFlag = (MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT) << MoveFlags.FLAG_SHIFT;
            moves.add(moveFlag | Move.encodeMove(position, captureTarget, getSquare()));
        }
    }

    /**
     * Helper function to add moves for the generateMoves method.
     *
     * @param position    the position we are looking for moves in.
     * @param moves array list legal moves are to be added to.
     * @param destination where the piece is going to.
     */
    public void addMove(Position position, IntArrayList moves, int destination) {
        PieceColour colour = getColour();
        if (destination < Ranks.SEVEN && colour == PieceColour.WHITE || destination >= Ranks.ONE && colour == PieceColour.BLACK) {
            int move = Move.encodeMove(position, destination, getSquare());
            int[] promotionPieceFlags = {MoveFlags.QUEEN, MoveFlags.ROOK, MoveFlags.BISHOP, MoveFlags.KNIGHT};
            for (int promotionPiece : promotionPieceFlags) {
                moves.add(move | ((promotionPiece | MoveFlags.PROMOTION_BIT) << MoveFlags.FLAG_SHIFT));
            }
        } else {
            moves.add(Move.encodeMove(position, destination, getSquare()));
        }
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(int move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        int square = getSquare();
        if (square + moveDirection == move) {
            return true;
        } else {
            if (SquareMapUtils.getRankContribution(square) != sourceRank) {
                return false;
            } else {
                return (square + 2 * moveDirection == move);
            }
        }
    }

    /**
     * Check if the pawn can capture a given square.
     * Used for detection of checks so en passant is excluded here.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    @Override
    public boolean canCaptureSquare(Board board, int targetSquare) {
        if (!super.isLegalMove(targetSquare)) {
            return false;
        }
        int square = getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        boolean leftCapture = false;
        boolean rightCapture = false;
        if (squareFile > Files.A) { //when on the A file the pawn cannot capture left
            leftCapture = square + moveDirection + ChessDirections.LEFT == targetSquare;
        }
        if (squareFile < Files.H) { //when on the H file the pawn cannot capture left
            rightCapture = square + moveDirection + ChessDirections.RIGHT == targetSquare;
        }
        return leftCapture || rightCapture;
    }

    /**
     * Creates a copy of the pawn at a given square.
     * @param square the square the piece copy will be at.
     * @return a pawn object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(int square) {
        return new Pawn(getColour(), square);
    }

    /**
     * Gets a bitmask of all the squares the pawn attacks.
     * @return long where each bit represents whether the pawn attacks that square or not.
     */
    @Override
    public long getAttackMask(Board board) {
        long attackMask = 0L;
        int square = getSquare();
        int file = SquareMapUtils.getFileContribution(square);
        if (file > Files.A && file < Files.H) { //pawn can attack on either side
            attackMask |= 1L << (square + moveDirection + ChessDirections.LEFT);
            attackMask |= 1L << (square + moveDirection + ChessDirections.RIGHT);
        } else if (file > Files.A) { //pawn can attack on left only
            attackMask |= 1L << (square + moveDirection + ChessDirections.LEFT);
        } else { //pawn can attack on right only
            attackMask |= 1L << (square + moveDirection + ChessDirections.RIGHT);
        }
        return attackMask;
    }
}


