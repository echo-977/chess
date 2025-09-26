import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Knight extends DirectionalPiece{

    /**
     * Constructs a knight with the specified name, color, rank, and file.
     *
     * @param colour the colour of the knight ("White" or "Black")
     * @param square the square the knight is on.
     */
    public Knight(PieceColour colour, int square) {
        super(PieceType.KNIGHT, colour, square);
    }

    /**
     * Generates all the legal moves the knight can do.
     * @param position the position that we are searching for moves on.
     * @param moves array list legal moves are to be added to.
     */
    @Override
    public void generateMoves(Position position, IntArrayList moves) {
        int square = getSquare();
        Piece piece;
        for (int candidateMove : MoveTables.knightMoves[square]) {
            if (isLegalMove(candidateMove)) {
                piece = position.getBoard().pieceSearch(candidateMove);
                if (piece == null || piece.getColour() != getColour()) { //opposite coloured piece so capture
                    moves.add(Move.encodeMove(position, candidateMove, square));
                }
            }
        }
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
        if (Math.abs(moveRank - squareRank) == ChessConstants.RANK_OFFSET && Math.abs(moveFile - squareFile) == 2 * ChessConstants.FILE_OFFSET) {
            return true;
        } else {
            return Math.abs(moveRank - squareRank) == 2 * ChessConstants.RANK_OFFSET && Math.abs(moveFile - squareFile) == ChessConstants.FILE_OFFSET;
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
    public boolean canCaptureSquare(Board board, int targetSquare) {
        return isLegalMove(targetSquare); //if the knight can move to where the king is it can capture it
    }

    /**
     * Creates a copy of the knight at a given square.
     * @param square the square the piece copy will be at.
     * @return a knight object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(int square) {
        return new Knight(getColour(), square);
    }
}

