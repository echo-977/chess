import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    public Move move;

    @Test
    @DisplayName("Test getAlgebraicNotation")
    void testGetAlgebraicNotation() {
    Piece piece = new Knight(PieceColour.WHITE, Files.B + Ranks.FIVE);
    Position position = FENUtils.positionFromFEN("8/8/8/1N6/8/8/8/8 w - - 0 1");
    move = new Move(position, piece, Files.C + Ranks.SEVEN);
    assertEquals("Nc7", move.getAlgebraicNotation());
    move.setCapture(true);
    move.setCheck(true);
    assertEquals("Nxc7+",  move.getAlgebraicNotation());
    move.setCapture(false);
    move.setFileDisambiguation(true);
    assertEquals("Nbc7+",  move.getAlgebraicNotation());
    move.setFileDisambiguation(false);
    move.setRankDisambiguation(true);
    move.setCheck(false);
    assertEquals("N5c7",  move.getAlgebraicNotation());
    move.setFileDisambiguation(true);
    assertEquals("Nb5c7",  move.getAlgebraicNotation());
        position = FENUtils.positionFromFEN("8/8/8/8/8/8/8/4K3 w - - 0 1");
    piece = new King(PieceColour.WHITE, Files.E + Ranks.ONE, false);
    move = new Move(position, piece, Files.C + Ranks.ONE);
    move.setCastleMask(FENConstants.WHITE_QUEENSIDE_CASTLE_MASK);
    assertEquals("O-O-O",  move.getAlgebraicNotation());
    move = new Move(position, piece, Files.G + Ranks.ONE);
    move.setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
    assertEquals("O-O",  move.getAlgebraicNotation());
    }

    @Test
    @DisplayName("Test moveConstructor")
    void testMove() {
        Position position = FENUtils.positionFromFEN("8/5k2/8/3r4/8/8/8/4K3 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Files.D + Ranks.FIVE);
        Move move = Move.createIfLegal(position, piece, Files.E + Ranks.FIVE);
        assertNotNull(move);
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test castling detection")
    void testCastlingDetection() {
        Position position = FENUtils.positionFromFEN("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        King whiteKing = position.getBoard().findKing(PieceColour.WHITE);
        Move move = new Move(position, whiteKing, Files.G + Ranks.ONE);
        assertEquals(FENConstants.WHITE_KINGSIDE_CASTLE_MASK, move.getCastleMask());
        move = new Move(position, whiteKing, Files.C + Ranks.ONE);
        assertEquals(FENConstants.WHITE_QUEENSIDE_CASTLE_MASK, move.getCastleMask());
        King blackKing = position.getBoard().findKing(PieceColour.BLACK);
        move = new Move(position, blackKing, Files.G + Ranks.EIGHT);
        assertEquals(FENConstants.BLACK_KINGSIDE_CASTLE_MASK, move.getCastleMask());
        move = new Move(position, blackKing, Files.C + Ranks.EIGHT);
        assertEquals(FENConstants.BLACK_QUEENSIDE_CASTLE_MASK, move.getCastleMask());
    }

    @Test
    @DisplayName("Test discoveredCheckDetection")
    void testDiscoveredCheckDetection() {
        Position position = FENUtils.positionFromFEN("8/2r5/2n5/5k2/8/2KR4/2B5/8 w - - 0 1");
        Board board = position.getBoard();
        Piece piece = board.pieceSearch(Files.D + Ranks.THREE);
        Move move = Move.createIfLegal(position, piece, Files.D + Ranks.FOUR);
        assertNotNull(move);
        assertTrue(move.isCheck());
        piece = board.pieceSearch(Files.C + Ranks.SIX);
        move = Move.createIfLegal(position, piece, Files.E + Ranks.FIVE);
        assertNotNull(move);
        assertTrue(move.isCheck());
        piece = board.pieceSearch(Files.D + Ranks.THREE);
        move = Move.createIfLegal(position, piece, Files.F + Ranks.THREE);
        assertNotNull(move);
        assertTrue(move.isCheck());
        piece = board.pieceSearch(Files.C + Ranks.TWO);
        move = Move.createIfLegal(position, piece, Files.B + Ranks.ONE);
        assertNotNull(move);
        assertFalse(move.isCheck());
    }

    @Test
    @DisplayName("Test castleCheckDetection")
    void testCastleCheckDetection() {
        Position position = FENUtils.positionFromFEN("8/8/8/8/8/5k2/8/4K2R w K - 0 1");
        King whiteKing = position.getBoard().findKing(PieceColour.WHITE);
        Move move = new Move(position, whiteKing, Files.G + Ranks.ONE);
        move.setCastleMask(FENConstants.WHITE_KINGSIDE_CASTLE_MASK);
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test promotionCheckDetection")
    void testPromotionCheckDetection() {
        Position position = FENUtils.positionFromFEN("8/3P4/5k2/8/8/8/8/8 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Files.D + Ranks.SEVEN);
        Move move = new Move(position, piece, Files.D + Ranks.EIGHT);
        move.setPromotionType(PieceType.BISHOP);
        assertTrue(move.isCheck());
    }

    @Test
    @DisplayName("Test illegalMoveDetection (pin)")
    void testIllegalMoveDetection() {
        Position position = FENUtils.positionFromFEN("8/8/R2b1k2/8/8/8/8/8 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Files.D + Ranks.SIX);
        Move[] expectedMoves = new Move[ChessConstants.MAX_BISHOP_MOVES];
        assertArrayEquals(expectedMoves, piece.generateMoves(position));
    }

    @Test
    @DisplayName("Test illegalMoveDetection (blocking check)")
    void testIllegalMoveDetection2() {
        Position position = FENUtils.positionFromFEN("8/8/R4k2/2b5/8/3K4/8/8 w - - 0 1");
        Piece piece = position.getBoard().pieceSearch(Files.C + Ranks.FIVE);
        Move[] expectedMoves = new Move[ChessConstants.MAX_BISHOP_MOVES];
        expectedMoves[0] = new Move(position, piece, Files.B + Ranks.SIX);
        expectedMoves[1] = new Move(position, piece, Files.D + Ranks.SIX);
        assertArrayEquals(expectedMoves, piece.generateMoves(position));
    }
}

