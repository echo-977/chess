import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    @DisplayName("Test pieceSearch")
    void testPieceSearch() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        Piece piece = board.pieceSearch(Squares.C1);
        assertEquals(Squares.C1, piece.getSquare());
        assertEquals(PieceType.BISHOP, piece.getType());
        piece = board.pieceSearch(Squares.D4);
        assertNull(piece);
    }

    @Test
    @DisplayName("Test findKing")
    void testFindKing() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        assertEquals(Squares.E1, whiteKing.getSquare());
        King blackKing = board.findKing(PieceColour.BLACK);
        assertEquals(Squares.E8, blackKing.getSquare());
    }

    @Test
    @DisplayName("Test getCaptureDestination")
    void testGetCaptureDestination() {
        Position position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Board board = position.getBoard();
        int moveFlag = MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT;
        assertEquals(Squares.G5, board.getCaptureDestination(moveFlag, Squares.G6, Squares.F5));
        moveFlag = MoveFlags.CAPTURE_BIT;
        assertEquals(Squares.G5, board.getCaptureDestination(moveFlag, Squares.G5, Squares.G2));
        assertEquals(Squares.G2, board.getCaptureDestination(moveFlag, Squares.G2, Squares.C6));
    }

    @Test
    @DisplayName("Test handleCastleMove")
    void testHandleCastle() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 1 1");
        Board board = position.getBoard();
        int moveFlag = MoveFlags.KINGSIDE_CASTLE;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.G1 << MoveFlags.DESTINATION_SHIFT | Squares.E1);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.WHITE_KINGSIDE_CASTLE_MASK));
        Piece piece = board.pieceSearch(Squares.F1);
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 1 1");
        board = position.getBoard();
        moveFlag = MoveFlags.QUEENSIDE_CASTLE;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.C1 << MoveFlags.DESTINATION_SHIFT | Squares.E1);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.WHITE_QUEENSIDE_CASTLE_MASK));
        piece = board.pieceSearch(Squares.D1);
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 1 1");
        board = position.getBoard();
        moveFlag = MoveFlags.KINGSIDE_CASTLE;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.G8 << MoveFlags.DESTINATION_SHIFT | Squares.E8);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.BLACK_KINGSIDE_CASTLE_MASK));
        piece = board.pieceSearch(Squares.F8);
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 1 1");
        board = position.getBoard();
        moveFlag = MoveFlags.QUEENSIDE_CASTLE;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.C8 << MoveFlags.DESTINATION_SHIFT | Squares.E8);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.BLACK_QUEENSIDE_CASTLE_MASK));
        piece = board.pieceSearch(Squares.D8);
        assertEquals(PieceType.ROOK, piece.getType());
    }

    @Test
    @DisplayName("Test handlePromotion")
    void testHandlePromotion() {
        Position position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        Board board = position.getBoard();
        int moveFlag = MoveFlags.PROMOTION_BIT | MoveFlags.ROOK;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.D8 << MoveFlags.DESTINATION_SHIFT | Squares.D7);
        Piece promotion = board.pieceSearch(Squares.D8);
        assertEquals(PieceType.ROOK, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        moveFlag = MoveFlags.PROMOTION_BIT | MoveFlags.BISHOP | MoveFlags.CAPTURE_BIT;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.E8 << MoveFlags.DESTINATION_SHIFT | Squares.D7);
        promotion = board.pieceSearch(Squares.E8);
        assertEquals(PieceType.BISHOP, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        moveFlag = MoveFlags.PROMOTION_BIT | MoveFlags.QUEEN;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.E1 << MoveFlags.DESTINATION_SHIFT | Squares.E2);
        promotion = board.pieceSearch(Squares.E1);
        assertEquals(PieceType.QUEEN, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        moveFlag = MoveFlags.PROMOTION_BIT | MoveFlags.KNIGHT;
        position.doMove(moveFlag << MoveFlags.FLAG_SHIFT | Squares.D1 << MoveFlags.DESTINATION_SHIFT | Squares.E2);
        promotion = board.pieceSearch(Squares.D1);
        assertEquals(PieceType.KNIGHT, promotion.getType());
        assertEquals(PieceColour.BLACK, promotion.getColour());
    }

    @Test
    @DisplayName("Test clone")
    void testClone() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        Board board2 = board.copy();
        assertEquals(board, board2);
    }
}
