import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {
    static Rook piece1;
    static Rook piece2;

    @BeforeEach
    public void init() {
        piece1 = new Rook(PieceColour.WHITE, Files.A + Ranks.ONE);
        piece2 = new Rook(PieceColour.BLACK, Files.C + Ranks.FIVE);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Files.A + Ranks.SEVEN));
        assertTrue(piece2.isLegalMove(Files.H + Ranks.FIVE));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.SIX));
        assertFalse(piece2.isLegalMove(Files.F + Ranks.EIGHT));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1.move(Files.C + Ranks.FOUR);
        piece2.move(Files.E + Ranks.FIVE);
        Position position = FENUtils.positionFromFEN("8/8/8/4r3/2R5/8/8/8 w - - 0 1");
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.B + Ranks.FOUR),
                new Move(position, piece1, Files.A + Ranks.FOUR),
                new Move(position, piece1, Files.D + Ranks.FOUR),
                new Move(position, piece1, Files.E + Ranks.FOUR),
                new Move(position, piece1, Files.F + Ranks.FOUR),
                new Move(position, piece1, Files.G + Ranks.FOUR),
                new Move(position, piece1, Files.H + Ranks.FOUR),
                new Move(position, piece1, Files.C + Ranks.FIVE),
                new Move(position, piece1, Files.C + Ranks.SIX),
                new Move(position, piece1, Files.C + Ranks.SEVEN),
                new Move(position, piece1, Files.C + Ranks.EIGHT),
                new Move(position, piece1, Files.C + Ranks.THREE),
                new Move(position, piece1, Files.C + Ranks.TWO),
                new Move(position, piece1, Files.C + Ranks.ONE)};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.D + Ranks.FIVE),
                new Move(position, piece2, Files.C + Ranks.FIVE),
                new Move(position, piece2, Files.B + Ranks.FIVE),
                new Move(position, piece2, Files.A + Ranks.FIVE),
                new Move(position, piece2, Files.F + Ranks.FIVE),
                new Move(position, piece2, Files.G + Ranks.FIVE),
                new Move(position, piece2, Files.H + Ranks.FIVE),
                new Move(position, piece2, Files.E + Ranks.SIX),
                new Move(position, piece2, Files.E + Ranks.SEVEN),
                new Move(position, piece2, Files.E + Ranks.EIGHT),
                new Move(position, piece2, Files.E + Ranks.FOUR),
                new Move(position, piece2, Files.E + Ranks.THREE),
                new Move(position, piece2, Files.E + Ranks.TWO),
                new Move(position, piece2, Files.E + Ranks.ONE)};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("rnbqkbn1/pppppp2/1r6/3R2pp/P3P3/2N5/1PPP1PPP/2BQKBNR w Kq - 0 1");
        Board board = position.getBoard();
        piece1 = (Rook) board.pieceSearch(Files.D + Ranks.FIVE);
        piece2 = (Rook) board.pieceSearch(Files.B + Ranks.SIX);
        Move move1 = new Move(position, piece1, Files.G + Ranks.FIVE);
        move1.setCapture(true);
        Move move2 = new Move(position, piece1, Files.D + Ranks.SEVEN);
        move2.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.C + Ranks.FIVE),
                new Move(position, piece1, Files.B + Ranks.FIVE),
                new Move(position, piece1, Files.A + Ranks.FIVE),
                new Move(position, piece1, Files.E + Ranks.FIVE),
                new Move(position, piece1, Files.F + Ranks.FIVE), move1,
                new Move(position, piece1, Files.D + Ranks.SIX), move2,
                new Move(position, piece1, Files.D + Ranks.FOUR),
                new Move(position, piece1, Files.D + Ranks.THREE),
                null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.B + Ranks.TWO);
        move1.setCapture(true);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.A + Ranks.SIX),
                new Move(position, piece2, Files.C + Ranks.SIX),
                new Move(position, piece2, Files.D + Ranks.SIX),
                new Move(position, piece2, Files.E + Ranks.SIX),
                new Move(position, piece2, Files.F + Ranks.SIX),
                new Move(position, piece2, Files.G + Ranks.SIX),
                new Move(position, piece2, Files.H + Ranks.SIX),
                new Move(position, piece2, Files.B + Ranks.FIVE),
                new Move(position, piece2, Files.B + Ranks.FOUR),
                new Move(position, piece2, Files.B + Ranks.THREE), move1, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/3P4/8/1P1r2P1/8/8/3P4/8 w - - 0 1");
        Board board = position.getBoard();
        Rook rook = (Rook) board.pieceSearch(Files.D + Ranks.FIVE);
        assertFalse(rook.canCaptureSquare(board, Files.D + Ranks.EIGHT));
        assertTrue(rook.canCaptureSquare(board, Files.D + Ranks.SEVEN));
        assertFalse(rook.canCaptureSquare(board, Files.H + Ranks.FIVE));
        assertTrue(rook.canCaptureSquare(board, Files.G + Ranks.FIVE));
        assertFalse(rook.canCaptureSquare(board, Files.D + Ranks.ONE));
        assertTrue(rook.canCaptureSquare(board, Files.D + Ranks.TWO));
        assertFalse(rook.canCaptureSquare(board, Files.A + Ranks.FIVE));
        assertTrue(rook.canCaptureSquare(board, Files.B + Ranks.FIVE));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Rook test = (Rook) piece1.copyToSquare(Files.D + Ranks.EIGHT);
        assertEquals(Files.D + Ranks.EIGHT, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
