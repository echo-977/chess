import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KnightTest {
    static Knight piece1;
    static Knight piece2;

    @BeforeEach
    public void init() {
        piece1 = new Knight(PieceColour.WHITE, 'a', 1);
        piece2 = new Knight(PieceColour.BLACK, 'd', 5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("c2"));
        assertTrue(piece2.isLegalMove("e3"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Board board = new Board("8/8/8/3n4/8/8/8/N7 w - - 0 1");
        piece1 = (Knight) board.getWhitePieces()[0];
        piece2 = (Knight) board.getBlackPieces()[0];
        Move[] piece1MovesExpected = {new Move(piece1, "b3"), new Move(piece1, "c2"), null, null,
                null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(piece2, "e7"), new Move(piece2, "f6"),
                new Move(piece2, "f4"), new Move(piece2, "e3"), new Move(piece2, "c3"),
                new Move(piece2, "b4"), new Move(piece2, "b6"), new Move(piece2, "c7")};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("8/8/8/3n1P2/1p4Q1/4N3/8/8 w - - 0 1");
        piece1 = (Knight) board.getWhitePieces()[2];
        piece2 = (Knight) board.getBlackPieces()[0];
        Move move1 = new Move(piece1, "d5");
        move1.setCapture(true);
        Move[] piece1MovesExpected = {new Move(piece1, "g2"), new Move(piece1, "f1"),
                new Move(piece1, "d1"), new Move(piece1, "c2"), new Move(piece1, "c4"),
                move1, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(piece2, "e3");
        move1.setCapture(true);
        Move[] piece2MovesExpected = {new Move(piece2, "e7"), new Move(piece2, "f6"),
                new Move(piece2, "f4"), move1, new Move(piece2, "c3"),
                new Move(piece2, "b6"), new Move(piece2, "c7"), null};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureKing")
    void testCanCaptureKing() { //given the canCaptureKing method just calls the isLegalMove method there is no test required
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Knight test = (Knight) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
