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
            public Move[] generateMoves(Position position) {
                return new Move[8]; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, int square) {
                return false; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public Piece copyToSquare(int square) {
                return null;
            }
        };
    }

    @Test
    @DisplayName("Test getSquare()")
    void getSquareTest() {
        piece = new Piece(PieceType.PAWN, PieceColour.WHITE, Files.A + Ranks.TWO) {
            @Override
            public Move[] generateMoves(Position position) {
                return new Move[8]; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, int square) {
                return false; // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public Piece copyToSquare(int square) {
                return null;
            }
        };
        assertEquals(Files.A + Ranks.TWO, piece.getSquare());
    }

    @Test
    @DisplayName("Test move")
    void moveTest() {
        piece.move(Files.B + Ranks.EIGHT);
        assertEquals(Files.B + Ranks.EIGHT, piece.getSquare());
    }

    @Test
    @DisplayName("Test  isLegalMove")
    void isLegalMoveTest() {
        assertFalse(piece.isLegalMove(64));
        assertFalse(piece.isLegalMove(78));
        assertFalse(piece.isLegalMove(-1));
        assertFalse(piece.isLegalMove(-15));
        assertTrue(piece.isLegalMove(Files.E + Ranks.SEVEN));
        assertTrue(piece.isLegalMove(Files.F + Ranks.TWO));
        assertTrue(piece.isLegalMove(Files.H + Ranks.ONE));
        assertTrue(piece.isLegalMove(Files.A + Ranks.EIGHT));
    }
}
