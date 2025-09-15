public abstract class DirectionalPiece extends Piece {

    /**
     * Constructs a chess piece with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param type   the type of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param square the square the piece is on.
     */
    public DirectionalPiece(PieceType type, PieceColour colour, int square) {
        super(type, colour, square);
    }

    /**
     * Searches each given direction from the piece, checking if it is valid to move to that square.
     * Used in the move generation methods for king and knight.
     * @param position the position the piece is moving on.
     * @param moves the array of moves generated (legal moves are added to this).
     * @param directions array of 2d directions the piece can go in.
     */
    public void directionalMoveSearch(Position position, int[] moves, int[] directions) {
        int square = getSquare();
        int candidateMove;
        Piece piece;
        int movesIndex = 0;
        for (int i = 0; i < ChessConstants.NUM_DIRECTIONS; i++) {
            candidateMove = square + directions[i];
            if (isLegalMove(candidateMove)) {
                piece = position.getBoard().pieceSearch(candidateMove);
                if (piece == null || piece.getColour() != getColour()) { //opposite coloured piece so capture
                    moves[movesIndex] = Move.createIfLegal(position, candidateMove, square);
                    if (moves[movesIndex] != MoveFlags.NO_MOVE) {
                        movesIndex++;
                    }
                }
            }
        }
    }
}
