import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UCIUtilsTest {
    @Test
    @DisplayName("Test moveToUCIString")
    void testMoveToUCIString() {
        Position position = FENUtils.positionFromFEN("8/8/3p4/8/2N5/8/8/8 w - - 0 1");
        int move = Move.encodeMove(position, Squares.D6, Squares.C4);
        assertEquals("c4d6",  UCIUtils.moveToUCIString(move));
        position = FENUtils.positionFromFEN("8/3p4/8/8/2N5/8/8/8 b - - 0 1");
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.BISHOP) << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.D8, Squares.D7);
        assertEquals("d7d8b",  UCIUtils.moveToUCIString(move));
    }

    @Test
    @DisplayName("Test UCIToEncodedMove")
    void testUCIToEncodedMove() {
        Position position = FENUtils.positionFromFEN("8/8/8/8/2N5/8/6P1/8 w - - 0 1");
        int move = MoveFlags.DOUBLE_PAWN_PUSH << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.G4, Squares.G2);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "g2g4"));
        position = FENUtils.positionFromFEN("8/8/8/8/2N5/8/6P1/R3K2R w KQ - 0 1");
        move = MoveFlags.KINGSIDE_CASTLE << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.G1, Squares.E1);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "e1g1"));
        move = MoveFlags.QUEENSIDE_CASTLE << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.C1, Squares.E1);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "e1c1"));
        position = FENUtils.positionFromFEN("8/8/8/7p/2N5/8/6P1/R3K2R w KQ - 0 1");
        move = MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.H5, Squares.H1);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "h1h5"));
        position = FENUtils.positionFromFEN("8/8/8/4Pp2/2N5/8/6P1/R3K2R w KQ f6 0 1");
        move = (MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT) << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.F6, Squares.E5);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "e5f6"));
        position = FENUtils.positionFromFEN("8/5p2/8/4P3/2N5/8/6P1/R3K2R w KQ - 0 1");
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.QUEEN) << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.F8, Squares.F7);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "f7f8q"));
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.ROOK) << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.F8, Squares.F7);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "f7f8r"));
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.BISHOP) << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.F8, Squares.F7);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "f7f8b"));
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.KNIGHT) << MoveFlags.FLAG_SHIFT | Move.encodeMove(position, Squares.F8, Squares.F7);
        assertEquals(move, UCIUtils.UCItoEncodedMove(position, "f7f8k"));
    }
}
