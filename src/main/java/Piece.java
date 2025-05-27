/**
 * Abstract class representing a chess piece.
 * Contains common properties such as name, color, rank (row), and file (column).
 * Provides utility methods and requires concrete subclasses to implement movement logic.
 */
public abstract class Piece {
    private final String name;
    private final String colour;
    private char file;
    private int rank;
    private String notation;

    /**
     * Constructs a chess piece with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param name  the type/name of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param file  the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank  the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Piece(String name, String colour, char file, int rank) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Piece name cannot be null or empty.");
        } else {
            this.name = name;
            if (!name.equals("Pawn") ) {
                notation = String.valueOf(name.charAt(0));
            } else {
                notation = "";
            }
        }
        if (!colour.equals("White") && !colour.equals("Black")) {
            throw new IllegalArgumentException("Colour must be 'White' or 'Black'.");
        } else {
            this.colour = colour;
        }
        if (!(file >= 'a' &&  file <= 'h')) {
            throw new IllegalArgumentException("File must be between 'a' and 'h'.");
        } else {
            this.file = file;
        }
        if (!(rank >= 1 && rank <= 8)) {
            throw new IllegalArgumentException("Rank must be between 1 and 8.");
        } else {
            this.rank = rank;
        }
    }

    /**
     * Gets the name/type of the piece.
     *
     * @return the piece name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the colour of the piece.
     *
     * @return the piece colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * Gets the current square the piece is at
     *
     * @return the location of the piece in standard algebraic notation
     */
    public String getSquare() {
        return String.valueOf(file) + String.valueOf(rank);
    }

    /**
     * Converts the file char (e.g., "a"-"h") to a zero-based index.
     *
     * @return the zero-based file index (0 for file "a")
     */
    public int mapFile() {
        int index = file - 'a';
        return index;
    }

    /**
     * Converts the file index (e.g., 0-7) to the respective character
     *
     * @param index the index to be mapped to a file character
     * @return the file character ("a" for index 0)
     * @throws IndexOutOfBoundsException when the index is invalid (not between 0 and 7)
     */
    public char mapIndexToFile(int index) {
        if (index >= 0 && index < 8) {
            char file = (char) (index + 'a');
            return file;
        } else {
            throw new IndexOutOfBoundsException("Index must be between 0 and 7 not: " + index);
        }
    }

    /**
     * Converts the rank (e.g., 1-8) to a zero-based index.
     *
     * @return the zero-based rank index (0 for rank 1)
     */
    public int mapRank() {
        return rank - 1;
    }

    /**
     * Converts the rank index (e.g., 0-7) to the correct rank.
     *
     * @param index the rank to be mapped
     * @return the rank int (1 for rank index 0)
     */
    public int mapIndexToRank(int index) {
        return index + 1;
    }

    /**
     * Abstract method to move the piece.
     * Concrete piece subclasses will implement this.
     */
    public abstract void move(String square);

    /**
     * Abstract method to generate all the legal moves the piece can do.
     * Concrete piece subclasses will implement this.
     * @return an array of all the legal moves in algebraic notation
     */

    public abstract String[] generateMoves();

    /**
     * Abstract method to check if a given move is legal.
     * Concrete piece subclasses will implement this.
     * @param move the move to be validated.
     * @return true if the move is legal, otherwise false.
     */
    public abstract boolean isLegalMove(String move);
}
