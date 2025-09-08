public abstract class DirectionalPiece extends Piece {

    /**
     * Constructs a chess piece with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param type   the type of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public DirectionalPiece(PieceType type, PieceColour colour, char file, int rank) {
        super(type, colour, file, rank);
    }

    /**
     * Searches each given direction from the piece, checking if it is valid to move to that square.
     * Used in the move generation methods for king and knight.
     * @param position the position the piece is moving on.
     * @param moves the current string of moves generated (legal moves are added to this).
     * @param directions array of 2d directions the piece can go in.
     */
    public void directionalMoveSearch(Position position, Move[] moves, int[][] directions) {
        char file = getFile();
        int rank = getRank();
        char checkFile;
        int checkRank;
        String candidateMove;
        Piece piece;
        int movesIndex = 0;
        for (int i = 0; i < 8; i++) {
            checkFile = (char) (file + directions[i][ChessConstants.FILE_DIRECTION_INDEX]);
            checkRank = rank + directions[i][ChessConstants.RANK_DIRECTION_INDEX];
            candidateMove = checkFile + String.valueOf(checkRank);
            if (isLegalMove(candidateMove)) {
                piece = position.getBoard().pieceSearch(candidateMove);
                if (piece == null || piece.getColour() != getColour()) { //opposite coloured piece so capture
                    moves[movesIndex] = Move.createIfLegal(position, candidateMove, this);
                    if (moves[movesIndex] != null) {
                        movesIndex++;
                    }
                }
            }
        }
    }
}
