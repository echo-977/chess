public class AttackTables {
    /**
     * Helper to add an attack move mask to the attack tables.
     * @param ATKTO contains all the squares that attack a given square.
     * @param ATKFR contains all the squares that the given square attacks.
     * @param mask mask of all the square attacks to be added to the tables.
     * @param square the square the attack mask exists for.
     */
    private static void addAttackMask(long[] ATKTO, long[] ATKFR, long mask, int square) {
        ATKFR[square] |= mask;
        int incomingAttackSquare;
        while (mask != 0) { //there are still attacks to add to the ATKTO table
            incomingAttackSquare = Long.numberOfTrailingZeros(mask);
            ATKTO[incomingAttackSquare] |= 1L << square;
            mask &= mask - 1; //remove lowest bit
        }
    }

    /**
     * Helper to remove an attack move mask to the attack tables.
     * @param ATKTO contains all the squares that attack a given square.
     * @param ATKFR contains all the squares that the given square attacks.
     * @param mask mask of all the square attacks to be removed from the tables.
     * @param square the square the attack mask exists for.
     */
    private static void removeAttackMask(long[] ATKTO, long[] ATKFR, long mask, int square) {
        ATKFR[square] &= ~mask;
        int incomingAttackSquare;
        while (mask != 0) { //move source is now empty so we remove it a bit at a time and remove the respective attacks from the incoming attacks
            incomingAttackSquare = Long.numberOfTrailingZeros(mask);
            ATKTO[incomingAttackSquare] &= ~(1L << square);
            mask &= mask - 1;
        }
    }

    /**
     * Sets up the attacks to and attacks from tables.
     * ATKTO contains all the squares that attack the given square.
     * ATKFR contains all the squares the given square attacks.
     * @param board the board the tables are for.
     */
    public static void setUpAttackTables(Board board) {
        long[] ATKTO = board.getATKTO();
        long[] ATKFR = board.getATKFR();
        Piece[] pieces = board.getPieces();
        Piece current;
        for (int square = 0; square < ChessConstants.NUM_SQUARES; square++) {
            current = pieces[square];
            if (current != null) {
                addAttackMask(ATKTO, ATKFR, current.getAttackMask(board), square);
            }
        }
    }

    /**
     * Incremental update of the ATKTO and ATKFR tables upon a move being done.
     * @param board the board the move is done on.
     * @param moveSource the source of the move.
     * @param moveDestination the destination of the move.
     * @param moveFlags the flag data for the move.
     * @param capturedPiece piece the move captured.
     */
    public static void updateAttackTables(Board board, int moveSource, int moveDestination, int moveFlags, Piece capturedPiece) {
        long[] ATKTO = board.getATKTO();
        long[] ATKFR = board.getATKFR();
        removeAttackMask(ATKTO, ATKFR, ATKFR[moveSource], moveSource);
        handleLinearPieceAttacks(board, moveSource, true);
        if (capturedPiece == null) { //no captured piece so we need to remove now blocked attacks
            if ((moveFlags & MoveFlags.PROMOTION_BIT) != MoveFlags.PROMOTION_BIT &&
                    (moveFlags & MoveFlags.CAPTURE_BIT) != MoveFlags.CAPTURE_BIT) {
                if ((moveFlags & MoveFlags.QUEENSIDE_CASTLE) == MoveFlags.QUEENSIDE_CASTLE) {
                    int rank = SquareMapUtils.getRankContribution(moveSource);
                    int rookSource = ChessConstants.QUEENSIDE_ROOK_SOURCE_FILE + rank;
                    int rookDestination = ChessConstants.QUEENSIDE_CASTLE_ROOK_FILE + rank;
                    removeAttackMask(ATKTO, ATKFR, ATKFR[rookSource], rookSource);
                    addAttackMask(ATKTO, ATKFR, board.pieceSearch(rookDestination).getAttackMask(board), rookDestination);
                } else if ((moveFlags & MoveFlags.KINGSIDE_CASTLE) == MoveFlags.KINGSIDE_CASTLE) {
                    int rank = SquareMapUtils.getRankContribution(moveSource);
                    int rookSource = ChessConstants.KINGSIDE_ROOK_SOURCE_FILE + rank;
                    int rookDestination = ChessConstants.KINGSIDE_CASTLE_ROOK_FILE + rank;
                    removeAttackMask(ATKTO, ATKFR, ATKFR[rookSource], rookSource);
                    addAttackMask(ATKTO, ATKFR, board.pieceSearch(rookDestination).getAttackMask(board), rookDestination);
                }
            }
            handleLinearPieceAttacks(board, moveDestination, false);
        } else { //captured piece so we need to remove that pieces attacks
            int capturedPieceSquare = capturedPiece.getSquare();
            removeAttackMask(ATKTO, ATKFR, ATKFR[capturedPieceSquare], capturedPieceSquare);
            if (capturedPieceSquare != moveDestination) { //piece captured is a pawn by en passant
                handleLinearPieceAttacks(board, capturedPieceSquare, true); //look for revealed attacks from the captured pawn
                handleLinearPieceAttacks(board, moveDestination, false); //look for blocked attacks from the pushed pawn
            }
        }
        addAttackMask(ATKTO, ATKFR, board.pieceSearch(moveDestination).getAttackMask(board), moveDestination);
    }

    /**
     * Either adds or removes linear piece attacks that will now be revealed or blocked by a move.
     * @param board the board the move is done on.
     * @param square the square to look for revealed or blocked attacks.
     * @param add boolean that toggles whether we are adding revealed moves or removing blocked moves.
     */
    private static void handleLinearPieceAttacks(Board board, int square, boolean add) {
        long[] ATKTO = board.getATKTO();
        long[] ATKFR = board.getATKFR();
        long attackMask;
        int direction;
        int currentSquare;
        Piece[] pieces = board.getPieces();
        Piece currentPiece;
        for (int directionIndex = 0; directionIndex < ChessConstants.NUM_DIRECTIONS; directionIndex++) {
            direction = ChessDirections.ALL[directionIndex];
            for (int step = 1; step <= MoveTables.DISTANCE_TO_EDGE[square][directionIndex]; step++) {
                currentSquare = square + step * direction; //look for linear pieces in a direction
                currentPiece = pieces[currentSquare];
                if (currentPiece == null) { //no piece
                    continue;
                }
                if (!currentPiece.getType().isLinear()) { //non linear piece found so we haven't discovered new attacks
                    break;
                }
                if (!currentPiece.getType().canTravel(direction)) { //linear piece can't travel in this direction so no attacks to add
                    break;
                }
                attackMask = 0L;
                int oppositeDirection = -direction;
                int oppositeDirectionIndex = ChessDirections.OPPOSITE_INDEX[directionIndex];
                int startStep = ((square - currentSquare) / oppositeDirection) + 1; //start one past the piece that just moved
                int maskSquare;
                for (int offset = startStep; offset <= MoveTables.DISTANCE_TO_EDGE[currentSquare][oppositeDirectionIndex]; offset++) {
                    maskSquare = currentSquare + offset * oppositeDirection;
                    attackMask |= 1L << maskSquare;
                    if (pieces[maskSquare] != null) { //attacks are blocked so stop searching in that direction
                        if (pieces[maskSquare].getType() == PieceType.KING && currentPiece.getColour() != pieces[maskSquare].getColour()) {
                            continue; //king the opposite colour of the ray piece so we the piece will attack behind the king once the king moves
                        }
                        break;
                    }
                }
                if (add) {
                    addAttackMask(ATKTO, ATKFR, attackMask, currentSquare);
                } else {
                    removeAttackMask(ATKTO, ATKFR, attackMask, currentSquare);
                }
                break; //we can only reveal attacks from one piece so move to next direction
            }
        }
    }
}
