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
        piece1 = new King(PieceColour.WHITE, Files.A + Ranks.ONE, false);
        piece2 = new King(PieceColour.WHITE, Files.D + Ranks.FIVE, true);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Files.A + Ranks.TWO));
        assertTrue(piece2.isLegalMove(Files.C + Ranks.SIX));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.SIX));
        assertFalse(piece2.isLegalMove(Files.A + Ranks.THREE));
    }

    @Test
    @DisplayName("Test generateMoves without other pieces")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/8/5k2/8/2K5/8/8/8 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (King) board.pieceSearch(Files.C + Ranks.FOUR);
        piece2 = (King) board.pieceSearch(Files.F + Ranks.SIX);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.C + Ranks.FIVE),
                new Move(position, piece1, Files.D + Ranks.FIVE),
                new Move(position, piece1, Files.D + Ranks.FOUR),
                new Move(position, piece1, Files.D + Ranks.THREE),
                new Move(position, piece1, Files.C + Ranks.THREE),
                new Move(position, piece1, Files.B + Ranks.THREE),
                new Move(position, piece1, Files.B + Ranks.FOUR),
                new Move(position, piece1, Files.B + Ranks.FIVE)};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.F + Ranks.SEVEN),
                new Move(position, piece2, Files.G + Ranks.SEVEN),
                new Move(position, piece2, Files.G + Ranks.SIX),
                new Move(position, piece2, Files.G + Ranks.FIVE),
                new Move(position, piece2, Files.F + Ranks.FIVE),
                new Move(position, piece2, Files.E + Ranks.FIVE),
                new Move(position, piece2, Files.E + Ranks.SIX),
                new Move(position, piece2, Files.E + Ranks.SEVEN)};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with other pieces")
    void testGenerateMovesWithOtherPieces() {
        Position position = FENUtils.positionFromFEN("8/6p1/4PkR1/3r4/2K2p2/2P5/8/8 b - - 0 1");
        Board board = position.getBoard();
        piece1 = (King) board.pieceSearch(Files.C + Ranks.FOUR);
        piece2 = (King) board.pieceSearch(Files.F + Ranks.SIX);
        Move move1 = new Move(position, piece1, Files.D + Ranks.FIVE);
        move1.setCapture(true);
        Move[] piece1MovesExpected = {move1, new Move(position, piece1, Files.B + Ranks.THREE),
                new Move(position, piece1, Files.B + Ranks.FOUR), null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.G + Ranks.SIX);
        move1.setCapture(true);
        Move[] piece2MovesExpected = {move1, new Move(position, piece2, Files.F + Ranks.FIVE),
                new Move(position, piece2, Files.E + Ranks.FIVE),
                new Move(position, piece2, Files.E + Ranks.SEVEN), null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
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
        King test = (King) piece1.copyToSquare(Files.D + Ranks.EIGHT);
        assertEquals(Files.D + Ranks.EIGHT, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
        assertEquals(piece1.isCheck(), test.isCheck());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Board board = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1").getBoard();
        assertTrue(piece2.canCaptureSquare(board, Files.D + Ranks.SIX));
        assertTrue(piece2.canCaptureSquare(board, Files.E + Ranks.SIX));
        assertTrue(piece2.canCaptureSquare(board, Files.E + Ranks.FIVE));
        assertTrue(piece2.canCaptureSquare(board, Files.E + Ranks.FOUR));
        assertTrue(piece2.canCaptureSquare(board, Files.D + Ranks.FOUR));
        assertTrue(piece2.canCaptureSquare(board, Files.C + Ranks.FOUR));
        assertTrue(piece2.canCaptureSquare(board, Files.C + Ranks.FIVE));
        assertTrue(piece2.canCaptureSquare(board, Files.C + Ranks.SIX));
    }

    @Test
    @DisplayName("Test findNextIndex")
    void testFindNextIndex() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = position.getBoard();
        Move[] moves = new Move[8];
        moves[0] = new Move(position, board.pieceSearch(Files.A + Ranks.TWO), Files.A + Ranks.THREE);
        King king = board.findKing(PieceColour.WHITE);
        assertEquals(1, king.findNextIndex(moves));
    }

    @Test
    @DisplayName("Test generateMoves with castling")
    void testGenerateMovesWithCastling() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/5R2/8/8/8/2b5/R3K2R w KQkq - 0 1");
        Board board = position.getBoard();
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move[] expectedMovesWhite = {new Move(position, whiteKing, Files.E + Ranks.TWO),
                new Move(position, whiteKing, Files.F + Ranks.TWO),
                new Move(position, whiteKing, Files.F + Ranks.ONE),
                new Move(position, whiteKing, Files.D + Ranks.TWO),
                new Move(position, whiteKing, Files.G + Ranks.ONE), null, null, null};
        expectedMovesWhite[4].setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
        assertArrayEquals(expectedMovesWhite, whiteKing.generateMoves(position));
        King blackKing = board.findKing(PieceColour.BLACK);
        Move[] expectedMovesBlack = {new Move(position, blackKing, Files.E + Ranks.SEVEN),
                new Move(position, blackKing, Files.D + Ranks.SEVEN),
                new Move(position, blackKing, Files.D + Ranks.EIGHT),
                new Move(position, blackKing, Files.C + Ranks.EIGHT), null, null,null, null};
        expectedMovesBlack[3].setCastleMask(FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        assertArrayEquals(expectedMovesBlack, blackKing.generateMoves(position));
    }

}
