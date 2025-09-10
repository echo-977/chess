public record State(
        Move move,
        Piece capturedPiece,
        int enPassantSquare,
        int castlingRights,
        int halfMoveClock,
        int moveCount,
        boolean[] whiteThreatMap,
        boolean[] blackThreatMap
) {}