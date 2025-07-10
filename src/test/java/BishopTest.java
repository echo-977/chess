import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BishopTest {
    static Bishop piece1;
    static Bishop piece2;

    @BeforeEach
    public void init() {
        piece1 = new Bishop("White", 'a', 1);
        piece2 = new Bishop("Black", 'd', 5);
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
    @DisplayName("Test generateMoves")
    void testGenerateMoves() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        String[] piece1MovesExpected = {"b2", "c3", "d4", "e5", "f6", "g7", "h8", null, null, null, null, null, null};
        String[] piece2MovesExpected = {"a2", "b3", "c4", "e6", "f7", "g8", "a8", "b7", "c6", "e4", "f3", "g2", "h1"};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
