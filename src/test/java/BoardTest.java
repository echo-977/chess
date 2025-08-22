import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    public Board board;

    @BeforeEach
    public void init() {
        board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    @DisplayName("Test pieceSearch")
    void testPieceSearch() {
        Piece piece = board.pieceSearch("c1");
        assertEquals("c1", piece.getSquare());
        assertEquals(PieceType.BISHOP, piece.getType());
        piece = board.pieceSearch("d4");
        assertNull(piece);
    }

    @Test
    @DisplayName("Test findKing")
    void testFindKing() {
        King whiteKing = board.findKing(PieceColour.WHITE);
        assertEquals("e1", whiteKing.getSquare());
        King blackKing = board.findKing(PieceColour.BLACK);
        assertEquals("e8", blackKing.getSquare());
    }

    @Test
    @DisplayName("Test getCaptureDestination")
    void testGetCaptureDestination() {
        Board board = FENUtils.boardFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "g6");
        move.setEnPassant(true);
        assertEquals("g5", board.getCaptureDestination(move));
        piece = board.getWhitePieces()[1];
        move = new Move(board, piece, "g5");
        move.setCapture(true);
        assertEquals("g5", board.getCaptureDestination(move));
        piece = board.getBlackPieces()[0];
        move = new Move(board, piece, "g2");
        move.setCapture(true);
        assertEquals("g2", board.getCaptureDestination(move));
    }

    @Test
    @DisplayName("Test handleCaptureMove")
    void testHandleCaptureMove() {
        Board board = FENUtils.boardFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "g6");
        move.setEnPassant(true);
        Piece capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        board.doMove(move);
        for (Piece blackPiece : board.getBlackPieces()) {
            if (blackPiece != null) {
                assertNotSame(blackPiece, capturedPiece);
            }
        }
        board = FENUtils.boardFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        piece = board.getWhitePieces()[1];
        move = new Move(board, piece, "g5");
        move.setCapture(true);
        capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        board.doMove(move);
        for  (Piece blackPiece : board.getBlackPieces()) {
            if (blackPiece != null) {
                assertNotSame(blackPiece, capturedPiece);
            }
        }
        board = FENUtils.boardFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        piece = board.getBlackPieces()[0];
        move = new Move(board, piece, "g2");
        move.setCapture(true);
        capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        board.doMove(move);
        for  (Piece whitePiece : board.getWhitePieces()) {
            if (whitePiece != null) {
                assertNotSame(whitePiece, capturedPiece);
            }
        }
    }

    @Test
    @DisplayName("Test handleCastleMove")
    void testHandleCastleMove() {
        Board board = FENUtils.boardFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move move = new Move(board, whiteKing, "g1");
        move.setCastle(true);
        board.doMove(move);
        Piece piece = board.pieceSearch("f1");
        assertEquals(PieceType.ROOK, piece.getType());
        board = FENUtils.boardFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        whiteKing = board.findKing(PieceColour.WHITE);
        move = new Move(board, whiteKing, "c1");
        move.setCastle(true);
        board.doMove(move);
        piece = board.pieceSearch("d1");
        assertEquals(PieceType.ROOK, piece.getType());
        King blackKing = board.findKing(PieceColour.BLACK);
        board = FENUtils.boardFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        move = new Move(board, blackKing, "g8");
        move.setCastle(true);
        board.doMove(move);
        piece = board.pieceSearch("f8");
        assertEquals(PieceType.ROOK, piece.getType());
        board = FENUtils.boardFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        blackKing = board.findKing(PieceColour.BLACK);
        move = new Move(board, blackKing, "c8");
        move.setCastle(true);
        board.doMove(move);
        piece = board.pieceSearch("d8");
        assertEquals(PieceType.ROOK, piece.getType());
    }

    @Test
    @DisplayName("Test handlePromotion")
    void testHandlePromotion() {
        Board board = FENUtils.boardFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "d8");
        move.setPromotionType(PieceType.ROOK);
        board.doMove(move);
        Piece promotion = board.pieceSearch("d8");
        assertEquals(PieceType.ROOK, promotion.getType());
        board = FENUtils.boardFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        move = new Move(board, piece, "e8");
        move.setPromotionType(PieceType.BISHOP);
        board.doMove(move);
        promotion = board.pieceSearch("e8");
        assertEquals(PieceType.BISHOP, promotion.getType());
        board = FENUtils.boardFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        piece = board.getBlackPieces()[1];
        move = new Move(board, piece, "e1");
        move.setPromotionType(PieceType.QUEEN);
        board.handlePromotion(move);
        promotion = board.pieceSearch("e1");
        assertEquals(PieceType.QUEEN, promotion.getType());
        board = FENUtils.boardFromFEN("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        move = new Move(board, piece, "d1");
        move.setPromotionType(PieceType.KNIGHT);
        board.doMove(move);
        promotion = board.pieceSearch("d1");
        assertEquals(PieceType.KNIGHT, promotion.getType());
        assertEquals(PieceColour.BLACK, promotion.getColour());
    }

    @Test
    @DisplayName("Test doMove")
    void testDoMove() {
        Board board = FENUtils.boardFromFEN("8/8/5r2/8/8/3Q4/8/8 w - - 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "g6");
        board.doMove(move);
        assertEquals("g6", piece.getSquare());
        assertEquals(1, board.getHalfMoveClock());
        assertEquals(1, board.getMoveCount());
        move = new Move(board, board.getBlackPieces()[0], "g6");
        board.doMove(move);
        assertEquals(0, board.getHalfMoveClock());
        assertEquals(2, board.getMoveCount());
    }

    @Test
    @DisplayName("Test promotionCheck")
    void testPromotionCheck() {
        Board board = FENUtils.boardFromFEN("8/3P4/5k2/8/8/8/8/8 w - - 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "d8");
        move.setPromotionType(PieceType.BISHOP);
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test clone")
    void testClone() {
        Board board2 = board.copy();
        assertEquals(board, board2);
    }

    @Test
    @DisplayName("Test getCastlingRights")
    void testGetCastlingRights() {
        Board board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int castlingRights = (FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.WHITE_QUEENSIDE_CASTLE_MASK |
                FENConstants.BLACK_KINGSIDE_CASTLE_MASK |  FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        assertEquals(castlingRights, board.getCastlingRights());
        board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        castlingRights = (FENConstants.WHITE_KINGSIDE_CASTLE_MASK |
                FENConstants.BLACK_KINGSIDE_CASTLE_MASK |  FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        assertEquals(castlingRights, board.getCastlingRights());
        board = FENUtils.boardFromFEN("4k2r/8/8/5R2/8/8/8/8 w k - 0 1");
        castlingRights = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
        assertEquals(castlingRights, board.getCastlingRights());
    }

    @Test
    @DisplayName("Test getEnPassantTarget")
    void testGetEnPassantTarget() {
        Board board = FENUtils.boardFromFEN("rn1qkb1r/pp3p1p/5n2/2pPp3/6p1/2B2P2/PPP1P1PP/RN1QKB1R w KQkq c6 0 10");
        assertEquals("c6", board.getEnPassantTarget());
        board = FENUtils.boardFromFEN("rnbqkbnr/pppp1p1p/8/4pPp1/8/8/PPPPP1PP/RNBQKBNR w KQkq g6 0 2");
        assertEquals("g6", board.getEnPassantTarget());
        board = FENUtils.boardFromFEN("rnbqkbnr/p1pppppp/8/8/1pP5/8/PP1PPPPP/RNBQKBNR b KQkq c3 0 3");
        assertEquals("c3", board.getEnPassantTarget());
    }
}
