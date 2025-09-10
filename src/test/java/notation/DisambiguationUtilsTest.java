import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisambiguationUtilsTest {
    @Test
    @DisplayName("Test setDisambiguationFlags (file only)")
    void testSetDisambiguationFlagsFile() {
        Position position = FENUtils.positionFromFEN("8/8/3N1N2/2N5/4p3/6N1/8/8 w - - 0 1");
        Board board = position.getBoard();
        Move[] moves = {new Move(position, board.pieceSearch(Files.C+ Ranks.FIVE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.D + Ranks.SIX), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.F + Ranks.SIX), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.G + Ranks.THREE), Files.E + Ranks.FOUR)};
        DisambiguationUtils.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertTrue(move.isFileDisambiguation());
            assertFalse(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (rank only)")
    void testSetDisambiguationFlagsRank() {
        Position position = FENUtils.positionFromFEN("8/8/8/2N5/4p3/2N5/8/8 w - - 0 1");
        Board board = position.getBoard();
        Move[] moves = {new Move(position, board.pieceSearch(Files.C + Ranks.FIVE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.C + Ranks.THREE), Files.E + Ranks.FOUR)};
        DisambiguationUtils.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertFalse(move.isFileDisambiguation());
            assertTrue(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (both)")
    void testSetDisambiguationFlagsBoth() {
        Position position = FENUtils.positionFromFEN("8/8/8/2N5/4p3/2N3N1/8/8 w - - 0 1");
        Board board = position.getBoard();
        Move[] moves = {new Move(position, board.pieceSearch(Files.C + Ranks.FIVE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.C + Ranks.THREE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.G + Ranks.THREE), Files.E + Ranks.FOUR)};
        DisambiguationUtils.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertTrue(move.isFileDisambiguation());
            assertTrue(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test handleDisambiguation")
    void  testHandleDisambiguation() {
        Position position = FENUtils.positionFromFEN("8/8/8/2N5/4p1R1/2N3N1/8/8 w - - 0 1");
        Board board = position.getBoard();
        Move[] moves = {new Move(position, board.pieceSearch(Files.C + Ranks.FIVE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.C + Ranks.THREE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.G + Ranks.THREE), Files.E + Ranks.FOUR),
                new Move(position, board.pieceSearch(Files.C + Ranks.FIVE), Files.D + Ranks.SEVEN),
                new Move(position, board.pieceSearch(Files.G + Ranks.FOUR), Files.E + Ranks.FOUR)};
        DisambiguationUtils.handleDisambiguation(moves);
        assertTrue(moves[0].isFileDisambiguation());
        assertTrue(moves[0].isRankDisambiguation());
        assertTrue(moves[1].isFileDisambiguation());
        assertTrue(moves[1].isRankDisambiguation());
        assertTrue(moves[2].isFileDisambiguation());
        assertTrue(moves[2].isRankDisambiguation());
        assertFalse(moves[3].isFileDisambiguation());
        assertFalse(moves[3].isRankDisambiguation());
        assertFalse(moves[4].isFileDisambiguation());
        assertFalse(moves[4].isRankDisambiguation());
    }
}
