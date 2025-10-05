import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreatMapGeneratorTest {

    @Test
    @DisplayName("Test getThreatMap (position 1)")
    void testGetThreatMap() {
        Board board = FENUtils.positionFromFEN("8/8/3r4/4r3/4b3/8/8/8 w - - 0 1").getBoard();
        long expectedThreatMap = 0b10001010_01001100_00101000_00011000_11101111_11110111_10011010_00011001L;
        long actualThreatMap = ThreatMapGenerator.getThreatMap(board, PieceColour.BLACK);
        assertEquals(expectedThreatMap,  actualThreatMap);
    }

    @Test
    @DisplayName("Test getThreatMap (position 2)")
    void testGetThreatMap2() {
        Board board = FENUtils.positionFromFEN("rnb1kbnr/ppppqppp/4p3/8/8/4P3/PPPPKPPP/RNBQ1BNR w kq - 0 1").getBoard();
        long expectedThreatMap = 0b00000000_00000000_00000001_10000010_01101100_11111111_11111011_01111010L;
        long actualThreatMap =  ThreatMapGenerator.getThreatMap(board, PieceColour.BLACK);
        assertEquals(expectedThreatMap,  actualThreatMap);
    }

    @Test
    @DisplayName("Test setUpAttackTables")
    void testSetUpAttackTables() {
        Board board = FENUtils.positionFromFEN("8/5B2/8/3q4/8/3R4/8/8 w - - 0 1").getBoard();
        long[] expectedATKFR = new long[ChessConstants.NUM_SQUARES];
        expectedATKFR[13] = 0b00000000_00000000_00000000_00000000_10001000_01010000_00000000_01010000L;
        expectedATKFR[27] = 0b10000000_01000001_00101010_00011100_11110111_00011100_00101010_00001001L;
        expectedATKFR[43] = 0b00001000_00001000_11110111_00001000_00001000_00000000_00000000_00000000L;
        assertArrayEquals(expectedATKFR, board.getATKFR());
        long queenSquare = 0b00000000_00000000_00000000_00000000_00001000_00000000_00000000_00000000L;
        long bishopSquare = 0b00000000_00000000_00000000_00000000_00000000_00000000_00100000_00000000L;
        long rookSquare = 0b00000000_00000000_00001000_00000000_00000000_00000000_00000000_00000000L;
        long[] expectedATKTO = {queenSquare, 0 , 0 , queenSquare, bishopSquare, 0, bishopSquare, 0,
                0, queenSquare, 0, queenSquare, 0, queenSquare, 0, 0,
                0, 0, queenSquare, queenSquare, queenSquare | bishopSquare, 0, bishopSquare, 0,
                queenSquare, queenSquare, queenSquare, rookSquare | bishopSquare, queenSquare, queenSquare, queenSquare, queenSquare | bishopSquare,
                0, 0, queenSquare, queenSquare | rookSquare, queenSquare, 0, 0, 0,
                rookSquare, queenSquare | rookSquare, rookSquare, queenSquare, rookSquare, queenSquare | rookSquare, rookSquare, rookSquare,
                queenSquare, 0, 0, rookSquare, 0, 0, queenSquare, 0,
                0, 0, 0, rookSquare, 0, 0, 0, queenSquare
        };
        assertArrayEquals(expectedATKTO, board.getATKTO());
    }

    @Test
    @DisplayName("Test updateAttackTables (quiet move)")
    void testUpdateAttackTablesQuiet() {
        Position before = FENUtils.positionFromFEN("8/5B2/8/3q4/8/3R4/8/6R1 w - - 0 1");
        before.doMove(Move.encodeMove(before, Squares.G5, Squares.D5));
        Board boardBefore = before.getBoard();
        ThreatMapGenerator.updateAttackTables(boardBefore, Squares.D5, Squares.G5, MoveFlags.QUIET_MOVE, null);
        Board boardAfter = FENUtils.positionFromFEN("8/5B2/8/6q1/8/3R4/8/6R1 b - - 0 1").getBoard();
        assertArrayEquals(boardAfter.getATKFR(), boardBefore.getATKFR());
        assertArrayEquals(boardAfter.getATKTO(), boardBefore.getATKTO());
    }

    @Test
    @DisplayName("Test updateAttackTables (capture move)")
    void testUpdateAttackTablesCapture() {
        Position before = FENUtils.positionFromFEN("8/5B2/8/3q2B1/8/3R4/8/6R1 w - - 0 1");
        int flags = MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT;
        State move = before.doMove(Move.encodeMove(before, Squares.G5, Squares.D5));
        Board boardBefore = before.getBoard();
        ThreatMapGenerator.updateAttackTables(boardBefore, Squares.D5, Squares.G5, flags, move.capturedPiece());
        Board boardAfter = FENUtils.positionFromFEN("8/5B2/8/6q1/8/3R4/8/6R1 b - - 0 1").getBoard();
        assertArrayEquals(boardAfter.getATKFR(), boardBefore.getATKFR());
        assertArrayEquals(boardAfter.getATKTO(), boardBefore.getATKTO());
    }

    @Test
    @DisplayName("Test updateAttackTables (promotion move)")
    void testUpdateAttackTablesPromotion() {
        Position before = FENUtils.positionFromFEN("8/3P2r1/8/8/8/8/8/8 w - - 0 1");
        int flags = MoveFlags.PROMOTION_BIT | MoveFlags.BISHOP;
        before.doMove(flags << MoveFlags.FLAG_SHIFT | Move.encodeMove(before, Squares.D8, Squares.D7));
        Board boardBefore = before.getBoard();
        ThreatMapGenerator.updateAttackTables(boardBefore, Squares.D7, Squares.D8, flags, null);
        Board boardAfter = FENUtils.positionFromFEN("3B4/6r1/8/8/8/8/8/8 b - - 0 1").getBoard();
        assertArrayEquals(boardAfter.getATKFR(), boardBefore.getATKFR());
        assertArrayEquals(boardAfter.getATKTO(), boardBefore.getATKTO());
    }

    @Test
    @DisplayName("Test updateAttackTables (castle move)")
    void testUpdateAttackTablesCastle() {
        Position before = FENUtils.positionFromFEN("8/8/8/8/8/8/8/4K2R w K - 0 1");
        int flags = MoveFlags.KINGSIDE_CASTLE;
        before.doMove(flags << MoveFlags.FLAG_SHIFT | Move.encodeMove(before, Squares.G1, Squares.E1));
        Board boardBefore = before.getBoard();
        ThreatMapGenerator.updateAttackTables(boardBefore, Squares.E1, Squares.G1, flags, null);
        Board boardAfter = FENUtils.positionFromFEN("8/8/8/8/8/8/8/5RK1 b - - 0 1").getBoard();
        assertArrayEquals(boardAfter.getATKFR(), boardBefore.getATKFR());
        assertArrayEquals(boardAfter.getATKTO(), boardBefore.getATKTO());
    }

    @Test
    @DisplayName("Test updateAttackTables (en passant move)")
    void testUpdateAttackTablesEnPassant() {
        Position before = FENUtils.positionFromFEN("8/8/8/3Pp3/8/8/8/8 w - e6 0 1");
        int flags = MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT;
        State move = before.doMove(flags << MoveFlags.FLAG_SHIFT | Move.encodeMove(before, Squares.E6, Squares.D5));
        Board boardBefore = before.getBoard();
        ThreatMapGenerator.updateAttackTables(boardBefore, Squares.D5, Squares.E6, flags, move.capturedPiece());
        Board boardAfter = FENUtils.positionFromFEN("8/8/4P3/8/8/8/8/8 b - - 0 1").getBoard();
        assertArrayEquals(boardAfter.getATKFR(), boardBefore.getATKFR());
        assertArrayEquals(boardAfter.getATKTO(), boardBefore.getATKTO());
    }
}
