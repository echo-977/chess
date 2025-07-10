public class Pawn extends Piece{
    private boolean moved;
    private boolean enPassantable;

    /**
     * Constructs a pawn with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour         the colour of the piece ("White" or "Black")
     * @param file           the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank           the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param moved          whether the pawn has moved
     * @param enPassantable  whether the pawn can be taken by en passant
     */
    public Pawn(String colour, char file, int rank, boolean moved, boolean enPassantable) {
        super("Pawn", colour, file, rank);
        this.moved = moved;
        this.enPassantable = enPassantable;
    }

    /**
     * Generates all the legal moves the queen can do (without considering other pieces).
     * @return an array of all the squares the queen can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        char file = getFile();
        int checkRank = getRank();
        String[] moves = new String[4]; //number of pawn moves in any position
        String move;
        int moveDirection;
        int movesIndex = 0;
        if (getColour().equals("White")) {
            moveDirection = 1;
        } else {
            moveDirection = -1;
        }
        for (int i = 0; i < 2; i++) {
            checkRank = checkRank + moveDirection;
            move = String.valueOf(file) + String.valueOf(checkRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            } else {
                break;
            }
        }
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
        char file = getFile();
        int rank = getRank();
        char moveFile = move.charAt(0);
        int moveRank = Integer.parseInt(move.substring(1, 2));
        if (Math.abs(moveRank - rank) == 1 && moveFile == file) {
            return true;
        }
        else {
            return (!getMoved() && Math.abs(moveRank - rank) == 2 && moveFile == file);
        }

    }

    /**
     * Overrides the move method in the piece class to update the moved flag.
     * @param move the square to move the piece to.
     */
    @Override
    public void move(String move) {
        if (!moved) {
            moved = true;
            enPassantable = true;
        } else {
            enPassantable = false;
        }
        super.move(move);
    }

    /**
     * Simple getter for the boolean moved
     * @return the moved boolean
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * Simple getter for the boolean enPassantable
     * @return the enPassantable boolean
     */
    public boolean getEnPassantable() {
        return enPassantable;
    }
}


