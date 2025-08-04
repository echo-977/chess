import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Rook extends LinearPiece{
    private boolean moved;

    /**
     * Constructs a rook with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param moved  boolean for if the rook has moved (used in castling logic)
     */
    public Rook(PieceColour colour, char file, int rank, boolean moved) {
        super(PieceType.ROOK, colour, file, rank);
        this.moved = moved;
    }

    /**
     * Generates all the legal moves the rook can do.
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the rook can move to as strings.
     */
    @Override
    public Move[] generateMoves(Board board) {
        Move[] moves = new Move[ChessConstants.MAX_ROOK_MOVES];
        int movesIndex = 0;
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.NONE);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.NONE);
        movesIndex = linearMoveSearch(board, moves, movesIndex, ChessDirections.NONE, ChessDirections.UP);
        linearMoveSearch(board, moves, movesIndex, ChessDirections.NONE, ChessDirections.DOWN);
        return moves;
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(String move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        char file = move.charAt(0);
        int rank = move.charAt(1) - '0';
        return (rank == getRank() ^ file == getFile());
    }

    /**
     * Overrides the move method in the piece class to update the moved flag.
     * @param move the square to move the piece to.
     */
    @Override
    public void move(String move) {
        if (!moved) {
            moved = true;
        }
        super.move(move);
    }

    /**
     * Check if the rook can capture a given square.
     * Used for detection of checks.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    @Override
    public boolean canCaptureSquare(Board board, String targetSquare) {
        char targetFile = targetSquare.charAt(0);
        int targetRank = targetSquare.charAt(1) - '0';
        if (!isLegalMove(targetSquare)) {
            return false;
        }
        int [] directions;
        int fileDirection, rankDirection;
        char file = getFile();
        int rank = getRank();
        if ((targetFile == file || targetRank == rank)) {
            directions = getOrthogonalDirections(targetFile, targetRank);
        } else {
            return false;
        }
        fileDirection = directions[ChessConstants.FILE_DIRECTION_INDEX];
        rankDirection = directions[ChessConstants.RANK_DIRECTION_INDEX];
        return recursiveCaptureCheck(board, (char) (targetFile + fileDirection), targetRank + rankDirection, fileDirection, rankDirection);
    }

    /**
     * Simple getter for the boolean moved
     * @return the moved boolean
     */
    public boolean getMoved() {
        return moved;
    }
    
    /**
     * Creates a copy of the rook at a given square.
     * @param square the square the piece copy will be at.
     * @return a rook object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(String square) {
        char file = square.charAt(0);
        int rank = square.charAt(1) - '0';
        return new Rook(getColour(), file, rank, getMoved());
    }
}
