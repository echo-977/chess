public enum PieceColour {
    WHITE, BLACK;

    public PieceColour opponentColour() {
        if (this == WHITE) {
            return PieceColour.BLACK;
        } else {
            return PieceColour.WHITE;
        }
    }
}
