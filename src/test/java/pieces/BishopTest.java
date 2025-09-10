import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    static Bishop piece1;
    static Bishop piece2;

    @BeforeEach
    public void init() {
        piece1 = new Bishop(PieceColour.WHITE, Files.A + Ranks.ONE);
        piece2 = new Bishop(PieceColour.BLACK, Files.D + Ranks.FIVE);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Files.B + Ranks.TWO));
        assertTrue(piece2.isLegalMove(Files.E + Ranks.FOUR));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.SIX));
        assertFalse(piece2.isLegalMove(Files.F + Ranks.EIGHT));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1 = new Bishop(PieceColour.WHITE, Files.D + Ranks.FOUR);
        piece2 = new Bishop(PieceColour.BLACK, Files.E + Ranks.FOUR);
        Position position = FENUtils.positionFromFEN("8/8/8/8/3Bb3/8/8/8 w - - 0 1");
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.C + Ranks.FIVE),
                new Move(position, piece1, Files.B + Ranks.SIX),
                new Move(position, piece1, Files.A + Ranks.SEVEN),
                new Move(position, piece1, Files.E + Ranks.FIVE),
                new Move(position, piece1, Files.F + Ranks.SIX),
                new Move(position, piece1, Files.G + Ranks.SEVEN),
                new Move(position, piece1, Files.H + Ranks.EIGHT),
                new Move(position, piece1, Files.E + Ranks.THREE),
                new Move(position, piece1, Files.F + Ranks.TWO),
                new Move(position, piece1, Files.G + Ranks.ONE),
                new Move(position, piece1, Files.C + Ranks.THREE),
                new Move(position, piece1, Files.B + Ranks.TWO),
                new Move(position, piece1, Files.A + Ranks.ONE)};
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.D + Ranks.FIVE),
                new Move(position, piece2, Files.C + Ranks.SIX),
                new Move(position, piece2, Files.B + Ranks.SEVEN),
                new Move(position, piece2, Files.A + Ranks.EIGHT),
                new Move(position, piece2, Files.F + Ranks.FIVE),
                new Move(position, piece2, Files.G + Ranks.SIX),
                new Move(position, piece2, Files.H + Ranks.SEVEN),
                new Move(position, piece2, Files.F + Ranks.THREE),
                new Move(position, piece2, Files.G + Ranks.TWO),
                new Move(position, piece2, Files.H + Ranks.ONE),
                new Move(position, piece2, Files.D + Ranks.THREE),
                new Move(position, piece2, Files.C + Ranks.TWO),
                new Move(position, piece2, Files.B + Ranks.ONE)};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("r2qkbnr/pp2pp2/n1p3pp/3p1b2/3P1B2/6PP/PPP1PP2/RN1QKBNR w KQkq - 0 1");
        Board board = position.getBoard();
        piece1 = (Bishop) board.pieceSearch(Files.F + Ranks.FOUR);
        piece2 = (Bishop) board.pieceSearch(Files.F + Ranks.FIVE);
        Move move1 = new Move(position, piece1, Files.H + Ranks.SIX);
        move1.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.E + Ranks.FIVE),
                new Move(position, piece1, Files.D + Ranks.SIX),
                new Move(position, piece1, Files.C + Ranks.SEVEN),
                new Move(position, piece1, Files.B + Ranks.EIGHT),
                new Move(position, piece1, Files.G + Ranks.FIVE), move1,
                new Move(position, piece1, Files.E + Ranks.THREE),
                new Move(position, piece1, Files.D + Ranks.TWO),
                new Move(position, piece1, Files.C + Ranks.ONE), null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.H + Ranks.THREE);
        move1.setCapture(true);
        Move move2 = new Move(position, piece2, Files.C + Ranks.TWO);
        move2.setCapture(true);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.E + Ranks.SIX),
                new Move(position, piece2, Files.D + Ranks.SEVEN),
                new Move(position, piece2, Files.C + Ranks.EIGHT),
                new Move(position, piece2, Files.G + Ranks.FOUR), move1,
                new Move(position, piece2, Files.E + Ranks.FOUR),
                new Move(position, piece2, Files.D + Ranks.THREE), move2, null, null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/1P3P2/8/3b4/8/1P6/6P1/8 w - - 0 1");
        Board board = position.getBoard();
        Bishop bishop = (Bishop) board.pieceSearch(Files.D + Ranks.FIVE);
        assertFalse(bishop.canCaptureSquare(board, Files.G + Ranks.EIGHT));
        assertTrue(bishop.canCaptureSquare(board, Files.F + Ranks.SEVEN));
        assertFalse(bishop.canCaptureSquare(board, Files.H + Ranks.ONE));
        assertTrue(bishop.canCaptureSquare(board, Files.G + Ranks.TWO));
        assertFalse(bishop.canCaptureSquare(board, Files.A + Ranks.TWO));
        assertTrue(bishop.canCaptureSquare(board, Files.B + Ranks.THREE));
        assertFalse(bishop.canCaptureSquare(board, Files.A + Ranks.EIGHT));
        assertTrue(bishop.canCaptureSquare(board, Files.B + Ranks.SEVEN));
    }


    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Bishop test = (Bishop) piece1.copyToSquare(Files.D + Ranks.EIGHT);
        assertEquals(Files.D + Ranks.EIGHT, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
