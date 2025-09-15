public class MoveGenerator {
    /**
     * Generates all the moves for a given colour.
     *
     * @param position the position to generate moves in.
     * @return an array of all the moves the colour can do.
     */
    public static int[] generateMoves(Position position) {
        PieceColour turn = position.getGameState().getTurn();
        Piece[] pieces = position.getBoard().getPieces();
        int moveCount = 0;
        int[][] allMoves = new int[ChessConstants.NUM_SQUARES][];
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != turn) {
                allMoves[squareIndex] = new int[0];
                continue;
            }
            allMoves[squareIndex] = pieces[squareIndex].generateMoves(position);
            for (int move : allMoves[squareIndex]) {
                if (move != MoveFlags.NO_MOVE) {
                    moveCount++;
                }
            }
        }
        int[] moves = new int[moveCount];
        moveCount = 0;
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES ; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != turn) {
                continue;
            }
            for (int move : allMoves[squareIndex]) {
                if (move != MoveFlags.NO_MOVE) {
                    moves[moveCount] = move;
                    moveCount++;
                }
            }
        }
        return moves;
    }
}
