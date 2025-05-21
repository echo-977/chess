import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PieceTest {

    static Piece piece;

    @BeforeAll
    public static void init() {
        piece = new Piece("Pawn", "White", 'a', 2) {
            @Override
            public void move(String square) {
                // minimal implementation here to allow testing of concrete functions
                System.out.println("Moved to " + square);
            }

            public String[] generateMoves() {
                // minimal implementation here to allow testing of concrete functions
                return new String[8];
            }
        };
    }

    @Test
    @DisplayName("Test getSquare()")
    void getSquareTest() {
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
}
