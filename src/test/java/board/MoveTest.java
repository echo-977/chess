import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    @DisplayName("Test move encoding (quiet)")
    void testQuietMoveEncoding() {
        Position position = FENUtils.positionFromFEN("8/8/8/2R5/8/8/8/8 w - - 0 1");
        int move = Move.encodeMove(position, Squares.D5, Squares.C5);
        assertEquals(Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C5, move);
    }

    @Test
    @DisplayName("Test move encoding (capture)")
    void testCaptureMoveEncoding() {
        Position position = FENUtils.positionFromFEN("8/8/8/2Rp4/8/8/8/8 w - - 0 1");
        int move = Move.encodeMove(position, Squares.D5, Squares.C5);
        assertEquals(MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C5, move);
    }
}

