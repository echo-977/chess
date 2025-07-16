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
        Move[] piece1MovesExpected = {new Move(piece1, "b4"), new Move(piece1, "a4"),
                new Move(piece1, "d4"), new Move(piece1, "e4"), new Move(piece1, "f4"),
                new Move(piece1, "g4"), new Move(piece1, "h4"), new Move(piece1, "c5"),
                new Move(piece1, "c6"), new Move(piece1, "c7"), new Move(piece1, "c8"),
                new Move(piece1, "c3"), new Move(piece1, "c2"), new Move(piece1, "c1")};
        Move[] piece2MovesExpected = {new Move(piece2, "d5"), new Move(piece2, "c5"), new Move(piece2, "b5"), new Move(piece2, "a5"), new Move(piece2, "f5"), new Move(piece2, "g5"), new Move(piece2, "h5"), new Move(piece2, "e6"), new Move(piece2, "e7"), new Move(piece2, "e8"), new Move(piece2, "e4"), new Move(piece2, "e3"), new Move(piece2, "e2"), new Move(piece2, "e1")};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("rnbqkbn1/pppppp2/1r6/3R2pp/P3P3/2N5/1PPP1PPP/2BQKBNR w Kq - 0 1");
        piece1 = (Rook) board.getWhitePieces()[0];
        piece2 = (Rook) board.getBlackPieces()[13];
        Move[] piece1MovesExpected = {new Move(piece1, "c5"), new Move(piece1, "b5"),
                new Move(piece1, "a5"), new Move(piece1, "e5"), new Move(piece1, "f5"),
                new Move(piece1, "g5"), new Move(piece1, "d6"), new Move(piece1, "d7"),
                new Move(piece1, "d4"), new Move(piece1, "d3"), null, null, null, null};
        Move[] piece2MovesExpected = {new Move(piece2, "a6"), new Move(piece2, "c6"),
                new Move(piece2, "d6"), new Move(piece2, "e6"), new Move(piece2, "f6"),
                new Move(piece2, "g6"), new Move(piece2, "h6"), new Move(piece2, "b5"),
                new Move(piece2, "b4"), new Move(piece2, "b3"), new Move(piece2, "b2"),
                null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(board);
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
