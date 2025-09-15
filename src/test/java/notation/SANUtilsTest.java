import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SANUtilsTest {
    @Test
    @DisplayName("Test getAlgebraicNotation")
    void testGetAlgebraicNotation() {
        Position position = FENUtils.positionFromFEN("8/4k3/8/1N6/8/4K3/8/8 w - - 0 1");
        int moveFlag = MoveFlags.QUIET_MOVE;
        assertEquals("Nc7", SANUtils.getAlgebraicNotation(position, moveFlag, Squares.C7, Squares.B5));
        moveFlag = MoveFlags.CAPTURE_BIT;
        position = FENUtils.positionFromFEN("8/2p5/4k3/1N6/8/4K3/8/8 w - - 0 1");
        assertEquals("Nxc7+", SANUtils.getAlgebraicNotation(position, moveFlag, Squares.C7, Squares.B5));
        position = FENUtils.positionFromFEN("8/8/8/4k3/1N1N4/4K3/8/8 w - - 0 1");
        moveFlag = MoveFlags.QUIET_MOVE;
        assertEquals("Nbc6+", SANUtils.getAlgebraicNotation(position, moveFlag, Squares.C6, Squares.B4));
        position = FENUtils.positionFromFEN("1N6/8/8/8/1N4k1/4K3/8/8 w - - 0 1");
        assertEquals("N4c6", SANUtils.getAlgebraicNotation(position, moveFlag, Squares.C6, Squares.B4));
        position = FENUtils.positionFromFEN("1N6/8/8/8/1N1N2k1/4K3/8/8 w - - 0 1");
        assertEquals("Nb4c6", SANUtils.getAlgebraicNotation(position, moveFlag, Squares.C6, Squares.B4));
        position = FENUtils.positionFromFEN("8/8/3k4/8/8/8/8/R3K2R w KQ - 0 1");
        moveFlag = MoveFlags.QUEENSIDE_CASTLE;
        assertEquals("O-O-O+",  SANUtils.getAlgebraicNotation(position, moveFlag, Squares.C1, Squares.E1));
        moveFlag = MoveFlags.KINGSIDE_CASTLE;
        assertEquals("O-O", SANUtils.getAlgebraicNotation(position, moveFlag, Squares.G1, Squares.E1));
    }
}
