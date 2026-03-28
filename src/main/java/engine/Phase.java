public class Phase {
    //phase implementation outlined in the Tapered Eval Page on the chess programming wiki
    //https://www.chessprogramming.org/Tapered_Eval
    public static final int KNIGHT_PHASE = 1;
    public static final int BISHOP_PHASE = 1;
    public static final int ROOK_PHASE = 2;
    public static final int QUEEN_PHASE = 4;
    public static final int TOTAL_PHASE = KNIGHT_PHASE * 4 + BISHOP_PHASE * 4 + ROOK_PHASE * 4 +  QUEEN_PHASE * 2;

    public static int computePhase(Board board) {
        int phase = TOTAL_PHASE;
        long pieces = board.getOccupancyBitboard(ChessConstants.BOTH_BITBOARD);
        while (pieces != 0){
            int square = Long.numberOfTrailingZeros(pieces);
            PieceType pieceAtSquareType = board.pieceSearch(square).getType();
            if (pieceAtSquareType != PieceType.KING && pieceAtSquareType != PieceType.PAWN) {
                switch (pieceAtSquareType) {
                    case KNIGHT -> phase -= KNIGHT_PHASE;
                    case BISHOP -> phase -= BISHOP_PHASE;
                    case ROOK -> phase -= ROOK_PHASE;
                    case QUEEN -> phase -= QUEEN_PHASE;
                }
            }
            pieces &= pieces - 1;
        }
        return (phase * 256 + (TOTAL_PHASE / 2)) / TOTAL_PHASE;
    }
}
