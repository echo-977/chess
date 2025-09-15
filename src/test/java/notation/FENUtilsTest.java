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
        Rook aRook = (Rook) board.pieceSearch(Squares.A8);
        assertEquals(Squares.A8, aRook.getSquare());
        assertEquals(Squares.B8, board.pieceSearch(Squares.B8).getSquare());
        assertEquals(Squares.C8, board.pieceSearch(Squares.C8).getSquare());
        assertEquals(Squares.D8, board.pieceSearch(Squares.D8).getSquare());
        King king = (King) board.pieceSearch(Squares.E8);
        assertEquals(Squares.E8, king.getSquare());
        assertEquals(Squares.F8, board.pieceSearch(Squares.F8).getSquare());
        assertEquals(Squares.G8, board.pieceSearch(Squares.G8).getSquare());
        Rook hRook = (Rook) board.pieceSearch(Squares.H8);
        assertEquals(Squares.H8, hRook.getSquare());
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
        aRook = (Rook) board.pieceSearch(Squares.A1);
        assertEquals(Squares.A1, aRook.getSquare());
        assertEquals(Squares.B1, board.pieceSearch(Squares.B1).getSquare());
        assertEquals(Squares.C1, board.pieceSearch(Squares.C1).getSquare());
        assertEquals(Squares.D1, board.pieceSearch(Squares.D1).getSquare());
        king = (King) board.pieceSearch(Squares.E1);
        assertEquals(Squares.E1, king.getSquare());

        assertEquals(Squares.F1, board.pieceSearch(Squares.F1).getSquare());
        assertEquals(Squares.G1, board.pieceSearch(Squares.G1).getSquare());
        hRook = (Rook) board.pieceSearch(Squares.H1);
        assertEquals(Squares.H1, hRook.getSquare());
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
        Rook aRook = (Rook) board.pieceSearch(Squares.A8);
        assertEquals(Squares.A8, aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        King king = (King) board.pieceSearch(Squares.E8);
        assertEquals(Squares.E8, king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(Squares.F8, board.pieceSearch(Squares.F8).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.F8).getType());
        Rook hRook = (Rook) board.pieceSearch(Squares.H8);
        assertEquals(Squares.H8, hRook.getSquare());
        assertEquals(PieceType.ROOK, hRook.getType());
        Pawn pawn = (Pawn) board.pieceSearch(Squares.A7);
        assertEquals(Squares.A7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.D7);
        assertEquals(Squares.D7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.H7);
        assertEquals(Squares.H7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.A6, board.pieceSearch(Squares.A6).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.A6).getType());
        pawn = (Pawn) board.pieceSearch(Squares.B6);
        assertEquals(Squares.B6, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.C6, board.pieceSearch(Squares.C6).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.C6).getType());
        pawn = (Pawn) board.pieceSearch(Squares.C5);
        assertEquals(Squares.C5, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.D5, board.pieceSearch(Squares.D5).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.D5).getType());
        pawn = (Pawn) board.pieceSearch(Squares.G5);
        assertEquals(Squares.G5, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.H5, board.pieceSearch(Squares.H5).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Squares.H5).getType());
        pawn = (Pawn) board.pieceSearch(Squares.E5);
        assertEquals(Squares.E5, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.F5);
        assertEquals(Squares.F5, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.D4);
        assertEquals(Squares.D4, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.F4, board.pieceSearch(Squares.F4).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.F4).getType());
        assertEquals(Squares.C3, board.pieceSearch(Squares.C3).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.C3).getType());
        assertEquals(Files.F + Ranks.THREE, board.pieceSearch(Squares.F3).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.F3).getType());
        pawn = (Pawn) board.pieceSearch(Squares.A2);
        assertEquals(Squares.A2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.B2);
        assertEquals(Squares.B2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.C2);
        assertEquals(Squares.C2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.D2, board.pieceSearch(Squares.D2).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Squares.D2).getType());
        pawn = (Pawn) board.pieceSearch(Squares.E2);
        assertEquals(Squares.E2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.G2, board.pieceSearch(Squares.G2).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.G2).getType());
        pawn = (Pawn) board.pieceSearch(Squares.H2);
        assertEquals(Squares.H2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        aRook = (Rook) board.pieceSearch(Squares.B1);
        assertEquals(Squares.B1, aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        aRook = (Rook) board.pieceSearch(Squares.F1);
        assertEquals(Squares.F1, aRook.getSquare());
        assertEquals(PieceType.ROOK, aRook.getType());
        king = (King) board.pieceSearch(Squares.G1);
        assertEquals(Squares.G1, king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        assertEquals(0, gameState.getHalfMoveClock());
        assertEquals(13, gameState.getMoveCount());
        assertEquals(PieceColour.WHITE, gameState.getTurn());
        int castlingRights = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
        assertEquals(castlingRights, gameState.getCastlingRights());
        assertEquals(Squares.G6, gameState.getEnPassantTarget());
    }

    @Test
    @DisplayName("Test making board with random middlegame")
    void testBoardFromFENWithRandomMiddlegame() {
        Position position = FENUtils.positionFromFEN("r1bq1rk1/ppp2ppp/2n2n2/3pp3/1b1P4/2P1PN2/PP1NBPPP/R1BQ1RK1 b - - 0 9");
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        Rook rook = (Rook) board.pieceSearch(Squares.A8);
        assertEquals(Squares.A8, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Squares.C8, board.pieceSearch(Squares.C8).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.C8).getType());
        assertEquals(Squares.D8, board.pieceSearch(Squares.D8).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Squares.D8).getType());
        rook = (Rook) board.pieceSearch(Squares.F8);
        assertEquals(Squares.F8, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        King king = (King) board.pieceSearch(Squares.G8);
        assertEquals(Squares.G8, king.getSquare());
        assertFalse(king.isCheck());
        assertEquals(PieceType.KING, king.getType());
        Pawn pawn = (Pawn) board.pieceSearch(Squares.A7);
        assertEquals(Squares.A7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.B7);
        assertEquals(Squares.B7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.C7);
        assertEquals(Squares.C7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.F7);
        assertEquals(Squares.F7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.G7);
        assertEquals(Squares.G7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.H7);
        assertEquals(Squares.H7, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.C6, board.pieceSearch(Squares.C6).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.C6).getType());
        assertEquals(Squares.F6, board.pieceSearch(Squares.F6).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.F6).getType());
        pawn = (Pawn) board.pieceSearch(Squares.D5);
        assertEquals(Squares.D5, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.E5);
        assertEquals(Squares.E5, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.B4, board.pieceSearch(Squares.B4).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.B4).getType());
        pawn = (Pawn) board.pieceSearch(Squares.D4);
        assertEquals(Squares.D4, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.C3);
        assertEquals(Squares.C3, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.E3);
        assertEquals(Squares.E3, pawn.getSquare());
        assertTrue(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.F3, board.pieceSearch(Squares.F3).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.F3).getType());
        pawn = (Pawn) board.pieceSearch(Squares.A2);
        assertEquals(Squares.A2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.B2);
        assertEquals(Squares.B2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Squares.D2, board.pieceSearch(Squares.D2).getSquare());
        assertEquals(PieceType.KNIGHT, board.pieceSearch(Squares.D2).getType());
        assertEquals(Squares.E2, board.pieceSearch(Squares.E2).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.E2).getType());
        pawn = (Pawn) board.pieceSearch(Squares.F2);
        assertEquals(Squares.F2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.G2);
        assertEquals(Squares.G2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        pawn = (Pawn) board.pieceSearch(Squares.H2);
        assertEquals(Squares.H2, pawn.getSquare());
        assertFalse(pawn.getMoved());
        assertEquals(PieceType.PAWN, pawn.getType());
        rook = (Rook) board.pieceSearch(Squares.A1);
        assertEquals(Squares.A1, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Squares.C1, board.pieceSearch(Squares.C1).getSquare());
        assertEquals(PieceType.BISHOP, board.pieceSearch(Squares.C1).getType());
        assertEquals(Squares.D1, board.pieceSearch(Squares.D1).getSquare());
        assertEquals(PieceType.QUEEN, board.pieceSearch(Squares.D1).getType());
        rook = (Rook) board.pieceSearch(Squares.F1);
        assertEquals(Squares.F1, rook.getSquare());
        assertEquals(PieceType.ROOK, rook.getType());
        king = (King) board.pieceSearch(Squares.G1);
        assertEquals(Squares.G1, king.getSquare());
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
