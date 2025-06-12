import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        assertEquals("b1",whitePieces[9].getSquare());
        assertEquals("c1",whitePieces[10].getSquare());
        assertEquals("d1",whitePieces[11].getSquare());
        king = (King) whitePieces[12];
        assertEquals("e1", king.getSquare());
        assertFalse(king.getMoved());
        assertEquals("f1",whitePieces[13].getSquare());
        assertEquals("g1",whitePieces[14].getSquare());
        hRook = (Rook) whitePieces[15];
        assertEquals("h1", hRook.getSquare());
        assertFalse(hRook.getMoved());
        assertTrue(board.getTurn());
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
        assertEquals("Rook", aRook.getName());
        King king = (King) blackPieces[1];
        assertEquals("e8", king.getSquare());
        assertFalse(king.getMoved());
        assertFalse(king.isCheck());
        assertEquals("King", king.getName());
        assertEquals("f8", blackPieces[2].getSquare());
        assertEquals("Bishop", blackPieces[2].getName());
        Rook hRook = (Rook) blackPieces[3];
        assertEquals("h8", hRook.getSquare());
        assertFalse(hRook.getMoved());
        assertEquals("Rook", hRook.getName());
        Pawn pawn = (Pawn) blackPieces[4];
        assertEquals("a7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[5];
        assertEquals("d7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[6];
        assertEquals("h7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("a6", blackPieces[7].getSquare());
        assertEquals("Bishop", blackPieces[7].getName());
        pawn = (Pawn) blackPieces[8];
        assertEquals("b6", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("c6", blackPieces[9].getSquare());
        assertEquals("Knight", blackPieces[9].getName());
        pawn = (Pawn) blackPieces[10];
        assertEquals("c5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("d5", blackPieces[11].getSquare());
        assertEquals("Knight", blackPieces[11].getName());
        pawn = (Pawn) blackPieces[12];
        assertEquals("g5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertTrue(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("h5", blackPieces[13].getSquare());
        assertEquals("Queen", blackPieces[13].getName());
        Piece[] whitePieces = board.getWhitePieces();
        pawn = (Pawn) whitePieces[0];
        assertEquals("e5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[1];
        assertEquals("f5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[2];
        assertEquals("d4", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("f4", whitePieces[3].getSquare());
        assertEquals("Bishop", whitePieces[3].getName());
        assertEquals("c3", whitePieces[4].getSquare());
        assertEquals("Knight", whitePieces[4].getName());
        assertEquals("f3", whitePieces[5].getSquare());
        assertEquals("Knight", whitePieces[5].getName());
        pawn = (Pawn) whitePieces[6];
        assertEquals("a2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[7];
        assertEquals("b2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[8];
        assertEquals("c2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("d2", whitePieces[9].getSquare());
        assertEquals("Queen", whitePieces[9].getName());
        pawn = (Pawn) whitePieces[10];
        assertEquals("e2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("g2", whitePieces[11].getSquare());
        assertEquals("Bishop", whitePieces[11].getName());
        pawn = (Pawn) whitePieces[12];
        assertEquals("h2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        aRook = (Rook) whitePieces[13];
        assertEquals("b1", aRook.getSquare());
        assertTrue(aRook.getMoved());
        assertEquals("Rook", aRook.getName());
        aRook = (Rook) whitePieces[14];
        assertEquals("f1", aRook.getSquare());
        assertTrue(aRook.getMoved());
        assertEquals("Rook", aRook.getName());
        assertEquals("f1", aRook.getSquare());
        king = (King) whitePieces[15];
        assertEquals("g1", king.getSquare());
        assertTrue(king.getMoved());
        assertFalse(king.isCheck());
        assertEquals("King", king.getName());
        assertEquals(0, board.getHalfMoveClock());
        assertEquals(13, board.getMoveCount());
        assertTrue(board.getTurn());
    }

    @Test
    @DisplayName("Test contructor with random middlegame")
    void testConstructorWithRandomMiddlegame() {
        board = new Board("r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9");
        Piece[] blackPieces = board.getBlackPieces();
        Rook rook = (Rook) blackPieces[0];
        assertEquals("a8", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals("Rook", rook.getName());
        assertEquals("c8", blackPieces[1].getSquare());
        assertEquals("Bishop", blackPieces[1].getName());
        assertEquals("d8", blackPieces[2].getSquare());
        assertEquals("Queen", blackPieces[2].getName());
        rook = (Rook) blackPieces[3];
        assertEquals("f8", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals("Rook", rook.getName());
        King king = (King) blackPieces[4];
        assertEquals("g8", king.getSquare());
        assertTrue(king.getMoved());
        assertFalse(king.isCheck());
        assertEquals("King", king.getName());
        Pawn pawn = (Pawn) blackPieces[5];
        assertEquals("a7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[6];
        assertEquals("b7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[7];
        assertEquals("c7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[8];
        assertEquals("f7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[9];
        assertEquals("g7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[10];
        assertEquals("h7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("c6", blackPieces[11].getSquare());
        assertEquals("Knight", blackPieces[11].getName());
        assertEquals("f6", blackPieces[12].getSquare());
        assertEquals("Knight", blackPieces[12].getName());
        pawn = (Pawn) blackPieces[13];
        assertEquals("d5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) blackPieces[14];
        assertEquals("e5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("b4", blackPieces[15].getSquare());
        assertEquals("Bishop", blackPieces[15].getName());
        Piece[] whitePieces = board.getWhitePieces();
        pawn = (Pawn) whitePieces[0];
        assertEquals("d4", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[1];
        assertEquals("c3", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[2];
        assertEquals("e3", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("f3", whitePieces[3].getSquare());
        assertEquals("Knight", whitePieces[3].getName());
        pawn = (Pawn) whitePieces[4];
        assertEquals("a2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[5];
        assertEquals("b2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        assertEquals("d2", whitePieces[6].getSquare());
        assertEquals("Knight", whitePieces[6].getName());
        assertEquals("e2", whitePieces[7].getSquare());
        assertEquals("Bishop", whitePieces[7].getName());
        pawn = (Pawn) whitePieces[8];
        assertEquals("f2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[9];
        assertEquals("g2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        pawn = (Pawn) whitePieces[10];
        assertEquals("h2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertFalse(pawn.getEnPassantable());
        assertEquals("Pawn", pawn.getName());
        rook = (Rook)  whitePieces[11];
        assertEquals("a1", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals("Rook", rook.getName());
        assertEquals("c1", whitePieces[12].getSquare());
        assertEquals("Bishop", whitePieces[12].getName());
        assertEquals("d1", whitePieces[13].getSquare());
        assertEquals("Queen", whitePieces[13].getName());
        rook = (Rook)  whitePieces[14];
        assertEquals("f1", rook.getSquare());
        assertTrue(rook.getMoved());
        assertEquals("Rook", rook.getName());
        king = (King) whitePieces[15];
        assertEquals("g1", king.getSquare());
        assertTrue(king.getMoved());
        assertEquals("King", king.getName());
        assertFalse(board.getTurn());
        assertEquals(0, board.getHalfMoveClock());
        assertEquals(9, board.getMoveCount());
    }

    @Test
    @DisplayName("Test pieceSearch")
    void testPieceSearch() {
        Piece piece = board.pieceSearch("c1");
        assertEquals("c1", piece.getSquare());
        assertEquals("Bishop", piece.getName());
        piece = board.pieceSearch("d4");
        assertNull(piece);
    }
}
