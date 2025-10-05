public enum PieceType {
    PAWN, KING, QUEEN, ROOK, BISHOP, KNIGHT;

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
