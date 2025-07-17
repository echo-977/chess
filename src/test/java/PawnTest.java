import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PawnTest {
    public Pawn piece1;
    public Pawn piece2;

    @BeforeEach
    public void init() {
        piece1 = new Pawn(PieceColour.WHITE, 'a', 1, false, false);
        piece2 = new Pawn(PieceColour.BLACK, 'd', 5, false, false);
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
        Board board = new Board("8/8/8/3p4/8/8/8/P7 w - - 0 1");
        Move[] piece1MovesExpected = {new Move(piece1, "a2"), new Move(piece1, "a3"), null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece2MovesExpected = {new Move(piece2, "d4"), new Move(piece2, "d3"), null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (pushes blocked)")
    void testGenerateMovesBlocked() {
        Board board = new Board("rnbqkb1r/pppppppp/5n2/8/N7/8/PPPPPPPP/R1BQKBNR b KQkq - 3 2");
        piece1 = (Pawn) board.getWhitePieces()[1];
        piece2 = (Pawn) board.getBlackPieces()[12];
        Move[] piece1MovesExpected = {new Move(piece1, "a3"), null, null, null, null, null, null, null, null,
                null, null, null};
        Move[] piece2MovesExpected = {null, null, null, null, null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (captures)")
    void testGenerateMovesCaptures() {
        Board board = new Board("rn1qkb1r/p1pp2pp/1p3p2/1b1npNB1/2PP4/5N2/PP2PPPP/R2QKB1R b KQkq - 7 10");
        piece1 = (Pawn) board.getWhitePieces()[2];
        piece2 = (Pawn) board.getBlackPieces()[12];
        Move[] piece1MovesExpected = {new Move(piece1, "c5"), new Move(piece1, "b5"),
                new Move(piece1, "d5"), null, null, null, null, null, null, null, null, null};
        Move[] piece2MovesExpected = {new Move(piece2, "g5"), null, null, null, null, null, null, null, null,
                null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMoves (en passant)")
    void testGenerateMovesEnPassant() {
        Board board = new Board("rn1qkb1r/pp3p1p/5n2/2pPp3/6p1/2B2P2/PPP1P1PP/RN1QKB1R w KQkq c6 0 10");
        piece1 = (Pawn) board.getWhitePieces()[0];
        piece2 = (Pawn) board.getBlackPieces()[13];
        Move move1 = new Move(piece1, "d6");
        Move move2 = new Move(piece1 ,"c6");
        move2.setEnPassant(true);
        move2.setCapture(true);
        Move[] piece1MovesExpected = {move1, move2, null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        move1 = new Move(piece2, "g3");
        move2 = new Move(piece2, "f3");
        Move[] piece2MovesExpected = {move1, move2, null, null,
                null, null, null, null, null, null, null, null};
        Move[] piece2MovesActual = piece2.generateMoves(board);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual);
    }

    @Test
    @DisplayName("Test generateMovesPromotions")
    void testGenerateMovesPromotions() {
        Board board = new Board("2r1N3/3P4/8/8/8/8/3p4/2R1R3 w - - 0 1");
        piece1 = (Pawn) board.getWhitePieces()[1];
        piece2 = (Pawn) board.getBlackPieces()[1];
        String[] destinations = {"d8", "c8"};
        Move[] piece1MovesExpected = new Move[12];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (PieceType type : PieceType.values()) {
                if (piece1.canPromoteTo(type)) {
                    piece1MovesExpected[index] = new Move(piece1, destinations[i]);
                    piece1MovesExpected[index].setPromotionType(type);
                    index++;
                }
            }
        }
        Move[] piece1MovesActual = piece1.generateMoves(board);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual);
        destinations = new String[] {"d1", "c1", "e1"};
        Move[] piece2MovesExpected = new Move[12];
        index = 0;
        for (int i = 0; i < 3; i++) {
            for (PieceType type : PieceType.values()) {
                if (piece2.canPromoteTo(type)) {
                    piece2MovesExpected[index] = new Move(piece2, destinations[i]);
                    piece2MovesExpected[index].setPromotionType(type);
                    index++;
                }
            }
        }
        Move[] piece2MovesActual = piece2.generateMoves(board);
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
    @DisplayName("Test enPassantable")
    void testEnPassantable() {
        assertFalse(piece1.getEnPassantable());
        piece1.move("a3");
        assertTrue(piece1.getEnPassantable());
        piece1.move("a4");
        assertFalse(piece1.getEnPassantable());
    }

    @Test
    @DisplayName("Test canCaptureKing")
    void testCanCaptureKing() {
        Board board = new Board("8/8/8/3p4/8/8/8/PK6 w - - 0 1");
        assertTrue(piece1.canCaptureKing(board, "b2"));
        assertFalse(piece1.canCaptureKing(board, "a2"));
        assertFalse(piece1.canCaptureKing(board, "b1"));
        assertTrue(piece2.canCaptureKing(board, "c4"));
        assertTrue(piece2.canCaptureKing(board, "e4"));
        assertFalse(piece2.canCaptureKing(board, "d4"));
        assertFalse(piece2.canCaptureKing(board, "d6"));
        assertFalse(piece2.canCaptureKing(board, "f3"));
    }
}
