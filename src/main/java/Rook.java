public class Rook extends LinearPiece{
    private boolean moved;

    /**
     * Constructs a rook with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param moved  boolean for if the rook has moved (used in castling logic)
     */
    public Rook(String colour, char file, int rank, boolean moved) {
        super("Rook", colour, file, rank);
        this.moved = moved;
    }

    /**
     * Generates all the legal moves the rook can do (without considering other pieces).
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the rook can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        String[] moves = new String[14]; //max number of rook moves in any position
        int movesIndex = 0;
        movesIndex = linearMoveSearch(board, moves, movesIndex, -1, 0); //left
        movesIndex = linearMoveSearch(board, moves, movesIndex, 1, 0); //right
        movesIndex = linearMoveSearch(board, moves, movesIndex, 0, 1); //up
        linearMoveSearch(board, moves, movesIndex, 0, -1); // down
        return moves;
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(String move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        char file = move.charAt(0);
        int rank = Integer.parseInt(move.substring(1, 2));
        if (rank == getRank() && file == getFile()) {
            return false;
        } else if (rank == getRank()) {
            return true;
        } else {
            return file == getFile();
        }

    }

    /**
     * Overrides the move method in the piece class to update the moved flag.
     * @param move the square to move the piece to.
     */
    @Override
    public void move(String move) {
        if (!moved) {
            moved = true;
        }
        super.move(move);
    }

    /**
     * Simple getter for the boolean moved
     * @return the moved boolean
     */
    public boolean getMoved() {
        return moved;
    }
}
