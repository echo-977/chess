import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    public Pawn piece1;
    public Pawn piece2;

    @BeforeEach
    public void init() {
        piece1 = new Pawn(PieceColour.WHITE, Squares.A1);
        piece2 = new Pawn(PieceColour.BLACK, Squares.D5);
    }

    @Test
    @DisplayName("Test isLegalMove")
    void testLegalMove() {
        piece1 = new Pawn(PieceColour.WHITE, Squares.A2);
        assertTrue(piece1.isLegalMove(Squares.A4));
        assertTrue(piece1.isLegalMove(Squares.A3));
        assertFalse(piece1.isLegalMove(Squares.A1));
        assertTrue(piece2.isLegalMove(Squares.D4));
        assertFalse(piece2.isLegalMove(Squares.C5));
    }

    @Test
    @DisplayName("Test generateMoves (pawn pushes only)")
    void testGenerateMoves() {
        Position position = FENUtils.positionFromFEN("8/3p4/8/8/8/8/P7/8 w - - 0 1");
        Board board = position.getBoard();
        Piece piece1 = board.pieceSearch(Squares.A2);
        Piece piece2 = board.pieceSearch(Squares.D7);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.A3 << MoveFlags.DESTINATION_SHIFT | Squares.A2,
                MoveFlags.DOUBLE_PAWN_PUSH << MoveFlags.FLAG_SHIFT | Squares.A4 << MoveFlags.DESTINATION_SHIFT | Squares.A2};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.D7,
                MoveFlags.DOUBLE_PAWN_PUSH << MoveFlags.FLAG_SHIFT | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.D7};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves (pushes blocked)")
    void testGenerateMovesBlocked() {
        Position position = FENUtils.positionFromFEN("rnbqkb1r/pppppppp/5n2/8/N7/8/PPPPPPPP/R1BQKBNR b KQkq - 3 2");
        Board board = position.getBoard();
        Piece piece1 = board.pieceSearch(Squares.A2);
        Piece piece2 = board.pieceSearch(Squares.F7);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.A3 << MoveFlags.DESTINATION_SHIFT | Squares.A2};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece2MovesExpected = {};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves (captures)")
    void testGenerateMovesCaptures() {
        Position position = FENUtils.positionFromFEN("rn1qkb1r/p1pp2pp/1p3p2/1b1npNB1/2PP4/5N2/PP2PPPP/R2QKB1R b KQkq - 7 10");
        Board board = position.getBoard();
        Pawn piece1 = (Pawn) board.pieceSearch(Squares.C4);
        Pawn piece2 = (Pawn) board.pieceSearch(Squares.F6);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE| Squares.C5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.B5 << MoveFlags.DESTINATION_SHIFT | Squares.C4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.D5 << MoveFlags.DESTINATION_SHIFT | Squares.C4};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece2MovesExpected = {MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.G5 << MoveFlags.DESTINATION_SHIFT | Squares.F6};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMoves (en passant)")
    void testGenerateMovesEnPassant() {
        Position position = FENUtils.positionFromFEN("rn1qkb1r/pp3p1p/5n2/2pPp3/6p1/2B2P2/PPP1P1PP/RN1QKB1R w KQkq c6 0 10");
        Board board = position.getBoard();
        piece1 = (Pawn) board.pieceSearch(Squares.D5);
        piece2 = (Pawn) board.pieceSearch(Squares.G4);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece1MovesExpected = {MoveFlags.QUIET_MOVE | Squares.D6 << MoveFlags.DESTINATION_SHIFT | Squares.D5,
                (MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT) << MoveFlags.FLAG_SHIFT | Squares.C6 << MoveFlags.DESTINATION_SHIFT | Squares.D5};
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece2MovesExpected = {MoveFlags.QUIET_MOVE | Squares.G3 << MoveFlags.DESTINATION_SHIFT | Squares.G4,
                MoveFlags.CAPTURE_BIT << MoveFlags.FLAG_SHIFT | Squares.F3 << MoveFlags.DESTINATION_SHIFT | Squares.G4};
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test generateMovesPromotions")
    void testGenerateMovesPromotions() {
        Position position = FENUtils.positionFromFEN("2r1N3/3P4/8/8/8/8/3p4/2R1R3 w - - 0 1");
        Board board = position.getBoard();
        piece1 = (Pawn) board.pieceSearch(Squares.D7);
        piece2 = (Pawn) board.pieceSearch(Squares.D2);
        IntArrayList piece1MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece1MovesExpected = new int[8];
        int[] promotionPieceFlags = {MoveFlags.QUEEN, MoveFlags.ROOK, MoveFlags.BISHOP, MoveFlags.KNIGHT};
        int index = 0;
        int move = MoveFlags.PROMOTION_BIT << MoveFlags.FLAG_SHIFT | Squares.D8 << MoveFlags.DESTINATION_SHIFT | Squares.D7;
        for (int flag : promotionPieceFlags) {
            piece1MovesExpected[index] = move | (flag << MoveFlags.FLAG_SHIFT);
            index++;
        }
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.CAPTURE_BIT) << MoveFlags.FLAG_SHIFT | Squares.C8 << MoveFlags.DESTINATION_SHIFT | Squares.D7;
        for (int flag : promotionPieceFlags) {
            piece1MovesExpected[index] = move | (flag << MoveFlags.FLAG_SHIFT);
            index++;
        }
        piece1.generateMoves(position, piece1MovesActual);
        assertArrayEquals(piece1MovesExpected, piece1MovesActual.toIntArray());
        IntArrayList piece2MovesActual = new IntArrayList(ChessConstants.MAX_PAWN_MOVES);
        int[] piece2MovesExpected = new int[ChessConstants.MAX_PAWN_MOVES];
        index = 0;
        move = MoveFlags.PROMOTION_BIT << MoveFlags.FLAG_SHIFT | Squares.D1 << MoveFlags.DESTINATION_SHIFT | Squares.D2;
        for (int flag : promotionPieceFlags) {
            piece2MovesExpected[index] = move | (flag << MoveFlags.FLAG_SHIFT);
            index++;
        }
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.CAPTURE_BIT) << MoveFlags.FLAG_SHIFT | Squares.C1 << MoveFlags.DESTINATION_SHIFT | Squares.D2;
        for (int flag : promotionPieceFlags) {
            piece2MovesExpected[index] = move | (flag << MoveFlags.FLAG_SHIFT);
            index++;
        }
        move = (MoveFlags.PROMOTION_BIT | MoveFlags.CAPTURE_BIT) << MoveFlags.FLAG_SHIFT | Squares.E1 << MoveFlags.DESTINATION_SHIFT | Squares.D2;
        for (int flag : promotionPieceFlags) {
            piece2MovesExpected[index] = move | (flag << MoveFlags.FLAG_SHIFT);
            index++;
        }
        piece2.generateMoves(position, piece2MovesActual);
        assertArrayEquals(piece2MovesExpected, piece2MovesActual.toIntArray());
    }

    @Test
    @DisplayName("Test canCaptureSquare")
    void testCanCaptureSquare() {
        Position position = FENUtils.positionFromFEN("8/8/8/3p4/8/8/8/PK6 w - - 0 1");
        Board board = position.getBoard();
        assertTrue(piece1.canCaptureSquare(board, Squares.B2));
        assertFalse(piece1.canCaptureSquare(board, Squares.A2));
        assertFalse(piece1.canCaptureSquare(board, Squares.B1));
        assertTrue(piece2.canCaptureSquare(board, Squares.C4));
        assertTrue(piece2.canCaptureSquare(board, Squares.E4));
        assertFalse(piece2.canCaptureSquare(board, Squares.D4));
        assertFalse(piece2.canCaptureSquare(board, Squares.D6));
        assertFalse(piece2.canCaptureSquare(board, Squares.F3));
    }

    @Test
    @DisplayName("Test copyToSquare")
    void testCopyToSquare() {
        Pawn test = (Pawn) piece1.copyToSquare(Squares.D8);
        assertEquals(Squares.D8, test.getSquare());
        assertEquals(piece1.getColour(), test.getColour());
    }

    @Test
    @DisplayName("Test getAttackMask")
    void testGetAttackMask() {
        long expectedAttackMask = 0b00000000_00000010_00000000_00000000_00000000_00000000_00000000_00000000L;
        assertEquals(expectedAttackMask, piece1.getAttackMask(null));
        expectedAttackMask = 0b00000000_00000000_00000000_00010100_00000000_00000000_00000000_00000000L;
        assertEquals(expectedAttackMask, piece2.getAttackMask(null));
    }
}
