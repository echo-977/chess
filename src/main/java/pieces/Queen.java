public class Queen extends LinearPiece{

    /**
     * Constructs a queen with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Queen(PieceColour colour, char file, int rank) {
        super(PieceType.QUEEN, colour, file, rank);
    }

    /**
     * Generates all the legal moves the queen can do.
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the queen can move to as strings.
     */
    @Override
    public Move[] generateMoves(Board board) {
        Move[] moves = new Move[ChessConstants.MAX_QUEEN_MOVES];
        int movesIndex = 0;
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.NONE, ChessDirections.UP);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.UP);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.NONE);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.DOWN);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.NONE, ChessDirections.DOWN);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.DOWN);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.NONE);
        linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.UP);
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
        char moveFile = SquareMapUtils.getFile(move);
        int moveRank = SquareMapUtils.getRank(move);
        char file = getFile();
        int rank = getRank();
        return ((moveRank == rank ^ moveFile == file) || (Math.abs(rank - moveRank) == Math.abs(file - moveFile)));
        //since a queen behaves either like a rook or bishop
    }

    /**
     * Check if the queen can capture a given square.
     * Used for detection of checks.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    @Override
    public boolean canCaptureSquare(Board board, String targetSquare) {
        char targetFile = SquareMapUtils.getFile(targetSquare);
        int targetRank = SquareMapUtils.getRank(targetSquare);
        if (!isLegalMove(targetSquare)) {
            return false;
        }
        int[] directions;
        int fileDirection, rankDirection;
        char file = getFile();
        int rank = getRank();
        if (Math.abs(targetFile - file) == Math.abs(targetRank - rank)) {
            directions = getDiagonalDirections(targetFile, targetRank);
        } else if ((targetFile == file || targetRank == rank)) {
            directions = getOrthogonalDirections(targetFile, targetRank);
        } else {
            return false;
        }
        fileDirection = directions[ChessConstants.FILE_DIRECTION_INDEX];
        rankDirection = directions[ChessConstants.RANK_DIRECTION_INDEX];
        return recursiveCaptureCheck(board, (char) (targetFile + fileDirection), targetRank + rankDirection, fileDirection, rankDirection);
    }

    /**
     * Creates a copy of the queen at a given square.
     * @param square the square the piece copy will be at.
     * @return a queen object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(String square) {
        char file = SquareMapUtils.getFile(square);
        int rank = SquareMapUtils.getRank(square);
        return new Queen(getColour(), file, rank);
    }
}