public class DisambiguationUtils {
    /**
     * Detects when disambiguation is required in an array of moves.
     * Then passes all cases of this to the setDisambiguationFlags method.
     * @param moves the moves that need to be checked for when disambiguation is required.
     */
    public static void handleDisambiguation(Move[] moves) {
        int numMoves = moves.length;
        int moveCount = 0;
        Move[] needDisambiguation = new Move[numMoves];
        int handledIndex = 0;
        Move[] handled = new Move[numMoves];
        boolean disambiguated = false;
        boolean handledMove = false;
        for (int i = 0; i < numMoves; i++) {
            for (Move move : handled) {
                if (moves[i] == move) {
                    handledMove = true;
                    break;
                }
            }
            if (handledMove) {
                handledMove = false;
                continue;
            }
            needDisambiguation[moveCount] = moves[i]; //move shares a destination with itself
            moveCount++;
            handled[handledIndex] = moves[i];
            handledIndex++;
            for (int j = i + 1; j < numMoves; j++) {
                if (moves[i].getDestination().equals(moves[j].getDestination()) &&
                        moves[i].getPiece().getType() == moves[j].getPiece().getType()) {
                    needDisambiguation[moveCount] = moves[j];
                    moveCount++;
                    handled[handledIndex] = moves[j];
                    handledIndex++;
                    disambiguated = true;
                }
            }
            if (disambiguated) {
                setDisambiguationFlags(needDisambiguation);
                disambiguated = false;
            }
            needDisambiguation = new Move[numMoves];
            moveCount = 0;
        }

    }

    /**
     * Sets the relevant disambiguation flags for a list of moves that have the same piece type and same destination.
     * @param needDisambiguation the moves that need disambiguation.
     */
    public static void setDisambiguationFlags(Move[] needDisambiguation) {
        boolean needsFileDisambiguation = true; //default disambiguation method
        boolean needsRankDisambiguation = false;
        int[] numPiecesPerFile = new int[ChessConstants.NUM_FILES];
        int[] numPiecesPerRank = new int[ChessConstants.NUM_RANKS];
        for (Move move : needDisambiguation) {
            if (move == null) {
                continue;
            }
            Piece piece = move.getPiece();
            int fileIndex = piece.mapFile();
            int rankIndex = piece.mapRank();
            numPiecesPerFile[fileIndex]++;
            numPiecesPerRank[rankIndex]++;
        }
        for (int i = 0; i < ChessConstants.NUM_FILES; i++) {
            if (numPiecesPerFile[i] > 1) {
                needsRankDisambiguation = true;
                needsFileDisambiguation = false;
                break;
            }
        }
        for (int i = 0; i < ChessConstants.NUM_RANKS; i++) {
            if (numPiecesPerRank[i] > 1) {
                needsFileDisambiguation = true;
                break;
            }
        }
        for (Move move : needDisambiguation) {
            if (move == null) {
                continue;
            }
            move.setFileDisambiguation(needsFileDisambiguation);
            move.setRankDisambiguation(needsRankDisambiguation);
        }
    }

}
