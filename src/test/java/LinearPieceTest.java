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
            public Move[] generateMoves(Board board) {
                return new Move[8]; //minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, String targetSquare) {
                return false; //minimal implementation here to allow testing of concrete methods
            }

            @Override
            public Piece copyToSquare(String square) {
                return null;
            }
        };
    }

    @Test
    @DisplayName("Test linearMoveSearch without other piece")
    void testLinearMoveSearchNoOtherPieces() {
        Board board = new Board("8/8/8/8/3Q4/8/8/8 w - - 0 1");
        Move[] moves = new Move[7];
        int movesIndex = 0;
        piece.linearMoveSearch(board, moves, movesIndex, ChessDirections.NONE, ChessDirections.UP); //up
        Move[] expectedMoves = {new Move(board, piece, "d5"), new Move(board, piece, "d6"),
                new Move(board, piece, "d7"), new Move(board, piece, "d8"), null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with other piece")
    void testLinearMoveSearchOtherPieces() {
        Board board = new Board("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        Move[] moves = new Move[7];
        int movesIndex = 0;
        piece.linearMoveSearch(board, moves, movesIndex, ChessDirections.LEFT, ChessDirections.UP); //up left
        Move[] expectedMoves = {new Move(board, piece, "c5"), null, null, null, null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with capture")
    void testLinearMoveSearchCapture() {
        Board board = new Board("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        Move[] moves = new Move[7];
        int movesIndex = 0;
        piece.linearMoveSearch(board, moves, movesIndex, ChessDirections.RIGHT, ChessDirections.UP); //up left
        Move move1 = new Move(board, piece, "f6");
        move1.setCapture(true);
        Move[] expectedMoves = {new Move(board, piece, "e5"), move1, null, null, null,
                null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test recursiveCaptureCheck")
    void testRecursiveCaptureCheck() {
        Board board = new Board("8/8/8/3q2P1/8/8/8/8 w - - 0 1");
        LinearPiece piece = (LinearPiece) board.getBlackPieces()[0];
        String targetSquare;
        //targetSquare = "h5";
        assertFalse(piece.recursiveCaptureCheck(board, 'g', 5, ChessDirections.LEFT, ChessDirections.NONE));
        //targetSquare = "g5";
        assertTrue(piece.recursiveCaptureCheck(board, 'f', 5, ChessDirections.LEFT, ChessDirections.NONE));
        //targetSquare = "f5";
        assertTrue(piece.recursiveCaptureCheck(board, 'e', 5, ChessDirections.LEFT, ChessDirections.NONE));
    }

    @Test
    @DisplayName("Test getOrthogonalDirections")
    void testGetOrthogonalDirections() {
        int[] directions;
        directions = piece.getOrthogonalDirections('d', 6);
        assertEquals(ChessDirections.NONE, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.DOWN, directions[ChessConstants.RANK_DIRECTION_INDEX]);
        directions = piece.getOrthogonalDirections('g', 4);
        assertEquals(ChessDirections.LEFT, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.NONE, directions[ChessConstants.RANK_DIRECTION_INDEX]);
        directions = piece.getOrthogonalDirections('d', 1);
        assertEquals(ChessDirections.NONE, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.UP, directions[ChessConstants.RANK_DIRECTION_INDEX]);
        directions = piece.getOrthogonalDirections('a', 4);
        assertEquals(ChessDirections.RIGHT, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.NONE, directions[ChessConstants.RANK_DIRECTION_INDEX]);
    }

    @Test
    @DisplayName("Test getDiagonalDirections")
    void testGetDiagonalDirections() {
        int[] directions;
        directions = piece.getDiagonalDirections('g', 7);
        assertEquals(ChessDirections.LEFT, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.DOWN, directions[ChessConstants.RANK_DIRECTION_INDEX]);
        directions = piece.getDiagonalDirections('g', 1);
        assertEquals(ChessDirections.LEFT, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.UP, directions[ChessConstants.RANK_DIRECTION_INDEX]);
        directions = piece.getDiagonalDirections('a', 1);
        assertEquals(ChessDirections.RIGHT, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.UP, directions[ChessConstants.RANK_DIRECTION_INDEX]);
        directions = piece.getDiagonalDirections('a', 7);
        assertEquals(ChessDirections.RIGHT, directions[ChessConstants.FILE_DIRECTION_INDEX]);
        assertEquals(ChessDirections.DOWN, directions[ChessConstants.RANK_DIRECTION_INDEX]);
    }
}
