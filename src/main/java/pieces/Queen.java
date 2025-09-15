public class Queen extends LinearPiece{

    /**
     * Constructs a queen with the specified name, color, rank, and file.
     *
     * @param colour the colour of the queen ("White" or "Black")
     * @param square the square the queen is on.
     */
    public Queen(PieceColour colour, int square) {
        super(PieceType.QUEEN, colour, square);
    }

    /**
     * Generates all the legal moves the queen can do.
     * @param position the position that we are searching for moves on.
     * @return an array of all the moves the queen can do.
     */
    @Override
    public int[] generateMoves(Position position) {
        int[] moves = new int[ChessConstants.MAX_QUEEN_MOVES];
        int movesIndex = 0;
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.NONE + ChessDirections.UP);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.RIGHT + ChessDirections.UP);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.RIGHT + ChessDirections.NONE);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.RIGHT + ChessDirections.DOWN);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.NONE + ChessDirections.DOWN);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.LEFT + ChessDirections.DOWN);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.LEFT + ChessDirections.NONE);
        linearMoveSearch(position, moves, movesIndex, ChessDirections.LEFT + ChessDirections.UP);
        return moves;
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(int move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        int square = getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int moveFile = SquareMapUtils.getFileContribution(move);
        int moveRank = SquareMapUtils.getRankContribution(move);
        return ((moveFile == squareFile ^ moveRank == squareRank) || (Math.abs(squareRank - moveRank) / ChessConstants.RANK_OFFSET == Math.abs(squareFile - moveFile)));
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
    public boolean canCaptureSquare(Board board, int targetSquare) {
        if (!isLegalMove(targetSquare)) {
            return false;
        }
        int direction;
        int square = getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int targetFile = SquareMapUtils.getFileContribution(targetSquare);
        int targetRank = SquareMapUtils.getRankContribution(targetSquare);
        if (Math.abs(targetRank - squareRank) / ChessConstants.RANK_OFFSET == Math.abs(targetFile - squareFile)) {
            direction = getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        } else if ((targetFile == squareFile || targetRank == squareRank)) {
            direction = getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        } else {
            return false;
        }
        return recursiveCaptureCheck(board, targetSquare + direction, direction);
    }

    /**
     * Creates a copy of the queen at a given square.
     * @param square the square the piece copy will be at.
     * @return a queen object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(int square) {
        return new Queen(getColour(), square);
    }
}