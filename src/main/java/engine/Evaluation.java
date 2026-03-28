public class Evaluation {
    public static int MOBILITY_COEFFICIENT = 1;
    public static int PIECE_TABLE_COEFFICIENT = 1;
    /**
     * Evaluates the position.
     * @param position the position to be evaluated.
     * @return integer representing the evaluation for the position in terms of centipawns.
     */
    public static int getEvaluation(Position position) {
        Board board = position.getBoard();
        PieceColour colour = position.getGameState().getTurn();
        PieceColour enemyColour = colour.opponentColour();
        int evaluation = getValueSum(board, colour) - getValueSum(board, enemyColour);
        evaluation += MOBILITY_COEFFICIENT * (getMobility(board, colour) - getMobility(board, enemyColour));
        evaluation += PIECE_TABLE_COEFFICIENT * (getPieceSquareEvaluation(board, colour) - getPieceSquareEvaluation(board, enemyColour));
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

    /**
     * Calculates the number of squares the piece can actually move to.
     * Ignores the king's squares since a kings mobility is often not a benefit for the position.
     * Ignores the pawns squares since a pawns mobility is constant.
     * @param board the board for the side to move on.
     * @param colour the colour to find the mobility of.
     * @return integer representing the total number squares the side can move to.
     */
    public static int getMobility(Board board, PieceColour colour) {
        long pieces = board.getOccupancyBitboard(colour.ordinal());
        long[] ATKFR = board.getATKFR();
        long pieceMask = pieces;
        int mobility = 0;
        while (pieces != 0){
            int square = Long.numberOfTrailingZeros(pieces);
            PieceType pieceAtSquareType = board.pieceSearch(square).getType();
            if (pieceAtSquareType != PieceType.KING && pieceAtSquareType != PieceType.PAWN) {
                mobility += Long.bitCount(ATKFR[square] & ~pieceMask);
            }
            pieces &= pieces - 1;
        }
        return mobility;
    }

    /**
     * Calculates the evaluation offsets based on how good of a square each piece is on.
     * @param board the board to evaluate.
     * @param colour the player whose perspective is evaluated.
     * @return integer representing the evaluation offset based on the pieces positions.
     */
    public static int getPieceSquareEvaluation(Board board, PieceColour colour) {
        long pieces = board.getOccupancyBitboard(colour.ordinal());
        int score = 0;
        int currentPhase = Phase.computePhase(board);
        while (pieces != 0){
            int square = Long.numberOfTrailingZeros(pieces);
            PieceType pieceAtSquareType = board.pieceSearch(square).getType();
            if (colour == PieceColour.BLACK) {
                square = EvaluationTables.BLACK_MAPPINGS[square]; //get the equivalent square from whites perspective
            }
            switch (pieceAtSquareType) {
                case KNIGHT -> score += EvaluationTables.KNIGHT[square];
                case QUEEN -> score += EvaluationTables.QUEEN[square];
                case ROOK -> score += EvaluationTables.ROOK[square];
                case BISHOP -> score += EvaluationTables.BISHOP[square];
                case PAWN -> score += ((EvaluationTables.PAWN_MIDDLEGAME[square] * (256 - currentPhase)) +
                        (EvaluationTables.PAWN_ENDGAME[square] * currentPhase)) / 256;
                case KING -> score += ((EvaluationTables.KING_MIDDLEGAME[square] * (256 - currentPhase)) +
                        (EvaluationTables.KING_ENDGAME[square] * currentPhase)) / 256;
            }
            pieces &= pieces - 1;
        }
        return score;
    }
}
