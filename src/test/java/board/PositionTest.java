import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {
    @Test
    @DisplayName("Test handleCaptureMove")
    void testHandleCaptureMove() {
        Position position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Board board = position.getBoard();
        Piece piece = board.pieceSearch(Files.F + Ranks.FIVE);
        Move move = new Move(position, piece, Files.G + Ranks.SIX);
        move.setEnPassant(true);
        Piece capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        State beforeMove = position.doMove(move);
        assertEquals(capturedPiece, beforeMove.capturedPiece());
        assertEquals(piece, board.pieceSearch(move.getDestination()));
        position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        board = position.getBoard();
        piece = board.pieceSearch(Files.G + Ranks.TWO);
        move = new Move(position, piece, Files.G + Ranks.FIVE);
        move.setCapture(true);
        capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        beforeMove = position.doMove(move);
        assertEquals(capturedPiece, beforeMove.capturedPiece());
        assertEquals(piece, board.pieceSearch(move.getDestination()));
        position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        board = position.getBoard();
        piece = board.pieceSearch(Files.C + Ranks.SIX);
        move = new Move(position, piece, Files.G + Ranks.TWO);
        move.setCapture(true);
        capturedPiece = board.pieceSearch(board.getCaptureDestination(move));
        beforeMove = position.doMove(move);
        assertEquals(capturedPiece, beforeMove.capturedPiece());
        assertEquals(piece, board.pieceSearch(move.getDestination()));
    }

    @Test
    @DisplayName("Test doMove")
    void testDoMove() {
        Position position = FENUtils.positionFromFEN("8/8/5r2/8/8/3Q4/8/8 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Files.D + Ranks.THREE);
        Move move = new Move(position, piece, Files.G + Ranks.SIX);
        position.doMove(move);
        assertEquals(Files.G + Ranks.SIX, piece.getSquare());
        assertEquals(1, position.getGameState().getHalfMoveClock());
        assertEquals(1, position.getGameState().getMoveCount());
        move = new Move(position, position.getBoard().pieceSearch(Files.F + Ranks.SIX), Files.G + Ranks.SIX);
        position.doMove(move);
        assertEquals(0, position.getGameState().getHalfMoveClock());
        assertEquals(2, position.getGameState().getMoveCount());
    }

    @Test
    @DisplayName("Test unDoMove (default)")
    void testUnDoMove() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Board board = position.getBoard();
        Move move = new Move(position, board.pieceSearch(Files.G + Ranks.ONE), Files.F + Ranks.THREE);
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (capture)")
    void testUnDoMoveCapture() {
        Position position = FENUtils.positionFromFEN("rn1qkbnr/pppppppp/8/3b4/5N2/8/PPPPPPPP/RNBQKB1R w KQkq - 0 1");
        Board board = position.getBoard();
        Move move = new Move(position, board.pieceSearch(Files.F + Ranks.FOUR), Files.D + Ranks.FIVE);
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("rn1qkbnr/pppppppp/8/3b4/5N2/8/PPPPPPPP/RNBQKB1R w KQkq - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (promotion)")
    void testUnDoMovePromotion() {
        Position position = FENUtils.positionFromFEN("3K1n2/2P5/4N3/8/1B2k3/8/8/8 w - - 0 1");
        Board board = position.getBoard();
        Move move = new Move(position, board.pieceSearch(Files.C + Ranks.SEVEN), Files.C + Ranks.EIGHT);
        move.setPromotionType(PieceType.QUEEN);
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("3K1n2/2P5/4N3/8/1B2k3/8/8/8 w - - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (castle)")
    void testUnDoMoveCastle() {
        Position position = FENUtils.positionFromFEN("5n2/2P5/4N3/8/1B2k3/8/8/4K2R w K - 0 1");
        Board board = position.getBoard();
        Move move = new Move(position, board.pieceSearch(Files.E + Ranks.ONE), Files.G + Ranks.ONE);
        move.setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("5n2/2P5/4N3/8/1B2k3/8/8/4K2R w K - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (en passant)")
    void testUnDoMoveEnPassant() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/ppppp1pp/8/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1");
        Board board = position.getBoard();
        Move move =  new Move(position, board.pieceSearch(Files.E + Ranks.FIVE), Files.F + Ranks.SIX);
        move.setEnPassant(true);
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("rnbqkbnr/ppppp1pp/8/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1", FENUtils.getFEN(position));
    }

}
