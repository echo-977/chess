import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ThreatMapGeneratorTest {

    @Test
    @DisplayName("Test getThreatMap")
    void testGetThreatMap() {
        Board board = FENUtils.boardFromFEN("8/8/3r4/4r3/4b3/8/8/8 w - - 0 1");
        boolean[] expectedThreatMap = {true, false, false, true, true, false, false, false,
                false, true, false, true, true, false, false, true,
                true, true, true, false, true, true, true, true,
                true, true, true, true, false, true, true, true,
                false, false, false, true, true, false, false, false,
                false, false, false, true, false, true, false, false,
                false, false, true, true, false, false, true, false,
                false, true, false, true, false, false, false, true};
        boolean[] actualThreatMap = ThreatMapGenerator.getThreatMap(board, PieceColour.BLACK);
        assertArrayEquals(expectedThreatMap,  actualThreatMap);
    }

    @Test
    @DisplayName("Test getThreatMap (position 2)")
    void testGetThreatMap2() {
        Board board = FENUtils.boardFromFEN("rnb1kbnr/ppppqppp/4p3/8/8/4P3/PPPPKPPP/RNBQ1BNR w kq - 0 1");
        boolean[] expectedThreatMap = {false, true, false, true, true, true, true, false,
                true, true, false, true, true, true, true, true,
                true, true, true, true, true, true, true, true,
                false, false, true, true, false, true, true, false,
                false, true, false, false, false, false, false, true,
                true, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false};
        boolean[] actualThreatMap =  ThreatMapGenerator.getThreatMap(board, PieceColour.BLACK);
        assertArrayEquals(expectedThreatMap,  actualThreatMap);
    }
}
