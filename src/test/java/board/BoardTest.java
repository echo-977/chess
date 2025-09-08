import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    @DisplayName("Test pieceSearch")
    void testPieceSearch() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        Piece piece = board.pieceSearch("c1");
        assertEquals("c1", piece.getSquare());
        assertEquals(PieceType.BISHOP, piece.getType());
        piece = board.pieceSearch("d4");
        assertNull(piece);
    }

    @Test
    @DisplayName("Test findKing")
    void testFindKing() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        assertEquals("e1", whiteKing.getSquare());
        King blackKing = board.findKing(PieceColour.BLACK);
        assertEquals("e8", blackKing.getSquare());
    }

    @Test
    @DisplayName("Test getCaptureDestination")
    void testGetCaptureDestination() {
        Position position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Board board = position.getBoard();
        Piece piece = position.getBoard().pieceSearch("f5");
        Move move = new Move(position, piece, "g6");
        move.setEnPassant(true);
        assertEquals("g5", board.getCaptureDestination(move));
        piece = board.pieceSearch("g2");
        move = new Move(position, piece, "g5");
        move.setCapture(true);
        assertEquals("g5", board.getCaptureDestination(move));
        piece = board.pieceSearch("c6");
        move = new Move(position, piece, "g2");
        move.setCapture(true);
        assertEquals("g2", board.getCaptureDestination(move));
    }

    @Test
    @DisplayName("Test handleCastleMove")
    void testHandleCastle() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        Board board = position.getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move move = new Move(position, whiteKing, "g1");
        move.setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.WHITE_KINGSIDE_CASTLE_MASK));
        Piece piece = board.pieceSearch("f1");
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        board = position.getBoard();
        whiteKing = board.findKing(PieceColour.WHITE);
        move = new Move(position, whiteKing, "c1");
        move.setCastleMask(FENConstants.WHITE_QUEENSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.WHITE_QUEENSIDE_CASTLE_MASK));
        piece = board.pieceSearch("d1");
        assertEquals(PieceType.ROOK, piece.getType());
        King blackKing = board.findKing(PieceColour.BLACK);
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        board = position.getBoard();
        move = new Move(position, blackKing, "g8");
        move.setCastleMask(FENConstants.BLACK_KINGSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.BLACK_KINGSIDE_CASTLE_MASK));
        piece = board.pieceSearch("f8");
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        board = position.getBoard();
        blackKing = board.findKing(PieceColour.BLACK);
        move = new Move(position, blackKing, "c8");
        move.setCastleMask(FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.BLACK_QUEENSIDE_CASTLE_MASK));
        piece = board.pieceSearch("d8");
        assertEquals(PieceType.ROOK, piece.getType());
    }

    @Test
    @DisplayName("Test handlePromotion")
    void testHandlePromotion() {
        Position position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        Board board = position.getBoard();
        Piece piece = board.pieceSearch("d7");
        Move move = new Move(position, piece, "d8");
        move.setPromotionType(PieceType.ROOK);
        position.doMove(move);
        Piece promotion = board.pieceSearch("d8");
        assertEquals(PieceType.ROOK, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        move = new Move(position, piece, "e8");
        move.setPromotionType(PieceType.BISHOP);
        position.doMove(move);
        promotion = board.pieceSearch("e8");
        assertEquals(PieceType.BISHOP, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        piece = board.pieceSearch("e2");
        move = new Move(position, piece, "e1");
        move.setPromotionType(PieceType.QUEEN);
        board.handlePromotion(move);
        promotion = board.pieceSearch("e1");
        assertEquals(PieceType.QUEEN, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        move = new Move(position, piece, "d1");
        move.setPromotionType(PieceType.KNIGHT);
        position.doMove(move);
        promotion = board.pieceSearch("d1");
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
