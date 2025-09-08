import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    public Pawn piece1;
    public Pawn piece2;

    @BeforeEach
    public void init() {
        piece1 = new Pawn(PieceColour.WHITE, 'a', 1, false);
        piece2 = new Pawn(PieceColour.BLACK, 'd', 5, false);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove("a2"));
        assertTrue(piece1.isLegalMove("a3"));
        assertTrue(piece2.isLegalMove("d4"));
        assertTrue(piece2.isLegalMove("d3"));
        assertFalse(piece1.isLegalMove("b1"));
        assertFalse(piece2.isLegalMove("c5"));
    }

    @Test
    @DisplayName("Test generateMoves (pawn pushes only)")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/3p4/8/8/8/8/P7/8 w - - 0 1");
        Board board = position.getBoard();
        Piece piece1 = board.pieceSearch("a2");
        Piece piece2 = board.pieceSearch("d7");
        Move[] piece1MovesExpected = {new Move(position, piece1, "a3"), new Move(position, piece1, "a4"),
                null, null, null, null, null, null, null, null, null, null};
        Move[] piece2MovesExpected = {new Move(position, piece2, "d6"), new Move(position, piece2, "d5"),
                null, null, null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (pushes blocked)")
    void testGenerateMovesBlocked() {
        Position position = FENUtils.positionFromFEN("rnbqkb1r/pppppppp/5n2/8/N7/8/PPPPPPPP/R1BQKBNR b KQkq - 3 2");
        Board board = position.getBoard();
        Piece piece1 = board.pieceSearch("a2");
        Piece piece2 = board.pieceSearch("f7");
        Move[] piece1MovesExpected = {new Move(position, piece1, "a3"), null, null, null, null, null, null, null,
                null, null, null, null};
        Move[] piece2MovesExpected = {null, null, null, null, null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (captures)")
    void testGenerateMovesCaptures() {
        Position position = FENUtils.positionFromFEN("rn1qkb1r/p1pp2pp/1p3p2/1b1npNB1/2PP4/5N2/PP2PPPP/R2QKB1R b KQkq - 7 10");
        Board board = position.getBoard();
        Pawn piece1 = (Pawn) board.pieceSearch("c4)");
        Pawn piece2 = (Pawn) board.pieceSearch("f6");
        Move move1 = new Move(position, piece1, "b5");
        move1.setCapture(true);
        Move move2 = new Move(position, piece1, "d5");
        move2.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, "c5"), move1, move2, null, null, null, null,
                null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, "g5");
        move1.setCapture(true);
        Move[] piece2MovesExpected = {move1, null, null, null, null, null, null, null, null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (en passant)")
    void testGenerateMovesEnPassant() {
        Position position = FENUtils.positionFromFEN("rn1qkb1r/pp3p1p/5n2/2pPp3/6p1/2B2P2/PPP1P1PP/RN1QKB1R w KQkq c6 0 10");
        Board board = position.getBoard();
        piece1 = (Pawn) board.pieceSearch("d5");
        piece2 = (Pawn) board.pieceSearch("g4");
        Move move1 = new Move(position, piece1, "d6");
        Move move2 = new Move(position, piece1 ,"c6");
        move2.setEnPassant(true);
        move2.setCapture(true);
        Move[] piece1MovesExpected = {move1, move2, null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, "g3");
        move2 = new Move(position, piece2, "f3");
        move2.setCapture(true);
        Move[] piece2MovesExpected = {move1, move2, null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMovesPromotions")
    void testGenerateMovesPromotions() {
        Position position = FENUtils.positionFromFEN("2r1N3/3P4/8/8/8/8/3p4/2R1R3 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Pawn) board.pieceSearch("d7");
        piece2 = (Pawn) board.pieceSearch("d2");
        String[] destinations = {"d8", "c8"};
        Move[] piece1MovesExpected = new Move[12];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (PieceType type : PieceType.values()) {
                if (piece1.canPromoteTo(type)) {
                    piece1MovesExpected[index] = new Move(position, piece1, destinations[i]);
                    piece1MovesExpected[index].setPromotionType(type);
                    index++;
                }
            }
        }
        for (int i = 4; i < 8; i++) {
            piece1MovesExpected[i].setCapture(true);
        }
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        destinations = new String[] {"d1", "c1", "e1"};
        Move[] piece2MovesExpected = new Move[12];
        index = 0;
        for (int i = 0; i < 3; i++) {
            for (PieceType type : PieceType.values()) {
                if (piece2.canPromoteTo(type)) {
                    piece2MovesExpected[index] = new Move(position, piece2, destinations[i]);
                    piece2MovesExpected[index].setPromotionType(type);
                    index++;
                }
            }
        }
        for (int i = 4; i < 12; i++) {
            piece2MovesExpected[i].setCapture(true);
        }
        Move[] piece2MovesActual = piece2.generateMoves(position);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test moved")
    void testMoved() {
        assertFalse(piece1.getMoved());
        piece1.move("a2");
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/8/8/3p4/8/8/8/PK6 w - - 0 1");
        Board board = position.getBoard();
        assertTrue(piece1.canCaptureSquare(board, "b2"));
        assertFalse(piece1.canCaptureSquare(board, "a2"));
        assertFalse(piece1.canCaptureSquare(board, "b1"));
        assertTrue(piece2.canCaptureSquare(board, "c4"));
        assertTrue(piece2.canCaptureSquare(board, "e4"));
        assertFalse(piece2.canCaptureSquare(board, "d4"));
        assertFalse(piece2.canCaptureSquare(board, "d6"));
        assertFalse(piece2.canCaptureSquare(board, "f3"));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Pawn test = (Pawn) piece1.copyToSquare("d8");
        assertEquals("d8", test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
        assertEquals(piece1.getMoved(), test.getMoved());
    }
}
