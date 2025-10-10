import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {
    @Test
    @DisplayName("Test handleCaptureMove")
    void testHandleCaptureMove() {
        Position position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        Board board = position.getBoard();
        int moveFlag = MoveFlags.EN_PASSANT | MoveFlags.CAPTURE_BIT;
        int move = moveFlag << MoveFlags.FLAG_SHIFT | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.F5;
        Piece piece = board.pieceSearch(Squares.F5);
        Piece capturedPiece = board.pieceSearch(Squares.G5);
        State beforeMove = position.doMove(move);
        assertEquals(capturedPiece, beforeMove.capturedPiece());
        assertEquals(piece, board.pieceSearch(Squares.G6));
        assertEquals(0b00000000_01000000_00000000_00000000_00000000_01000000_00000000_00000000L,
                board.getOccupancyBitboard(ChessConstants.WHITE_BITBOARD));
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000100_00000000_00000000L,
                board.getOccupancyBitboard(ChessConstants.BLACK_BITBOARD));
        position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        board = position.getBoard();
        moveFlag = MoveFlags.CAPTURE_BIT;
        move = moveFlag << MoveFlags.FLAG_SHIFT | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.G2;
        piece = board.pieceSearch(Squares.G2);
        capturedPiece = board.pieceSearch(Squares.G5);
        beforeMove = position.doMove(move);
        assertEquals(capturedPiece, beforeMove.capturedPiece());
        assertEquals(piece, board.pieceSearch(Squares.G5));
        assertEquals(0b00000000_00000000_00000000_00000000_01100000_00000000_00000000_00000000L,
                board.getOccupancyBitboard(ChessConstants.WHITE_BITBOARD));
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000100_00000000_00000000L,
                board.getOccupancyBitboard(ChessConstants.BLACK_BITBOARD));
        position = FENUtils.positionFromFEN("8/8/2b5/5Pp1/8/8/6R1/8 w - g6 0 1");
        board = position.getBoard();
        move = moveFlag << MoveFlags.FLAG_SHIFT | Squares.G2 << MoveFlags.DESTINATION_SHIFT | Squares.C6;
        piece = board.pieceSearch(Squares.C6);
        capturedPiece = board.pieceSearch(Squares.G2);
        beforeMove = position.doMove(move);
        assertEquals(capturedPiece, beforeMove.capturedPiece());
        assertEquals(piece, board.pieceSearch(Squares.G2));
        assertEquals(0b00000000_00000000_00000000_00000000_00100000_00000000_00000000_00000000L,
                board.getOccupancyBitboard(ChessConstants.WHITE_BITBOARD));
        assertEquals(0b00000000_01000000_00000000_00000000_01000000_00000000_00000000_00000000L,
                board.getOccupancyBitboard(ChessConstants.BLACK_BITBOARD));
    }

    @Test
    @DisplayName("Test doMove")
    void testDoMove() {
        Position position = FENUtils.positionFromFEN("8/8/5r2/8/8/3Q4/8/8 w - - 0 1");
        int move = MoveFlags.QUIET_MOVE << MoveFlags.FLAG_SHIFT | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.D3;
        Piece piece = position.getBoard().pieceSearch(Squares.D3);
        assertEquals(0b00000000_00000000_00001000_00000000_00000000_00000000_00000000_00000000L,
                position.getBoard().getOccupancyBitboard(ChessConstants.WHITE_BITBOARD));
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00100000_00000000_00000000L,
                position.getBoard().getOccupancyBitboard(ChessConstants.BLACK_BITBOARD));
        position.doMove(move);
        assertEquals(Squares.G6, piece.getSquare());
        assertEquals(1, position.getGameState().getHalfMoveClock());
        assertEquals(1, position.getGameState().getMoveCount());
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_01000000_00000000_00000000L,
                position.getBoard().getOccupancyBitboard(ChessConstants.WHITE_BITBOARD));
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00100000_00000000_00000000L,
                position.getBoard().getOccupancyBitboard(ChessConstants.BLACK_BITBOARD));
        move = MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.G6 << MoveFlags.DESTINATION_SHIFT | Squares.F6;
        position.doMove(move);
        assertEquals(0, position.getGameState().getHalfMoveClock());
        assertEquals(2, position.getGameState().getMoveCount());
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L,
                position.getBoard().getOccupancyBitboard(ChessConstants.WHITE_BITBOARD));
        assertEquals(0b00000000_00000000_00000000_00000000_00000000_01000000_00000000_00000000L,
                position.getBoard().getOccupancyBitboard(ChessConstants.BLACK_BITBOARD));
    }

    @Test
    @DisplayName("Test unDoMove (default)")
    void testUnDoMove() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int move = MoveFlags.QUIET_MOVE << MoveFlags.FLAG_SHIFT | Squares.F3 << MoveFlags.DESTINATION_SHIFT | Squares.G1;
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (capture)")
    void testUnDoMoveCapture() {
        Position position = FENUtils.positionFromFEN("rn1qkbnr/pppppppp/8/3b4/5N2/8/PPPPPPPP/RNBQKB1R w KQkq - 0 1");
        int move = MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.F4;
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("rn1qkbnr/pppppppp/8/3b4/5N2/8/PPPPPPPP/RNBQKB1R w KQkq - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (promotion)")
    void testUnDoMovePromotion() {
        Position position = FENUtils.positionFromFEN("3K1n2/2P5/4N3/8/1B2k3/8/8/8 w - - 0 1");
        int move = (MoveFlags.PROMOTION_BIT | MoveFlags.QUEEN) << MoveFlags.FLAG_SHIFT | Squares.C8 << MoveFlags.DESTINATION_SHIFT | Squares.C7;
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("3K1n2/2P5/4N3/8/1B2k3/8/8/8 w - - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (castle)")
    void testUnDoMoveCastle() {
        Position position = FENUtils.positionFromFEN("5n2/2P5/4N3/8/1B2k3/8/8/4K2R w K - 0 1");
        int move = MoveFlags.KINGSIDE_CASTLE << MoveFlags.FLAG_SHIFT | Squares.G1 << MoveFlags.DESTINATION_SHIFT | Squares.E1;
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("5n2/2P5/4N3/8/1B2k3/8/8/4K2R w K - 0 1", FENUtils.getFEN(position));
    }

    @Test
    @DisplayName("Test unDoMove (en passant)")
    void testUnDoMoveEnPassant() {
        Position position = FENUtils.positionFromFEN("rnbqkbnr/ppppp1pp/8/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1");
        int move  = (MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT) << MoveFlags.FLAG_SHIFT | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.E5;
        State stateBeforeMove = position.doMove(move);
        position.unDoMove(stateBeforeMove);
        assertEquals("rnbqkbnr/ppppp1pp/8/4Pp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1", FENUtils.getFEN(position));
    }

}
