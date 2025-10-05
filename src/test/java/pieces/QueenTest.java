import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueenTest {
    static Queen piece1;
    static Queen piece2;

    @BeforeEach
    public void init() {
        piece1 = new Queen(PieceColour.WHITE, Squares.A1);
        piece2 = new Queen(PieceColour.BLACK, Squares.D5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Squares.B2));
        assertTrue(piece2.isLegalMove(Squares.E4));
        assertFalse(piece1.isLegalMove(Squares.B6));
        assertFalse(piece2.isLegalMove(Squares.F8));
        assertTrue(piece1.isLegalMove(Squares.A7));
        assertTrue(piece2.isLegalMove(Squares.H5));
        assertFalse(piece1.isLegalMove(Squares.B6));
        assertFalse(piece2.isLegalMove(Squares.F8));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/8/8/4q3/2Q5/8/8/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Queen) board.pieceSearch(Squares.C4);
        piece2 = (Queen) board.pieceSearch(Squares.E5);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_QUEEN_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C6 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C8 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.E4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.F4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.G4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.H4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C2 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C1 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.A4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.G8 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.E2 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.F1 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.A2 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.A6 << MoveFlags.DESTINATION_SHIFT | Squares.C4};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_QUEEN_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E8 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.H5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E4 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E2 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E1 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.A5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.H8 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.F4 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.G3 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.H2 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.B2 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.A1 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.B8 << MoveFlags.DESTINATION_SHIFT | Squares.E5};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("8/2P5/b7/4q3/2Q2P2/8/4p3/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Queen) board.pieceSearch(Squares.C4);
        piece2 = (Queen) board.pieceSearch(Squares.E5);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_QUEEN_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C6 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.E4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C2 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C1 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.A4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.G8 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.E2 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.A2 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.A6 << MoveFlags.DESTINATION_SHIFT | Squares.C4};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_QUEEN_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E8 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.H5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E4 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.A5 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.H8 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.F4 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.B2 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.A1 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.E5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.E5};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName(("Test canCaptureSquare"))
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/1P1P1P2/8/1P1q2P1/8/1P3P2/3P4/8 w - - 0 1");
        Board board = position.getBoard();
        Queen queen =  (Queen) board.pieceSearch(Squares.D5);
        assertFalse(queen.canCaptureSquare(board, Squares.D8));
        assertTrue(queen.canCaptureSquare(board, Squares.D7));
        assertFalse(queen.canCaptureSquare(board, Squares.G8));
        assertTrue(queen.canCaptureSquare(board, Squares.F7));
        assertFalse(queen.canCaptureSquare(board, Squares.H5));
        assertTrue(queen.canCaptureSquare(board, Squares.G5));
        assertFalse(queen.canCaptureSquare(board, Squares.G2));
        assertTrue(queen.canCaptureSquare(board, Squares.F3));
        assertFalse(queen.canCaptureSquare(board, Squares.D1));
        assertTrue(queen.canCaptureSquare(board, Squares.D2));
        assertFalse(queen.canCaptureSquare(board, Squares.A2));
        assertTrue(queen.canCaptureSquare(board, Squares.B3));
        assertFalse(queen.canCaptureSquare(board, Squares.A5));
        assertTrue(queen.canCaptureSquare(board, Squares.B5));
        assertFalse(queen.canCaptureSquare(board, Squares.A7));
        assertTrue(queen.canCaptureSquare(board, Squares.B7));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Queen test = (Queen) piece1.copyToSquare(Squares.D8);
        assertEquals(Squares.D8, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }

    @Test
    @DisplayName("Test getAttackMask")
    void testGetAttackMask() {
        Board board = FENUtils.positionFromFEN("8/5B2/8/3q4/8/3R4/8/8 w - - 0 1").getBoard();
        long expectedAttackMask = 0b10000000_01000001_00101010_00011100_11110111_00011100_00101010_00001001L;
        assertEquals(expectedAttackMask, piece2.getAttackMask(board));
    }
}
