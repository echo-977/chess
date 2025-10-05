import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    static Piece piece;

    @BeforeAll
    public static void init() {
        piece = new Piece(PieceType.PAWN, PieceColour.WHITE, Files.A + Ranks.TWO) {
            @Override
            public void generateMoves(Position position, IntArrayList moves) {
                // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, int square) {
                return false;
            }

            @Override
            public Piece copyToSquare(int square) {
                return null;
            }

            @Override
            public long getAttackMask(Board board) {
                return 0L;
            }
        };
    }

    @Test
    @DisplayName("Test getSquare()")
    void getSquareTest() {
        piece = new Piece(PieceType.PAWN, PieceColour.WHITE, Squares.A2) {
            @Override
            public void generateMoves(Position position, IntArrayList moves) {
                // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, int square) {
                return false; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public Piece copyToSquare(int square) {
                return null;
            }

            @Override
            public long getAttackMask(Board board) {
                return 0L;
            }
        };
        assertEquals(Squares.A2, piece.getSquare());
    }

    @Test
    @DisplayName("Test move")
    void moveTest() {
        piece.move(Squares.B8);
        assertEquals(Squares.B8, piece.getSquare());
    }

    @Test
    @DisplayName("Test  isLegalMove")
    void isLegalMoveTest() {
        assertFalse(piece.isLegalMove(64));
        assertFalse(piece.isLegalMove(78));
        assertFalse(piece.isLegalMove(-1));
        assertFalse(piece.isLegalMove(-15));
        assertTrue(piece.isLegalMove(Squares.E7));
        assertTrue(piece.isLegalMove(Squares.F2));
        assertTrue(piece.isLegalMove(Squares.H1));
        assertTrue(piece.isLegalMove(Squares.A8));
    }
}
