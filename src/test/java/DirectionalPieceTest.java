import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DirectionalPieceTest {
    public DirectionalPiece piece;

    @BeforeEach
    public void init() {
        piece = new DirectionalPiece(PieceType.KING, PieceColour.WHITE, 'b', 1) {
            @Override
            public String[] generateMoves(Board board) {
                return new String[0];
            }
        };
    }

    @Test
    @DisplayName("Test directionalMoveSearch")
    public void testDirectionalMoveSearch() {
        Board board = new Board("8/8/8/8/8/8/8/1K6 w - - 0 1");
        String[] moves = new String[8];
        int[][] directions = {
                {ChessDirections.NONE, ChessDirections.UP}, {ChessDirections.RIGHT, ChessDirections.UP},
                {ChessDirections.RIGHT, ChessDirections.NONE}, {ChessDirections.RIGHT, ChessDirections.DOWN},
                {ChessDirections.NONE, ChessDirections.DOWN},  {ChessDirections.LEFT, ChessDirections.DOWN},
                {ChessDirections.LEFT, ChessDirections.NONE}, {ChessDirections.LEFT, ChessDirections.UP}
        };
        piece.directionalMoveSearch(board, moves, directions);
        String[] expectedMoves = {"b2", "c2", "c1", "a1", "a2", null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }
}
