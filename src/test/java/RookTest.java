import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RookTest {
    static Rook piece1;
    static Rook piece2;

    @BeforeEach
    public void init() {
        piece1 = new Rook("White", 'a', 1, false);
        piece2 = new Rook("Black", 'c', 5, false);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a7"));
        assertTrue(piece2.isLegalMove("h5"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test move")
    void testMove() {
        assertFalse(piece1.getMoved());
        piece1.move("a7");
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test generateMoves")
    void testGenerateMoves() {
        String[] piece1MovesExpected = {"b1", "c1", "d1", "e1", "f1", "g1", "h1",
                                        "a2", "a3", "a4", "a5", "a6", "a7", "a8"};
        String[] piece2MovesExpected = {"a5", "b5", "d5", "e5", "f5", "g5", "h5",
                                        "c1", "c2", "c3", "c4", "c6", "c7", "c8"};
        String[] piece1MovesActual = piece1.generateMoves();
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves();
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
