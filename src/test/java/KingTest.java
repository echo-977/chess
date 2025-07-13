import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KingTest {
    static King piece1;
    static King piece2;

    @BeforeEach
    public void init() {
        piece1 = new King(ChessConstants.WHITE, 'a', 1, false, false);
        piece2 = new King(ChessConstants.WHITE, 'd', 5, true, true);
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
        String[] piece1MovesExpected = {"c5", "d5", "d4", "d3", "c3", "b3", "b4", "b5"};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesExpected = {"f7", "g7", "g6", "g5", "f5", "e5", "e6", "e7"};
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with other pieces")
    void testGenerateMovesWithOtherPieces() {
        Board board = new Board("8/6p1/4PkR1/3r4/2K2p2/2P5/8/8 b - - 0 1");
        piece1 = (King) board.getWhitePieces()[2];
        piece2 = (King) board.getBlackPieces()[1];
        String[] piece1MovesExpected = {"c5", "d5", "d4", "d3", "b3", "b4", "b5", null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesExpected = {"f7", "g6", "g5", "f5", "e5", "e6", "e7", null};
        String[] piece2MovesActual = piece2.generateMoves(board);
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
}
