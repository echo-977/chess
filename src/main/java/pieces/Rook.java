public class Rook extends LinearPiece{

    /**
     * Constructs a rook with the specified name, color, rank, and file.
     *
     * @param colour the colour of the rook ("White" or "Black")
     * @param square the square the rook is on.

     */
    public Rook(PieceColour colour, int square) {
        super(PieceType.ROOK, colour, square);
    }

    /**
     * Generates all the legal moves the rook can do.
     * @param position the position that we are searching for moves on.
     * @return an array of all the moves the rook can do.
     */
    @Override
    public int[] generateMoves(Position position) {
        int[] moves = new int[ChessConstants.MAX_ROOK_MOVES];
        int movesIndex = 0;
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.LEFT);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.RIGHT);
        movesIndex = linearMoveSearch(position, moves, movesIndex, ChessDirections.UP);
        linearMoveSearch(position, moves, movesIndex, ChessDirections.DOWN);
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
        return (SquareMapUtils.getFileContribution(move) == SquareMapUtils.getFileContribution(square) ^
                SquareMapUtils.getRankContribution(move) == SquareMapUtils.getRankContribution(square));
    }

    /**
     * Check if the rook can capture a given square.
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
        int square = getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int targetFile = SquareMapUtils.getFileContribution(targetSquare);
        int targetRank = SquareMapUtils.getRankContribution(targetSquare);
        int direction = getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        return recursiveCaptureCheck(board, targetSquare + direction, direction);
    }

    /**
     * Creates a copy of the rook at a given square.
     * @param square the square the piece copy will be at.
     * @return a rook object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(int square) {
        return new Rook(getColour(), square);
    }
}
