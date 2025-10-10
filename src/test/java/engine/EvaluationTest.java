import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluationTest {
    @Test
    @DisplayName("Test getValueSum")
    void testGetValueSum() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals(3900, Evaluation.getValueSum(position.getBoard(), PieceColour.WHITE));
        assertEquals(3900, Evaluation.getValueSum(position.getBoard(), PieceColour.BLACK));
        position = FENUtils.positionFromFEN("8/3b4/8/2k3r1/3pp3/2P2P2/2K1QN2/8 w - - 0 1");
        assertEquals(1400, Evaluation.getValueSum(position.getBoard(), PieceColour.WHITE));
        assertEquals(1000, Evaluation.getValueSum(position.getBoard(), PieceColour.BLACK));
    }
}
