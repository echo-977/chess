import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SquareMapUtilsTest {
    @Test
    @DisplayName("Test mapIntToSquare")
    void testMapIntToSquare() {
        assertEquals("a8", SquareMapUtils.mapIntToSquare(Squares.A8));
        assertEquals("h8", SquareMapUtils.mapIntToSquare(Squares.H8));
        assertEquals("h1", SquareMapUtils.mapIntToSquare(Squares.H1));
        assertEquals("d4", SquareMapUtils.mapIntToSquare(Squares.D4));
    }

    @Test
    @DisplayName("Test mapSquareToInt")
    void testMapSquareToInt() {
        assertEquals(Squares.H1, SquareMapUtils.mapSquareToInt("h1"));
        assertEquals(Squares.A8, SquareMapUtils.mapSquareToInt("a8"));
        assertEquals(Squares.D4, SquareMapUtils.mapSquareToInt("d4"));
    }

    @Test
    @DisplayName("Test getFile (int)")
    void testGetFile() {
        assertEquals('a', SquareMapUtils.getFile(Squares.A8));
        assertEquals('h', SquareMapUtils.getFile(Squares.H8));
        assertEquals('d', SquareMapUtils.getFile(Squares.D4));
    }

    @Test
    @DisplayName("Test getRank (int)")
    void testGetRank() {
        assertEquals(8, SquareMapUtils.getRank(Squares.A8));
        assertEquals(6, SquareMapUtils.getRank(Squares.H6));
        assertEquals(1, SquareMapUtils.getRank(Squares.E1));
    }

    @Test
    @DisplayName("Test getSquare")
    void testGetSquare() {
        assertEquals(Squares.D7, SquareMapUtils.getSquare('d', 7));
        assertEquals(Squares.A8, SquareMapUtils.getSquare('a', 8));
        assertEquals(Squares.H1, SquareMapUtils.getSquare('h', 1));
    }

    @Test
    @DisplayName("Test findDirection")
    void testFindDirection() {
        assertEquals(ChessDirections.UP, SquareMapUtils.findDirection(Squares.A5, Squares.A7));
        assertEquals(ChessDirections.RIGHT, SquareMapUtils.findDirection(Squares.C7, Squares.H7));
        assertEquals(ChessDirections.DOWN, SquareMapUtils.findDirection(Squares.D6, Squares.D3));
        assertEquals(ChessDirections.LEFT, SquareMapUtils.findDirection(Squares.G4, Squares.B4));
        assertEquals(ChessDirections.UP_RIGHT, SquareMapUtils.findDirection(Squares.C4, Squares.F7));
        assertEquals(ChessDirections.DOWN_RIGHT, SquareMapUtils.findDirection(Squares.E6, Squares.H3));
        assertEquals(ChessDirections.DOWN_LEFT, SquareMapUtils.findDirection(Squares.G2, Squares.F1));
        assertEquals(ChessDirections.UP_LEFT, SquareMapUtils.findDirection(Squares.F3, Squares.A8));
        assertEquals(ChessDirections.NONE, SquareMapUtils.findDirection(Squares.A5, Squares.D6));
    }
}
