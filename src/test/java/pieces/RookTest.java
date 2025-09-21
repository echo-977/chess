import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {
    static Rook piece1;
    static Rook piece2;

    @BeforeEach
    public void init() {
        piece1 = new Rook(PieceColour.WHITE, Squares.A1);
        piece2 = new Rook(PieceColour.BLACK, Squares.C5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Squares.A7));
        assertTrue(piece2.isLegalMove(Squares.H5));
        assertFalse(piece1.isLegalMove(Squares.B6));
        assertFalse(piece2.isLegalMove(Squares.F8));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1.move(Squares.C4);
        piece2.move(Squares.E5);
        Position position = FENUtils.positionFromFEN("8/8/8/4r3/2R5/8/8/8 w - - 0 1");
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_ROOK_MOVES);
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
                MoveFlags.QUIET_MOVE | Squares.A4 << MoveFlags.DESTINATION_SHIFT | Squares.C4};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_ROOK_MOVES);
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
                MoveFlags.QUIET_MOVE | Squares.A5 << MoveFlags.DESTINATION_SHIFT | Squares.E5};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("rnbqkbn1/pppppp2/1r6/3R2pp/P3P3/2N5/1PPP1PPP/2BQKBNR w Kq - 0 1");
        Board board = position.getBoard();
        piece1 = (Rook) board.pieceSearch(Squares.D5);
        piece2 = (Rook) board.pieceSearch(Squares.B6);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_ROOK_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.D7 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.D3 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.A5 << MoveFlags.DESTINATION_SHIFT | Squares.D5};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_ROOK_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.C6 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.H6 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.B3 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.B2 << MoveFlags.DESTINATION_SHIFT | Squares.B6,
                MoveFlags.QUIET_MOVE | Squares.A6 << MoveFlags.DESTINATION_SHIFT | Squares.B6};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/3P4/8/1P1r2P1/8/8/3P4/8 w - - 0 1");
        Board board = position.getBoard();
        Rook rook = (Rook) board.pieceSearch(Squares.D5);
        assertFalse(rook.canCaptureSquare(board, Squares.D8));
        assertTrue(rook.canCaptureSquare(board, Squares.D7));
        assertFalse(rook.canCaptureSquare(board, Squares.H5));
        assertTrue(rook.canCaptureSquare(board, Squares.G5));
        assertFalse(rook.canCaptureSquare(board, Squares.D1));
        assertTrue(rook.canCaptureSquare(board, Squares.D2));
        assertFalse(rook.canCaptureSquare(board, Squares.A5));
        assertTrue(rook.canCaptureSquare(board, Squares.B5));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Rook test = (Rook) piece1.copyToSquare(Squares.D8);
        assertEquals(Squares.D8, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
