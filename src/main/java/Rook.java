public class Rook extends Piece{
    private boolean moved;

    /**
     * Constructs a rook with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param name   the type/name of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param moved  boolean for if the rook has moved (used in castling logic)
     */
    public Rook(String name, String colour, char file, int rank, boolean moved) {
        super(name, colour, file, rank);
        this.moved = moved;
    }

    /**
     * Generates all the legal moves the rook can do (without considering other pieces).
     * @return an array of all the squares the rook can move to as strings.
     */
    @Override
    public String[] generateMoves() {
        String file = String.valueOf(getFile());
        String rank = String.valueOf(getRank());
        String[] moves = new String[14]; //max number of rook moves in any position
        int movesIndex = 0;
        String move;
        for (char pFile = 'a'; pFile <= 'h'; pFile++) {
            move = String.valueOf(pFile) + rank;
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            }
        }
        for (int pRank = 1; pRank <= 8; pRank++) {
            move = file + String.valueOf(pRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            }
        }
        return moves;
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(String move) {
        if (move.equals(getSquare())) {
            return false;
        }
        char file = move.charAt(0);
        if (file < 'a' || file > 'h') {
            return false;
        }
        int rank = Integer.parseInt(move.substring(1, 2));
        if (rank < 1 || rank > 8) {
            return false;
        }
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
