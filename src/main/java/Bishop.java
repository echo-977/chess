public class Bishop extends LinearPiece{

    /**
     * Constructs a bishop with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Bishop(PieceColour colour, char file, int rank) {
        super(PieceType.BISHOP, colour, file, rank);
    }

    /**
     * Generates all the legal moves the bishop can do.
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the bishop can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        String[] moves = new String[ChessConstants.MAX_BISHOP_MOVES];
        int movesIndex = 0;
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.UP);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.UP);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.DOWN);
        linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.LEFT);
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
