import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    public Pawn piece1;
    public Pawn piece2;

    @BeforeEach
    public void init() {
        piece1 = new Pawn(PieceColour.WHITE, Files.A + Ranks.ONE, false);
        piece2 = new Pawn(PieceColour.BLACK, Files.D + Ranks.FIVE, false);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        assertTrue(piece1.isLegalMove(Files.A + Ranks.TWO));
        assertTrue(piece1.isLegalMove(Files.A + Ranks.TWO));
        assertTrue(piece2.isLegalMove(Files.D + Ranks.FOUR));
        assertTrue(piece2.isLegalMove(Files.D + Ranks.THREE));
        assertFalse(piece1.isLegalMove(Files.B + Ranks.ONE));
        assertFalse(piece2.isLegalMove(Files.C + Ranks.FIVE));
    }

    @Test
    @DisplayName("Test generateMoves (pawn pushes only)")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/3p4/8/8/8/8/P7/8 w - - 0 1");
        Board board = position.getBoard();
        Piece piece1 = board.pieceSearch(Files.A + Ranks.TWO);
        Piece piece2 = board.pieceSearch(Files.D + Ranks.SEVEN);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.A + Ranks.THREE),
                new Move(position, piece1, Files.A + Ranks.FOUR),
                null, null, null, null, null, null, null, null, null, null};
        Move[] piece2MovesExpected = {new Move(position, piece2, Files.D + Ranks.SIX),
                new Move(position, piece2, Files.D + Ranks.FIVE),
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
        Piece piece1 = board.pieceSearch(Files.A + Ranks.TWO);
        Piece piece2 = board.pieceSearch(Files.F + Ranks.SEVEN);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.A + Ranks.THREE),
                null, null, null, null, null, null, null, null, null, null, null};
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
        Pawn piece1 = (Pawn) board.pieceSearch(Files.C + Ranks.FOUR);
        Pawn piece2 = (Pawn) board.pieceSearch(Files.F + Ranks.SIX);
        Move move1 = new Move(position, piece1, Files.B + Ranks.FIVE);
        move1.setCapture(true);
        Move move2 = new Move(position, piece1, Files.D + Ranks.FIVE);
        move2.setCapture(true);
        Move[] piece1MovesExpected = {new Move(position, piece1, Files.C + Ranks.FIVE), move1, move2, null,
                null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.G + Ranks.FIVE);
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
        piece1 = (Pawn) board.pieceSearch(Files.D + Ranks.FIVE);
        piece2 = (Pawn) board.pieceSearch(Files.G + Ranks.FOUR);
        Move move1 = new Move(position, piece1, Files.D + Ranks.SIX);
        Move move2 = new Move(position, piece1 ,Files.C + Ranks.SIX);
        move2.setEnPassant(true);
        move2.setCapture(true);
        Move[] piece1MovesExpected = {move1, move2, null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(position);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(position, piece2, Files.G + Ranks.THREE);
        move2 = new Move(position, piece2, Files.F + Ranks.THREE);
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
        piece1 = (Pawn) board.pieceSearch(Files.D + Ranks.SEVEN);
        piece2 = (Pawn) board.pieceSearch(Files.D + Ranks.TWO);
        int[] destinations = {Files.D + Ranks.EIGHT, Files.C + Ranks.EIGHT};
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
        destinations = new int[] {Files.D + Ranks.ONE, Files.C + Ranks.ONE, Files.E + Ranks.ONE};
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
        piece1.move(Files.A + Ranks.TWO);
        assertTrue(piece1.getMoved());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/8/8/3p4/8/8/8/PK6 w - - 0 1");
        Board board = position.getBoard();
        assertTrue(piece1.canCaptureSquare(board, Files.B + Ranks.TWO));
        assertFalse(piece1.canCaptureSquare(board, Files.A + Ranks.TWO));
        assertFalse(piece1.canCaptureSquare(board, Files.B + Ranks.ONE));
        assertTrue(piece2.canCaptureSquare(board, Files.C + Ranks.FOUR));
        assertTrue(piece2.canCaptureSquare(board, Files.E + Ranks.FOUR));
        assertFalse(piece2.canCaptureSquare(board, Files.D + Ranks.FOUR));
        assertFalse(piece2.canCaptureSquare(board, Files.D + Ranks.SIX));
        assertFalse(piece2.canCaptureSquare(board, Files.F + Ranks.THREE));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Pawn test = (Pawn) piece1.copyToSquare(Files.D + Ranks.EIGHT);
        assertEquals(Files.D + Ranks.EIGHT, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
        assertEquals(piece1.getMoved(), test.getMoved());
    }
}
