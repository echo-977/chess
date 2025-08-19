import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    public Board board;

    @BeforeEach
    public void init() {
        board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    @DisplayName("Test mapIntToSquare")
    void testMapIntToSquare() {
        assertEquals("a8", board.mapIntToSquare(0));
        assertEquals("h8", board.mapIntToSquare(7));
        assertEquals("h1", board.mapIntToSquare(63));
        assertEquals("d4", board.mapIntToSquare(35));

    }

    @Test
    @DisplayName("Test constructor with initial position")
    void testConstructorDefaultFEN() {
        board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Piece[] blackPieces = board.getBlackPieces();
        Rook aRook = (Rook) blackPieces[0];
        assertEquals("a8", aRook.getSquare());
        assertFalse(aRook.getMoved());
        assertEquals("b8", blackPieces[1].getSquare());
        assertEquals("c8", blackPieces[2].getSquare());
        assertEquals("d8", blackPieces[3].getSquare());
        King king = (King) blackPieces[4];
        assertEquals("e8", king.getSquare());
        assertFalse(king.getMoved());
        assertEquals("f8", blackPieces[5].getSquare());
        assertEquals("g8", blackPieces[6].getSquare());
        Rook hRook = (Rook) blackPieces[7];
        assertEquals("h8", hRook.getSquare());
        assertFalse(hRook.getMoved());
        Pawn pawn;
        char file;
        String square;
        for (int i = 8; i < 16; i++) {
            pawn = (Pawn) blackPieces[i];
            file = (char) ('a' + (i - 8));
            square = String.valueOf(file) + '7';
            assertEquals(square, pawn.getSquare());
            assertFalse(pawn.getMoved());
            assertFalse(pawn.getEnPassantable());
        }
        Piece[] whitePieces = board.getWhitePieces();
        for (int i = 0; i < 8; i++) {
            pawn = (Pawn) whitePieces[i];
            file = (char) ('a' + (i));
            square = String.valueOf(file) + '2';
            assertEquals(square, pawn.getSquare());
            assertFalse(pawn.getMoved());
            assertFalse(pawn.getEnPassantable());
        }
        aRook = (Rook) whitePieces[8];
        assertEquals("a1", aRook.getSquare());
        assertFalse(aRook.getMoved());
        assertEquals("b1", whitePieces[9].getSquare());
        assertEquals("c1", whitePieces[10].getSquare());
        assertEquals("d1", whitePieces[11].getSquare());
        king = (King) whitePieces[12];
        assertEquals("e1", king.getSquare());
        assertFalse(king.getMoved());
        assertEquals("f1", whitePieces[13].getSquare());
        assertEquals("g1", whitePieces[14].getSquare());
        hRook = (Rook) whitePieces[15];
        assertEquals("h1", hRook.getSquare());
        assertFalse(hRook.getMoved());
        assertEquals(PieceColour.WHITE, board.getTurn());
        assertEquals(1, board.getMoveCount());
        assertEquals(0, board.getHalfMoveClock());
    }

    @Test
    @DisplayName("Test constructor with en passant")
    void testConstructorWithEnPassant() {
        board = new Board("r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13");
        Piece[] blackPieces = board.getBlackPieces();
        Rook aRook = (Rook) blackPieces[0];
        assertEquals("a8", aRook.getSquare());
        assertTrue(aRook.getMoved());
        assertEquals(PieceType.ROOK, aRook.getType());
        King king = (King) blackPieces[1];
        assertEquals("e8", king.getSquare());
        assertFalse(king.getMoved());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals("f8", blackPieces[2].getSquare());
        assertEquals(PieceType.BISHOP, blackPieces[2].getType());
        Rook hRook = (Rook) blackPieces[3];
        assertEquals("h8", hRook.getSquare());
        assertFalse(hRook.getMoved());
        assertEquals(PieceType.ROOK, hRook.getType());
        Pawn pawn = (Pawn) blackPieces[4];
        assertEquals("a7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[5];
        assertEquals("d7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[6];
        assertEquals("h7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("a6", blackPieces[7].getSquare());
        assertEquals(PieceType.BISHOP, blackPieces[7].getType());
        pawn = (Pawn) blackPieces[8];
        assertEquals("b6", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("c6", blackPieces[9].getSquare());
        assertEquals(PieceType.KNIGHT, blackPieces[9].getType());
        pawn = (Pawn) blackPieces[10];
        assertEquals("c5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("d5", blackPieces[11].getSquare());
        assertEquals(PieceType.KNIGHT, blackPieces[11].getType());
        pawn = (Pawn) blackPieces[12];
        assertEquals("g5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertTrue(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("h5", blackPieces[13].getSquare());
        assertEquals(PieceType.QUEEN, blackPieces[13].getType());
        Piece[] whitePieces = board.getWhitePieces();
        pawn = (Pawn) whitePieces[0];
        assertEquals("e5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[1];
        assertEquals("f5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[2];
        assertEquals("d4", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("f4", whitePieces[3].getSquare());
        assertEquals(PieceType.BISHOP, whitePieces[3].getType());
        assertEquals("c3", whitePieces[4].getSquare());
        assertEquals(PieceType.KNIGHT, whitePieces[4].getType());
        assertEquals("f3", whitePieces[5].getSquare());
        assertEquals(PieceType.KNIGHT, whitePieces[5].getType());
        pawn = (Pawn) whitePieces[6];
        assertEquals("a2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[7];
        assertEquals("b2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[8];
        assertEquals("c2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("d2", whitePieces[9].getSquare());
        assertEquals(PieceType.QUEEN, whitePieces[9].getType());
        pawn = (Pawn) whitePieces[10];
        assertEquals("e2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("g2", whitePieces[11].getSquare());
        assertEquals(PieceType.BISHOP, whitePieces[11].getType());
        pawn = (Pawn) whitePieces[12];
        assertEquals("h2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        aRook = (Rook) whitePieces[13];
        assertEquals("b1", aRook.getSquare());
        assertTrue(aRook.getMoved());
        assertEquals(PieceType.ROOK, aRook.getType());
        aRook = (Rook) whitePieces[14];
        assertEquals("f1", aRook.getSquare());
        assertTrue(aRook.getMoved());
        assertEquals(PieceType.ROOK, aRook.getType());
        assertEquals("f1", aRook.getSquare());
        king = (King) whitePieces[15];
        assertEquals("g1", king.getSquare());
        assertTrue(king.getMoved());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(0, board.getHalfMoveClock());
        assertEquals(13, board.getMoveCount());
        assertEquals(PieceColour.WHITE, board.getTurn());
    }

    @Test
    @DisplayName("Test contructor with random middlegame")
    void testConstructorWithRandomMiddlegame() {
        board = new Board("r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9");
        Piece[] blackPieces = board.getBlackPieces();
        Rook rook = (Rook) blackPieces[0];
        assertEquals("a8", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals("c8", blackPieces[1].getSquare());
        assertEquals(PieceType.BISHOP, blackPieces[1].getType());
        assertEquals("d8", blackPieces[2].getSquare());
        assertEquals(PieceType.QUEEN, blackPieces[2].getType());
        rook = (Rook) blackPieces[3];
        assertEquals("f8", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals(PieceType.ROOK, rook.getType());
        King king = (King) blackPieces[4];
        assertEquals("g8", king.getSquare());
        assertTrue(king.getMoved());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        Pawn pawn = (Pawn) blackPieces[5];
        assertEquals("a7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[6];
        assertEquals("b7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[7];
        assertEquals("c7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[8];
        assertEquals("f7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[9];
        assertEquals("g7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[10];
        assertEquals("h7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("c6", blackPieces[11].getSquare());
        assertEquals(PieceType.KNIGHT, blackPieces[11].getType());
        assertEquals("f6", blackPieces[12].getSquare());
        assertEquals(PieceType.KNIGHT, blackPieces[12].getType());
        pawn = (Pawn) blackPieces[13];
        assertEquals("d5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) blackPieces[14];
        assertEquals("e5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("b4", blackPieces[15].getSquare());
        assertEquals(PieceType.BISHOP, blackPieces[15].getType());
        Piece[] whitePieces = board.getWhitePieces();
        pawn = (Pawn) whitePieces[0];
        assertEquals("d4", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[1];
        assertEquals("c3", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[2];
        assertEquals("e3", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("f3", whitePieces[3].getSquare());
        assertEquals(PieceType.KNIGHT, whitePieces[3].getType());
        pawn = (Pawn) whitePieces[4];
        assertEquals("a2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[5];
        assertEquals("b2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("d2", whitePieces[6].getSquare());
        assertEquals(PieceType.KNIGHT, whitePieces[6].getType());
        assertEquals("e2", whitePieces[7].getSquare());
        assertEquals(PieceType.BISHOP, whitePieces[7].getType());
        pawn = (Pawn) whitePieces[8];
        assertEquals("f2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[9];
        assertEquals("g2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) whitePieces[10];
        assertEquals("h2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals(PieceType.PAWN, pawn.getType());
        rook = (Rook) whitePieces[11];
        assertEquals("a1", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals("c1", whitePieces[12].getSquare());
        assertEquals(PieceType.BISHOP, whitePieces[12].getType());
        assertEquals("d1", whitePieces[13].getSquare());
        assertEquals(PieceType.QUEEN, whitePieces[13].getType());
        rook = (Rook) whitePieces[14];
        assertEquals("f1", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals(PieceType.ROOK, rook.getType());
        king = (King) whitePieces[15];
        assertEquals("g1", king.getSquare());
        assertTrue(king.getMoved());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(PieceColour.BLACK, board.getTurn());
        assertEquals(0, board.getHalfMoveClock());
        assertEquals(9, board.getMoveCount());
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
    @DisplayName("Test getFEN (starting position)")
    void testGetFEN() {
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", board.getFEN());
    }

    @Test
    @DisplayName("Test getFEN (test position 1)")
    void testGetFENWithPos1() {
        String fen = "r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9";
        board = new Board(fen);
        assertEquals(fen, board.getFEN());
    }

    @Test
    @DisplayName("Test getFEN (test position 2)")
    void testGetFENWithPos2() {
        String fen = "r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13";
        board = new Board(fen);
        assertEquals(fen, board.getFEN());
    }

    @Test
    @DisplayName("Test getFEN (test position 3)")
    void testGetFenWithPos3() {
        String fen = "rnbqkbnr/pp1ppppp/2p5/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 3";
        board = new Board(fen);
        assertEquals(fen, board.getFEN());
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
        Board board = new Board("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
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
        Board board = new Board("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "g6");
        move.setEnPassant(true);
        Piece capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        assertTrue(board.doMove(move));
        for (Piece blackPiece : board.getBlackPieces()) {
            if (blackPiece != null) {
                assertNotSame(blackPiece, capturedPiece);
            }
        }
        board = new Board("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        piece = board.getWhitePieces()[1];
        move = new Move(board, piece, "g5");
        move.setCapture(true);
        capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        assertTrue(board.doMove(move));
        for  (Piece blackPiece : board.getBlackPieces()) {
            if (blackPiece != null) {
                assertNotSame(blackPiece, capturedPiece);
            }
        }
        board = new Board("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        piece = board.getBlackPieces()[0];
        move = new Move(board, piece, "g2");
        move.setCapture(true);
        capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        assertTrue(board.doMove(move));
        for  (Piece whitePiece : board.getWhitePieces()) {
            if (whitePiece != null) {
                assertNotSame(whitePiece, capturedPiece);
            }
        }
    }

    @Test
    @DisplayName("Test handleCastleMove")
    void testHandleCastleMove() {
        Board board = new Board("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move move = new Move(board, whiteKing, "g1");
        move.setCastle(true);
        board.doMove(move);
        Piece piece = board.pieceSearch("f1");
        assertEquals(PieceType.ROOK, piece.getType());
        board = new Board("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        whiteKing = board.findKing(PieceColour.WHITE);
        move = new Move(board, whiteKing, "c1");
        move.setCastle(true);
        board.doMove(move);
        piece = board.pieceSearch("d1");
        assertEquals(PieceType.ROOK, piece.getType());
        King blackKing = board.findKing(PieceColour.BLACK);
        board = new Board("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
        move = new Move(board, blackKing, "g8");
        move.setCastle(true);
        board.doMove(move);
        piece = board.pieceSearch("f8");
        assertEquals(PieceType.ROOK, piece.getType());
        board = new Board("r3k2r/8/8/8/8/8/8/R3K2R w - - 0 1");
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
        Board board = new Board("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "d8");
        move.setPromotionType(PieceType.ROOK);
        assertTrue(board.doMove(move));
        Piece promotion = board.pieceSearch("d8");
        assertEquals(PieceType.ROOK, promotion.getType());
        board = new Board("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        move = new Move(board, piece, "e8");
        move.setPromotionType(PieceType.BISHOP);
        assertTrue(board.doMove(move));
        promotion = board.pieceSearch("e8");
        assertEquals(PieceType.BISHOP, promotion.getType());
        board = new Board("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        piece = board.getBlackPieces()[1];
        move = new Move(board, piece, "e1");
        move.setPromotionType(PieceType.QUEEN);
        assertTrue(board.handlePromotion(move));
        promotion = board.pieceSearch("e1");
        assertEquals(PieceType.QUEEN, promotion.getType());
        board = new Board("4n3/3P4/8/8/8/8/4p3/3N4 w - - 0 1");
        move = new Move(board, piece, "d1");
        move.setPromotionType(PieceType.KNIGHT);
        assertTrue(board.doMove(move));
        promotion = board.pieceSearch("d1");
        assertEquals(PieceType.KNIGHT, promotion.getType());
        assertEquals(PieceColour.BLACK, promotion.getColour());
    }

    @Test
    @DisplayName("Test doMove")
    void testDoMove() {
        Board board = new Board("8/8/8/8/8/3Q4/8/8 w - - 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "g6");
        board.doMove(move);
        assertEquals("g6", piece.getSquare());
    }

    @Test
    @DisplayName("Test getThreatMap")
    void testGetThreatMap() {
        Board board = new Board("8/8/3r4/4r3/4b3/8/8/8 w - - 0 1");
        boolean[] expectedThreatMap = {true, false, false, true, true, false, false, false,
                false, true, false, true, true, false, false, true,
                true, true, true, false, true, true, true, true,
                true, true, true, true, false, true, true, true,
                false, false, false, true, true, false, false, false,
                false, false, false, true, false, true, false, false,
                false, false, true, true, false, false, true, false,
                false, true, false, true, false, false, false, true};
        boolean[] actualThreatMap = board.getThreatMap(PieceColour.BLACK);
        assertArrayEquals(expectedThreatMap,  actualThreatMap);
    }

    @Test
    @DisplayName("Test mapSquareToInt")
    void testMapSquareToInt() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1");
        assertEquals(63, board.mapSquareToInt("h1"));
        assertEquals(0, board.mapSquareToInt("a8"));
        assertEquals(35, board.mapSquareToInt("d4"));
    }

    @Test
    @DisplayName("Test promotionCheck")
    void testPromotionCheck() {
        Board board = new Board("8/3P4/5k2/8/8/8/8/8 w - - 0 1");
        Piece piece = board.getWhitePieces()[0];
        Move move = new Move(board, piece, "d8");
        move.setPromotionType(PieceType.BISHOP);
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (file only)")
    void testSetDisambiguationFlagsFile() {
        Board board = new Board("8/8/3N1N2/2N5/4p3/6N1/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("d6"), "e4"),
                new Move(board, board.pieceSearch("f6"), "e4"),
                new Move(board, board.pieceSearch("g3"), "e4")};
        board.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertTrue(move.isFileDisambiguation());
            assertFalse(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (rank only)")
    void testSetDisambiguationFlagsRank() {
        Board board = new Board("8/8/8/2N5/4p3/2N5/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("c3"), "e4")};
        board.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertFalse(move.isFileDisambiguation());
            assertTrue(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (both)")
    void testSetDisambiguationFlagsBoth() {
        Board board = new Board("8/8/8/2N5/4p3/2N3N1/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("c3"), "e4"),
                new Move(board, board.pieceSearch("g3"), "e4")};
        board.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertTrue(move.isFileDisambiguation());
            assertTrue(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test handleDisambiguation")
    void  testHandleDisambiguation() {
        Board board = new Board("8/8/8/2N5/4p1R1/2N3N1/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("c3"), "e4"),
                new Move(board, board.pieceSearch("g3"), "e4"),
                new Move(board, board.pieceSearch("c5"), "d7"),
                new Move(board, board.pieceSearch("g4"), "e4")};
        board.handleDisambiguation(moves);
        assertTrue(moves[0].isFileDisambiguation());
        assertTrue(moves[0].isRankDisambiguation());
        assertTrue(moves[1].isFileDisambiguation());
        assertTrue(moves[1].isRankDisambiguation());
        assertTrue(moves[2].isFileDisambiguation());
        assertTrue(moves[2].isRankDisambiguation());
        assertFalse(moves[3].isFileDisambiguation());
        assertFalse(moves[3].isRankDisambiguation());
        assertFalse(moves[4].isFileDisambiguation());
        assertFalse(moves[4].isRankDisambiguation());
    }

    @Test
    @DisplayName("Test generateMoves")
    void  testGenerateMovesStartingPosition() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int numMoves = 20;
        assertEquals(numMoves, board.generateMoves(PieceColour.WHITE).length);
        assertEquals(numMoves, board.generateMoves(PieceColour.BLACK).length);

        board = new Board("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertEquals(48, board.generateMoves(PieceColour.WHITE).length);
        assertEquals(43, board.generateMoves(PieceColour.BLACK).length);

        board = new Board("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ");
        assertEquals(14, board.generateMoves(PieceColour.WHITE).length);
        assertEquals(15, board.generateMoves(PieceColour.BLACK).length);

        board = new Board("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        assertEquals(6, board.generateMoves(PieceColour.WHITE).length);
        assertEquals(46, board.generateMoves(PieceColour.BLACK).length);

        board = new Board("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        assertEquals(44, board.generateMoves(PieceColour.WHITE).length);
        assertEquals(34, board.generateMoves(PieceColour.BLACK).length);

        board = new Board("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        assertEquals(46, board.generateMoves(PieceColour.WHITE).length);
        assertEquals(46, board.generateMoves(PieceColour.BLACK).length);
    }
}
