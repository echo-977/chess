public class SquareMapUtils {
    /**
     * Give the square that an integer from 0 to 63 relates to
     * 0 refers to a8 then it reads to the right and down
     * @param location the integer that a square returns to
     * @return a string of the square
     */
    public static String mapIntToSquare(int location) {
        int rank = 8 - location / 8;
        char file = (char) ('a' + (location % 8));
        return file + String.valueOf(rank);
    }

    /**
     * Give the integer index for a given square.
     * @param square the square in question.
     * @return the index of the square.
     */
    public static int mapSquareToInt(String square) {
        char file =  getFile(square);
        int rank = getRank(square);
        return (8 - rank) * 8 + ((int) file - 'a');
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
}
