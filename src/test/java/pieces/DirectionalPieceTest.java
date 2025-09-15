import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DirectionalPieceTest {
    public DirectionalPiece piece;

    @BeforeEach
    public void init() {
        piece = new Knight(PieceColour.WHITE, Squares.B1);
    }

    @Test
    @DisplayName("Test directionalMoveSearch")
    public void testDirectionalMoveSearch() {
        Position position = FENUtils.positionFromFEN("8/8/2K2k2/8/8/8/8/1N6 w - - 0 1");
        int[] moves = new int[8];
        int[] directions = {
                ChessDirections.RIGHT + 2 * ChessDirections.UP, 2 * ChessDirections.RIGHT + ChessDirections.UP,
                2 * ChessDirections.RIGHT + ChessDirections.DOWN, ChessDirections.RIGHT + 2 * ChessDirections.DOWN,
                ChessDirections.LEFT + 2 * ChessDirections.DOWN, 2 * ChessDirections.LEFT + ChessDirections.DOWN,
                2 * ChessDirections.LEFT + ChessDirections.UP, ChessDirections.LEFT + 2 * ChessDirections.UP
        };
        piece.directionalMoveSearch(position, moves, directions);
        int[] expectedMoves = {MoveFlags.QUIET_MOVE | Squares.C3 << MoveFlags.DESTINATION_SHIFT | Squares.B1,
                MoveFlags.QUIET_MOVE | Squares.D2 << MoveFlags.DESTINATION_SHIFT | Squares.B1,
                MoveFlags.QUIET_MOVE | Squares.A3 << MoveFlags.DESTINATION_SHIFT | Squares.B1,
                MoveFlags.NO_MOVE , MoveFlags.NO_MOVE, MoveFlags.NO_MOVE, MoveFlags.NO_MOVE, MoveFlags.NO_MOVE};
        assertArrayEquals(expectedMoves, moves);
    }
}
