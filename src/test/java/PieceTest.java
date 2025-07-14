import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PieceTest {

    static Piece piece;

    @BeforeAll
    public static void init() {
        piece = new Piece(PieceType.PAWN, PieceColour.WHITE, 'a', 2) {
            @Override
            public String[] generateMoves(Board board) {
                return new String[8]; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureKing(Board board, String color) {
                return false; // minimal implementation here to allow testing of concrete methods
            }
        };
    }

    @Test
    @DisplayName("Test getSquare()")
    void getSquareTest() {
        piece = new Piece(PieceType.PAWN, PieceColour.WHITE, 'a', 2) {
            @Override
            public String[] generateMoves(Board board) {
                return new String[8]; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureKing(Board board, String color) {
                return false; // minimal implementation here to allow testing of concrete methods
            }
        };
        assertEquals("a2", piece.getSquare());
    }

    @Test
    @DisplayName("Test mapFile()")
    void mapFileTest() {
        assertEquals(0, piece.mapFile());
    }

    @Test
    @DisplayName("Test mapIndexToFile")
    void mapIndexToFileTest() {
        assertEquals('a', piece.mapIndexToFile(0));
    }

    @Test
    @DisplayName("Test mapRank")
    void mapRankTest() {
        assertEquals(1, piece.mapRank());
    }

    @Test
    @DisplayName("Test mapIndexToRank")
    void mapIndexToRankTest() {
        assertEquals(1, piece.mapIndexToRank(0));
    }

    @Test
    @DisplayName("Test move")
    void moveTest() {
        piece.move("b8");
        assertEquals("b8", piece.getSquare());
    }

    @Test
    @DisplayName("Test  isLegalMove")
    void isLegalMoveTest() {
        assertFalse(piece.isLegalMove("c9"));
        assertFalse(piece.isLegalMove("`6"));
        assertFalse(piece.isLegalMove("i8"));
        assertFalse(piece.isLegalMove("b0"));
        assertFalse(piece.isLegalMove("b8"));
        assertTrue(piece.isLegalMove("e7"));
        assertTrue(piece.isLegalMove("f8"));
    }
}
