import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreatMapGeneratorTest {

    @Test
    @DisplayName("Test getThreatMap (position 1)")
    void testComputeThreatMap() {
        Board board = FENUtils.positionFromFEN("8/8/3r4/4r3/4b3/8/8/8 w - - 0 1").getBoard();
        long expectedThreatMap = 0b10001010_01001100_00101000_00011000_11101111_11110111_10011010_00011001L;
        long actualThreatMap = ThreatMapGenerator.computeThreatMap(board, PieceColour.BLACK);
        assertEquals(expectedThreatMap,  actualThreatMap);
    }

    @Test
    @DisplayName("Test getThreatMap (position 2)")
    void testComputeThreatMap2() {
        Board board = FENUtils.positionFromFEN("rnb1kbnr/ppppqppp/4p3/8/8/4P3/PPPPKPPP/RNBQ1BNR w kq - 0 1").getBoard();
        long expectedThreatMap = 0b00000000_00000000_00000001_10000010_01101100_11111111_11111011_01111010L;
        long actualThreatMap =  ThreatMapGenerator.computeThreatMap(board, PieceColour.BLACK);
        assertEquals(expectedThreatMap,  actualThreatMap);
    }
}
