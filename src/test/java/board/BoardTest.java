import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    @DisplayName("Test pieceSearch")
    void testPieceSearch() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        Piece piece = board.pieceSearch(Files.C + Ranks.ONE);
        assertEquals(Files.C + Ranks.ONE, piece.getSquare());
        assertEquals(PieceType.BISHOP, piece.getType());
        piece = board.pieceSearch(Files.D + Ranks.FOUR);
        assertNull(piece);
    }

    @Test
    @DisplayName("Test findKing")
    void testFindKing() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1").getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        assertEquals(Files.E + Ranks.ONE, whiteKing.getSquare());
        King blackKing = board.findKing(PieceColour.BLACK);
        assertEquals(Files.E + Ranks.EIGHT, blackKing.getSquare());
    }

    @Test
    @DisplayName("Test getCaptureDestination")
    void testGetCaptureDestination() {
        Position position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Board board = position.getBoard();
        Piece piece = position.getBoard().pieceSearch(Files.F + Ranks.FIVE);
        Move move = new Move(position, piece, Files.G + Ranks.SIX);
        move.setEnPassant(true);
        assertEquals(Files.G + Ranks.FIVE, board.getCaptureDestination(move));
        piece = board.pieceSearch(Files.G + Ranks.TWO);
        move = new Move(position, piece, Files.G + Ranks.FIVE);
        move.setCapture(true);
        assertEquals(Files.G + Ranks.FIVE, board.getCaptureDestination(move));
        piece = board.pieceSearch(Files.C + Ranks.SIX);
        move = new Move(position, piece, Files.G + Ranks.TWO);
        move.setCapture(true);
        assertEquals(Files.G + Ranks.TWO, board.getCaptureDestination(move));
    }

    @Test
    @DisplayName("Test handleCastleMove")
    void testHandleCastle() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        Board board = position.getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move move = new Move(position, whiteKing, Files.G + Ranks.ONE);
        move.setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.WHITE_KINGSIDE_CASTLE_MASK));
        Piece piece = board.pieceSearch(Files.F + Ranks.ONE);
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        board = position.getBoard();
        whiteKing = board.findKing(PieceColour.WHITE);
        move = new Move(position, whiteKing, Files.C + Ranks.ONE);
        move.setCastleMask(FENConstants.WHITE_QUEENSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.WHITE_QUEENSIDE_CASTLE_MASK));
        piece = board.pieceSearch(Files.D + Ranks.ONE);
        assertEquals(PieceType.ROOK, piece.getType());
        King blackKing = board.findKing(PieceColour.BLACK);
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        board = position.getBoard();
        move = new Move(position, blackKing, Files.G + Ranks.EIGHT);
        move.setCastleMask(FENConstants.BLACK_KINGSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.BLACK_KINGSIDE_CASTLE_MASK));
        piece = board.pieceSearch(Files.F + Ranks.EIGHT);
        assertEquals(PieceType.ROOK, piece.getType());
        position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        board = position.getBoard();
        blackKing = board.findKing(PieceColour.BLACK);
        move = new Move(position, blackKing, Files.C + Ranks.EIGHT);
        move.setCastleMask(FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        position.doMove(move);
        assertEquals(FENConstants.NO_CASTLING_MASK, (position.getGameState().getCastlingRights() & FENConstants.BLACK_QUEENSIDE_CASTLE_MASK));
        piece = board.pieceSearch(Files.D + Ranks.EIGHT);
        assertEquals(PieceType.ROOK, piece.getType());
    }

    @Test
    @DisplayName("Test handlePromotion")
    void testHandlePromotion() {
        Position position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        Board board = position.getBoard();
        Piece piece = board.pieceSearch(Files.D + Ranks.SEVEN);
        Move move = new Move(position, piece, Files.D + Ranks.EIGHT);
        move.setPromotionType(PieceType.ROOK);
        position.doMove(move);
        Piece promotion = board.pieceSearch(Files.D + Ranks.EIGHT);
        assertEquals(PieceType.ROOK, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        move = new Move(position, piece, Files.E + Ranks.EIGHT);
        move.setPromotionType(PieceType.BISHOP);
        position.doMove(move);
        promotion = board.pieceSearch(Files.E + Ranks.EIGHT);
        assertEquals(PieceType.BISHOP, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        piece = board.pieceSearch(Files.E + Ranks.TWO);
        move = new Move(position, piece, Files.E + Ranks.ONE);
        move.setPromotionType(PieceType.QUEEN);
        board.handlePromotion(move);
        promotion = board.pieceSearch(Files.E + Ranks.ONE);
        assertEquals(PieceType.QUEEN, promotion.getType());
        position = FENUtils.positionFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        board = position.getBoard();
        move = new Move(position, piece, Files.D + Ranks.ONE);
        move.setPromotionType(PieceType.KNIGHT);
        position.doMove(move);
        promotion = board.pieceSearch(Files.D + Ranks.ONE);
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
