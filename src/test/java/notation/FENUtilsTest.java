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
        Rook aRook = (Rook) board.pieceSearch("a8");
        assertEquals("a8", aRook.getSquare());
        assertEquals("b8", board.pieceSearch("b8").getSquare());
        assertEquals("c8", board.pieceSearch("c8").getSquare());
        assertEquals("d8", board.pieceSearch("d8").getSquare());
        King king = (King) board.pieceSearch("e8");
        assertEquals("e8", king.getSquare());
        assertEquals("f8", board.pieceSearch("f8").getSquare());
        assertEquals("g8", board.pieceSearch("g8").getSquare());
        Rook hRook = (Rook) board.pieceSearch("h8");
        assertEquals("h8", hRook.getSquare());
        Pawn pawn;
        char file;
        String square;
        for (int i = 8; i < 16; i++) {
            file = (char) ('a' + (i - 8));
            square = String.valueOf(file) + '7';
            pawn = (Pawn) board.pieceSearch(square);
            assertEquals(square, pawn.getSquare());
            assertFalse(pawn.getMoved());
        }
        for (int i = 0; i < 8; i++) {
            file = (char) ('a' + (i));
            square = String.valueOf(file) + '2';
            pawn = (Pawn) board.pieceSearch(square);
            assertEquals(square, pawn.getSquare());
            assertFalse(pawn.getMoved());
        }
        aRook = (Rook) board.pieceSearch("a1");
        assertEquals("a1", aRook.getSquare());
        assertEquals("b1", board.pieceSearch("b1").getSquare());
        assertEquals("c1", board.pieceSearch("c1").getSquare());
        assertEquals("d1", board.pieceSearch("d1").getSquare());
        king = (King) board.pieceSearch("e1");
        assertEquals("e1", king.getSquare());

        assertEquals("f1", board.pieceSearch("f1").getSquare());
        assertEquals("g1", board.pieceSearch("g1").getSquare());
        hRook = (Rook) board.pieceSearch("h1");
        assertEquals("h1", hRook.getSquare());
        assertEquals(PieceColour.WHITE, gameState.getTurn());
        assertEquals(1, gameState.getMoveCount());
        assertEquals(0, gameState.getHalfMoveClock());
        int castlingRights = FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.WHITE_QUEENSIDE_CASTLE_MASK |
                FENConstants.BLACK_KINGSIDE_CASTLE_MASK | FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals(FENConstants.NONE, gameState.getEnPassantTarget());
    }

    @Test
    @DisplayName("Test making board with en passant")
    void testBoardFromFENWithEnPassant() {
        Position position = FENUtils.positionFromFEN("r3kb1r/p2p3p/bpn5/2pnPPpq/3P1B2/2N2N2/PPPQP1BP/1R3RK1 w k g6 0 13");
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        Rook aRook = (Rook) board.pieceSearch("a8");
        assertEquals("a8", aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        King king = (King) board.pieceSearch("e8");
        assertEquals("e8", king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals("f8", board.pieceSearch("f8").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("f8").getType());
        Rook hRook = (Rook) board.pieceSearch("h8");
        assertEquals("h8", hRook.getSquare());
        assertEquals(PieceType.ROOK, hRook.getType());
        Pawn pawn = (Pawn) board.pieceSearch("a7");
        assertEquals("a7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("d7");
        assertEquals("d7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("h7");
        assertEquals("h7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("a6", board.pieceSearch("a6").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("a6").getType());
        pawn = (Pawn) board.pieceSearch("b6");
        assertEquals("b6", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("c6", board.pieceSearch("c6").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("c6").getType());
        pawn = (Pawn) board.pieceSearch("c5");
        assertEquals("c5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("d5", board.pieceSearch("d5").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("d5").getType());
        pawn = (Pawn) board.pieceSearch("g5");
        assertEquals("g5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("h5", board.pieceSearch("h5").getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch("h5").getType());
        pawn = (Pawn) board.pieceSearch("e5");
        assertEquals("e5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("f5");
        assertEquals("f5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("d4");
        assertEquals("d4", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("f4",board.pieceSearch("f4").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("f4").getType());
        assertEquals("c3", board.pieceSearch("c3").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("c3").getType());
        assertEquals("f3", board.pieceSearch("f3").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("f3").getType());
        pawn = (Pawn) board.pieceSearch("a2");
        assertEquals("a2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("b2");
        assertEquals("b2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("c2");
        assertEquals("c2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("d2", board.pieceSearch("d2").getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch("d2").getType());
        pawn = (Pawn) board.pieceSearch("e2");
        assertEquals("e2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("g2", board.pieceSearch("g2").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("g2").getType());
        pawn = (Pawn) board.pieceSearch("h2");
        assertEquals("h2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        aRook = (Rook) board.pieceSearch("b1");
        assertEquals("b1", aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        aRook = (Rook) board.pieceSearch("f1");
        assertEquals("f1", aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        assertEquals("f1", aRook.getSquare());
        king = (King) board.pieceSearch("g1");
        assertEquals("g1", king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(0, gameState.getHalfMoveClock());
        assertEquals(13, gameState.getMoveCount());
        assertEquals(PieceColour.WHITE, gameState.getTurn());
        int castlingRights = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals("g6", gameState.getEnPassantTarget());
    }

    @Test
    @DisplayName("Test making board with random middlegame")
    void testBoardFromFENWithRandomMiddlegame() {
        Position position = FENUtils.positionFromFEN("r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9");
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        Rook rook = (Rook) board.pieceSearch("a8");
        assertEquals("a8", rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals("c8", board.pieceSearch("c8").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("c8").getType());
        assertEquals("d8", board.pieceSearch("d8").getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch("d8").getType());
        rook = (Rook) board.pieceSearch("f8");
        assertEquals("f8", rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        King king = (King) board.pieceSearch("g8");
        assertEquals("g8", king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        Pawn pawn = (Pawn) board.pieceSearch("a7");
        assertEquals("a7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("b7");
        assertEquals("b7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("c7");
        assertEquals("c7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("f7");
        assertEquals("f7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("g7");
        assertEquals("g7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("h7");
        assertEquals("h7", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("c6", board.pieceSearch("c6").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("c6").getType());
        assertEquals("f6", board.pieceSearch("f6").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("f6").getType());
        pawn = (Pawn) board.pieceSearch("d5");
        assertEquals("d5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("e5");
        assertEquals("e5", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("b4", board.pieceSearch("b4").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("b4").getType());
        pawn = (Pawn) board.pieceSearch("d4");
        assertEquals("d4", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("c3");
        assertEquals("c3", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("e3");
        assertEquals("e3", pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("f3", board.pieceSearch("f3").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("f3").getType());
        pawn = (Pawn) board.pieceSearch("a2");
        assertEquals("a2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("b2");
        assertEquals("b2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals("d2", board.pieceSearch("d2").getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch("d2").getType());
        assertEquals("e2", board.pieceSearch("e2").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("e2").getType());
        pawn = (Pawn) board.pieceSearch("f2");
        assertEquals("f2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("g2");
        assertEquals("g2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch("h2");
        assertEquals("h2", pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        rook = (Rook) board.pieceSearch("a1");
        assertEquals("a1", rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals("c1", board.pieceSearch("c1").getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch("c1").getType());
        assertEquals("d1", board.pieceSearch("d1").getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch("d1").getType());
        rook = (Rook) board.pieceSearch("f1");
        assertEquals("f1", rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        king = (King) board.pieceSearch("g1");
        assertEquals("g1", king.getSquare());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(PieceColour.BLACK, gameState.getTurn());
        assertEquals(0, gameState.getHalfMoveClock());
        assertEquals(9, gameState.getMoveCount());
        int castlingRights = FENConstants.NO_CASTLING_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals(FENConstants.NONE, gameState.getEnPassantTarget());
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
