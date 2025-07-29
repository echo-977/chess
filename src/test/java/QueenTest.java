import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueenTest {
    static Queen piece1;
    static Queen piece2;

    @BeforeEach
    public void init() {
        piece1 = new Queen(PieceColour.WHITE, 'a', 1);
        piece2 = new Queen(PieceColour.BLACK, 'd', 5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("b2"));
        assertTrue(piece2.isLegalMove("e4"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
        assertTrue(piece1.isLegalMove("a7"));
        assertTrue(piece2.isLegalMove("h5"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Board board = new Board("8/8/8/4q3/2Q5/8/8/8 w - - 0 1");
        piece1 = (Queen) board.getWhitePieces()[0];
        piece2 = (Queen) board.getBlackPieces()[0];
        Move[] piece1MovesExpected = {new Move(board, piece1, "c5"), new Move(board, piece1, "c6"),
                new Move(board, piece1, "c7"), new Move(board, piece1, "c8"),
                new Move(board, piece1, "d5"), new Move(board, piece1, "e6"),
                new Move(board, piece1, "f7"), new Move(board, piece1, "g8"),
                new Move(board, piece1, "d4"), new Move(board, piece1, "e4"),
                new Move(board, piece1, "f4"), new Move(board, piece1, "g4"),
                new Move(board, piece1, "h4"), new Move(board, piece1, "d3"),
                new Move(board, piece1, "e2"), new Move(board, piece1, "f1"),
                new Move(board, piece1, "c3"), new Move(board, piece1, "c2"),
                new Move(board, piece1, "c1"), new Move(board, piece1, "b3"),
                new Move(board, piece1, "a2"), new Move(board, piece1, "b4"),
                new Move(board, piece1, "a4"), new Move(board, piece1, "b5"),
                new Move(board, piece1, "a6"), null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(board, piece2, "e6"), new Move(board, piece2, "e7"),
                new Move(board, piece2, "e8"), new Move(board, piece2, "f6"),
                new Move(board, piece2, "g7"), new Move(board, piece2, "h8"),
                new Move(board, piece2, "f5"), new Move(board, piece2, "g5"),
                new Move(board, piece2, "h5"), new Move(board, piece2, "f4"),
                new Move(board, piece2, "g3"), new Move(board, piece2, "h2"),
                new Move(board, piece2, "e4"), new Move(board, piece2, "e3"),
                new Move(board, piece2, "e2"), new Move(board, piece2, "e1"),
                new Move(board, piece2, "d4"), new Move(board, piece2, "c3"),
                new Move(board, piece2, "b2"), new Move(board, piece2, "a1"),
                new Move(board, piece2, "d5"), new Move(board, piece2, "c5"),
                new Move(board, piece2, "b5"), new Move(board, piece2, "a5"),
                new Move(board, piece2, "d6"), new Move(board, piece2, "c7"),
                new Move(board, piece2, "b8")};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("8/2P5/b7/4q3/2Q2P2/8/4p3/8 w - - 0 1");
        piece1 = (Queen) board.getWhitePieces()[1];
        piece2 = (Queen) board.getBlackPieces()[1];
        Move move1 = new Move(board, piece1, "e2");
        move1.setCapture(true);
        Move move2 = new Move(board, piece1, "a6");
        move2.setCapture(true);
        Move[] piece1MovesExpected = {new Move(board, piece1, "c5"), new Move(board, piece1, "c6"),
                new Move(board, piece1, "d5"), new Move(board, piece1, "e6"),
                new Move(board, piece1, "f7"), new Move(board, piece1, "g8"),
                new Move(board, piece1, "d4"), new Move(board, piece1, "e4"),
                new Move(board, piece1, "d3"), move1, new Move(board, piece1, "c3"),
                new Move(board, piece1, "c2"), new Move(board, piece1, "c1"),
                new Move(board, piece1, "b3"), new Move(board, piece1, "a2"),
                new Move(board, piece1, "b4"), new Move(board, piece1, "a4"),
                new Move(board, piece1, "b5"), move2, null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(board, piece2, "f4");
        move1.setCapture(true);
        move2 = new Move(board, piece2, "c7");
        move2.setCapture(true);
        Move[] piece2MovesExpected = {new Move(board, piece2, "e6"), new Move(board, piece2, "e7"),
                new Move(board, piece2, "e8"), new Move(board, piece2, "f6"),
                new Move(board, piece2, "g7"), new Move(board, piece2, "h8"),
                new Move(board, piece2, "f5"), new Move(board, piece2, "g5"),
                new Move(board, piece2, "h5"), move1, new Move(board, piece2, "e4"),
                new Move(board, piece2, "e3"), new Move(board, piece2, "d4"),
                new Move(board, piece2, "c3"), new Move(board, piece2, "b2"),
                new Move(board, piece2, "a1"), new Move(board, piece2, "d5"),
                new Move(board, piece2, "c5"), new Move(board, piece2, "b5"),
                new Move(board, piece2, "a5"), new Move(board, piece2, "d6"),
                move2, null, null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName(("Test canCaptureKing"))
    void testCanCaptureKing() {
        Board board = new Board("8/1P1P1P2/8/1P1q2P1/8/1P3P2/3P4/8 w - - 0 1");
        Queen queen =  (Queen) board.getBlackPieces()[0];
        assertFalse(queen.canCaptureKing(board, "d8"));
        assertTrue(queen.canCaptureKing(board, "d7"));
        assertFalse(queen.canCaptureKing(board, "g8"));
        assertTrue(queen.canCaptureKing(board, "f7"));
        assertFalse(queen.canCaptureKing(board, "h5"));
        assertTrue(queen.canCaptureKing(board, "g5"));
        assertFalse(queen.canCaptureKing(board, "g2"));
        assertTrue(queen.canCaptureKing(board, "f3"));
        assertFalse(queen.canCaptureKing(board, "d1"));
        assertTrue(queen.canCaptureKing(board, "d2"));
        assertFalse(queen.canCaptureKing(board, "a2"));
        assertTrue(queen.canCaptureKing(board, "b3"));
        assertFalse(queen.canCaptureKing(board, "a5"));
        assertTrue(queen.canCaptureKing(board, "b5"));
        assertFalse(queen.canCaptureKing(board, "a8"));
        assertTrue(queen.canCaptureKing(board, "b7"));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Queen test = (Queen) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
