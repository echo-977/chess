public class UCIUtils {
    /**
     * Gets the uci string equivalent of a move.
     * @param move the move we are converting to a string.
     * @return the source square and destination square of the move.
     */
    public static String moveToUCIString(int move) {
        String UCI = SquareMapUtils.mapIntToSquare(move & MoveFlags.SOURCE_MASK) +
                SquareMapUtils.mapIntToSquare((move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT);
        int flag = move >> MoveFlags.FLAG_SHIFT;
        if ((flag & MoveFlags.PROMOTION_BIT) == MoveFlags.PROMOTION_BIT) {
            if ((flag & MoveFlags.QUEEN) == MoveFlags.QUEEN) {
                UCI += FENConstants.QUEEN_CHAR;
            } else if ((flag & MoveFlags.ROOK) == MoveFlags.ROOK) {
                UCI += FENConstants.ROOK_CHAR;
            } else if ((flag & MoveFlags.BISHOP) == MoveFlags.BISHOP) {
                UCI += FENConstants.BISHOP_CHAR;
            } else {
                UCI += FENConstants.KNIGHT_CHAR;
            }
        }
        return UCI;
    }

    /**
     * Convert a UCI move to an encoded move the board can handle.
     * @param position the position the move will be done in.
     * @param UCI the move in UCI format.
     * @return an encoded move.
     */
    public static int UCItoEncodedMove(Position position, String UCI) {
        Board board = position.getBoard();
        char promotionType = UCI.length() > UCIConstants.DESTINATION_END ?
                UCI.charAt(UCIConstants.PROMOTION_CHAR) : UCIConstants.NO_PROMOTION;
        int source = SquareMapUtils.mapSquareToInt(
                UCI.substring(UCIConstants.SOURCE_START, UCIConstants.SOURCE_END));
        int destination = SquareMapUtils.mapSquareToInt(
                UCI.substring(UCIConstants.DESTINATION_START, UCIConstants.DESTINATION_END));
        int moveFlags = MoveFlags.QUIET_MOVE;
        Piece movePiece = board.pieceSearch(source);
        PieceType pieceType = movePiece.getType();
        if (pieceType == PieceType.PAWN && destination == position.getGameState().getEnPassantTarget()) {
            moveFlags |= (MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT);
        } else if (board.pieceSearch(destination) != null) {
            moveFlags |= MoveFlags.CAPTURE_BIT;
        }
        if (pieceType == PieceType.PAWN) {
            int direction = (movePiece.getColour() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
            if (source + 2 * direction == destination) {
                moveFlags |= MoveFlags.DOUBLE_PAWN_PUSH;
            }
        }
        if (pieceType == PieceType.KING) {
            if (source + 2 * ChessDirections.RIGHT == destination) {
                moveFlags |= MoveFlags.KINGSIDE_CASTLE;
            } else if (source + 2 * ChessDirections.LEFT == destination) {
                moveFlags |= MoveFlags.QUEENSIDE_CASTLE;
            }
        }
        if (promotionType != UCIConstants.NO_PROMOTION) {
            moveFlags |= MoveFlags.PROMOTION_BIT;
            switch (promotionType) {
                case UCIConstants.QUEEN_CHAR -> moveFlags |= MoveFlags.QUEEN;
                case UCIConstants.ROOK_CHAR ->  moveFlags |= MoveFlags.ROOK;
                case UCIConstants.BISHOP_CHAR -> moveFlags |= MoveFlags.BISHOP;
                case UCIConstants.KNIGHT_CHAR -> moveFlags |= MoveFlags.KNIGHT;
            }
        }
        return ((moveFlags << MoveFlags.FLAG_SHIFT) | destination << MoveFlags.DESTINATION_SHIFT | source);
    }
}
