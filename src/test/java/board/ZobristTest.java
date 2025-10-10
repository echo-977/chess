import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ZobristTest {

    @Test
    @DisplayName("Test consistent")
    void testConsistentKey() {
        Position pos1 = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Position pos2 = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals(pos1.getZobristKey(), pos2.getZobristKey());
    }

    @Test
    @DisplayName("Test unDoMove restores original key")
    void testMoveUndoRestoresKey() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("e2e4");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test capture updates key")
    void testCaptureUpdatesKey() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("e4d5");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test promotion updates key")
    void testPromotionUpdatesKey() {
        Position position = FENUtils.positionFromFEN("4k3/P7/8/8/8/8/8/4K3 w - - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("a7a8q");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test castling updates key")
    void testCastlingUpdatesKey() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("e1g1");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test en passant capture updates key")
    void testEnPassantUpdatesKey() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/ppppp1pp/8/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("e5f6");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test double pawn push sets en passant key")
    void testDoublePawnPushSetsKey() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("e2e4");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test castling rights removal updates key")
    void testRemoveCastlingRightsKey() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        position.doMove("e1e2");
        Position positionWithoutWhiteCastling = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/4K3/R6R b kq - 1 1");
        assertEquals(positionWithoutWhiteCastling.getZobristKey(), position.getZobristKey());
    }

    @Test
    @DisplayName("Test turn change updates key")
    void testTurnKey() {
        Position whiteToMove = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Position blackToMove = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1");
        assertNotEquals(whiteToMove.getZobristKey(), blackToMove.getZobristKey());
        long difference = whiteToMove.getZobristKey() ^ blackToMove.getZobristKey();
        assertEquals(Zobrist.TURN_KEY, difference);
    }

    @Test
    @DisplayName("Test transposition detection")
    void testTranspositionDetection() {
        Position positon1 = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Position position2 = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        positon1.doMove("e2e4");
        positon1.doMove("e7e5");
        positon1.doMove("g1f3");
        positon1.doMove("b8c6");
        positon1.doMove("f1c4");
        position2.doMove("g1f3");
        position2.doMove("b8c6");
        position2.doMove("e2e4");
        position2.doMove("e7e5");
        position2.doMove("f1c4");
        assertEquals(positon1.getZobristKey(), position2.getZobristKey());
    }

    @Test
    @DisplayName("Test multiple moves maintain key consistency")
    void testMultipleMoves() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        State[] states = new State[10];
        long[] keys = new long[11];
        keys[0] = position.getZobristKey();
        String[] moves = {"e2e4", "e7e5", "g1f3", "b8c6", "f1c4", "g8f6", "d2d3", "f8c5", "b1c3", "d7d6"};
        for (int i = 0; i < moves.length; i++) {
            states[i] = position.doMove(moves[i]);
            keys[i + 1] = position.getZobristKey();
        }
        for (int i = moves.length - 1; i >= 0; i--) {
            position.unDoMove(states[i]);
            assertEquals(keys[i], position.getZobristKey());
        }
    }

    @Test
    @DisplayName("Test rook capture removes castling rights")
    void testRookCaptureUpdatesKey() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/6R1/4K3 w kq - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("g2h8");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test different positions have different keys")
    void testDifferentKeys() {
        Position pos1 = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Position pos2 = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1");
        assertNotEquals(pos1.getZobristKey(), pos2.getZobristKey());
    }

    @Test
    @DisplayName("Test en passant target affects key")
    void testEnPassantTargetUpdatesKey() {
        Position withEP = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        Position withoutEP = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        assertNotEquals(withEP.getZobristKey(), withoutEP.getZobristKey());
    }

    @Test
    @DisplayName("Test queenside castling updates key")
    void testQueensideCastlingUpdatesKey() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("e1c1");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test promotion with capture updates key")
    void testPromotionCaptureUpdatesKey() {
        Position position = FENUtils.positionFromFEN("1nbqkbn1/P6P/8/8/8/8/8/4K3 w - - 0 1");
        long key = position.getZobristKey();
        State state = position.doMove("a7b8q");
        assertNotEquals(key, position.getZobristKey());
        position.unDoMove(state);
        assertEquals(key, position.getZobristKey());
    }

    @Test
    @DisplayName("Test rook move removes castling rights")
    void testRookMoveRemovesCastling() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        State state = position.doMove("a1a2");
        Position noCastle = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/R7/4K2R b Kkq - 1 1");
        assertEquals(noCastle.getZobristKey(), position.getZobristKey());
        position.unDoMove(state);
        Position originalPosition = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        assertEquals(originalPosition.getZobristKey(), position.getZobristKey());
    }

    @Test
    @DisplayName("Test complex sequence with multiple captures")
    void testComplexSequence() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        State[] states = new State[6];
        long[] keys = new long[7];
        keys[0] = position.getZobristKey();
        String[] moves = {"e2e4", "d7d5", "e4d5", "d8d5", "b1c3", "d5a5"};
        for (int i = 0; i < moves.length; i++) {
            states[i] = position.doMove(moves[i]);
            keys[i + 1] = position.getZobristKey();
            assertNotEquals(keys[i], keys[i + 1]);
        }
        for (int i = moves.length - 1; i >= 0; i--) {
            position.unDoMove(states[i]);
            assertEquals(keys[i], position.getZobristKey());
        }
    }
}
