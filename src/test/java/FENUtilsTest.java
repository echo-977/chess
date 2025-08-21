import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FENUtilsTest {
    @Test
    @DisplayName("Test making board with initial position")
    void testBoardFromFENDefaultFEN() {
        Board board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
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
    @DisplayName("Test making board with en passant")
    void testBoardFromFENWithEnPassant() {
        Board board = FENUtils.boardFromFEN("r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13");
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
    @DisplayName("Test making board with random middlegame")
    void testBoardFromFENWithRandomMiddlegame() {
        Board board = FENUtils.boardFromFEN("r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9");
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
    @DisplayName("Test getFEN (starting position)")
    void testGetFEN() {
        Board board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", FENUtils.getFEN(board));
    }

    @Test
    @DisplayName("Test getFEN (test position 1)")
    void testGetFENWithPos1() {
        String fen = "r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9";
        Board board = FENUtils.boardFromFEN(fen);
        assertEquals(fen, FENUtils.getFEN(board));
    }

    @Test
    @DisplayName("Test getFEN (test position 2)")
    void testGetFENWithPos2() {
        String fen = "r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13";
        Board board = FENUtils.boardFromFEN(fen);
        assertEquals(fen, FENUtils.getFEN(board));
    }

    @Test
    @DisplayName("Test getFEN (test position 3)")
    void testGetFenWithPos3() {
        String fen = "rnbqkbnr/pp1ppppp/2p5/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 3";
        Board board = FENUtils.boardFromFEN(fen);
        assertEquals(fen, FENUtils.getFEN(board));
    }
}
