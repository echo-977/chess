import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveGeneratorTest {
    @Test
    @DisplayName("Test computeRays")
    public void computeRaysTest() {
        Position position = FENUtils.positionFromFEN("7b/b7/1N3R2/2N5/r1RK2q1/8/8/6b1 w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator();
        moveGenerator.computeRays(position);
        long checkRay = moveGenerator.getCheckRay();
        long expectedCheckRay = 0b01000000_00100000_00010000_01111000_00000000_00000000_00000000_00000000L;
        assertEquals(expectedCheckRay, checkRay);
        long pinRay = moveGenerator.getPinRay();
        long expectedPinRay = 0b00000000_00000000_00000000_00001111_00010000_00100000_01000000_10000000L;
        assertEquals(expectedPinRay, pinRay);
        assertTrue(moveGenerator.isDoubleCheck());
        position = FENUtils.positionFromFEN("8/8/8/2q5/3K4/8/8/8 w - - 0 1");
        moveGenerator.computeRays(position);
        checkRay = moveGenerator.getCheckRay();
        expectedCheckRay = 0b00000000_00000000_00000000_00001000_00000100_00000000_00000000_00000000L;
        assertEquals(expectedCheckRay, checkRay);
        pinRay = moveGenerator.getPinRay();
        expectedPinRay = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertEquals(expectedPinRay, pinRay);
        assertFalse(moveGenerator.isDoubleCheck());
    }
}
