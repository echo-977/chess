import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KingTest {
    static King piece1;
    static King piece2;

    @BeforeEach
    public void init() {
        piece1 = new King(PieceColour.WHITE, Squares.A1, false);
        piece2 = new King(PieceColour.WHITE, Squares.D5, true);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Squares.A2));
        assertTrue(piece2.isLegalMove(Squares.C6));
        assertFalse(piece1.isLegalMove(Squares.B6));
        assertFalse(piece2.isLegalMove(Squares.A3));
    }

    @Test
    @DisplayName("Test generateMoves without other pieces")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/8/5k2/8/2K5/8/8/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (King) board.pieceSearch(Squares.C4);
        piece2 = (King) board.pieceSearch(Squares.F6);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_KING_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.D3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.C4};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_KING_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.F7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E7    << MoveFlags.DESTINATION_SHIFT | Squares. F6};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves with other pieces")
    void testGenerateMovesWithOtherPieces() {
        Position position = FENUtils.positionFromFEN("8/6p1/4PkR1/3r4/2K2p2/2P5/8/8 b - - 0 1");
        Board board = position.getBoard();
        piece1 = (King) board.pieceSearch(Squares.C4);
        piece2 = (King) board.pieceSearch(Squares.F6);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_KING_MOVES);
        int[] piece1MovesExpected = {
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B3 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.C4};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_KING_MOVES);
        int[] piece2MovesExpected = {
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F6,
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.F6};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test isCheck")
    void testIsCheck() {
        assertFalse(piece1.isCheck());
        assertTrue(piece2.isCheck());
    }

    @Test
    @DisplayName("Test setCheck")
    void testSetCheck() {
        piece1.setCheck(true);
        assertTrue(piece1.isCheck());
        piece2.setCheck(false);
        assertFalse(piece2.isCheck());
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        King test = (King) piece1.copyToSquare(Squares.D8);
        assertEquals(Squares.D8, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
        assertEquals(piece1.isCheck(), test.isCheck());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1").getBoard();
        assertTrue(piece2.canCaptureSquare(board, Squares.E6));
        assertTrue(piece2.canCaptureSquare(board, Squares.E5));
        assertTrue(piece2.canCaptureSquare(board, Squares.E4));
        assertTrue(piece2.canCaptureSquare(board, Squares.D4));
        assertTrue(piece2.canCaptureSquare(board, Squares.C4));
        assertTrue(piece2.canCaptureSquare(board, Squares.C5));
        assertTrue(piece2.canCaptureSquare(board, Squares.C6));
    }

    @Test
    @DisplayName("Test generateMoves with castling")
    void testGenerateMovesWithCastling() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/5R2/8/8/8/2b5/R3K2R w KQkq - 0 1");
        Board board = position.getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_KING_MOVES);
        int[] expectedMovesWhite = {MoveFlags.QUIET_MOVE | Squares.E2 << MoveFlags.DESTINATION_SHIFT | Squares.E1,
                MoveFlags.QUIET_MOVE | Squares.F2 << MoveFlags.DESTINATION_SHIFT | Squares.E1,
                MoveFlags.QUIET_MOVE | Squares.F1 << MoveFlags.DESTINATION_SHIFT | Squares.E1,
                MoveFlags.QUIET_MOVE | Squares.D2 << MoveFlags.DESTINATION_SHIFT | Squares.E1,
                MoveFlags.KINGSIDE_CASTLE << MoveFlags.FLAG_SHIFT | Squares.G1 << MoveFlags.DESTINATION_SHIFT | Squares.E1};
        whiteKing.generateMoves(position, piece1MovesActual);
        assertArrayEquals(expectedMovesWhite, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_KING_MOVES);
        King blackKing = board.findKing(PieceColour.BLACK);
        int[] expectedMovesBlack = {MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.E8,
                MoveFlags.QUIET_MOVE | Squares.D7 << MoveFlags.DESTINATION_SHIFT | Squares.E8,
                MoveFlags.QUIET_MOVE | Squares.D8 << MoveFlags.DESTINATION_SHIFT | Squares.E8,
                MoveFlags.QUEENSIDE_CASTLE << MoveFlags.FLAG_SHIFT | Squares.C8 << MoveFlags.DESTINATION_SHIFT | Squares.E8};
        blackKing.generateMoves(position, piece2MovesActual);
        assertArrayEquals(expectedMovesBlack, piece2MovesActual.toIntArray());
    }

}
