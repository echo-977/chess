import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SquareMapUtilsTest {
    @Test
    @DisplayName("Test mapIntToSquare")
    void testMapIntToSquare() {
        assertEquals("a8", SquareMapUtils.mapIntToSquare(0));
        assertEquals("h8", SquareMapUtils.mapIntToSquare(7));
        assertEquals("h1", SquareMapUtils.mapIntToSquare(63));
        assertEquals("d4", SquareMapUtils.mapIntToSquare(35));
    }

    @Test
    @DisplayName("Test mapSquareToInt")
    void testMapSquareToInt() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1");
        assertEquals(63, SquareMapUtils.mapSquareToInt("h1"));
        assertEquals(0, SquareMapUtils.mapSquareToInt("a8"));
        assertEquals(35, SquareMapUtils.mapSquareToInt("d4"));
    }
}
