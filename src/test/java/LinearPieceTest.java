import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearPieceTest {
    public LinearPiece piece;

    @BeforeEach
    public void init() {
        piece = new LinearPiece(PieceType.QUEEN, PieceColour.WHITE, 'd', 4) {
            @Override
            public String[] generateMoves(Board board) {
                // minimal implementation here to allow testing of concrete functions
                return new String[8];
            }
        };
    }

    @Test
    @DisplayName("Test linearMoveSearch without other piece")
    void testLinearMoveSearchNoOtherPieces() {
        Board board = new Board("8/8/8/8/3Q4/8/8/8 w - - 0 1");
        String[] moves = new String[7];
        int movesIndex = 0;
        piece.linearMoveSearch(board, moves, movesIndex, ChessDirections.NONE, ChessDirections.UP); //up
        String [] expectedMoves = {"d5", "d6", "d7", "d8", null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with other piece")
    void testLinearMoveSearchOtherPieces() {
        Board board = new Board("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        String[] moves = new String[7];
        int movesIndex = 0;
        piece.linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.UP); //up left
        String [] expectedMoves = {"c5", null, null, null, null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with capture")
    void testLinearMoveSearchCapture() {
        Board board = new Board("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        String[] moves = new String[7];
        int movesIndex = 0;
        piece.linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.UP); //up left
        String [] expectedMoves = {"e5", "f6", null, null, null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }


}
