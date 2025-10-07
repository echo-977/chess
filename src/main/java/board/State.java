public record State(
        int move,
        Piece capturedPiece,
        int enPassantSquare,
        int castlingRights,
        int halfMoveClock,
        int moveCount,
        long whiteThreatMap,
        long blackThreatMap,
        long[] ATKFR,
        long[] ATKTO
) {}