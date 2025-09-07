import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KingTest {
    static King piece1;
    static King piece2;

    @BeforeEach
    public void init() {
        piece1 = new King(PieceColour.WHITE, 'a', 1, false);
        piece2 = new King(PieceColour.WHITE, 'd', 5, true);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a2"));
        assertTrue(piece2.isLegalMove("c6"));
        assertFalse(piece1.isLegalMove("b6"));
        assertFalse(piece2.isLegalMove("a3"));
    }

    @Test
    @DisplayName("Test generateMoves without other pieces")
    void testGenerateMoves() {
        Board board = FENUtils.boardFromFEN("8/8/5k2/8/2K5/8/8/8 w - - 0 1");
        piece1 = (King) board.pieceSearch("c4");
        piece2 = (King) board.pieceSearch("f6");
        Move[] piece1MovesExpected = {new Move(board, piece1, "c5"), new Move(board, piece1, "d5"),
                new Move(board, piece1, "d4"), new Move(board, piece1, "d3"),
                new Move(board, piece1, "c3"), new Move(board, piece1, "b3"),
                new Move(board, piece1, "b4"), new Move(board, piece1, "b5")};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesExpected = {new Move(board, piece2, "f7"), new Move(board, piece2, "g7"),
                new Move(board, piece2, "g6"), new Move(board, piece2, "g5"),
                new Move(board, piece2, "f5"), new Move(board, piece2, "e5"),
                new Move(board, piece2, "e6"), new Move(board, piece2, "e7")};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves with other pieces")
    void testGenerateMovesWithOtherPieces() {
        Board board = FENUtils.boardFromFEN("8/6p1/4PkR1/3r4/2K2p2/2P5/8/8 b - - 0 1");
        piece1 = (King) board.pieceSearch("c4");
        piece2 = (King) board.pieceSearch("f6");
        Move move1 = new Move(board, piece1, "d5");
        move1.setCapture(true);
        Move[] piece1MovesExpected = {move1, new Move(board, piece1, "b3"),
                new Move(board, piece1, "b4"), null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(board, piece2, "g6");
        move1.setCapture(true);
        Move[] piece2MovesExpected = {move1, new Move(board, piece2, "f5"),
                new Move(board, piece2, "e5"), new Move(board, piece2, "e7"), null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test isCheck")
    void testIsCheck() {
        assertFalse(piece1.isCheck());
        assertTrue(piece2.isCheck());
    }

    @Test
    @DisplayName("Test setCheck")
    void testSetCheck() {
        piece1.setCheck(true);
        assertTrue(piece1.isCheck());
        piece2.setCheck(false);
        assertFalse(piece2.isCheck());
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        King test = (King) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
        assertEquals(piece1.isCheck(), test.isCheck());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Board board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kq - 0 1");
        assertTrue(piece2.canCaptureSquare(board, "d6"));
        assertTrue(piece2.canCaptureSquare(board, "e6"));
        assertTrue(piece2.canCaptureSquare(board, "e5"));
        assertTrue(piece2.canCaptureSquare(board, "e4"));
        assertTrue(piece2.canCaptureSquare(board, "d4"));
        assertTrue(piece2.canCaptureSquare(board, "c4"));
        assertTrue(piece2.canCaptureSquare(board, "c5"));
        assertTrue(piece2.canCaptureSquare(board, "c6"));
    }

    @Test
    @DisplayName("Test findNextIndex")
    void testFindNextIndex() {
        Board board = FENUtils.boardFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Move[] moves = new Move[8];
        moves[0] = new Move(board, board.pieceSearch("a2"), "a3");
        King king = board.findKing(PieceColour.WHITE);
        assertEquals(1, king.findNextIndex(moves));
    }

    @Test
    @DisplayName("Test generateMoves with castling")
    void testGenerateMovesWithCastling() {
        Board board = FENUtils.boardFromFEN("r3k2r/8/5R2/8/8/8/2b5/R3K2R w KQkq - 0 1");
        King whiteKing = board.findKing(PieceColour.WHITE);
        Move[] expectedMovesWhite = {new Move(board, whiteKing, "e2"),
                new Move(board, whiteKing, "f2"), new Move(board, whiteKing, "f1"),
                new Move(board, whiteKing, "d2"), new Move(board, whiteKing, "g1"),
                null, null, null};
        expectedMovesWhite[4].setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
        assertArrayEquals(expectedMovesWhite, whiteKing.generateMoves(board));
        King blackKing = board.findKing(PieceColour.BLACK);
        Move[] expectedMovesBlack = {new Move(board, blackKing, "e7"),
                new Move(board, blackKing, "d7"), new Move(board, blackKing, "d8"),
                new Move(board, blackKing, "c8"), null, null,null, null};
        expectedMovesBlack[3].setCastleMask(FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
        assertArrayEquals(expectedMovesBlack, blackKing.generateMoves(board));
    }

}
