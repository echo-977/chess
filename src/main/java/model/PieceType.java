public enum PieceType {
    PAWN(100), KING(0), QUEEN(900), ROOK(500), BISHOP(300), KNIGHT(300);

    private final int value;

    /**
     * Allows a piece to hold a value.
     * @param value the value of the piece.
     */
    PieceType(int value) {
        this.value = value;
    }

    /**
     * Simple getter for the value of a piece type in centipawns
     * @return the integer value of the piece
     */
    public int getValue() {
        return value;
    }

    /**
     * Simple check for if the piece is linear.
     * @return true if the piece is a linear piece, otherwise false.
     */
    public boolean isLinear() {
        return this == QUEEN || this == BISHOP || this == ROOK;
    }

    /**
     * Checks if the piece can travel in that direction (only called by linear pieces).
     * @param direction the direction to check.
     * @return boolean value for if the piece can travel in that direction.
     */
    public boolean canTravel(int direction) {
        int index = -1;
        for (int i = 0; i < ChessConstants.NUM_DIRECTIONS; i++) {
            if (direction == ChessDirections.ALL[i]) {
                index = i;
            }
        }
        if (index == -1) {
            return false;
        }
        return switch (this) {
            case ROOK -> (index < 4);
            case BISHOP -> (index >= 4);
            default -> true;
        };
    }
}
