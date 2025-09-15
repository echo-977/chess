import it.unimi.dsi.fastutil.ints.IntArrayList;

public class MoveGenerator {
    /**
     * Generates all the moves for a given colour.
     *
     * @param position the position to generate moves in.
     * @return an integer array list of all the moves the colour can do.
     */
    public static IntArrayList generateMoves(Position position) {
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
}
