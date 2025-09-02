public record State(
        Move move,
        Piece capturedPiece,
        String enPassantSquare,
        int castlingRights,
        int halfMoveClock,
        int moveCount,
        boolean[] whiteThreatMap,
        boolean[] blackThreatMap
) {}