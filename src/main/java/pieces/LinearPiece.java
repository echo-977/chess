import it.unimi.dsi.fastutil.ints.IntArrayList;

public abstract class LinearPiece extends Piece {

    /**
     * Constructs a chess piece with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param type   the type of the piece (e.g., "Pawn", "Knight")
     * @param colour the colour of the piece ("White" or "Black")
     * @param square the square the piece is on.
     */
    public LinearPiece(PieceType type, PieceColour colour, int square) {
        super(type, colour, square);
    }

    /**
     * Searches out in a given direction from the piece, checking if it is valid to move to that square.
     * Used in the move generation methods for rook, bishop and queen.
     *
     * @param position      the position we are searching for moves in.
     * @param moves         array list legal moves are to be added to.
     * @param direction     the direction to go in.
     */
    public void linearMoveSearch(Position position, IntArrayList moves, int direction) {
        int square = getSquare();
        int candidateMoveSquare = square + direction;
        int squareRank = SquareMapUtils.getRankContribution(square);
        Piece piece;
        while (candidateMoveSquare >= 0 && candidateMoveSquare <= 63) {
            if (direction == ChessDirections.LEFT || direction == ChessDirections.RIGHT) {
                if (candidateMoveSquare < squareRank || candidateMoveSquare >= squareRank + ChessConstants.RANK_OFFSET) {
                    break;
                }
            }
            int prevFile = SquareMapUtils.getFileContribution(candidateMoveSquare - direction);
            int prevRank = SquareMapUtils.getRankContribution(candidateMoveSquare - direction);
            int currentFile = SquareMapUtils.getFileContribution(candidateMoveSquare);
            int currentRank = SquareMapUtils.getRankContribution(candidateMoveSquare);
            if (direction == ChessDirections.LEFT + ChessDirections.UP ||
                    direction == ChessDirections.RIGHT + ChessDirections.UP ||
                    direction == ChessDirections.LEFT + ChessDirections.DOWN ||
                    direction == ChessDirections.RIGHT + ChessDirections.DOWN) {
                if (Math.abs(currentFile - prevFile) != ChessConstants.FILE_OFFSET ||
                        Math.abs(currentRank - prevRank) != ChessConstants.RANK_OFFSET) {
                    break;
                }
            }
            if (isLegalMove(candidateMoveSquare)) {
                piece = position.getBoard().pieceSearch(candidateMoveSquare);
                if (piece == null) { //no piece so the move is legal
                   Move.createIfLegal(position, moves, candidateMoveSquare, square);
                } else if (piece.getColour() != getColour()) { //opposite coloured piece so capture
                    Move.createIfLegal(position, moves, candidateMoveSquare, square);
                    break;
                } else { //same coloured piece
                    break;
                }
            }
            candidateMoveSquare += direction;
        }
    }

    /**
     * Recursive function to move from a certain square towards the piece, checking that each square is empty.
     *
     * @param board the board we are searching on.
     * @param currentTarget the current square to check.
     * @param direction the direction to go towards the piece's square.
     * @return true or false value for whether the piece can capture the given square
     */
    public boolean recursiveCaptureCheck(Board board, int currentTarget, int direction) {
        Piece pieceInPath = board.pieceSearch(currentTarget);
        if (currentTarget == getSquare()) { //base case, we are back to the piece's square
            return true;
        } else if (pieceInPath != null) { //if we run into a piece then we cannot move to the target
            if (pieceInPath instanceof King && pieceInPath.getColour() != getColour()) {
                return recursiveCaptureCheck(board, currentTarget + direction, direction);
                //if it is the enemy king we can move to the square on the next move since the king must move
                // out of the way (it is in check), therefore we continue checking
            } else {
                return false;
            }
        } else { //recursive step
            return recursiveCaptureCheck(board, currentTarget + direction, direction);
        }
    }

    /**
     * Used as part of subclasses canCaptureKing method to get the necessary direction for performing a recursive capture check.
     *
     * @param squareFile the square's file.
     * @param squareRank the square's rank.
     * @param targetFile the target square's file.
     * @param targetRank the target square's rank.
     * @return the direction to add to the target square to reach the pieces square.
     */
    protected int getOrthogonalDirection(int squareFile, int squareRank, int targetFile, int targetRank) {
        if (targetFile == squareFile) { //files are equal
            return (targetRank > squareRank) ? ChessDirections.UP : ChessDirections.DOWN;
        } else { //ranks are equal
            return (targetFile > squareFile) ? ChessDirections.LEFT : ChessDirections.RIGHT;
        }
    }

    /**
     * Used as part of subclasses canCaptureKing method to get the necessary directions for performing a recursive capture check.
     *
     * @param squareFile the square's file.
     * @param squareRank the square's rank.
     * @param targetFile the target square's file.
     * @param targetRank the target square's rank.
     */
    protected int getDiagonalDirection(int squareFile, int squareRank, int targetFile, int targetRank) {
        int fileDirection = (targetFile > squareFile) ? ChessDirections.LEFT : ChessDirections.RIGHT;
        int rankDirection = (targetRank > squareRank) ? ChessDirections.UP : ChessDirections.DOWN;
        return fileDirection + rankDirection;
    }
}
