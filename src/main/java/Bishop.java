public class Bishop extends LinearPiece{

    /**
     * Constructs a bishop with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Bishop(String colour, char file, int rank) {
        super("Bishop", colour, file, rank);
    }

    /**
     * Generates all the legal moves the bishop can do (without considering other pieces).
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the bishop can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        String[] moves = new String[13]; //max number of bishop moves in any position
        int movesIndex = 0;
        movesIndex = linearMoveSearch(board, moves, movesIndex, -1, 1); //up left
        movesIndex = linearMoveSearch(board, moves, movesIndex, 1, 1); //up right
        movesIndex = linearMoveSearch(board, moves, movesIndex, 1, -1); //down right
        linearMoveSearch(board, moves, movesIndex, -1, -1); // down left
        return moves;
    }

    /**
     * Check if a given move is legal.
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(String move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        char moveFile = move.charAt(0);
        int moveRank = Integer.parseInt(move.substring(1, 2));
        char file = getFile();
        int rank = getRank();
        return Math.abs(rank - moveRank) == Math.abs(file - moveFile);

    }
}
