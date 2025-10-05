import it.unimi.dsi.fastutil.ints.IntArrayList;

public class SANUtils {
    /**
     * Detects what disambiguation is required for a given move.
     * @param position move is to be disambiguated in.
     * @param move the move that needs to be checked for when disambiguation is required.
     * @param moveGenerator the move generator object.
     * @return the string value of the disambiguation needed.
     */
    private static String getDisambiguation(Position position, int move, MoveGenerator moveGenerator) {
        StringBuilder disambiguation = new StringBuilder();
        IntArrayList moves = moveGenerator.generateLegalMoves(position);
        int moveSourceSquare = move & MoveFlags.SOURCE_MASK;
        int moveDestinationSquare = (move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT;
        int moveSourceRank = SquareMapUtils.getRankContribution(moveSourceSquare);
        int moveSourceFile = SquareMapUtils.getFileContribution(moveSourceSquare);
        int destinationSquare, generatedMoveSource;
        boolean needsRankDisambiguation = false;
        boolean needsFileDisambiguation = false;
        for (int generatedMove : moves) {
            destinationSquare = (generatedMove & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT;
            if (moveDestinationSquare == destinationSquare && generatedMove != move) {
                generatedMoveSource = generatedMove & MoveFlags.SOURCE_MASK;
                if (SquareMapUtils.getRankContribution(generatedMoveSource) == moveSourceRank) {
                    needsFileDisambiguation = true;
                }
                if (SquareMapUtils.getFileContribution(generatedMoveSource) == moveSourceFile) {
                    needsRankDisambiguation = true;
                }
            }
        }
        if (needsFileDisambiguation) {
            disambiguation.append(SquareMapUtils.getFile(moveSourceSquare));
        }
        if (needsRankDisambiguation) {
            disambiguation.append(SquareMapUtils.getRank(moveSourceSquare));
        }
        return disambiguation.toString();
    }

    /**
     * Takes the move and converts it to notation.
     * @param position the position the move is in.
     * @param moveFlag flag containing whether a move is a capture or en passant and other move information.
     * @param destinationSquare the destination of a move.
     * @param sourceSquare the square of the piece moving.
     * @param moveGenerator the move generator object.
     * @return the move in standard algebraic notation as a String.
     */
    public static String getAlgebraicNotation(Position position, int moveFlag, int destinationSquare, int sourceSquare,
                                              MoveGenerator moveGenerator) {
        StringBuilder moveNotation =  new StringBuilder();
        Board board = position.getBoard();
        Piece piece = board.pieceSearch(sourceSquare);
        boolean castle = false;
        switch (piece.getType()) {
            case KING:
                if (moveFlag == MoveFlags.KINGSIDE_CASTLE) {
                    moveNotation.append("O-O");
                    castle = true;
                } else if (moveFlag == MoveFlags.QUEENSIDE_CASTLE) {
                    moveNotation.append("O-O-O");
                    castle = true;
                } else {
                    moveNotation.append(AlgebraicNotation.KING);
                }
                break;
            case QUEEN:
                moveNotation.append(AlgebraicNotation.QUEEN);
                break;
            case PAWN:
                moveNotation.append(AlgebraicNotation.PAWN);
                break;
            case ROOK:
                moveNotation.append(AlgebraicNotation.ROOK);
                break;
            case BISHOP:
                moveNotation.append(AlgebraicNotation.BISHOP);
                break;
            case KNIGHT:
                moveNotation.append(AlgebraicNotation.KNIGHT);
                break;
        }
        int move = moveFlag << MoveFlags.FLAG_SHIFT | destinationSquare << MoveFlags.DESTINATION_SHIFT | sourceSquare;
        if (!castle) {
            moveNotation.append(getDisambiguation(position, move, moveGenerator));
            if ((moveFlag & MoveFlags.CAPTURE_BIT) == MoveFlags.CAPTURE_BIT) {
                if (piece.getType() == PieceType.PAWN) {
                    moveNotation.append(SquareMapUtils.getFile(piece.getSquare()));
                }
                moveNotation.append(AlgebraicNotation.CAPTURE);
            }
            moveNotation.append(SquareMapUtils.mapIntToSquare(destinationSquare));
            if ((moveFlag & MoveFlags.PROMOTION_BIT) == MoveFlags.PROMOTION_BIT) {
                moveNotation.append(AlgebraicNotation.PROMOTION);
                if ((moveFlag & MoveFlags.BISHOP) ==  MoveFlags.BISHOP) {
                    moveNotation.append(AlgebraicNotation.BISHOP);
                } else if ((moveFlag & MoveFlags.ROOK) ==  MoveFlags.ROOK) {
                    moveNotation.append(AlgebraicNotation.ROOK);
                } else if ((moveFlag & MoveFlags.QUEEN) ==  MoveFlags.QUEEN) {
                    moveNotation.append(AlgebraicNotation.QUEEN);
                } else {
                    moveNotation.append(AlgebraicNotation.KNIGHT);
                }
            }
        }
        State stateBeforeMove = position.doMove(move);
        PieceColour turn = position.getGameState().getTurn();
        if (((board.getThreatMap(turn.opponentColour()) >> board.findKing(turn).getSquare()) & 1L) == 1) {
            if (moveGenerator.generateLegalMoves(position).isEmpty()) { //king is in check without any moves
                moveNotation.append(AlgebraicNotation.CHECKMATE);
            } else {
                moveNotation.append(AlgebraicNotation.CHECK);
            }
        }
        position.unDoMove(stateBeforeMove);
        return moveNotation.toString();
    }

}
