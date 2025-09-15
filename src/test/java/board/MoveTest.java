import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @Test
    @DisplayName("Test moveConstructor")
    void testMove() {
        Position position = FENUtils.positionFromFEN("8/5k2/8/3r4/8/8/8/4K3 w - - 0 1");
        int move = Move.createIfLegal(position, Squares.E5, Squares.D5);
        assertNotEquals(MoveFlags.NO_MOVE, move);
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

    @Test
    @DisplayName("Test illegalMoveDetection (pin)")
    void testIllegalMoveDetection() {
        Position position = FENUtils.positionFromFEN("8/8/R2b1k2/8/8/8/8/8 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Squares.D6);
        int[] expectedMoves = new int[ChessConstants.MAX_BISHOP_MOVES];
        assertArrayEquals(expectedMoves, piece.generateMoves(position));
    }

    @Test
    @DisplayName("Test illegalMoveDetection (blocking check)")
    void testIllegalMoveDetection2() {
        Position position = FENUtils.positionFromFEN("8/8/R4k2/2b5/8/3K4/8/8 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Files.C + Ranks.FIVE);
        int[] expectedMoves = new int[ChessConstants.MAX_BISHOP_MOVES];
        expectedMoves[0] = (MoveFlags.QUIET_MOVE << MoveFlags.FLAG_SHIFT) | (Squares.B6 << MoveFlags.DESTINATION_SHIFT) | Squares.C5;
        expectedMoves[1] = (MoveFlags.QUIET_MOVE << MoveFlags.FLAG_SHIFT) | (Squares.D6 << MoveFlags.DESTINATION_SHIFT) | Squares.C5;
        assertArrayEquals(expectedMoves, piece.generateMoves(position));
    }
}

