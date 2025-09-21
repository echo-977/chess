import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Bishop extends LinearPiece{

    /**
     * Constructs a bishop with the specified name, color, rank, and file.
     *
     * @param colour the colour of the bishop ("White" or "Black")
     * @param square the square the bishop is on.
     */
    public Bishop(PieceColour colour, int square) {
        super(PieceType.BISHOP, colour, square);
    }

    /**
     * Generates all the legal moves the bishop can do.
     * @param position the position that we are searching for moves on.
     * @param moves array list legal moves are to be added to.
     */
    @Override
    public void generateMoves(Position position, IntArrayList moves) {
        int square = getSquare();
        int[][] bishopMoves = MoveTables.bishopMoves[square];
        Piece piece;
        for (int direction = 0; direction < ChessConstants.BISHOP_DIRECTIONS; direction++) {
            for (int candidateMove : bishopMoves[direction]) {
                piece = position.getBoard().pieceSearch(candidateMove);
                if (piece == null) { //no piece so the move is legal
                    Move.createIfLegal(position, moves, candidateMove, square);
                } else if (piece.getColour() != getColour()) { //opposite coloured piece so capture
                    Move.createIfLegal(position, moves, candidateMove, square);
                    break;
                } else { //same coloured piece
                    break;
                }
            }
        }
    }

    /**
     * Check if a given move is legal.
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
        return Math.abs(moveRank - squareRank) / ChessConstants.RANK_OFFSET == Math.abs(moveFile - squareFile);

    }

    /**
     * Check if the bishop can capture a given square.
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
        if (Math.abs(targetRank - squareRank) / ChessConstants.RANK_OFFSET == Math.abs(targetFile - squareFile)) {
            int direction = getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
            return recursiveCaptureCheck(board, targetSquare + direction, direction);
        } else {
            return false;
        }
    }

    /**
     * Creates a copy of the bishop at a given square.
     * @param square the square the piece copy will be at.
     * @return a bishop object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(int square) {
        return new Bishop(getColour(), square);
    }
}
