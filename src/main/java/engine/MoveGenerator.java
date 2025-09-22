import it.unimi.dsi.fastutil.ints.IntArrayList;

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
    public IntArrayList generateMoves(Position position) {
        PieceColour turn = position.getGameState().getTurn();
        Piece[] pieces = position.getBoard().getPieces();
        IntArrayList moves = new IntArrayList();
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != turn) {
                continue;
            }
            pieces[squareIndex].generateMoves(position, moves);
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
                        if (piece.isLegalMove(kingSquare)) {
                            if (friendlyAlongRay) { //we have a friendly piece then an enemy piece so a pin exists here
                                pinRay |= ray;
                            } else { //enemy piece without friendly piece means there is a check currently
                                checkRay |= ray;
                                doubleCheck = check;
                                check = true;
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
