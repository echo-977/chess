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
     * Gets the uci string equivalent of a move.
     * @param move the move we are converting to a string.
     * @return the source square and destination square of the move.
     */
    public static String toUCIString(int move) {
        String name = SquareMapUtils.mapIntToSquare(move & MoveFlags.SOURCE_MASK) +
                SquareMapUtils.mapIntToSquare((move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT);
        int flag = move >> MoveFlags.FLAG_SHIFT;
        if ((flag & MoveFlags.PROMOTION_BIT) == MoveFlags.PROMOTION_BIT) {
            if ((flag & MoveFlags.QUEEN) == MoveFlags.QUEEN) {
                name += FENConstants.QUEEN_CHAR;
            } else if ((flag & MoveFlags.ROOK) == MoveFlags.ROOK) {
                name += FENConstants.ROOK_CHAR;
            } else if ((flag & MoveFlags.BISHOP) == MoveFlags.BISHOP) {
                name += FENConstants.BISHOP_CHAR;
            } else {
                name += FENConstants.KNIGHT_CHAR;
            }
        }
        return name;
    }
}