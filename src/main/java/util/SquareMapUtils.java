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
        char file =  square.charAt(0);
        int rank = square.charAt(1) - '0';
        return (8 - rank) * 8 + ((int) file - 'a');
    }
}
