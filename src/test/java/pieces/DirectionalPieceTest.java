import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DirectionalPieceTest {
    public DirectionalPiece piece;

    @BeforeEach
    public void init() {
        piece = new Knight(PieceColour.WHITE, 'b', 1);
    }

    @Test
    @DisplayName("Test directionalMoveSearch")
    public void testDirectionalMoveSearch() {
        Position position = FENUtils.positionFromFEN("8/8/2K2k2/8/8/8/8/1N6 w - - 0 1");
        Move[] moves = new Move[8];
        int[][] directions = {
                {ChessDirections.RIGHT, 2 * ChessDirections.UP}, {2 * ChessDirections.RIGHT, ChessDirections.UP},
                {2 * ChessDirections.RIGHT, ChessDirections.DOWN}, {ChessDirections.RIGHT, 2 * ChessDirections.DOWN},
                {ChessDirections.LEFT, 2 * ChessDirections.DOWN}, {2 * ChessDirections.LEFT, ChessDirections.DOWN},
                {2 * ChessDirections.LEFT, ChessDirections.UP}, {ChessDirections.LEFT, 2 * ChessDirections.UP}
        };

        piece.directionalMoveSearch(position, moves, directions);
        Move[] expectedMoves = {new Move(position, piece, "c3"), new Move(position, piece, "d2"),
                new Move(position, piece, "a3"), null , null, null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }
}
