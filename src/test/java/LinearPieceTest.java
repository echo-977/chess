import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearPieceTest {
    public LinearPiece piece;

    @BeforeEach
    public void init() {
        piece = new LinearPiece("Queen", "White", 'd', 4) {
            @Override
            public String[] generateMoves(Board board) {
                // minimal implementation here to allow testing of concrete functions
                return new String[8];
            }
        };
    }

    @Test
    @DisplayName("Test linearMoveSearch without other pieces")
    void testNoOtherPieces() {
        Board board = new Board("8/8/8/8/3Q4/8/8/8 w - - 0 1");
        String[] moves = new String[27]; //max number of queen moves
        int movesIndex = 0;
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 0, 1); //up
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 1, 1); //up right
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 1, 0); //right
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 1, -1); //down right
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 0, -1); // down
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, -1, -1); //down left
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, -1, 0); //left
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, -1, 1); //up left
        assertEquals(27, movesIndex);
        String [] expectedMoves = {"d5", "d6", "d7", "d8", "e5", "f6", "g7", "h8", "e4", "f4", "g4", "h4", "e3", "f2", "g1", "d3", "d2", "d1", "c3", "b2", "a1", "c4", "b4", "a4", "c5", "b6", "a7"};
        assertArrayEquals(expectedMoves, moves);
    }

    @Test
    @DisplayName("Test linearMoveSearch with other pieces")
    void testOtherPieces() {
        Board board = new Board("8/8/1P3n2/8/3Q4/8/8/8 w - - 0 1");
        String[] moves = new String[27]; //max number of queen moves
        int movesIndex = 0;
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 0, 1); //up
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 1, 1); //up right
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 1, 0); //right
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 1, -1); //down right
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, 0, -1); // down
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, -1, -1); //down left
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, -1, 0); //left
        movesIndex = piece.linearMoveSearch(board, moves, movesIndex, -1, 1); //up left
        assertEquals(23, movesIndex);
        String [] expectedMoves = {"d5", "d6", "d7", "d8", "e5", "f6", "e4", "f4", "g4", "h4", "e3", "f2", "g1", "d3", "d2", "d1", "c3", "b2", "a1", "c4", "b4", "a4", "c5", null, null, null, null};
        assertArrayEquals(expectedMoves, moves);

    }


}
