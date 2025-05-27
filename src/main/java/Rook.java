public class Rook extends Piece{
    boolean moved;

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
        String position = getSquare();
        String file = position.substring(0, 1);
        String rank = position.substring(1, 2);
        String[] moves = new String[14]; //max number of rook moves in any position
        String move;
        for (char pFile = 'a'; pFile <= 'h'; pFile++) {
            move = String.valueOf(pFile) + rank;
            if (isLegalMove(move)) {
                moves[moves.length - 1] = move;
            }
        }
        for (int pRank = 1; pRank <= 8; pRank++) {
            move = file + String.valueOf(pRank);
            if (isLegalMove(move)) {
                moves[moves.length - 1] = move;
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
}
