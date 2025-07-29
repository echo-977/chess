import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    static Bishop piece1;
    static Bishop piece2;

    @BeforeEach
    public void init() {
        piece1 = new Bishop(PieceColour.WHITE, 'a', 1);
        piece2 = new Bishop(PieceColour.BLACK, 'd', 5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("b2"));
        assertTrue(piece2.isLegalMove("e4"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1 = new Bishop(PieceColour.WHITE, 'd', 4);
        piece2 = new Bishop(PieceColour.BLACK, 'e', 4);
        Board board = new Board("8/8/8/8/3Bb3/8/8/8 w - - 0 1");
        Move[] piece1MovesExpected = {new Move(piece1, "c5"), new Move(piece1, "b6"),
                new Move(piece1, "a7"), new Move(piece1, "e5"), new Move(piece1, "f6"),
                new Move(piece1, "g7"), new Move(piece1, "h8"), new Move(piece1, "e3"),
                new Move(piece1, "f2"), new Move(piece1, "g1"), new Move(piece1, "c3"),
                new Move(piece1, "b2"), new Move(piece1, "a1")};
        Move[] piece2MovesExpected = {new Move(piece2, "d5"), new Move(piece2, "c6"),
                new Move(piece2, "b7"), new Move(piece2, "a8"), new Move(piece2, "f5"),
                new Move(piece2, "g6"), new Move(piece2, "h7"), new Move(piece2, "f3"),
                new Move(piece2, "g2"), new Move(piece2, "h1"), new Move(piece2, "d3"),
                new Move(piece2, "c2"), new Move(piece2, "b1")};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("r2qkbnr/pp2pp2/n1p3pp/3p1b2/3P1B2/6PP/PPP1PP2/RN1QKBNR w KQkq - 0 1");
        piece1 = (Bishop) board.getWhitePieces()[1];
        piece2 = (Bishop) board.getBlackPieces()[15];
        Move move1 = new Move(piece1, "h6");
        move1.setCapture(true);
        Move[] piece1MovesExpected = {new Move(piece1, "e5"), new Move(piece1, "d6"),
                new Move(piece1, "c7"), new Move(piece1, "b8"), new Move(piece1, "g5"),
                move1, new Move(piece1, "e3"), new Move(piece1, "d2"),
                new Move(piece1, "c1"), null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(piece2, "h3");
        move1.setCapture(true);
        Move move2 = new Move(piece2, "c2");
        move2.setCapture(true);
        Move[] piece2MovesExpected = {new Move(piece2, "e6"), new Move(piece2, "d7"),
                new Move(piece2, "c8"), new Move(piece2, "g4"), move1,
                new Move(piece2, "e4"), new Move(piece2, "d3"), move2, null, null, null, null,
                null};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureKing")
    void testCanCaptureKing() {
        Board board = new Board("8/1P3P2/8/3b4/8/1P6/6P1/8 w - - 0 1");
        Bishop bishop = (Bishop) board.getBlackPieces()[0];
        assertFalse(bishop.canCaptureKing(board, "g8"));
        assertTrue(bishop.canCaptureKing(board, "f7"));
        assertFalse(bishop.canCaptureKing(board, "h1"));
        assertTrue(bishop.canCaptureKing(board, "g2"));
        assertFalse(bishop.canCaptureKing(board, "a2"));
        assertTrue(bishop.canCaptureKing(board, "b3"));
        assertFalse(bishop.canCaptureKing(board, "a8"));
        assertTrue(bishop.canCaptureKing(board, "b7"));
    }


    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Bishop test = (Bishop) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
