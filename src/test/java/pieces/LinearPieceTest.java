import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearPieceTest {
    public LinearPiece piece;

    @BeforeEach
    public void init() {
        piece = new LinearPiece(PieceType.QUEEN, PieceColour.WHITE, Squares.D4) {
            @Override
            public void generateMoves(Position position, IntArrayList moves) {
                // minimal implementation here to allow testing of concrete methods
            }

            @Override
            public boolean canCaptureSquare(Board board, int targetSquare) {
                return false; //minimal implementation here to allow testing of concrete methods
            }

            @Override
            public Piece copyToSquare(int square) {
                return null;
            }
        };
    }

    @Test
    @DisplayName("Test linearMoveSearch without other piece")
    void testLinearMoveSearchNoOtherPieces() {
        Position position = FENUtils.positionFromFEN("8/8/8/8/3Q4/8/8/8 w - - 0 1");
        IntArrayList actualMoves = new IntArrayList(4);
        piece.linearMoveSearch(position, actualMoves, ChessDirections.NONE + ChessDirections.UP);
        int[] expectedMoves = {MoveFlags.QUIET_MOVE | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.D7 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.QUIET_MOVE | Squares.D8 << MoveFlags.DESTINATION_SHIFT | Squares.D4};
        assertArrayEquals(expectedMoves, actualMoves.toIntArray());
    }

    @Test
    @DisplayName("Test linearMoveSearch with other piece")
    void testLinearMoveSearchOtherPieces() {
        Position position = FENUtils.positionFromFEN("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        IntArrayList actualMoves = new IntArrayList(7);
        piece.linearMoveSearch(position, actualMoves, ChessDirections.LEFT + ChessDirections.UP);
        int[] expectedMoves = {MoveFlags.QUIET_MOVE | Squares.C5 <<  MoveFlags.DESTINATION_SHIFT | Squares.D4};
                assertArrayEquals(expectedMoves, actualMoves.toIntArray());
    }

    @Test
    @DisplayName("Test linearMoveSearch with capture")
    void testLinearMoveSearchCapture() {
        Position position = FENUtils.positionFromFEN("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        IntArrayList actualMoves = new IntArrayList(7);
        piece.linearMoveSearch(position, actualMoves, ChessDirections.RIGHT + ChessDirections.UP);
        int[] expectedMoves = {MoveFlags.QUIET_MOVE | Squares.E5 << MoveFlags.DESTINATION_SHIFT | Squares.D4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.F6 << MoveFlags.DESTINATION_SHIFT | Squares.D4};
        assertArrayEquals(expectedMoves, actualMoves.toIntArray());
    }

    @Test
    @DisplayName("Test recursiveCaptureCheck")
    void testRecursiveCaptureCheck() {
        Board board = FENUtils.positionFromFEN("8/8/8/3q2P1/8/8/8/8 w - - 0 1").getBoard();
        LinearPiece piece = (LinearPiece) board.pieceSearch(Squares.D5);
        //targetSquare = "h5";
        assertFalse(piece.recursiveCaptureCheck(board, Squares.G5, ChessDirections.LEFT + ChessDirections.NONE));
        //targetSquare = "g5";
        assertTrue(piece.recursiveCaptureCheck(board, Squares.F5, ChessDirections.LEFT + ChessDirections.NONE));
        //targetSquare = "f5";
        assertTrue(piece.recursiveCaptureCheck(board, Squares.E5, ChessDirections.LEFT + ChessDirections.NONE));
    }

    @Test
    @DisplayName("Test getOrthogonalDirections")
    void testGetOrthogonalDirections() {
        int direction;
        int square = piece.getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int targetFile = SquareMapUtils.getFileContribution(Squares.D6);
        int targetRank = SquareMapUtils.getRankContribution(Squares.D6);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.DOWN, direction);
        targetFile = SquareMapUtils.getFileContribution(Squares.G4);
        targetRank = SquareMapUtils.getRankContribution(Squares.G4);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.LEFT, direction);
        targetFile = SquareMapUtils.getFileContribution(Squares.D1);
        targetRank = SquareMapUtils.getRankContribution(Squares.D1);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.UP, direction);
        targetFile = SquareMapUtils.getFileContribution(Squares.A4);
        targetRank = SquareMapUtils.getRankContribution(Squares.A4);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.RIGHT, direction);
    }

    @Test
    @DisplayName("Test getDiagonalDirections")
    void testGetDiagonalDirections() {
        int direction;
        int square = piece.getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int targetFile = SquareMapUtils.getFileContribution(Squares.G7);
        int targetRank = SquareMapUtils.getRankContribution(Squares.G7);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.LEFT + ChessDirections.DOWN, direction);
        targetFile = SquareMapUtils.getFileContribution(Squares.G1);
        targetRank = SquareMapUtils.getRankContribution(Squares.G1);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.LEFT + ChessDirections.UP, direction);
        targetFile = SquareMapUtils.getFileContribution(Squares.A1);
        targetRank = SquareMapUtils.getRankContribution(Squares.A1);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.RIGHT + ChessDirections.UP, direction);
        targetFile = SquareMapUtils.getFileContribution(Squares.A7);
        targetRank = SquareMapUtils.getRankContribution(Squares.A7);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.RIGHT + ChessDirections.DOWN, direction);
    }
}
