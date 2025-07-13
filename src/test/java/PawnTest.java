import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnTest {
    public Pawn piece1;
    public Pawn piece2;

    @BeforeEach
    public void init() {
        piece1 = new Pawn(ChessConstants.WHITE, 'a', 1, false, false);
        piece2 = new Pawn(ChessConstants.BLACK, 'd', 5, false, false);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a2"));
        assertTrue(piece1.isLegalMove("a3"));
        assertTrue(piece2.isLegalMove("d4"));
        assertTrue(piece2.isLegalMove("d3"));
        assertFalse(piece1.isLegalMove("b1"));
        assertFalse(piece2.isLegalMove("c5"));
    }

    @Test
    @DisplayName("Test generateMoves (pawn pushes only)")
    void testGenerateMoves() {
        Board board = new Board("8/8/8/3p4/8/8/8/P7 w - - 0 1");
        String[] piece1MovesExpected = {"a2", "a3", null, null};
        String[] piece2MovesExpected = {"d4", "d3", null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (pushes blocked)")
    void testGenerateMovesBlocked() {
        Board board = new Board("rnbqkb1r/pppppppp/5n2/8/N7/8/PPPPPPPP/R1BQKBNR b KQkq - 3 2");
        piece1 = (Pawn) board.getWhitePieces()[1];
        piece2 = (Pawn) board.getBlackPieces()[12];
        String[] piece1MovesExpected = {"a3", null, null, null};
        String[] piece2MovesExpected = {null, null, null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (captures)")
    void testGenerateMovesCaptures() {
        Board board = new Board("rn1qkb1r/p1pp2pp/1p3p2/1b1npNB1/2PP4/5N2/PP2PPPP/R2QKB1R b KQkq - 7 10");
        piece1 = (Pawn) board.getWhitePieces()[2];
        piece2 = (Pawn) board.getBlackPieces()[12];
        String[] piece1MovesExpected = {"c5", "b5", "d5", null};
        String[] piece2MovesExpected = {"g5", null, null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (en passant)")
    void testGenerateMovesEnPassant() {
        Board board = new Board("rn1qkb1r/pp3p1p/5n2/2pPp3/6p1/2B2P2/PPP1P1PP/RN1QKB1R w KQkq c6 0 10");
        piece1 = (Pawn) board.getWhitePieces()[0];
        piece2 = (Pawn) board.getBlackPieces()[13];
        String[] piece1MovesExpected = {"d6", "c6", null, null};
        String[] piece2MovesExpected = {"g3", "f3", null, null};
        String[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        String[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test moved")
    void testMoved() {
        assertFalse(piece1.getMoved());
        piece1.move("a2");
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test enPassantable")
    void testEnPassantable() {
        assertFalse(piece1.getEnPassantable());
        piece1.move("a3");
        assertTrue(piece1.getEnPassantable());
        piece1.move("a4");
        assertFalse(piece1.getEnPassantable());
    }
}
