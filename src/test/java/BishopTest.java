import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        String[] piece1MovesExpected = {"c5", "b6", "a7", "e5", "f6", "g7", "h8", "e3", "f2", "g1", "c3", "b2", "a1"};
        String[] piece2MovesExpected = {"d5", "c6", "b7", "a8", "f5", "g6", "h7", "f3", "g2", "h1", "d3", "c2", "b1"};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("r2qkbnr/pp2pp2/n1p3pp/3p1b2/3P1B2/6PP/PPP1PP2/RN1QKBNR w KQkq - 0 1");
        piece1 = (Bishop) board.getWhitePieces()[1];
        piece2 = (Bishop) board.getBlackPieces()[15];
        String[] piece1MovesExpected = {"e5", "d6", "c7", "b8", "g5", "h6", "e3", "d2", "c1", null, null, null, null};
        String[] piece2MovesExpected = {"e6", "d7", "c8", "g4", "h3", "e4", "d3", "c2", null, null, null, null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
