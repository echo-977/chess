import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveGeneratorTest {
    @Test
    @DisplayName("Test computeRays")
    public void computeRaysTest() {
        Position position = FENUtils.positionFromFEN("7b/b7/1N3R2/2N5/r1RK2q1/8/8/6b1 w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator();
        moveGenerator.computeRays(position);
        long checkRay = moveGenerator.getCheckRay();
        long expectedCheckRay = 0b01000000_00100000_00010000_01111000_00000000_00000000_00000000_00000000L;
        assertEquals(expectedCheckRay, checkRay);
        long pinRay = moveGenerator.getPinRay();
        long expectedPinRay = 0b00000000_00000000_00000000_00001111_00010000_00100000_01000000_10000000L;
        assertEquals(expectedPinRay, pinRay);
        assertTrue(moveGenerator.isDoubleCheck());
        position = FENUtils.positionFromFEN("8/8/8/2q5/3K4/8/8/8 w - - 0 1");
        moveGenerator.computeRays(position);
        checkRay = moveGenerator.getCheckRay();
        expectedCheckRay = 0b00000000_00000000_00000000_00001000_00000100_00000000_00000000_00000000L;
        assertEquals(expectedCheckRay, checkRay);
        pinRay = moveGenerator.getPinRay();
        expectedPinRay = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertEquals(expectedPinRay, pinRay);
        assertFalse(moveGenerator.isDoubleCheck());
    }
    @Test
    @DisplayName("Test illegalMoveFiltering (pin)")
    void testIllegalMoveFiltering() {
        Position position = FENUtils.positionFromFEN("8/8/R2b1k2/8/8/8/8/8 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator();
        IntArrayList pseudoLegalMoves = moveGenerator.generatePseudoLegalMoves(position);
        int[] expectedPseudoLegalMoves = {MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.F8 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.F4 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.G3 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.H2 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.A3 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.B8 << MoveFlags.DESTINATION_SHIFT | Squares.D6,
                MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.F6};
        assertArrayEquals(expectedPseudoLegalMoves, pseudoLegalMoves.toIntArray());
        IntArrayList legalMoves = moveGenerator.generateLegalMoves(position);
        int[] expectedLegalMoves = {MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.F6};
        assertArrayEquals(expectedLegalMoves, legalMoves.toIntArray());
    }

    @Test
    @DisplayName("Test illegalMoveFiltering (blocking check)")
    void testIllegalMoveFiltering2() {
        Position position = FENUtils.positionFromFEN("8/8/R4k2/2b5/8/3K4/8/8 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator();
        IntArrayList pseudoLegalMoves = moveGenerator.generatePseudoLegalMoves(position);
        int[] expectedPseudoLegalMoves = {MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.F8 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.F2 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.G1 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.A3 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.B6 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.A7 << MoveFlags.DESTINATION_SHIFT | Squares.C5};
        assertArrayEquals(expectedPseudoLegalMoves, pseudoLegalMoves.toIntArray());
        IntArrayList legalMoves = moveGenerator.generateLegalMoves(position);
        int[] expectedLegalMoves = {MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.C5,
                MoveFlags.QUIET_MOVE | Squares.B6 << MoveFlags.DESTINATION_SHIFT | Squares.C5};
        assertArrayEquals(expectedLegalMoves, legalMoves.toIntArray());
    }

    @Test
    @DisplayName("Test pawnCheckSearch")
    void testPawnCheckSearch() {
        Position position = FENUtils.positionFromFEN("8/8/2p5/1K6/4k3/8/8/8 w - - 0 1");
        assertEquals(Squares.C6, MoveGenerator.pawnCheckSearch(position));
        position = FENUtils.positionFromFEN("8/8/5k2/8/3p4/4K3/8/8 w - - 0 1");
        assertEquals(Squares.D4, MoveGenerator.pawnCheckSearch(position));
        position = FENUtils.positionFromFEN("8/8/5k2/6P1/8/4K3/8/8 b - - 0 1");
        assertEquals(Squares.G5, MoveGenerator.pawnCheckSearch(position));
        position = FENUtils.positionFromFEN("8/8/8/2k5/1P6/4K3/8/8 b - - 0 1");
        assertEquals(Squares.B4, MoveGenerator.pawnCheckSearch(position));
        position = FENUtils.positionFromFEN("8/8/8/2k5/8/4K3/8/8 w - - 0 1");
        assertEquals(Squares.NONE, MoveGenerator.pawnCheckSearch(position));
    }

    @Test
    @DisplayName("Test knightCheckSearch")
    void testKnightCheckSearch() {
        Position position = FENUtils.positionFromFEN("8/8/3n4/1K6/4k3/8/8/8 w - - 0 1");
        assertEquals(Squares.D6, MoveGenerator.knightCheckSearch(position));
        position = FENUtils.positionFromFEN("8/8/6k1/8/8/2K5/n7/8 w - - 0 1");
        assertEquals(Squares.A2, MoveGenerator.knightCheckSearch(position));
        position = FENUtils.positionFromFEN("8/8/6k1/8/8/2K5/8/8 w - - 0 1");
        assertEquals(Squares.NONE, MoveGenerator.knightCheckSearch(position));
    }

    @Test
    @DisplayName("Test enPassantPinned")
    void testEnPassantPinned() {
        Position position = FENUtils.positionFromFEN("8/8/3p4/KPp4r/7R/6k1/4P1P1/8 w - c6 0 3");
        assertTrue(MoveGenerator.enPassantPinned(position, position.getBoard().findKing(PieceColour.WHITE), Squares.B5));
        position = FENUtils.positionFromFEN("8/8/3p4/KPp5/7R/6k1/4P1P1/8 w - c6 0 3");
        assertFalse(MoveGenerator.enPassantPinned(position, position.getBoard().findKing(PieceColour.WHITE), Squares.B5));
        position = FENUtils.positionFromFEN("8/8/3p4/KPpp2r1/7R/6k1/4P1P1/8 w - c6 0 3");
        assertFalse(MoveGenerator.enPassantPinned(position, position.getBoard().findKing(PieceColour.WHITE), Squares.B5));
        position = FENUtils.positionFromFEN("8/8/3p4/KPpB2r1/7R/6k1/4P1P1/8 w - c6 0 3");
        assertFalse(MoveGenerator.enPassantPinned(position, position.getBoard().findKing(PieceColour.WHITE), Squares.B5));
    }
}
