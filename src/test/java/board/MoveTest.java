import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    @DisplayName("Test castling detection")
    void testCastlingDetection() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        int moveFlag = ((Move.encodeMove(position, Squares.G1, Squares.E1) >> MoveFlags.FLAG_SHIFT) & MoveFlags.KINGSIDE_CASTLE);
        assertEquals(MoveFlags.KINGSIDE_CASTLE, moveFlag);
        moveFlag = ((Move.encodeMove(position, Squares.C1, Squares.E1) >> MoveFlags.FLAG_SHIFT) & MoveFlags.QUEENSIDE_CASTLE);
        assertEquals(MoveFlags.QUEENSIDE_CASTLE, moveFlag);
        moveFlag = ((Move.encodeMove(position, Squares.G8, Squares.E8) >> MoveFlags.FLAG_SHIFT) & MoveFlags.KINGSIDE_CASTLE);
        assertEquals(MoveFlags.KINGSIDE_CASTLE, moveFlag);
        moveFlag = ((Move.encodeMove(position, Squares.C8, Squares.E8) >> MoveFlags.FLAG_SHIFT) & MoveFlags.QUEENSIDE_CASTLE);
        assertEquals(MoveFlags.QUEENSIDE_CASTLE, moveFlag);
    }
}

