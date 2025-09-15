import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Move {
    /**
     * Static factory method to filter out illegal moves (moves that leave the king in check).
     * In the event that the move is illegal a null object is returned, otherwise the move is returned.
     *
     * @param position          the position the move is played in.
     * @param moves             the array list legal moves are added to.
     * @param destinationSquare the square the piece is moved to.
     * @param sourceSquare      the square the piece is moved from.
     * @return true if the move was legal, false if not.
     */
    public static boolean createIfLegal(Position position, IntArrayList moves, int destinationSquare, int sourceSquare) {
        int potentialMove = encodeMove(position, destinationSquare, sourceSquare);
        Board board = position.getBoard();
        PieceColour colour = board.pieceSearch(sourceSquare).getColour();
        State stateBeforeMove = position.doMove(potentialMove);
        King king = board.findKing(colour);
        if (king == null) { //only occurs in test positions in which case there is no check to worry about
            position.unDoMove(stateBeforeMove);
            moves.add(potentialMove);
            return true;
        }
        boolean[] threatMap = board.getThreatMap(colour.opponentColour());
        if (threatMap[king.getSquare()]) { //king is in check so move is invalid
            position.unDoMove(stateBeforeMove);
            return false;
        } else {
            position.unDoMove(stateBeforeMove);
            moves.add(potentialMove);
            return true;
        }
    }

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
        Piece movePiece = board.pieceSearch(sourceSquare);
        int moveFlag = MoveFlags.QUIET_MOVE; //assume by default it is just a simple move
        if (board.pieceSearch(destinationSquare) != null) {
            moveFlag |= MoveFlags.CAPTURE_BIT;
        } else if (movePiece != null && movePiece.getType() == PieceType.PAWN) {
            if (Math.abs(destinationSquare - sourceSquare) == 2 * ChessConstants.RANK_OFFSET) {
                moveFlag |= MoveFlags.DOUBLE_PAWN_PUSH;
            } else if (destinationSquare == position.getGameState().getEnPassantTarget()) {
                moveFlag |= (MoveFlags.EN_PASSANT | MoveFlags.CAPTURE_BIT); //move destination for a pawn is the en passant square so move must be en passant capture
            }
        } else if (movePiece != null && movePiece.getType() == PieceType.KING) {
            if (Math.abs(destinationSquare - sourceSquare) == 2 * ChessConstants.FILE_OFFSET) { //king moved 2 files so is castling
                if (SquareMapUtils.getFileContribution(destinationSquare) == ChessConstants.KINGSIDE_CASTLE_FILE) {
                    moveFlag |= MoveFlags.KINGSIDE_CASTLE;
                } else if (SquareMapUtils.getFileContribution(destinationSquare) == ChessConstants.QUEENSIDE_CASTLE_FILE) {
                    moveFlag |= MoveFlags.QUEENSIDE_CASTLE;
                }
            }
        }
        if (movePiece != null && movePiece.getType() == PieceType.PAWN) {
            int promotionSourceRank = (movePiece.getColour() == PieceColour.WHITE) ? Ranks.SEVEN : Ranks.TWO;
            if (SquareMapUtils.getRankContribution(sourceSquare) == promotionSourceRank) {
                moveFlag |= MoveFlags.PROMOTION_BIT;
            }
        }
        return ((moveFlag << MoveFlags.FLAG_SHIFT) | (destinationSquare << MoveFlags.DESTINATION_SHIFT) | sourceSquare) & MoveFlags.FORCE_16_BIT;
    }

    /**
     * Gets the string equivalent of a move.
     * @param move the move we are converting to a string.
     * @return the source square and destination square of the move.
     */
    public static String toString(int move) {
        return SquareMapUtils.mapIntToSquare((move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT) +
                SquareMapUtils.mapIntToSquare(move & MoveFlags.SOURCE_MASK);
    }
}