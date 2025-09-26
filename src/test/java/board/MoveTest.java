import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    @DisplayName("Test moveConstructor")
    void testMove() {
        Position position = FENUtils.positionFromFEN("8/5k2/8/3r4/8/8/8/4K3 w - - 0 1");
        IntArrayList moves = new IntArrayList(1);
        Move.createIfLegal(position, moves, Squares.E5, Squares.D5);
        assertNotEquals(MoveFlags.NO_MOVE, moves.getInt(0));
    }

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

