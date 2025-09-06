public class ThreatMapGenerator {
    /**
     * Gets a boolean array of all squared attacked by a certain colour.
     * @param board the board we are generating a threat map of.
     * @param colour the colour of squares we want.
     * @return array of true values for which squares can be captured by the colour.
     */
    public static boolean[] getThreatMap(Board board, PieceColour colour) {
        Piece[] pieces = board.getPieces();
        boolean[] threatMap = new boolean[ChessConstants.NUM_SQUARES];
        for (int i = 0; i < ChessConstants.NUM_SQUARES; i++) {
            for (Piece piece : pieces) {
                if (piece != null && piece.getColour() == colour && piece.canCaptureSquare(board, SquareMapUtils.mapIntToSquare(i))) {
                    threatMap[i] = true;
                    break;
                }
            }
        }
        return threatMap;
    }
}
