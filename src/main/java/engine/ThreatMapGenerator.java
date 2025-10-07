public class ThreatMapGenerator {
    /**
     * Gets a bitstring of all squared attacked by a certain colour.
     * @param board the board we are generating a threat map of.
     * @param colour the colour of squares we want.
     * @return long with a 1 for which squares can be captured by the colour.
     */
    public static long getThreatMap(Board board, PieceColour colour) {
        Piece[] pieces = board.getPieces();
        long threatMap = 0L;
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            for (Piece piece : pieces) {
                if (piece == null) {
                    continue;
                }
                if (piece.getColour() != colour) {
                    continue;
                }
                if (piece.canCaptureSquare(board, squareIndex)) {
                    threatMap |= (1L << squareIndex);
                    break;
                }
            }
        }
        return threatMap;
    }
}
