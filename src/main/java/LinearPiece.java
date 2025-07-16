public abstract class LinearPiece extends Piece {

    /**
     * Constructs a chess piece with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param type   the type of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public LinearPiece(PieceType type, PieceColour colour, char file, int rank) {
        super(type, colour, file, rank);
    }

    /**
     * Searches out in a given direction from the piece, checking if it is valid to move to that square.
     * Used in the move generation methods for rook, bishop and queen
     *
     * @param board the board the piece is moving on
     * @param moves the current string of moves generated (legal moves are added to this)
     * @param movesIndex the index of the moves array
     * @param fileDirection direction to go in for the file (e.g. -1, 0, 1)
     * @param rankDirection direction to go in for the rank (e.g. -1, 0, 1)
     * @return new index for the next available space in the moves array
     */
    public int linearMoveSearch(Board board, Move[] moves, int movesIndex, int fileDirection, int rankDirection) {
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
                    moves[movesIndex] = new Move(this, candidateMove);
                    movesIndex++;
                } else if (piece.getColour() != getColour()) { //opposite coloured piece so capture
                    moves[movesIndex] = new Move(this, candidateMove);
                    movesIndex++;
                    break;
                } else { //same coloured piece
                    break;
                }
            }
        }
        return movesIndex;
    }

    /**
     * Recursive function to move from a certain square towards the piece, checking that each square is empty.
     *
     * @param board the board we are searching on.
     * @param file the current file of the square to check.
     * @param rank the current rank of the square to check.
     * @param fileDirection the direction to go towards the piece's square for the file.
     * @param rankDirection the direction to go towards the piece's square for the rank.
     * @return true or false value for whether the piece can capture the given square
     */
    public boolean recursiveCaptureCheck(Board board, char file, int rank, int fileDirection, int rankDirection) {
        String currentSquare = file + String.valueOf(rank);
        if (currentSquare.equals(getSquare())) { //base case, we are back to the piece's square
            return true;
        } else if (board.pieceSearch(currentSquare) != null) { //if we run into a piece then we cannot move to the target
            return false;
        } else { //recursive step
            return recursiveCaptureCheck(board, (char) (file + fileDirection), rank + rankDirection, fileDirection,  rankDirection);
        }
    }

    /**
     * Used as part of subclasses canCaptureKing method to get the necessary directions for performing a recursive capture check.
     *
     * @param targetFile the file of the target square.
     * @param targetRank the rank of the target square.
     */
    protected int[] getOrthogonalDirections(char targetFile, int targetRank) {
        int file = getFile();
        int rank = getRank();
        int fileDirection, rankDirection;
        if (targetFile == file) {
            fileDirection = ChessDirections.NONE;
            if (targetRank > rank) {
                rankDirection = ChessDirections.DOWN;
            } else {
                rankDirection = ChessDirections.UP;
            }
        } else {
            rankDirection = ChessDirections.NONE;
            if (targetFile > file) {
                fileDirection = ChessDirections.LEFT;
            } else {
                fileDirection = ChessDirections.RIGHT;
            }
        }
        return new int[] {fileDirection, rankDirection};
    }

    /**
     * Used as part of subclasses canCaptureKing method to get the necessary directions for performing a recursive capture check.
     *
     * @param targetFile the file of the target square.
     * @param targetRank the rank of the target square.
     */
    protected int[] getDiagonalDirections(char targetFile, int targetRank) {
        int file = getFile();
        int rank = getRank();
        int fileDirection, rankDirection;
        if (targetFile > file) {
            fileDirection = ChessDirections.LEFT;
        } else {
            fileDirection = ChessDirections.RIGHT;
        }
        if (targetRank > rank) {
            rankDirection = ChessDirections.DOWN;
        } else {
            rankDirection = ChessDirections.UP;
        }
        return new int[] {fileDirection, rankDirection};
    }
}
