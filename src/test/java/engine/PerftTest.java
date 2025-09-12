import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

public class PerftTest {
    //all perft tests are disabled as they are very time-consuming so are only run manually
    @Disabled
    @Test
    @DisplayName("Test generateMoves (start position)")
    void  testGenerateMovesStartingPosition() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        //assertEquals(20, Perft.ThreadedPerft(position, 1, false));
        //assertEquals(400, Perft.ThreadedPerft(position, 2, false));
        //assertEquals(8902, Perft.ThreadedPerft(position, 3, false));
        //assertEquals(197281, Perft.ThreadedPerft(position, 4, false));
        //assertEquals(4865609, Perft.ThreadedPerft(position, 5, false));
        assertEquals(119060324, Perft.ThreadedPerft(position, 6, true));
    }

    @Disabled
    @Test
    @DisplayName("Test generateMoves (position 2)")
    void testGenerateMovesPosition2() {
        Position position = FENUtils.positionFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        //assertEquals(48, Perft.ThreadedPerft(position, 1, false));
        //assertEquals(2039, Perft.ThreadedPerft(position, 2, false));
        //assertEquals(97862, Perft.ThreadedPerft(position, 3, false));
        //assertEquals(4085603, Perft.ThreadedPerft(position, 4, false));
        assertEquals(193690690, Perft.ThreadedPerft(position, 5, false));
    }

    @Disabled
    @Test
    @DisplayName("Test generateMoves (position 3)")
    void testGenerateMovesPosition3() {
        Position position = FENUtils.positionFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        //assertEquals(14, Perft.ThreadedPerft(position, 1, false));
        //assertEquals(191, Perft.ThreadedPerft(position, 2, false));
        //assertEquals(2812, Perft.ThreadedPerft(position, 3, false));
        //assertEquals(43238, Perft.ThreadedPerft(position, 4, false));
        //assertEquals(674624, Perft.ThreadedPerft(position, 5, false));
        //assertEquals(11030083, Perft.ThreadedPerft(position, 6, false));
    }

    @Disabled
    @Test
    @DisplayName("Test generateMoves (position 4)")
    void testGenerateMovesPosition4() {
        Position position = FENUtils.positionFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        //assertEquals(6, Perft.ThreadedPerft(position, 1, false));
        //assertEquals(264, Perft.ThreadedPerft(position, 2, false));
        //assertEquals(9467, Perft.ThreadedPerft(position, 3, false));
        //assertEquals(422333, Perft.ThreadedPerft(position, 4, false));
        //assertEquals(15833292, Perft.ThreadedPerft(position, 5, false));
        assertEquals(706045033, Perft.ThreadedPerft(position, 6, true));
    }

    @Disabled
    @Test
    @DisplayName("Test generateMoves (position 5)")
    void  testGenerateMovesPosition5() {
        Position position = FENUtils.positionFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        //assertEquals(44, Perft.ThreadedPerft(position, 1, false));
        //assertEquals(1486, Perft.ThreadedPerft(position, 2, false));
        //assertEquals(62379, Perft.ThreadedPerft(position, 3, false));
        //assertEquals(2103487, Perft.ThreadedPerft(position, 4, false));
        assertEquals(89941194, Perft.ThreadedPerft(position, 5, true));
    }

    @Disabled
    @Test
    @DisplayName("Test generateMoves (position 6)")
    void testGenerateMovesPosition6() {
        Position position = FENUtils.positionFromFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
        //assertEquals(46, Perft.ThreadedPerft(position, 1, false));
        //assertEquals(2079, Perft.ThreadedPerft(position, 2, false));
        //assertEquals(89890, Perft.ThreadedPerft(position, 3, false));
        //assertEquals(3894594, Perft.ThreadedPerft(position, 4, false));
        assertEquals(164075551, Perft.ThreadedPerft(position, 5, true));
    }
}
