import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KnightTest {
    static Knight piece1;
    static Knight piece2;

    @BeforeEach
    public void init() {
        piece1 = new Knight(PieceColour.WHITE, Squares.A1);
        piece2 = new Knight(PieceColour.BLACK, Squares.D5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Files.C + Ranks.TWO));
        assertTrue(piece2.isLegalMove(Files.E + Ranks.THREE));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.SIX));
        assertFalse(piece2.isLegalMove(Files.F + Ranks.EIGHT));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/8/8/3n4/8/8/8/N7 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Knight) board.pieceSearch(Squares.A1);
        piece2 = (Knight) board.pieceSearch(Squares.D5);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_KNIGHT_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.B3 << MoveFlags.DESTINATION_SHIFT | Squares.A1,
                MoveFlags.QUIET_MOVE | Squares.C2 << MoveFlags.DESTINATION_SHIFT | Squares.A1};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_KNIGHT_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.F4 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.B4 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.B6 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.D5};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("8/8/8/3n1P2/1p4Q1/4N3/8/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Knight) board.pieceSearch(Squares.E3);
        piece2 = (Knight) board.pieceSearch(Squares.D5);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_KNIGHT_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.G2 << MoveFlags.DESTINATION_SHIFT | Squares.E3,
                MoveFlags.QUIET_MOVE | Squares.F1 << MoveFlags.DESTINATION_SHIFT | Squares.E3,
                MoveFlags.QUIET_MOVE | Squares.D1 << MoveFlags.DESTINATION_SHIFT | Squares.E3,
                MoveFlags.QUIET_MOVE | Squares.C2 << MoveFlags.DESTINATION_SHIFT | Squares.E3,
                MoveFlags.QUIET_MOVE | Squares.C4 << MoveFlags.DESTINATION_SHIFT | Squares.E3,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.E3};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_KNIGHT_MOVES);
        int[] piece2MovesExpected = {
                MoveFlags.QUIET_MOVE | Squares.E7 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.F4 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.E3 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.B6 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                MoveFlags.QUIET_MOVE | Squares.C7 << MoveFlags.DESTINATION_SHIFT | Squares.D5};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() { //given the canCaptureKing method just calls the isLegalMove method there is no test required
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Knight test = (Knight) piece1.copyToSquare(Files.D + Ranks.EIGHT);
        assertEquals(Files.D + Ranks.EIGHT, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
