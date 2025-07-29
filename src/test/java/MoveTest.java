import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTest {
    public Move move;

    @Test
    @DisplayName("Test getAlgebraicNotation")
    void testGetAlgebraicNotation() {
    Piece piece = new Knight(PieceColour.WHITE, 'b', 5);
    Board board = new Board("8/8/8/1N6/8/8/8/8 w - - 0 1");
    move = new Move(board, piece, "c7");
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
    board = new Board("8/8/8/8/8/8/8/4K3 w - - 0 1");
    piece = new King(PieceColour.WHITE, 'e', 1, false, false);
    move = new Move(board, piece, "c1");
    move.setCastle(true);
    assertEquals("O-O-O",  move.getAlgebraicNotation());
    move = new Move(board, piece, "g1");
    move.setCastle(true);
    assertEquals("O-O",  move.getAlgebraicNotation());
    }
}

