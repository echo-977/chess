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
    public DirectionalPiece(PieceType type, String colour, char file, int rank) {
        super(type, colour, file, rank);
    }

    /**
     * Searches each given direction from the piece, checking if it is valid to move to that square.
     * Used in the move generation methods for rook, bishop and queen
     * @param board the board the piece is moving on
     * @param moves the current string of moves generated (legal moves are added to this)
     * @param directions array of 2d directions the piece can go in
     */
    public void directionalMoveSearch(Board board, String[] moves, int[][] directions) {
        char file = getFile();
        int rank = getRank();
        char checkFile;
        int checkRank;
        String candidateMove;
        Piece piece;
        int movesIndex = 0;
        for (int i = 0; i < 8; i++) {
            checkFile = (char) (file + directions[i][0]);
            checkRank = rank + directions[i][1];
            candidateMove = checkFile + String.valueOf(checkRank);
            if (isLegalMove(candidateMove)) {
                piece = board.pieceSearch(candidateMove);
                if (piece == null || !piece.getColour().equals(getColour())) {
                    moves[movesIndex] = candidateMove;
                    movesIndex++;
                }
            }
        }
    }
}
