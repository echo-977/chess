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
            public Move[] generateMoves(Board board) {
                return new Move[0]; //minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, String move) {
                return false; //minimal implementation here to allow testing of concrete methods
            }

            @Override
            public Piece copyToSquare(String square) {
                return null;
            }
        };
    }

    @Test
    @DisplayName("Test directionalMoveSearch")
    public void testDirectionalMoveSearch() {
        Board board = new Board("8/8/8/8/8/8/8/1K6 w - - 0 1");
        Move[] moves = new Move[8];
        int[][] directions = {
                {ChessDirections.NONE, ChessDirections.UP}, {ChessDirections.RIGHT, ChessDirections.UP},
                {ChessDirections.RIGHT, ChessDirections.NONE}, {ChessDirections.RIGHT, ChessDirections.DOWN},
                {ChessDirections.NONE, ChessDirections.DOWN},  {ChessDirections.LEFT, ChessDirections.DOWN},
                {ChessDirections.LEFT, ChessDirections.NONE}, {ChessDirections.LEFT, ChessDirections.UP}
        };
        piece.directionalMoveSearch(board, moves, directions);
        Move[] expectedMoves = {new Move(board, piece, "b2"), new Move(board, piece, "c2"),
                new Move(board, piece, "c1"), new Move(board, piece, "a1"),
                new Move(board, piece, "a2"), null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }
}
