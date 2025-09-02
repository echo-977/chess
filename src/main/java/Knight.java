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
    public Move[] generateMoves(Board board) {
        Move[] moves = new Move[ChessConstants.MAX_KNIGHT_MOVES];
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
        char moveFile = SquareMapUtils.getFile(move);
        int moveRank = SquareMapUtils.getRank(move);
        if (Math.abs(moveRank - rank) == 1 && Math.abs(moveFile - file) == 2) {
            return true;
        } else {
            return Math.abs(moveRank - rank) == 2 && Math.abs(moveFile - file) == 1;
        }
    }

    /**
     * Check if the knight can capture a given square.
     * Used for detection of checks.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    @Override
    public boolean canCaptureSquare(Board board, String targetSquare) {
        return isLegalMove(targetSquare); //if the knight can move to where the king is it can capture it
    }

    /**
     * Creates a copy of the knight at a given square.
     * @param square the square the piece copy will be at.
     * @return a knight object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(String square) {
        char file = SquareMapUtils.getFile(square);
        int rank = SquareMapUtils.getRank(square);
        return new Knight(getColour(), file, rank);
    }
}

