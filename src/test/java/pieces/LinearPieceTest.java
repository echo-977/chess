import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearPieceTest {
    public LinearPiece piece;

    @BeforeEach
    public void init() {
        piece = new LinearPiece(PieceType.QUEEN, PieceColour.WHITE, Files.D + Ranks.FOUR) {
            @Override
            public Move[] generateMoves(Position position) {
                return new Move[8]; //minimal implementation here to allow testing of concrete methods
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
        Move[] moves = new Move[7];
        int movesIndex = 0;
        piece.linearMoveSearch(position, moves, movesIndex, ChessDirections.NONE + ChessDirections.UP); //up
        Move[] expectedMoves = {new Move(position, piece, Files.D + Ranks.FIVE),
                new Move(position, piece, Files.D + Ranks.SIX),
                new Move(position, piece, Files.D + Ranks.SEVEN),
                new Move(position, piece, Files.D + Ranks.EIGHT), null, null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with other piece")
    void testLinearMoveSearchOtherPieces() {
        Position position = FENUtils.positionFromFEN("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        Move[] moves = new Move[7];
        int movesIndex = 0;
        piece.linearMoveSearch(position, moves, movesIndex, ChessDirections.LEFT + ChessDirections.UP); //up left
        Move[] expectedMoves = {new Move(position, piece, Files.C + Ranks.FIVE), null, null, null, null,
                null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with capture")
    void testLinearMoveSearchCapture() {
        Position position = FENUtils.positionFromFEN("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        Move[] moves = new Move[7];
        int movesIndex = 0;
        piece.linearMoveSearch(position, moves, movesIndex, ChessDirections.RIGHT + ChessDirections.UP); //up left
        Move move1 = new Move(position, piece, Files.F + Ranks.SIX);
        move1.setCapture(true);
        Move[] expectedMoves = {new Move(position, piece, Files.E + Ranks.FIVE), move1, null, null, null,
                null, null};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test recursiveCaptureCheck")
    void testRecursiveCaptureCheck() {
        Board board = FENUtils.positionFromFEN("8/8/8/3q2P1/8/8/8/8 w - - 0 1").getBoard();
        LinearPiece piece = (LinearPiece) board.pieceSearch(Files.D + Ranks.FIVE);
        //targetSquare = "h5";
        assertFalse(piece.recursiveCaptureCheck(board, Files.G + Ranks.FIVE,
                ChessDirections.LEFT + ChessDirections.NONE));
        //targetSquare = "g5";
        assertTrue(piece.recursiveCaptureCheck(board, Files.F + Ranks.FIVE,
                ChessDirections.LEFT + ChessDirections.NONE));
        //targetSquare = "f5";
        assertTrue(piece.recursiveCaptureCheck(board, Files.E + Ranks.FIVE,
                ChessDirections.LEFT + ChessDirections.NONE));
    }

    @Test
    @DisplayName("Test getOrthogonalDirections")
    void testGetOrthogonalDirections() {
        int direction;
        int square = piece.getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int targetFile = SquareMapUtils.getFileContribution(Files.D + Ranks.SIX);
        int targetRank = SquareMapUtils.getRankContribution(Files.D + Ranks.SIX);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.DOWN, direction);
        targetFile = SquareMapUtils.getFileContribution(Files.G + Ranks.FOUR);
        targetRank = SquareMapUtils.getRankContribution(Files.G + Ranks.FOUR);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.LEFT, direction);
        targetFile = SquareMapUtils.getFileContribution(Files.D + Ranks.ONE);
        targetRank = SquareMapUtils.getRankContribution(Files.D + Ranks.ONE);
        direction = piece.getOrthogonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.UP, direction);
        targetFile = SquareMapUtils.getFileContribution(Files.A + Ranks.FOUR);
        targetRank = SquareMapUtils.getRankContribution(Files.A + Ranks.FOUR);
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
        int targetFile = SquareMapUtils.getFileContribution(Files.G + Ranks.SEVEN);
        int targetRank = SquareMapUtils.getRankContribution(Files.G + Ranks.SEVEN);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.LEFT + ChessDirections.DOWN, direction);
        targetFile = SquareMapUtils.getFileContribution(Files.G + Ranks.ONE);
        targetRank = SquareMapUtils.getRankContribution(Files.G + Ranks.ONE);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.LEFT + ChessDirections.UP, direction);
        targetFile = SquareMapUtils.getFileContribution(Files.A + Ranks.ONE);
        targetRank = SquareMapUtils.getRankContribution(Files.A + Ranks.ONE);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.RIGHT + ChessDirections.UP, direction);
        targetFile = SquareMapUtils.getFileContribution(Files.A + Ranks.SEVEN);
        targetRank = SquareMapUtils.getRankContribution(Files.A + Ranks.SEVEN);
        direction = piece.getDiagonalDirection(squareFile, squareRank, targetFile, targetRank);
        assertEquals(ChessDirections.RIGHT + ChessDirections.DOWN, direction);
    }
}
