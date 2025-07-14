import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RookTest {
    static Rook piece1;
    static Rook piece2;

    @BeforeEach
    public void init() {
        piece1 = new Rook(PieceColour.WHITE, 'a', 1, false);
        piece2 = new Rook(PieceColour.BLACK, 'c', 5, false);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a7"));
        assertTrue(piece2.isLegalMove("h5"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test move")
    void testMove() {
        assertFalse(piece1.getMoved());
        piece1.move("a7");
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1.move("c4");
        piece2.move("e5");
        Board board = new Board("8/8/8/4r3/2R5/8/8/8 w - - 0 1");
        String[] piece1MovesExpected = {"b4", "a4", "d4", "e4", "f4", "g4", "h4", "c5", "c6", "c7", "c8", "c3", "c2", "c1"};
        String[] piece2MovesExpected = {"d5", "c5", "b5", "a5", "f5", "g5", "h5", "e6", "e7", "e8", "e4", "e3", "e2", "e1"};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("rnbqkbn1/pppppp2/1r6/3R2pp/P3P3/2N5/1PPP1PPP/2BQKBNR w Kq - 0 1");
        piece1 = (Rook) board.getWhitePieces()[0];
        piece2 = (Rook) board.getBlackPieces()[13];
        String[] piece1MovesExpected = {"c5", "b5", "a5", "e5", "f5", "g5", "d6", "d7", "d4", "d3", null, null, null, null};
        String[] piece2MovesExpected = {"a6", "c6", "d6", "e6", "f6", "g6", "h6", "b5", "b4", "b3", "b2", null, null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureKing")
    void testCanCaptureKing() {
        Board board = new Board("8/3P4/8/1P1r2P1/8/8/3P4/8 w - - 0 1");
        Rook rook =  (Rook) board.getBlackPieces()[0];
        assertFalse(rook.canCaptureKing(board, "d8"));
        assertTrue(rook.canCaptureKing(board, "d7"));
        assertFalse(rook.canCaptureKing(board, "h5"));
        assertTrue(rook.canCaptureKing(board, "g5"));
        assertFalse(rook.canCaptureKing(board, "d1"));
        assertTrue(rook.canCaptureKing(board, "d2"));
        assertFalse(rook.canCaptureKing(board, "a5"));
        assertTrue(rook.canCaptureKing(board, "b5"));

    }
}
