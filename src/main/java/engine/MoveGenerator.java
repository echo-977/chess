public class MoveGenerator {
    /**
     * Generates all the moves for a given colour.
     *
     * @param position the position to generate moves in.
     * @return an array of all the moves the colour can do.
     */
    public static Move[] generateMoves(Position position) {
        PieceColour turn = position.getGameState().getTurn();
        Piece[] pieces = position.getBoard().getPieces();
        int moveCount = 0;
        Move[][] allMoves = new Move[ChessConstants.NUM_SQUARES][];
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != turn) {
                allMoves[squareIndex] = new Move[0];
                continue;
            }
            allMoves[squareIndex] = pieces[squareIndex].generateMoves(position);
            for (Move move : allMoves[squareIndex]) {
                if (move != null) {
                    moveCount++;
                }
            }
        }
        Move[] moves = new Move[moveCount];
        moveCount = 0;
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES ; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != turn) {
                continue;
            }
            for (Move move : allMoves[squareIndex]) {
                if (move != null) {
                    moves[moveCount] = move;
                    moveCount++;
                }
            }
        }
        return moves;
    }
}
