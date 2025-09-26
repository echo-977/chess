public enum PieceType {
    PAWN, KING, QUEEN, ROOK, BISHOP, KNIGHT;

    /**
     * Simple check for if the piece is linear.
     * @return true if the piece is a linear piece, otherwise false.
     */
    public boolean isLinear() {
        return this == QUEEN || this == BISHOP || this == ROOK;
    }
}
