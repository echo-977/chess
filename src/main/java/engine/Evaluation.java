public class Evaluation {
    /**
     * Evaluates the position.
     * @param position the position to be evaluated.
     * @return integer representing the evaluation for the position in terms of centipawns.
     */
    public static int getEvaluation(Position position) {
        PieceColour colour = position.getGameState().getTurn();
        PieceColour enemyColour = colour.opponentColour();
        int evaluation = getValueSum(position.getBoard(), colour) - getValueSum(position.getBoard(), enemyColour);
        return evaluation;
    }

    /**
     * Calculates the sum of piece values in the position for a given colour.
     * @param board the board to be calculated.
     * @param colour the colour to get the value sum for.
     * @return integer representing the total material value for the colour in centipawns.
     */
    public static int getValueSum(Board board, PieceColour colour) {
        int sum = 0;
        long occupancy = board.getOccupancyBitboard(colour.ordinal());
        while (occupancy != 0){
            int square = Long.numberOfTrailingZeros(occupancy);
            sum += board.pieceSearch(square).getType().getValue();
            occupancy &= occupancy - 1;
        }
        return sum;
    }
}
