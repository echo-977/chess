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
        piece1 = new Knight(PieceColour.WHITE, Files.A + Ranks.ONE);
        piece2 = new Knight(PieceColour.BLACK, Files.D + Ranks.FIVE);
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
        piece1 = (Knight) board.pieceSearch(Files.A + Ranks.ONE);
        piece2 = (Knight) board.pieceSearch(Files.D + Ranks.FIVE);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.B + Ranks.THREE),
                new Move(position, piece1, Files.C + Ranks.TWO), null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.E + Ranks.SEVEN),
                new Move(position, piece2, Files.F + Ranks.SIX),
                new Move(position, piece2, Files.F + Ranks.FOUR),
                new Move(position, piece2, Files.E + Ranks.THREE),
                new Move(position, piece2, Files.C + Ranks.THREE),
                new Move(position, piece2, Files.B + Ranks.FOUR),
                new Move(position, piece2, Files.B  + Ranks.SIX),
                new Move(position, piece2, Files.C + Ranks.SEVEN)};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("8/8/8/3n1P2/1p4Q1/4N3/8/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Knight) board.pieceSearch(Files.E + Ranks.THREE);
        piece2 = (Knight) board.pieceSearch(Files.D + Ranks.FIVE);
        Move move1 = new Move(position, piece1, Files.D + Ranks.FIVE);
        move1.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.G + Ranks.TWO),
                new Move(position, piece1, Files.F + Ranks.ONE),
                new Move(position, piece1, Files.D + Ranks.ONE),
                new Move(position, piece1, Files.C + Ranks.TWO),
                new Move(position, piece1, Files.C + Ranks.FOUR), move1, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.E + Ranks.THREE);
        move1.setCapture(true);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.E + Ranks.SEVEN),
                new Move(position, piece2, Files.F + Ranks.SIX),
                new Move(position, piece2, Files.F + Ranks.FOUR), move1,
                new Move(position, piece2, Files.C + Ranks.THREE),
                new Move(position, piece2, Files.B + Ranks.SIX),
                new Move(position, piece2, Files.C + Ranks.SEVEN), null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
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
