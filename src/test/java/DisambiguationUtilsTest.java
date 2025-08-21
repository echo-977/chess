import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisambiguationUtilsTest {
    @Test
    @DisplayName("Test setDisambiguationFlags (file only)")
    void testSetDisambiguationFlagsFile() {
        Board board = FENUtils.boardFromFEN("8/8/3N1N2/2N5/4p3/6N1/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("d6"), "e4"),
                new Move(board, board.pieceSearch("f6"), "e4"),
                new Move(board, board.pieceSearch("g3"), "e4")};
        DisambiguationUtils.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertTrue(move.isFileDisambiguation());
            assertFalse(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (rank only)")
    void testSetDisambiguationFlagsRank() {
        Board board = FENUtils.boardFromFEN("8/8/8/2N5/4p3/2N5/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("c3"), "e4")};
        DisambiguationUtils.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertFalse(move.isFileDisambiguation());
            assertTrue(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test setDisambiguationFlags (both)")
    void testSetDisambiguationFlagsBoth() {
        Board board = FENUtils.boardFromFEN("8/8/8/2N5/4p3/2N3N1/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("c3"), "e4"),
                new Move(board, board.pieceSearch("g3"), "e4")};
        DisambiguationUtils.setDisambiguationFlags(moves);
        for (Move move : moves) {
            assertTrue(move.isFileDisambiguation());
            assertTrue(move.isRankDisambiguation());
        }
    }

    @Test
    @DisplayName("Test handleDisambiguation")
    void  testHandleDisambiguation() {
        Board board = FENUtils.boardFromFEN("8/8/8/2N5/4p1R1/2N3N1/8/8 w - - 0 1");
        Move[] moves = {new Move(board, board.pieceSearch("c5"), "e4"),
                new Move(board, board.pieceSearch("c3"), "e4"),
                new Move(board, board.pieceSearch("g3"), "e4"),
                new Move(board, board.pieceSearch("c5"), "d7"),
                new Move(board, board.pieceSearch("g4"), "e4")};
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
