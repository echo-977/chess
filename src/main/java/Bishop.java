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
    public Move[] generateMoves(Board board) {
        Move[] moves = new Move[ChessConstants.MAX_BISHOP_MOVES];
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
        int moveRank = move.charAt(1) - '0';
        char file = getFile();
        int rank = getRank();
        return Math.abs(rank - moveRank) == Math.abs(file - moveFile);

    }

    /**
     * Check if the bishop can capture a given square.
     * Used for detection of checks.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    @Override
    public boolean canCaptureKing(Board board, String targetSquare) {
        char targetFile = targetSquare.charAt(0);
        int targetRank = targetSquare.charAt(1) - '0';
        if (!isLegalMove(targetSquare)) {
            return false;
        }
        int[] directions;
        int fileDirection, rankDirection;
        char file = getFile();
        int rank = getRank();
        if ((targetFile - file) == (targetRank - rank) || -(targetFile - file) == (targetRank - rank)) {
            directions = getDiagonalDirections(targetFile, targetRank);
        } else {
            return false;
        }
        fileDirection = directions[ChessConstants.FILE_DIRECTION_INDEX];
        rankDirection = directions[ChessConstants.RANK_DIRECTION_INDEX];
        return recursiveCaptureCheck(board, (char) (targetFile + fileDirection), targetRank + rankDirection, fileDirection, rankDirection);
    }
}
