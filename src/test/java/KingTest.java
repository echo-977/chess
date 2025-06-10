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
        piece1 = new King("White", 'a', 1, false);
        piece2 = new King("Black", 'd', 5, true);
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
    @DisplayName("Test generateMoves")
    void testGenerateMoves() {
        String[] piece1MovesExpected = {"a2", "b2", "b1", null, null, null, null, null};
        String[] piece2MovesExpected = {"d6", "e6", "e5", "e4", "d4", "c4", "c5", "c6"};
        String[] piece1MovesActual = piece1.generateMoves();
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves();
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
}
