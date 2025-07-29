import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KingTest {
    static King piece1;
    static King piece2;

    @BeforeEach
    public void init() {
        piece1 = new King(PieceColour.WHITE, 'a', 1, false, false);
        piece2 = new King(PieceColour.WHITE, 'd', 5, true, true);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a2"));
        assertTrue(piece2.isLegalMove("c6"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("a3"));
    }

    @Test
    @DisplayName("Test generateMoves without other pieces")
    void testGenerateMoves() {
        Board board = new Board("8/8/5k2/8/2K5/8/8/8 w - - 0 1");
        piece1 = (King) board.getWhitePieces()[0];
        piece2 = (King) board.getBlackPieces()[0];
        Move[] piece1MovesExpected = {new Move(piece1, "c5"), new Move(piece1, "d5"),
                new Move(piece1, "d4"), new Move(piece1, "d3"), new Move(piece1, "c3"),
                new Move(piece1, "b3"), new Move(piece1, "b4"), new Move(piece1, "b5")};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(piece2, "f7"), new Move(piece2, "g7"),
                new Move(piece2, "g6"), new Move(piece2, "g5"), new Move(piece2, "f5"),
                new Move(piece2, "e5"), new Move(piece2, "e6"), new Move(piece2, "e7")};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with other pieces")
    void testGenerateMovesWithOtherPieces() {
        Board board = new Board("8/6p1/4PkR1/3r4/2K2p2/2P5/8/8 b - - 0 1");
        piece1 = (King) board.getWhitePieces()[2];
        piece2 = (King) board.getBlackPieces()[1];
        Move move1 = new Move(piece1, "d5");
        move1.setCapture(true);
        Move[] piece1MovesExpected = {new Move(piece1, "c5"), move1, new Move(piece1, "d4"),
                new Move(piece1, "d3"), new Move(piece1, "b3"), new Move(piece1, "b4"),
                new Move(piece1, "b5"), null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(piece2, "g6");
        move1.setCapture(true);
        Move move2 = new Move(piece2, "e6");
        move2.setCapture(true);
        Move[] piece2MovesExpected = {new Move(piece2, "f7"), move1, new Move(piece2, "g5"),
                new Move(piece2, "f5"), new Move(piece2, "e5"), move2,
                new Move(piece2, "e7"), null};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test isCheck")
    void testIsCheck() {
        assertFalse(piece1.isCheck());
        assertTrue(piece2.isCheck());
    }

    @Test
    @DisplayName("Test setCheck")
    void testSetCheck() {
        piece1.setCheck(true);
        assertTrue(piece1.isCheck());
        piece2.setCheck(false);
        assertFalse(piece2.isCheck());
    }

    @Test
    @DisplayName("Test getMoved")
    void testGetMoved() {
        assertFalse(piece1.getMoved());
        assertTrue(piece2.getMoved());
    }

    @Test
    @DisplayName("Test move")
    void testMove() {
        piece1.move("a2");
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        King test = (King) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
        assertEquals(piece1.getMoved(), test.getMoved());
        assertEquals(piece1.isCheck(), test.isCheck());
    }
}
