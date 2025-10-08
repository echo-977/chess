public class ThreatMapGenerator {
    /**
     * Gets a bitstring of all squared attacked by a certain colour.
     * @param board the board we are generating a threat map of.
     * @param colour the colour of squares we want.
     * @return long with a 1 for which squares can be captured by the colour.
     */
    public static long computeThreatMap(Board board, PieceColour colour) {
        if (colour == PieceColour.WHITE) {
            return computeThreatMap(board, board.getWhiteBitboard());
        } else {
            return computeThreatMap(board, board.getBlackBitboard());
        }
    }

    public static long computeThreatMap(Board board, long pieceBitboard) {
        long threatMap = 0L;
        long[] ATKTO = board.getATKTO();
        for (int square = 0; square < ChessConstants.NUM_SQUARES; square++) {
            if ((ATKTO[square] & pieceBitboard) != 0) {
                threatMap |= 1L << square;
            }
        }
        return threatMap;
    }
}
