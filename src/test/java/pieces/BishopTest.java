import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    static Bishop piece1;
    static Bishop piece2;

    @BeforeEach
    public void init() {
        piece1 = new Bishop(PieceColour.WHITE, Squares.A1);
        piece2 = new Bishop(PieceColour.BLACK, Squares.D5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Squares.B2));
        assertTrue(piece2.isLegalMove(Squares.E4));
        assertFalse(piece1.isLegalMove(Squares.B6));
        assertFalse(piece2.isLegalMove(Squares.F8));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1 = new Bishop(PieceColour.WHITE, Squares.D4);
        piece2 = new Bishop(PieceColour.BLACK, Squares.E4);
        Position position = FENUtils.positionFromFEN("8/8/8/8/3Bb3/8/8/8 w - - 0 1");
        IntArrayList moves = new IntArrayList(ChessConstants.MAX_BISHOP_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.B6 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.A7 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.G7 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.H8 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.F2 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.G1 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.B2 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.A1 << MoveFlags.DESTINATION_SHIFT | Squares.D4};
        piece1.generateMoves(position, moves);
        int[] piece1MovesActual = moves.toIntArray();
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        moves = new IntArrayList(ChessConstants.MAX_BISHOP_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.C6 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.B7 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.A8 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.F5 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.H7 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.F3 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.G2 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.H1 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.D3 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.C2 << MoveFlags.DESTINATION_SHIFT | Squares.E4,
                MoveFlags.QUIET_MOVE | Squares.B1 << MoveFlags.DESTINATION_SHIFT | Squares.E4};
        piece2.generateMoves(position, moves);
        int[] piece2MovesActual = moves.toIntArray();
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("r2qkbnr/pp2pp2/n1p3pp/3p1b2/3P1B2/6PP/PPP1PP2/RN1QKBNR w KQkq - 0 1");
        Board board = position.getBoard();
        piece1 = (Bishop) board.pieceSearch(Squares.F4);
        piece2 = (Bishop) board.pieceSearch(Squares.F5);
        IntArrayList moves = new IntArrayList(ChessConstants.MAX_BISHOP_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.B8 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.H6 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.D2 << MoveFlags.DESTINATION_SHIFT | Squares.F4,
                MoveFlags.QUIET_MOVE | Squares.C1 << MoveFlags.DESTINATION_SHIFT | Squares.F4};
        piece1.generateMoves(position, moves);
        int[] piece1MovesActual = moves.toIntArray();
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        moves = new IntArrayList(ChessConstants.MAX_BISHOP_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.E6 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.QUIET_MOVE | Squares.D7 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.QUIET_MOVE | Squares.C8 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.QUIET_MOVE | Squares.G4 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.H3 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.QUIET_MOVE | Squares.E4 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.QUIET_MOVE | Squares.D3 << MoveFlags.DESTINATION_SHIFT | Squares.F5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.C2 << MoveFlags.DESTINATION_SHIFT | Squares.F5};
        piece2.generateMoves(position, moves);
        int[] piece2MovesActual = moves.toIntArray();
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/1P3P2/8/3b4/8/1P6/6P1/8 w - - 0 1");
        Board board = position.getBoard();
        Bishop bishop = (Bishop) board.pieceSearch(Squares.D5);
        assertFalse(bishop.canCaptureSquare(board, Squares.G8));
        assertTrue(bishop.canCaptureSquare(board, Squares.F7));
        assertFalse(bishop.canCaptureSquare(board, Squares.H1));
        assertTrue(bishop.canCaptureSquare(board, Squares.G2));
        assertFalse(bishop.canCaptureSquare(board, Squares.A2));
        assertTrue(bishop.canCaptureSquare(board, Squares.B3));
        assertFalse(bishop.canCaptureSquare(board, Squares.A8));
        assertTrue(bishop.canCaptureSquare(board, Squares.B7));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Bishop test = (Bishop) piece1.copyToSquare(Squares.D8);
        assertEquals(Squares.D8, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
