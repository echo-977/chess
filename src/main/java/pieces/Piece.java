/**
 * Abstract class representing a chess piece.
 * Contains common properties such as type, color, rank (row), and file (column).
 * Provides utility methods and requires concrete subclasses to implement movement logic.
 */
public abstract class Piece{
    private final PieceType type;
    private final PieceColour colour;
    private int square;

    /**
     * Constructs a chess piece with the specified type, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param type  the type of the piece (e.g., "Pawn", "Knight").
     * @param colour the colour of the piece ("White" or "Black").
     * @param square the square the piece is on.
     */
    public Piece(PieceType type, PieceColour colour, int square) {
        this.type = type;
        this.colour = colour;
        this.square = square;
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
     * @return the piece colour.
     */
    public PieceColour getColour() {
        return colour;
    }

    /**
     * Gets the current square the piece is at.
     *
     * @return the location of the piece.
     */
    public int getSquare() {
        return square;
    }

    /**
     * Method to move the piece to a given square.
     * Validation of if the move is legal will be done elsewhere.
     * @param square the square the piece is moved to.
     */
    public void move(int square) {
        this.square = square;
    }

    /**
     * Method to check if a given move is legal (without considering other pieces).
     * Piece subclasses will add to this.
     * @param move the move to be validated.
     * @return true if the move is legal, otherwise false.
     */
    public boolean isLegalMove(int move) {
        if (move == getSquare()) {
            return false;
        }
        return (move <= 63 && move >= 0);
    }

    /**
     * Abstract method to generate all the legal moves the piece can do.
     * Concrete piece subclasses will implement this.
     * @param position the position that we are searching for moves on.
     * @return a string array of all the legal moves.
     */
    public abstract Move[] generateMoves(Position position);

    /**
     * Abstract method to check if a piece can capture a given square.
     * Concrete piece subclasses will implement this.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    public abstract boolean canCaptureSquare(Board board, int targetSquare);

    /**
     * Abstract method to create a copy of a piece at a given square.
     * Concrete subclasses will implement this.
     * @param square the square the piece copy will be at.
     * @return a copy of the piece at the given square.
     */
    public abstract Piece copyToSquare(int square);

    /**
     * Compares the piece to a given object to check if they are equal.
     * Equality is determined by all the attributes of the pieces matching.
     * @param object the object we compare the piece to
     * @return a boolean stating whether the object is equal to the piece.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        }
        if (!(object instanceof Piece other)) {
            return false;
        }
        return other.type == this.type && other.colour == this.colour && other.square == this.square;
    }

    /**
     * Returns all the information about the piece.
     * @return a string of the colour, type and location.
     */
    @Override
    public String toString() {
        return SquareMapUtils.mapIntToSquare(square);
    }
}
