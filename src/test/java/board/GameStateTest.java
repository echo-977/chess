import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameStateTest {
    @Test
    @DisplayName("Test castlingRights")
    void testCastlingRights() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int castlingRights = (FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.WHITE_QUEENSIDE_CASTLE_MASK |
                FENConstants.BLACK_KINGSIDE_CASTLE_MASK |  FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        assertEquals(castlingRights, position.getGameState().getCastlingRights());
        position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kkq - 0 1");
        castlingRights = (FENConstants.WHITE_KINGSIDE_CASTLE_MASK |
                FENConstants.BLACK_KINGSIDE_CASTLE_MASK |  FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        assertEquals(castlingRights, position.getGameState().getCastlingRights());
        position = FENUtils.positionFromFEN("4k2r/8/8/5R2/8/8/8/8 w k - 0 1");
        castlingRights = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
        assertEquals(castlingRights, position.getGameState().getCastlingRights());
    }

    @Test
    @DisplayName("Test getEnPassantTarget")
    void testGetEnPassantTarget() {
        Position position = FENUtils.positionFromFEN("rn1qkb1r/pp3p1p/5n2/2pPp3/6p1/2B2P2/PPP1P1PP/RN1QKB1R w KQkq c6 0 10");
        assertEquals(Files.C + Ranks.SIX, position.getGameState().getEnPassantTarget());
        position = FENUtils.positionFromFEN("rnbqkbnr/pppp1p1p/8/4pPp1/8/8/PPPPP1PP/RNBQKBNR w KQkq g6 0 2");
        assertEquals(Files.G + Ranks.SIX, position.getGameState().getEnPassantTarget());
        position = FENUtils.positionFromFEN("rnbqkbnr/p1pppppp/8/8/1pP5/8/PP1PPPPP/RNBQKBNR b KQkq c3 0 3");
        assertEquals(Files.C + Ranks.THREE, position.getGameState().getEnPassantTarget());
    }
}
