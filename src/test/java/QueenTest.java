import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class QueenTest {
    static Queen piece1;
    static Queen piece2;

    @BeforeEach
    public void init() {
        piece1 = new Queen(ChessConstants.WHITE, 'a', 1);
        piece2 = new Queen(ChessConstants.BLACK, 'd', 5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("b2"));
        assertTrue(piece2.isLegalMove("e4"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
        assertTrue(piece1.isLegalMove("a7"));
        assertTrue(piece2.isLegalMove("h5"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("f8"));
    }

    @Test
    @DisplayName("Test generateMoves without pieces")
    void testGenerateMoves() {
        Board board = new Board("8/8/8/4q3/2Q5/8/8/8 w - - 0 1");
        piece1 = (Queen) board.getWhitePieces()[0];
        piece2 = (Queen) board.getBlackPieces()[0];
        String[] piece1MovesExpected = {"c5", "c6", "c7", "c8", "d5", "e6", "f7", "g8", "d4", "e4", "f4", "g4",
                "h4", "d3", "e2", "f1", "c3", "c2", "c1", "b3", "a2", "b4", "a4", "b5", "a6", null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesExpected = {"e6", "e7", "e8", "f6", "g7", "h8", "f5", "g5", "h5", "f4", "g3", "h2",
                "e4", "e3", "e2", "e1", "d4", "c3", "b2", "a1", "d5", "c5", "b5", "a5", "d6", "c7", "b8"};
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with pieces")
    void testGenerateMovesWithPieces() {
        Board board = new Board("8/2P5/b7/4q3/2Q2P2/8/4p3/8 w - - 0 1");
        piece1 = (Queen) board.getWhitePieces()[1];
        piece2 = (Queen) board.getBlackPieces()[1];
        String[] piece1MovesExpected = {"c5", "c6", "d5", "e6", "f7", "g8", "d4", "e4", "d3", "e2", "c3", "c2",
                "c1", "b3", "a2", "b4", "a4", "b5", "a6", null, null, null, null, null, null, null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesExpected = {"e6", "e7", "e8", "f6", "g7", "h8", "f5", "g5", "h5", "f4", "e4", "e3",
                "d4", "c3", "b2", "a1", "d5", "c5", "b5", "a5", "d6", "c7", null, null, null, null, null};
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }
}
