public record State(
        int move,
        Piece capturedPiece,
        int enPassantSquare,
        int castlingRights,
        int halfMoveClock,
        int moveCount,
        long[] ATKFR,
        long[] ATKTO,
        long zobristKey
) {}