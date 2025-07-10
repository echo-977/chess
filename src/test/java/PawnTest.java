import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnTest {
    public Pawn piece1;
    public Pawn piece2;

    @BeforeEach
    public void init() {
        piece1 = new Pawn("White", 'a', 1, false, false);
        piece2 = new Pawn("Black", 'd', 5, false, false);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a2"));
        assertTrue(piece1.isLegalMove("a3"));
        assertTrue(piece2.isLegalMove("d4"));
        assertTrue(piece2.isLegalMove("d3"));
        assertFalse(piece1.isLegalMove("b1"));
        assertFalse(piece2.isLegalMove("c5"));
    }

    @Test
    @DisplayName("Test generateMoves")
    void testGenerateMoves() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        String[] piece1MovesExpected = {"a2", "a3", null, null};
        String[] piece2MovesExpected = {"d4", "d3", null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test moved")
    void testMoved() {
        assertFalse(piece1.getMoved());
        piece1.move("a2");
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test enPassantable")
    void testEnPassantable() {
        assertFalse(piece1.getEnPassantable());
        piece1.move("a3");
        assertTrue(piece1.getEnPassantable());
        piece1.move("a4");
        assertFalse(piece1.getEnPassantable());
    }
}
