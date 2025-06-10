import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class QueenTest {
    static Queen piece1;
    static Queen piece2;

    @BeforeEach
    public void init() {
        piece1 = new Queen("White", 'a', 1);
        piece2 = new Queen("Black", 'd', 5);
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
    @DisplayName("Test generateMoves")
    void testGenerateMoves() {
        String[] piece1MovesExpected = {"b1", "c1", "d1", "e1", "f1", "g1", "h1",
                "a2", "a3", "a4", "a5", "a6", "a7", "a8", "b2", "c3", "d4", "e5",
                "f6", "g7", "h8", null, null, null, null, null, null};
        String[] piece2MovesExpected = {"a5", "b5", "c5", "e5", "f5", "g5", "h5",
                "d1", "d2", "d3", "d4", "d6", "d7", "d8", "a2", "b3", "c4", "e6",
                "f7", "g8", "a8", "b7", "c6", "e4", "f3", "g2", "h1"};
        String[] piece1MovesActual = piece1.generateMoves();
        System.out.println(Arrays.toString(piece1MovesActual));
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves();
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
