/**
 * Abstract class representing a chess piece.
 * Contains common properties such as type, color, rank (row), and file (column).
 * Provides utility methods and requires concrete subclasses to implement movement logic.
 */
public abstract class Piece{
    private final PieceType type;
    private final PieceColour colour;
    private char file;
    private int rank;

    /**
     * Constructs a chess piece with the specified type, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param type  the type of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param file  the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank  the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Piece(PieceType type, PieceColour colour, char file, int rank) {
        if (type != PieceType.PAWN && type != PieceType.KING && type != PieceType.QUEEN && type != PieceType.ROOK && type != PieceType.BISHOP && type != PieceType.KNIGHT) {
            throw new IllegalArgumentException("Piece type cannot be null");
        } else {
            this.type = type;
        }
        if (colour != PieceColour.WHITE && colour != PieceColour.BLACK) {
            throw new IllegalArgumentException("Colour must be 'White' or 'Black' enum.");
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
    public PieceType getType() {
        return type;
    }

    /**
     * Gets the colour of the piece.
     *
     * @return the piece colour
     */
    public PieceColour getColour() {
        return colour;
    }

    /**
     * Gets the current square the piece is at
     *
     * @return the location of the piece in standard algebraic notation
     */
    public String getSquare() {
        return file + String.valueOf(rank);
    }

    /**
     * Simple getter for the pieces file.
     * @return the file.
     */
    public char getFile() {
        return file;
    }

    /**
     * Simple getter for the pieces rank.
     * @return the rank.
     */
    public int getRank() {
        return rank;
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
     * Method to move the piece to a given square.
     * Validation of if the move is legal will be done elsewhere.
     */
    public void move(String square) {
        file = square.charAt(0);
        rank = Character.getNumericValue(square.charAt(1));
    }

    /**
     * Method to check if a given move is legal (without considering other pieces).
     * Piece subclasses will add to this.
     * @param move the move to be validated.
     * @return true if the move is legal, otherwise false.
     */
    public boolean isLegalMove(String move) {
        if (move.equals(getSquare())) {
            return false;
        }
        char file = move.charAt(0);
        if (file < 'a' || file > 'h') {
            return false;
        }
        try {
            int rank = Integer.parseInt(move.substring(1, 2));
            if (rank < 1 || rank > 8) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Abstract method to generate all the legal moves the piece can do.
     * Concrete piece subclasses will implement this.
     * @param board the board that we are searching for moves on.
     * @return a string array of all the legal moves.
     */
    public abstract String[] generateMoves(Board board);

    /**
     * Abstract method to check if a piece can capture a given square.
     * Concrete piece subclasses will implement this.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    public abstract boolean canCaptureKing(Board board, String targetSquare);
}
