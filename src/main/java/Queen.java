public class Queen extends LinearPiece{

    /**
     * Constructs a queen with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Queen(String colour, char file, int rank) {
        super("Queen", colour, file, rank);
    }

    /**
     * Generates all the legal moves the queen can do.
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the queen can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        String[] moves = new String[27]; //max number of queen moves
        int movesIndex = 0;
        movesIndex = linearMoveSearch(board, moves, movesIndex, 0, 1); //up
        movesIndex = linearMoveSearch(board, moves, movesIndex, 1, 1); //up right
        movesIndex = linearMoveSearch(board, moves, movesIndex, 1, 0); //right
        movesIndex = linearMoveSearch(board, moves, movesIndex, 1, -1); //down right
        movesIndex = linearMoveSearch(board, moves, movesIndex, 0, -1); // down
        movesIndex = linearMoveSearch(board, moves, movesIndex, -1, -1); //down left
        movesIndex = linearMoveSearch(board, moves, movesIndex, -1, 0); //left
        linearMoveSearch(board, moves, movesIndex, -1, 1); //up left
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
        char moveFile = move.charAt(0);
        int moveRank = Integer.parseInt(move.substring(1, 2));
        char file = getFile();
        int rank = getRank();
        return ((moveRank == rank ^ moveFile == file) || (Math.abs(rank - moveRank) == Math.abs(file - moveFile)));
        //since a queen behaves either like a rook or bishop
    }
}