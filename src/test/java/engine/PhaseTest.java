import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PhaseTest {
    @Test
    @DisplayName("Test computePhase")
    void testPhase() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals(0, Phase.computePhase(position.getBoard()));
        position = FENUtils.positionFromFEN("8/8/3k4/2p2PPp/8/3K4/8/8 w - - 0 1");
        assertEquals(256, Phase.computePhase(position.getBoard()));
        position = FENUtils.positionFromFEN("8/3b4/8/2k3r1/3pp3/2P2P2/2K1QN2/8 w - - 0 1");
        assertEquals(171, Phase.computePhase(position.getBoard()));
        position = FENUtils.positionFromFEN("r4rk1/1bpp1pqp/3b2p1/1p2p1B1/1n2P3/P4N1Q/1PP2PPP/R4RK1 b - - 0 16");
        assertEquals(32, Phase.computePhase(position.getBoard()));
    }
}