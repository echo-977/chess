public abstract class LinearPiece extends Piece {

    /**
     * Constructs a chess piece with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param name   the type/name of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public LinearPiece(String name, String colour, char file, int rank) {
        super(name, colour, file, rank);
    }

    /**
     * Searches out in a given direction from the piece, checking if it is valid to move to that square.
     * Used in the move generation methods for rook, bishop and queen
     * @param board the board the piece is moving on
     * @param moves the current string of moves generated (legal moves are added to this)
     * @param movesIndex the index of the moves array
     * @param fileDirection direction to go in for the file (e.g. -1, 0, 1)
     * @param rankDirection direction to go in for the rank (e.g. -1, 0, 1)
     * @return new index for the next available space in the moves array
     */
    public int linearMoveSearch(Board board, String[] moves, int movesIndex, int fileDirection, int rankDirection) {
        char file = getFile();
        int rank = getRank();
        String candidateMove;
        Piece piece;
        while (!(file < 'a' || file > 'h' || rank < 1 || rank > 8)) {
            file = (char) (file + fileDirection);
            rank += rankDirection;
            candidateMove = file + String.valueOf(rank);
            if (isLegalMove(candidateMove)) {
                piece = board.pieceSearch(candidateMove);
                if (piece == null) { //no piece so the move is legal
                    moves[movesIndex] = candidateMove;
                    movesIndex++;
                } else if (!piece.getColour().equals(getColour())) { //opposite coloured piece so capture
                    moves[movesIndex] = candidateMove;
                    movesIndex++;
                    break;
                } else { //same coloured piece
                    break;
                }
            }
        }
        return movesIndex;
    }
}
