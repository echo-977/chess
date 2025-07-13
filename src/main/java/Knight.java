public class Knight extends DirectionalPiece{

    /**
     * Constructs a knight with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Knight(PieceColour colour, char file, int rank) {
        super(PieceType.KNIGHT, colour, file, rank);
    }

    /**
     * Generates all the legal moves the knight can do.
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the knight can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        String[] moves = new String[ChessConstants.MAX_KNIGHT_MOVES];
        int[][] directions = {
                {ChessDirections.RIGHT, 2 * ChessDirections.UP}, {2 * ChessDirections.RIGHT, ChessDirections.UP},
                {2 * ChessDirections.RIGHT, ChessDirections.DOWN}, {ChessDirections.RIGHT, 2 * ChessDirections.DOWN},
                {ChessDirections.LEFT, 2 * ChessDirections.DOWN}, {2 * ChessDirections.LEFT, ChessDirections.DOWN},
                {2 * ChessDirections.LEFT, ChessDirections.UP}, {ChessDirections.LEFT, 2 * ChessDirections.UP}
        };
        directionalMoveSearch(board, moves, directions);
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
        char file = getFile();
        int rank = getRank();
        char moveFile = move.charAt(0);
        int moveRank = Integer.parseInt(move.substring(1, 2));
        if (Math.abs(moveRank - rank) == 1 && Math.abs(moveFile - file) == 2) {
            return true;
        } else {
            return Math.abs(moveRank - rank) == 2 && Math.abs(moveFile - file) == 1;
        }
    }
}

