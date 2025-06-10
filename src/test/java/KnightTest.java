import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KnightTest {
    static Knight piece1;
    static Knight piece2;

    @BeforeEach
    public void init() {
        piece1 = new Knight("White", 'a', 1);
        piece2 = new Knight("Black", 'd', 5);
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
    @DisplayName("Test generateMoves")
    void testGenerateMoves() {
        String[] piece1MovesExpected = {"b3", "c2", null, null, null, null, null, null};
        String[] piece2MovesExpected = {"e7", "f6", "f4", "e3", "c3", "b4", "b6", "c7"};
        String[] piece1MovesActual = piece1.generateMoves();
        System.out.println(Arrays.toString(piece1MovesActual));
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves();
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
