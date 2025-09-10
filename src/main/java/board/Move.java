public class Move {
    private final Piece piece;
    private final int destination;
    private final int source;
    private boolean isCheck;
    private boolean isCapture;
    private boolean isCheckmate;
    private int castleMask;
    private boolean fileDisambiguation;
    private boolean rankDisambiguation;
    private boolean isEnPassant;
    private PieceType promotionType;
    private final Position position;

    /**
     * Simple constructor.
     *
     * @param position    the position the move is played in.
     * @param piece       the piece that will do the move.
     * @param destination square the piece moves to.
     */
    public Move(Position position, Piece piece, int destination) {
        this.piece = piece;
        this.source = piece.getSquare();
        this.destination = destination;
        this.position = position;
        int kingsideCastleMask, queensideCastleMask;
        if (piece.getColour() == PieceColour.WHITE) {
            kingsideCastleMask = FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
            queensideCastleMask = FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
        } else {
            kingsideCastleMask = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
            queensideCastleMask = FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
        }
        if (piece.getType() == PieceType.KING) {
            if (piece.getSquare() + ChessDirections.RIGHT * 2 == destination) {
                castleMask = kingsideCastleMask;
            } else if (piece.getSquare() + ChessDirections.LEFT * 2 == destination) {
                castleMask = queensideCastleMask;
            } else {
                castleMask = FENConstants.NO_CASTLING_MASK;
            }
        }
        if (position.getBoard().pieceSearch(destination) != null) {
            isCapture = true;
        }
        if (piece.getType() == PieceType.PAWN && destination == position.getGameState().getEnPassantTarget()) {
            isEnPassant = true;
            isCapture = true;
        }
    }

    /**
     * Simple setter for the isCheck boolean.
     * @param check state isCheck boolean is set to.
     */
    public void setCheck(boolean check) {
        isCheck = check;
    }

    /**
     * Simple setter for the isCapture boolean.
     * @param capture state isCapture boolean is set to.
     */
    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    /**
     * Simple setter for the isCheckmate boolean.
     * @param checkmate state isCheckmate is set to.
     */
    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    /**
     * Simple setter for the fileDisambiguation boolean.
     * @param isRequired state fileDisambiguation boolean is set to.
     */
    public void setFileDisambiguation(boolean isRequired) {
        fileDisambiguation = isRequired;
    }

    /**
     * Simple setter for the rankDisambiguation boolean.
     * @param isRequired state rankDisambiguation boolean is set to.
     */
    public void setRankDisambiguation(boolean isRequired) {
        rankDisambiguation = isRequired;
    }

    /**
     * Simple setter for the castleMask flag.
     * Checks again whether the move is a check since the rook movement wouldn't have been accounted for.
     * @param castleMask single bit value for the type of castling occurring in the move.
     */
    public void setCastleMask(int castleMask) {
        this.castleMask = castleMask;
        if (castleMask != FENConstants.NO_CASTLING_MASK) {
            calculateCheck();
        }
    }

    /**
     * Simple setter for promotionType.
     * @param promotionType the type of promotion.
     */
    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
        calculateCheck();
    }

    /**
     * Simple setter for isEnPassant.
     * @param enPassant state isEnPassant is set to.
     */
    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
        if (enPassant) {
            isCapture = true;
        }
    }

    /**
     * Simpler getter for the piece.
     * @return the piece.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Simple getter for source.
     * @return source.
     */
    public int getSource() {
        return source;
    }

    /**
     * Simpler getter for the destination square.
     * @return the destination.
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Simple getter for isCheck.
     * @return whether the move is a check.
     */
    public boolean isCheck() {
        return isCheck;
    }

    /**
     * Simple getter for isCapture.
     * @return whether the move is a capture.
     */
    public boolean isCapture() {
        return isCapture;
    }

    /**
     * Simple getter for isCheckmate.
     * @return whether the move is checkmate.
     */
    public boolean isCheckmate() {
        return isCheckmate;
    }

    /**
     * Simple getter for fileDisambiguation.
     * @return whether fileDisambiguation is needed.
     */
    public boolean isFileDisambiguation() {
        return fileDisambiguation;
    }

    /**
     * Simple getter for rankDisambiguation.
     * @return whether fileDisambiguation is needed.
     */
    public boolean isRankDisambiguation() {
        return rankDisambiguation;
    }

    /**
     * Simple getter for the castle mask flag.
     * @return the relevant flag for castling.
     */
    public int getCastleMask() {
        return castleMask;
    }

    /**
     * Simple getter for promotionType.
     * @return the promotionType.
     */
    public PieceType getPromotionType() {
        return promotionType;
    }

    /**
     * Simple getter for isEnPassant.
     * @return whether the move is an en passant capture.
     */
    public boolean isEnPassant() {
        return isEnPassant;
    }

    /**
     * Calculates whether the given move (castling or promotion) is a check or not.
     * Works by simulating where the piece would end up and whether it can reach the opponents king square.
     */
    public void calculateCheck() {
        Board board = position.getBoard();
        PieceColour colour = piece.getColour();
        int file;
        int rank = SquareMapUtils.getRankContribution(destination);
        Piece piece;
        if (promotionType != null) {
            piece = switch (promotionType) {
                case PieceType.QUEEN -> new Queen(colour, destination);
                case PieceType.KNIGHT -> new Knight(colour, destination);
                case PieceType.ROOK -> new Rook(colour, destination);
                default -> new Bishop(colour, destination);
            };
        } else {
            if ((castleMask & (FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.BLACK_KINGSIDE_CASTLE_MASK)) != FENConstants.NO_CASTLING_MASK) {
                file = ChessConstants.KINGSIDE_CASTLE_ROOK_FILE;
            } else {
                file = ChessConstants.QUEENSIDE_CASTLE_ROOK_FILE;
            }
            piece = new Rook(colour, file + rank);
        }
        King king = board.findKing(colour.opponentColour());
        if (king != null){
            isCheck = piece.canCaptureSquare(board, board.findKing(colour.opponentColour()).getSquare());
        }
    }

    /**
     * Takes the move and converts it to notation
     * @return the move in standard algebraic notation as a String
     */
    public String getAlgebraicNotation() {
        StringBuilder move =  new StringBuilder();
        switch (piece.getType()) {
            case KING:
                if (castleMask != FENConstants.NO_CASTLING_MASK) {
                    if ((castleMask & (FENConstants.WHITE_KINGSIDE_CASTLE_MASK |
                            FENConstants.BLACK_KINGSIDE_CASTLE_MASK)) != 0) {
                        move.append("O-O");
                    } else if ((castleMask & (FENConstants.WHITE_QUEENSIDE_CASTLE_MASK |
                            FENConstants.BLACK_QUEENSIDE_CASTLE_MASK)) != 0) {
                        move.append("O-O-O");
                    }
                    return move.toString();
                } else {
                    move.append(AlgebraicNotation.KING);
                    break;
                }
            case QUEEN:
                move.append(AlgebraicNotation.QUEEN);
                break;
            case PAWN:
                move.append(AlgebraicNotation.PAWN);
                if (promotionType != null) {
                    move.append(destination);
                    move.append(AlgebraicNotation.PROMOTION);
                    switch (promotionType) {
                        case QUEEN ->  move.append(AlgebraicNotation.QUEEN);
                        case ROOK ->  move.append(AlgebraicNotation.ROOK);
                        case BISHOP ->   move.append(AlgebraicNotation.BISHOP);
                        case KNIGHT -> move.append(AlgebraicNotation.KNIGHT);
                    }
                }
                break;
            case ROOK:
                move.append(AlgebraicNotation.ROOK);
                break;
            case BISHOP:
                move.append(AlgebraicNotation.BISHOP);
                break;
            case KNIGHT:
                move.append(AlgebraicNotation.KNIGHT);
                break;
        }
        if (fileDisambiguation) {
            move.append(SquareMapUtils.getFile(piece.getSquare()));
        }
        if (rankDisambiguation) {
            move.append(SquareMapUtils.getRank(piece.getSquare()));
        }
        if (isCapture) {
            if (piece.getType() == PieceType.PAWN) {
                move.append(SquareMapUtils.getFile(piece.getSquare()));
            }
            move.append(AlgebraicNotation.CAPTURE);
        }
        move.append(SquareMapUtils.mapIntToSquare(destination));
        if (isCheckmate) {
            move.append(AlgebraicNotation.CHECKMATE);
        }
        if (isCheck) {
            move.append(AlgebraicNotation.CHECK);
        }
        return move.toString();
    }

    /**
     * Static factory method to filter out illegal moves (moves that leave the king in check).
     * In the event that the move is illegal a null object is returned, otherwise the move is returned.
     *
     * @param position    the position the move is played in.
     * @param piece       the piece that is being moved.
     * @param destination the square the piece is moved to.
     * @return null if the move is illegal, otherwise the move object.
     */
    public static Move createIfLegal(Position position, Piece piece, int destination) {
        Move potentialMove = new Move(position, piece, destination);
        Board board = position.getBoard();
        State stateBeforeMove = position.doMove(potentialMove);
        PieceColour colour = piece.getColour();
        King king = board.findKing(colour);
        if (king == null) { //only occurs in test positions in which case there is no check to worry about
            position.unDoMove(stateBeforeMove);
            return potentialMove;
        }
        boolean[] threatMap = board.getThreatMap(colour.opponentColour());
        if (threatMap[king.getSquare()]) { //king is in check so move is invalid
            position.unDoMove(stateBeforeMove);
            return null;
        } else {
            potentialMove.isCheck = board.getThreatMap(colour)[board.findKing(colour.opponentColour()).getSquare()];
            position.unDoMove(stateBeforeMove);
            return potentialMove;
        }
    }

    /**
     * Method to produce an exact copy of the move on a given board.
     *
     * @param position the position the move is for.
     * @return a move that has identical properties just on the given board.
     */
    public Move clone(Position position) {
        Move copy = new Move(position, position.getBoard().pieceSearch(getPiece().getSquare()), getDestination());
        copy.setCapture(isCapture());
        copy.setCheck(isCheck());
        copy.setCheckmate(isCheckmate());
        copy.setCastleMask(getCastleMask());
        copy.setFileDisambiguation(isFileDisambiguation());
        copy.setRankDisambiguation(isRankDisambiguation());
        copy.setEnPassant(isEnPassant());
        copy.setPromotionType(getPromotionType());
        return copy;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        }
        if (!(object instanceof Move other)) {
            return false;
        }
        return other.piece.equals(this.piece) && other.destination == this.destination &&
                other.promotionType == this.promotionType && other.isEnPassant == this.isEnPassant &&
                other.isCapture == this.isCapture;
    }

    @Override
    public String toString() {
        return SquareMapUtils.mapIntToSquare(source) + SquareMapUtils.mapIntToSquare(destination);
    }
}
