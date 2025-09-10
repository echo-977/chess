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
        piece1 = new Queen(PieceColour.WHITE, Files.A + Ranks.ONE);
        piece2 = new Queen(PieceColour.BLACK, Files.D + Ranks.FIVE);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Files.B + Ranks.TWO));
        assertTrue(piece2.isLegalMove(Files.E + Ranks.FOUR));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.SIX));
        assertFalse(piece2.isLegalMove(Files.F + Ranks.EIGHT));
        assertTrue(piece1.isLegalMove(Files.A + Ranks.SEVEN));
        assertTrue(piece2.isLegalMove(Files.H + Ranks.FIVE));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.SIX));
        assertFalse(piece2.isLegalMove(Files.F + Ranks.EIGHT));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/8/8/4q3/2Q5/8/8/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Queen) board.pieceSearch(Files.C + Ranks.FOUR);
        piece2 = (Queen) board.pieceSearch(Files.E + Ranks.FIVE);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.C + Ranks.FIVE),
                new Move(position, piece1, Files.C + Ranks.SIX),
                new Move(position, piece1, Files.C + Ranks.SEVEN),
                new Move(position, piece1, Files.C + Ranks.EIGHT),
                new Move(position, piece1, Files.D + Ranks.FIVE),
                new Move(position, piece1, Files.E + Ranks.SIX),
                new Move(position, piece1, Files.F + Ranks.SEVEN),
                new Move(position, piece1, Files.G + Ranks.EIGHT),
                new Move(position, piece1, Files.D + Ranks.FOUR),
                new Move(position, piece1, Files.E + Ranks.FOUR),
                new Move(position, piece1, Files.F + Ranks.FOUR),
                new Move(position, piece1, Files.G + Ranks.FOUR),
                new Move(position, piece1, Files.H + Ranks.FOUR),
                new Move(position, piece1, Files.D + Ranks.THREE),
                new Move(position, piece1, Files.E + Ranks.TWO),
                new Move(position, piece1, Files.F + Ranks.ONE),
                new Move(position, piece1, Files.C + Ranks.THREE),
                new Move(position, piece1, Files.C + Ranks.TWO),
                new Move(position, piece1, Files.C + Ranks.ONE),
                new Move(position, piece1, Files.B + Ranks.THREE),
                new Move(position, piece1, Files.A + Ranks.TWO),
                new Move(position, piece1, Files.B + Ranks.FOUR),
                new Move(position, piece1, Files.A + Ranks.FOUR),
                new Move(position, piece1, Files.B + Ranks.FIVE),
                new Move(position, piece1, Files.A + Ranks.SIX), null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.E + Ranks.SIX),
                new Move(position, piece2, Files.E + Ranks.SEVEN),
                new Move(position, piece2, Files.E + Ranks.EIGHT),
                new Move(position, piece2, Files.F + Ranks.SIX),
                new Move(position, piece2, Files.G + Ranks.SEVEN),
                new Move(position, piece2, Files.H + Ranks.EIGHT),
                new Move(position, piece2, Files.F + Ranks.FIVE),
                new Move(position, piece2, Files.G + Ranks.FIVE),
                new Move(position, piece2, Files.H + Ranks.FIVE),
                new Move(position, piece2, Files.F + Ranks.FOUR),
                new Move(position, piece2, Files.G + Ranks.THREE),
                new Move(position, piece2, Files.H + Ranks.TWO),
                new Move(position, piece2, Files.E + Ranks.FOUR),
                new Move(position, piece2, Files.E + Ranks.THREE),
                new Move(position, piece2, Files.E + Ranks.TWO),
                new Move(position, piece2, Files.E + Ranks.ONE),
                new Move(position, piece2, Files.D + Ranks.FOUR),
                new Move(position, piece2, Files.C + Ranks.THREE),
                new Move(position, piece2, Files.B + Ranks.TWO),
                new Move(position, piece2, Files.A + Ranks.ONE),
                new Move(position, piece2, Files.D + Ranks.FIVE),
                new Move(position, piece2, Files.C + Ranks.FIVE),
                new Move(position, piece2, Files.B + Ranks.FIVE),
                new Move(position, piece2, Files.A + Ranks.FIVE),
                new Move(position, piece2, Files.D + Ranks.SIX),
                new Move(position, piece2, Files.C + Ranks.SEVEN),
                new Move(position, piece2, Files.B + Ranks.EIGHT)};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("8/2P5/b7/4q3/2Q2P2/8/4p3/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Queen) board.pieceSearch(Files.C + Ranks.FOUR);
        piece2 = (Queen) board.pieceSearch(Files.E + Ranks.FIVE);
        Move move1 = new Move(position, piece1, Files.E + Ranks.TWO);
        move1.setCapture(true);
        Move move2 = new Move(position, piece1, Files.A + Ranks.SIX);
        move2.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.C + Ranks.FIVE),
                new Move(position, piece1, Files.C + Ranks.SIX),
                new Move(position, piece1, Files.D + Ranks.FIVE),
                new Move(position, piece1, Files.E + Ranks.SIX),
                new Move(position, piece1, Files.F + Ranks.SEVEN),
                new Move(position, piece1, Files.G + Ranks.EIGHT),
                new Move(position, piece1, Files.D + Ranks.FOUR),
                new Move(position, piece1, Files.E + Ranks.FOUR),
                new Move(position, piece1, Files.D + Ranks.THREE), move1,
                new Move(position, piece1, Files.C + Ranks.THREE),
                new Move(position, piece1, Files.C + Ranks.TWO),
                new Move(position, piece1, Files.C + Ranks.ONE),
                new Move(position, piece1, Files.B + Ranks.THREE),
                new Move(position, piece1, Files.A + Ranks.TWO),
                new Move(position, piece1, Files.B + Ranks.FOUR),
                new Move(position, piece1, Files.A + Ranks.FOUR),
                new Move(position, piece1, Files.B + Ranks.FIVE),
                move2, null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.F + Ranks.FOUR);
        move1.setCapture(true);
        move2 = new Move(position, piece2, Files.C + Ranks.SEVEN);
        move2.setCapture(true);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.E + Ranks.SIX),
                new Move(position, piece2, Files.E + Ranks.SEVEN),
                new Move(position, piece2, Files.E + Ranks.EIGHT),
                new Move(position, piece2, Files.F + Ranks.SIX),
                new Move(position, piece2, Files.G + Ranks.SEVEN),
                new Move(position, piece2, Files.H + Ranks.EIGHT),
                new Move(position, piece2, Files.F + Ranks.FIVE),
                new Move(position, piece2, Files.G + Ranks.FIVE),
                new Move(position, piece2, Files.H + Ranks.FIVE), move1,
                new Move(position, piece2, Files.E + Ranks.FOUR),
                new Move(position, piece2, Files.E + Ranks.THREE),
                new Move(position, piece2, Files.D + Ranks.FOUR),
                new Move(position, piece2, Files.C + Ranks.THREE),
                new Move(position, piece2, Files.B + Ranks.TWO),
                new Move(position, piece2, Files.A + Ranks.ONE),
                new Move(position, piece2, Files.D + Ranks.FIVE),
                new Move(position, piece2, Files.C + Ranks.FIVE),
                new Move(position, piece2, Files.B + Ranks.FIVE),
                new Move(position, piece2, Files.A + Ranks.FIVE),
                new Move(position, piece2, Files.D + Ranks.SIX), move2, null, null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName(("Test canCaptureSquare"))
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/1P1P1P2/8/1P1q2P1/8/1P3P2/3P4/8 w - - 0 1");
        Board board = position.getBoard();
        Queen queen =  (Queen) board.pieceSearch(Files.D + Ranks.FIVE);
        assertFalse(queen.canCaptureSquare(board, Files.D + Ranks.EIGHT));
        assertTrue(queen.canCaptureSquare(board, Files.D + Ranks.SEVEN));
        assertFalse(queen.canCaptureSquare(board, Files.G + Ranks.EIGHT));
        assertTrue(queen.canCaptureSquare(board, Files.F + Ranks.SEVEN));
        assertFalse(queen.canCaptureSquare(board, Files.H + Ranks.FIVE));
        assertTrue(queen.canCaptureSquare(board, Files.G + Ranks.FIVE));
        assertFalse(queen.canCaptureSquare(board, Files.G + Ranks.TWO));
        assertTrue(queen.canCaptureSquare(board, Files.F + Ranks.THREE));
        assertFalse(queen.canCaptureSquare(board, Files.D + Ranks.ONE));
        assertTrue(queen.canCaptureSquare(board, Files.D + Ranks.TWO));
        assertFalse(queen.canCaptureSquare(board, Files.A + Ranks.TWO));
        assertTrue(queen.canCaptureSquare(board, Files.B + Ranks.THREE));
        assertFalse(queen.canCaptureSquare(board, Files.A + Ranks.FIVE));
        assertTrue(queen.canCaptureSquare(board, Files.B + Ranks.FIVE));
        assertFalse(queen.canCaptureSquare(board, Files.A + Ranks.SEVEN));
        assertTrue(queen.canCaptureSquare(board, Files.B + Ranks.SEVEN));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Queen test = (Queen) piece1.copyToSquare(Files.D + Ranks.EIGHT);
        assertEquals(Files.D + Ranks.EIGHT, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
