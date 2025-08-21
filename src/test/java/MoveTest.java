import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    public Move move;

    @Test
    @DisplayName("Test getAlgebraicNotation")
    void testGetAlgebraicNotation() {
    Piece piece = new Knight(PieceColour.WHITE, 'b', 5);
    Board board = FENUtils.boardFromFEN("8/8/8/1N6/8/8/8/8 w - - 0 1");
    move = new Move(board, piece, "c7");
    assertEquals("Nc7", move.getAlgebraicNotation());
    move.setCapture(true);
    move.setCheck(true);
    assertEquals("Nxc7+",  move.getAlgebraicNotation());
    move.setCapture(false);
    move.setFileDisambiguation(true);
    assertEquals("Nbc7+",  move.getAlgebraicNotation());
    move.setFileDisambiguation(false);
    move.setRankDisambiguation(true);
    move.setCheck(false);
    assertEquals("N5c7",  move.getAlgebraicNotation());
    move.setFileDisambiguation(true);
    assertEquals("Nb5c7",  move.getAlgebraicNotation());
    board = FENUtils.boardFromFEN("8/8/8/8/8/8/8/4K3 w - - 0 1");
    piece = new King(PieceColour.WHITE, 'e', 1, false, false);
    move = new Move(board, piece, "c1");
    move.setCastle(true);
    assertEquals("O-O-O",  move.getAlgebraicNotation());
    move = new Move(board, piece, "g1");
    move.setCastle(true);
    assertEquals("O-O",  move.getAlgebraicNotation());
    }


    @Test
    @DisplayName("Test moveConstructor")
    void testMove() {
        Board board = FENUtils.boardFromFEN("8/8/8/3r4/8/8/8/4K3 w - - 0 1");
        Piece piece = board.getBlackPieces()[0];
        Move move = new Move(board, piece, "e5");
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test castling detection")
    void testCastlingDetection() {
        Board board = FENUtils.boardFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move move = new Move(board, whiteKing, "g1");
        assertTrue(move.isCastle());
        move = new Move(board, whiteKing, "c1");
        assertTrue(move.isCastle());
        King blackKing = board.findKing(PieceColour.BLACK);
        move = new Move(board, blackKing, "g8");
        assertTrue(move.isCastle());
        move = new Move(board, blackKing, "c8");
        assertTrue(move.isCastle());
    }

    @Test
    @DisplayName("Test discoveredCheckDetection")
    void testDiscoveredCheckDetection() {
        Board board = FENUtils.boardFromFEN("8/2r5/2n5/5k2/8/2KR4/2B5/8 w - - 0 1");
        Piece piece = board.getWhitePieces()[1];
        Move move = new Move(board, piece, "d4");
        assertTrue(move.isCheck());
        piece = board.getBlackPieces()[1];
        move = new Move(board, piece, "e5");
        assertTrue(move.isCheck());
        piece = board.getWhitePieces()[1];
        move = new Move(board, piece, "f3");
        assertTrue(move.isCheck());
        piece = board.getWhitePieces()[2];
        move = new Move(board, piece, "b1");
        assertFalse(move.isCheck());
    }

    @Test
    @DisplayName("Test castleCheckDetection")
    void testCastleCheckDetection() {
        Board board = FENUtils.boardFromFEN("8/8/8/8/8/5k2/8/4K2R w K - 0 1");
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move move = new Move(board, whiteKing, "g1");
        move.setCastle(true);
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test illegalMoveDetection (pin)")
    void testIllegalMoveDetection() {
        Board board = FENUtils.boardFromFEN("8/8/R2b1k2/8/8/8/8/8 w - - 0 1");
        Piece piece = board.getBlackPieces()[0];
        Move[] expectedMoves = new Move[ChessConstants.MAX_BISHOP_MOVES];
        assertArrayEquals(expectedMoves, piece.generateMoves(board));
    }

    @Test
    @DisplayName("Test illegalMoveDetection (blocking check)")
    void testIllegalMoveDetection2() {
        Board board = FENUtils.boardFromFEN("8/8/R4k2/2b5/8/8/8/8 w - - 0 1");
        Piece piece = board.getBlackPieces()[1];
        Move[] expectedMoves = new Move[ChessConstants.MAX_BISHOP_MOVES];
        expectedMoves[0] = new Move(board, piece, "b6");
        expectedMoves[1] = new Move(board, piece, "d6");
        assertArrayEquals(expectedMoves, piece.generateMoves(board));
    }
}

