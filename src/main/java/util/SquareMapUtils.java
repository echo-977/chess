public class SquareMapUtils {
    /**
     * Give the square that an integer from 0 to 63 relates to
     * 0 refers to a8 then it reads to the right and down
     * @param location the integer that a square returns to
     * @return a string of the square
     */
    public static String mapIntToSquare(int location) {
        int rank = ChessConstants.NUM_RANKS - location / ChessConstants.NUM_RANKS;
        char file = (char) ('a' + (location % ChessConstants.NUM_FILES));
        return file + String.valueOf(rank);
    }

    /**
     * Give the integer index for a given square.
     * @param square the square in question.
     * @return the index of the square.
     */
    public static int mapSquareToInt(String square) {
        char file = getFile(square);
        int rank = getRank(square);
        return (ChessConstants.NUM_RANKS - rank) * ChessConstants.NUM_RANKS + ((int) file - 'a');
    }

    /**
     * Get the file of a given square.
     * @param square the square.
     * @return the file of the square.
     */
    public static char getFile(String square) {
        return square.charAt(0);
    }

    /**
     * Get the rank of the given square.
     * @param square the square.
     * @return the rank of the square.
     */
    public static int getRank(String square) {
        return square.charAt(1) - '0';
    }

    /**
     * Get the file from a given square.
     * @param square the square.
     * @return the file of the square.
     */
    public static char getFile(int square) {
        return (char) ('a' + (square % ChessConstants.NUM_FILES));
    }

    /**
     * Get the rank from a given square.
     * @param square the square.
     * @return the rank of the square.
     */
    public static int getRank(int square) {
        return ChessConstants.NUM_RANKS - square / ChessConstants.NUM_FILES;
    }

    /**
     * Get the square for a given file and rank.
     * @param file the file of the square.
     * @param rank the rank of the square.
     * @return the square of a given file and rank.
     */
    public static int getSquare(char file, int rank) {
        return (ChessConstants.NUM_RANKS - rank) * ChessConstants.NUM_RANKS + ((int) file - 'a');
    }

    /**
     * Returns the file's contribution to the integer square value.
     * @param square the square.
     * @return the integer for the file of the square to the square int.
     */
    public static int getFileContribution(int square) {
        return square % ChessConstants.NUM_FILES;
    }

    /**
     * Returns the rank's contribution to the integer square value.
     * @param square the square.
     * @return the contribution for the rank of the square to the square int.
     */
    public static int getRankContribution(int square) {
        return square - (square % ChessConstants.NUM_FILES);
    }
}
