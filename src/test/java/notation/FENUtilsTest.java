import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FENUtilsTest {
    @Test
    @DisplayName("Test making board with initial position")
    void testBoardFromFENDefaultFEN() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        Rook aRook = (Rook) board.pieceSearch(Files.A + Ranks.EIGHT);
        assertEquals(Files.A + Ranks.EIGHT, aRook.getSquare());
        assertEquals(Files.B + Ranks.EIGHT, board.pieceSearch(Files.B + Ranks.EIGHT).getSquare());
        assertEquals(Files.C + Ranks.EIGHT, board.pieceSearch(Files.C + Ranks.EIGHT).getSquare());
        assertEquals(Files.D + Ranks.EIGHT, board.pieceSearch(Files.D + Ranks.EIGHT).getSquare());
        King king = (King) board.pieceSearch(Files.E + Ranks.EIGHT);
        assertEquals(Files.E + Ranks.EIGHT, king.getSquare());
        assertEquals(Files.F + Ranks.EIGHT, board.pieceSearch(Files.F + Ranks.EIGHT).getSquare());
        assertEquals(Files.G + Ranks.EIGHT, board.pieceSearch(Files.G + Ranks.EIGHT).getSquare());
        Rook hRook = (Rook) board.pieceSearch(Files.H + Ranks.EIGHT);
        assertEquals(Files.H + Ranks.EIGHT, hRook.getSquare());
        Pawn pawn;
        char file;
        int square;
        for (int i = 8; i < 16; i++) {
            file = (char) ('a' + (i - 8));
            square = SquareMapUtils.getSquare(file, 7);
            pawn = (Pawn) board.pieceSearch(square);
            assertEquals(square, pawn.getSquare());
            assertFalse(pawn.getMoved());
        }
        for (int i = 0; i < 8; i++) {
            file = (char) ('a' + (i));
            square = SquareMapUtils.getSquare(file, 2);
            pawn = (Pawn) board.pieceSearch(square);
            assertEquals(square, pawn.getSquare());
            assertFalse(pawn.getMoved());
        }
        aRook = (Rook) board.pieceSearch(Files.A + Ranks.ONE);
        assertEquals(Files.A + Ranks.ONE, aRook.getSquare());
        assertEquals(Files.B + Ranks.ONE, board.pieceSearch(Files.B + Ranks.ONE).getSquare());
        assertEquals(Files.C + Ranks.ONE, board.pieceSearch(Files.C + Ranks.ONE).getSquare());
        assertEquals(Files.D + Ranks.ONE, board.pieceSearch(Files.D + Ranks.ONE).getSquare());
        king = (King) board.pieceSearch(Files.E + Ranks.ONE);
        assertEquals(Files.E + Ranks.ONE, king.getSquare());

        assertEquals(Files.F + Ranks.ONE, board.pieceSearch(Files.F + Ranks.ONE).getSquare());
        assertEquals(Files.G + Ranks.ONE, board.pieceSearch(Files.G + Ranks.ONE).getSquare());
        hRook = (Rook) board.pieceSearch(Files.H + Ranks.ONE);
        assertEquals(Files.H + Ranks.ONE, hRook.getSquare());
        assertEquals(PieceColour.WHITE, gameState.getTurn());
        assertEquals(1, gameState.getMoveCount());
        assertEquals(0, gameState.getHalfMoveClock());
        int castlingRights = FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.WHITE_QUEENSIDE_CASTLE_MASK |
                FENConstants.BLACK_KINGSIDE_CASTLE_MASK | FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals(ChessConstants.NO_EN_PASSANT_TARGET, gameState.getEnPassantTarget());
    }

    @Test
    @DisplayName("Test making board with en passant")
    void testBoardFromFENWithEnPassant() {
        Position position = FENUtils.positionFromFEN("r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13");
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        Rook aRook = (Rook) board.pieceSearch(Files.A + Ranks.EIGHT);
        assertEquals(Files.A + Ranks.EIGHT, aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        King king = (King) board.pieceSearch(Files.E + Ranks.EIGHT);
        assertEquals(Files.E + Ranks.EIGHT, king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(Files.F + Ranks.EIGHT, board.pieceSearch(Files.F + Ranks.EIGHT).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.F + Ranks.EIGHT).getType());
        Rook hRook = (Rook) board.pieceSearch(Files.H + Ranks.EIGHT);
        assertEquals(Files.H + Ranks.EIGHT, hRook.getSquare());
        assertEquals(PieceType.ROOK, hRook.getType());
        Pawn pawn = (Pawn) board.pieceSearch(Files.A + Ranks.SEVEN);
        assertEquals(Files.A + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.D + Ranks.SEVEN);
        assertEquals(Files.D + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.H + Ranks.SEVEN);
        assertEquals(Files.H + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.A + Ranks.SIX, board.pieceSearch(Files.A + Ranks.SIX).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.A + Ranks.SIX).getType());
        pawn = (Pawn) board.pieceSearch(Files.B + Ranks.SIX);
        assertEquals(Files.B + Ranks.SIX, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.C + Ranks.SIX, board.pieceSearch(Files.C + Ranks.SIX).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.C + Ranks.SIX).getType());
        pawn = (Pawn) board.pieceSearch(Files.C + Ranks.FIVE);
        assertEquals(Files.C + Ranks.FIVE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.D + Ranks.FIVE, board.pieceSearch(Files.D + Ranks.FIVE).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.D + Ranks.FIVE).getType());
        pawn = (Pawn) board.pieceSearch(Files.G + Ranks.FIVE);
        assertEquals(Files.G + Ranks.FIVE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.H + Ranks.FIVE, board.pieceSearch(Files.H + Ranks.FIVE).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Files.H + Ranks.FIVE).getType());
        pawn = (Pawn) board.pieceSearch(Files.E + Ranks.FIVE);
        assertEquals(Files.E + Ranks.FIVE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.F + Ranks.FIVE);
        assertEquals(Files.F + Ranks.FIVE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.D + Ranks.FOUR);
        assertEquals(Files.D + Ranks.FOUR, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.F + Ranks.FOUR,board.pieceSearch(Files.F + Ranks.FOUR).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.F + Ranks.FOUR).getType());
        assertEquals(Files.C + Ranks.THREE, board.pieceSearch(Files.C + Ranks.THREE).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.C + Ranks.THREE).getType());
        assertEquals(Files.F + Ranks.THREE, board.pieceSearch(Files.F + Ranks.THREE).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.F + Ranks.THREE).getType());
        pawn = (Pawn) board.pieceSearch(Files.A + Ranks.TWO);
        assertEquals(Files.A + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.B + Ranks.TWO);
        assertEquals(Files.B + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.C + Ranks.TWO);
        assertEquals(Files.C + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.D + Ranks.TWO, board.pieceSearch(Files.D + Ranks.TWO).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Files.D + Ranks.TWO).getType());
        pawn = (Pawn) board.pieceSearch(Files.E + Ranks.TWO);
        assertEquals(Files.E + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.G + Ranks.TWO, board.pieceSearch(Files.G + Ranks.TWO).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.G + Ranks.TWO).getType());
        pawn = (Pawn) board.pieceSearch(Files.H + Ranks.TWO);
        assertEquals(Files.H + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        aRook = (Rook) board.pieceSearch(Files.B + Ranks.ONE);
        assertEquals(Files.B + Ranks.ONE, aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        aRook = (Rook) board.pieceSearch(Files.F + Ranks.ONE);
        assertEquals(Files.F + Ranks.ONE, aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        assertEquals(Files.F + Ranks.ONE, aRook.getSquare());
        king = (King) board.pieceSearch(Files.G + Ranks.ONE);
        assertEquals(Files.G + Ranks.ONE, king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(0, gameState.getHalfMoveClock());
        assertEquals(13, gameState.getMoveCount());
        assertEquals(PieceColour.WHITE, gameState.getTurn());
        int castlingRights = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals(Files.G + Ranks.SIX, gameState.getEnPassantTarget());
    }

    @Test
    @DisplayName("Test making board with random middlegame")
    void testBoardFromFENWithRandomMiddlegame() {
        Position position = FENUtils.positionFromFEN("r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9");
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        Rook rook = (Rook) board.pieceSearch(Files.A + Ranks.EIGHT);
        assertEquals(Files.A + Ranks.EIGHT, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Files.C + Ranks.EIGHT, board.pieceSearch(Files.C + Ranks.EIGHT).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.C + Ranks.EIGHT).getType());
        assertEquals(Files.D + Ranks.EIGHT, board.pieceSearch(Files.D + Ranks.EIGHT).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Files.D + Ranks.EIGHT).getType());
        rook = (Rook) board.pieceSearch(Files.F + Ranks.EIGHT);
        assertEquals(Files.F + Ranks.EIGHT, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        King king = (King) board.pieceSearch(Files.G + Ranks.EIGHT);
        assertEquals(Files.G + Ranks.EIGHT, king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        Pawn pawn = (Pawn) board.pieceSearch(Files.A + Ranks.SEVEN);
        assertEquals(Files.A + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.B + Ranks.SEVEN);
        assertEquals(Files.B + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files. C+ Ranks.SEVEN);
        assertEquals(Files.C + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.F + Ranks.SEVEN);
        assertEquals(Files.F + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.G + Ranks.SEVEN);
        assertEquals(Files.G + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.H + Ranks.SEVEN);
        assertEquals(Files.H + Ranks.SEVEN, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.C + Ranks.SIX, board.pieceSearch(Files.C + Ranks.SIX).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.C + Ranks.SIX).getType());
        assertEquals(Files.F + Ranks.SIX, board.pieceSearch(Files.F + Ranks.SIX).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.F + Ranks.SIX).getType());
        pawn = (Pawn) board.pieceSearch(Files.D + Ranks.FIVE);
        assertEquals(Files.D + Ranks.FIVE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.E + Ranks.FIVE);
        assertEquals(Files.E + Ranks.FIVE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.B + Ranks.FOUR, board.pieceSearch(Files.B + Ranks.FOUR).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.B + Ranks.FOUR).getType());
        pawn = (Pawn) board.pieceSearch(Files.D + Ranks.FOUR);
        assertEquals(Files.D + Ranks.FOUR, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.C + Ranks.THREE);
        assertEquals(Files.C + Ranks.THREE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.E + Ranks.THREE);
        assertEquals(Files.E + Ranks.THREE, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.F + Ranks.THREE, board.pieceSearch(Files.F + Ranks.THREE).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.F + Ranks.THREE).getType());
        pawn = (Pawn) board.pieceSearch(Files.A + Ranks.TWO);
        assertEquals(Files.A + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.B + Ranks.TWO);
        assertEquals(Files.B + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Files.D + Ranks.TWO, board.pieceSearch(Files.D + Ranks.TWO).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Files.D + Ranks.TWO).getType());
        assertEquals(Files.E + Ranks.TWO, board.pieceSearch(Files.E + Ranks.TWO).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.E + Ranks.TWO).getType());
        pawn = (Pawn) board.pieceSearch(Files.F + Ranks.TWO);
        assertEquals(Files.F + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.G + Ranks.TWO);
        assertEquals(Files.G + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Files.H + Ranks.TWO);
        assertEquals(Files.H + Ranks.TWO, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        rook = (Rook) board.pieceSearch(Files.A + Ranks.ONE);
        assertEquals(Files.A + Ranks.ONE, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Files.C + Ranks.ONE, board.pieceSearch(Files.C + Ranks.ONE).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Files.C + Ranks.ONE).getType());
        assertEquals(Files.D + Ranks.ONE, board.pieceSearch(Files.D + Ranks.ONE).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Files.D + Ranks.ONE).getType());
        rook = (Rook) board.pieceSearch(Files.F + Ranks.ONE);
        assertEquals(Files.F + Ranks.ONE, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        king = (King) board.pieceSearch(Files.G + Ranks.ONE);
        assertEquals(Files.G + Ranks.ONE, king.getSquare());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(PieceColour.BLACK, gameState.getTurn());
        assertEquals(0, gameState.getHalfMoveClock());
        assertEquals(9, gameState.getMoveCount());
        int castlingRights = FENConstants.NO_CASTLING_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals(ChessConstants.NO_EN_PASSANT_TARGET, gameState.getEnPassantTarget());
    }

    @Test
    @DisplayName("Test getFEN (starting position)")
    void testGetFEN() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test getFEN (test position 1)")
    void testGetFENWithPos1() {
        String fen = "r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9";
        Position position = FENUtils.positionFromFEN(fen);
        assertEquals(fen, FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test getFEN (test position 2)")
    void testGetFENWithPos2() {
        String fen = "r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13";
        Position position = FENUtils.positionFromFEN(fen);
        assertEquals(fen, FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test getFEN (test position 3)")
    void testGetFenWithPos3() {
        String fen = "rnbqkbnr/pp1ppppp/2p5/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 3";
        Position position = FENUtils.positionFromFEN(fen);
        assertEquals(fen, FENUtils.getFEN(position));
    }
}
