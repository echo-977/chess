import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class KnightTest {
    static Knight piece1;
    static Knight piece2;

    @BeforeEach
    public void init() {
        piece1 = new Knight(ChessConstants.WHITE, 'a', 1);
        piece2 = new Knight(ChessConstants.BLACK, 'd', 5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("c2"));
        assertTrue(piece2.isLegalMove("e3"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Board board = new Board("8/8/8/3n4/8/8/8/N7 w - - 0 1");
        piece1 = (Knight) board.getWhitePieces()[0];
        piece2 = (Knight) board.getBlackPieces()[0];
        String[] piece1MovesExpected = {"b3", "c2", null, null, null, null, null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesExpected = {"e7", "f6", "f4", "e3", "c3", "b4", "b6", "c7"};
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("8/8/8/3n1P2/1p4Q1/4N3/8/8 w - - 0 1");
        piece1 = (Knight) board.getWhitePieces()[2];
        piece2 = (Knight) board.getBlackPieces()[0];
        String[] piece1MovesExpected = {"g2", "f1", "d1", "c2", "c4", "d5", null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesExpected = {"e7", "f6", "f4", "e3", "c3", "b6", "c7", null};
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
