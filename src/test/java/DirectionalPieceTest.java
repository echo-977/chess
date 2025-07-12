import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DirectionalPieceTest {
    public DirectionalPiece piece;

    @BeforeEach
    public void init() {
        piece = new DirectionalPiece("King", "White", 'b', 1) {
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
                {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1},  {-1, -1}, {-1, 0}, {-1, 1}
        };
        piece.directionalMoveSearch(board, moves, directions);
        String[] expectedMoves = {"b2", "c2", "c1", "a1", "a2", null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }
}
