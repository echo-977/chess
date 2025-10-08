public class Move {
    /**
     * Returns the unique integer to represent a move from the source to destination.
     * The leftmost 4 bits are flags used to contain move data.
     * The next 6 bits are for the destination square.
     * The final 6 bits are for the source square.
     * @param position the position the move is made in.
     * @param destinationSquare the square the move is to.
     * @param sourceSquare the square the move is from.
     * @return a 16 bit integer defining a move.
     */
    public static int encodeMove(Position position, int destinationSquare, int sourceSquare) {
        Board board = position.getBoard();
        int moveFlag = MoveFlags.QUIET_MOVE; //assume by default it is just a simple move
        if (board.pieceSearch(destinationSquare) != null) {
            moveFlag |= MoveFlags.CAPTURE_BIT;
        }
        return ((moveFlag << MoveFlags.FLAG_SHIFT) | (destinationSquare << MoveFlags.DESTINATION_SHIFT) | sourceSquare) & MoveFlags.FORCE_16_BIT;
    }
}