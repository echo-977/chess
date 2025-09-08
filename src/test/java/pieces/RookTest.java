import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {
    static Rook piece1;
    static Rook piece2;

    @BeforeEach
    public void init() {
        piece1 = new Rook(PieceColour.WHITE, 'a', 1);
        piece2 = new Rook(PieceColour.BLACK, 'c', 5);
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
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        piece1.move("c4");
        piece2.move("e5");
        Position position = FENUtils.positionFromFEN("8/8/8/4r3/2R5/8/8/8 w - - 0 1");
        Move[] piece1MovesExpected = {new Move(position, piece1, "b4"),
                new Move(position, piece1, "a4"), new Move(position, piece1, "d4"),
                new Move(position, piece1, "e4"), new Move(position, piece1, "f4"),
                new Move(position, piece1, "g4"), new Move(position, piece1, "h4"),
                new Move(position, piece1, "c5"), new Move(position, piece1, "c6"),
                new Move(position, piece1, "c7"), new Move(position, piece1, "c8"),
                new Move(position, piece1, "c3"), new Move(position, piece1, "c2"),
                new Move(position, piece1, "c1")};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(position, piece2, "d5"),
                new Move(position, piece2, "c5"), new Move(position, piece2, "b5"),
                new Move(position, piece2, "a5"), new Move(position, piece2, "f5"),
                new Move(position, piece2, "g5"), new Move(position, piece2, "h5"),
                new Move(position, piece2, "e6"), new Move(position, piece2, "e7"),
                new Move(position, piece2, "e8"), new Move(position, piece2, "e4"),
                new Move(position, piece2, "e3"), new Move(position, piece2, "e2"),
                new Move(position, piece2, "e1")};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Position position = FENUtils.positionFromFEN("rnbqkbn1/pppppp2/1r6/3R2pp/P3P3/2N5/1PPP1PPP/2BQKBNR w Kq - 0 1");
        Board board = position.getBoard();
        piece1 = (Rook) board.pieceSearch("d5");
        piece2 = (Rook) board.pieceSearch("b6");
        Move move1 = new Move(position, piece1, "g5");
        move1.setCapture(true);
        Move move2 = new Move(position, piece1, "d7");
        move2.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, "c5"),
                new Move(position, piece1, "b5"), new Move(position, piece1, "a5"),
                new Move(position, piece1, "e5"), new Move(position, piece1, "f5"), move1,
                new Move(position, piece1, "d6"), move2, new Move(position, piece1, "d4"),
                new Move(position, piece1, "d3"), null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, "b2");
        move1.setCapture(true);
        Move[] piece2MovesExpected = {new Move(position, piece2, "a6"),
                new Move(position, piece2, "c6"), new Move(position, piece2, "d6"),
                new Move(position, piece2, "e6"), new Move(position, piece2, "f6"),
                new Move(position, piece2, "g6"), new Move(position, piece2, "h6"),
                new Move(position, piece2, "b5"), new Move(position, piece2, "b4"),
                new Move(position, piece2, "b3"), move1, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/3P4/8/1P1r2P1/8/8/3P4/8 w - - 0 1");
        Board board = position.getBoard();
        Rook rook = (Rook) board.pieceSearch("d5");
        assertFalse(rook.canCaptureSquare(board, "d8"));
        assertTrue(rook.canCaptureSquare(board, "d7"));
        assertFalse(rook.canCaptureSquare(board, "h5"));
        assertTrue(rook.canCaptureSquare(board, "g5"));
        assertFalse(rook.canCaptureSquare(board, "d1"));
        assertTrue(rook.canCaptureSquare(board, "d2"));
        assertFalse(rook.canCaptureSquare(board, "a5"));
        assertTrue(rook.canCaptureSquare(board, "b5"));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Rook test = (Rook) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }
}
