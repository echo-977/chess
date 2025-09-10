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
        assertEquals(63, SquareMapUtils.mapSquareToInt("h1"));
        assertEquals(0, SquareMapUtils.mapSquareToInt("a8"));
        assertEquals(35, SquareMapUtils.mapSquareToInt("d4"));
    }

    @Test
    @DisplayName("Test getFile (int)")
    void testGetFile() {
        assertEquals('a', SquareMapUtils.getFile(Files.A + Ranks.EIGHT));
        assertEquals('h', SquareMapUtils.getFile(Files.H + Ranks.EIGHT));
        assertEquals('d', SquareMapUtils.getFile(Files.D + Ranks.FOUR));
    }

    @Test
    @DisplayName("Test getRank (int)")
    void testGetRank() {
        assertEquals(8, SquareMapUtils.getRank(Files.A + Ranks.EIGHT));
        assertEquals(6, SquareMapUtils.getRank(Files.H + Ranks.SIX));
        assertEquals(1, SquareMapUtils.getRank(Files.E + Ranks.ONE));
    }

    @Test
    @DisplayName("Test getSquare")
    void testGetSquare() {
        assertEquals(Files.D + Ranks.SEVEN, SquareMapUtils.getSquare('d', 7));
        assertEquals(Files.A + Ranks.EIGHT, SquareMapUtils.getSquare('a', 8));
        assertEquals(Files.H + Ranks.ONE, SquareMapUtils.getSquare('h', 1));
    }
}
