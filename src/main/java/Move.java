public class Move {
    private final Piece piece;
    private final String destination;
    private boolean isCheck;
    private boolean isCapture;
    private boolean isCheckmate;
    private boolean isCastle;
    private boolean fileDisambiguation;
    private boolean rankDisambiguation;
    private boolean isEnPassant;
    private PieceType promotionType;

    /**
     * Simple constructor
     * @param piece the piece that will do the move
     * @param destination square the piece moves to
     */
    public Move(Piece piece, String destination) {
        this.piece = piece;
        this.destination = destination;
    }

    /**
     * Simple setter for the isCheck boolean
     * @param check state isCheck boolean is set to
     */
    public void setCheck(boolean check) {
        isCheck = check;
    }

    /**
     * Simple setter for the isCapture boolean
     * @param capture state isCapture boolean is set to
     */
    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    /**
     * Simple setter for the isCheckmate boolean
     * @param checkmate state isCheckmate is set to
     */
    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    /**
     * Simple setter for the fileDisambiguation boolean
     * @param isRequired state fileDisambiguation boolean is set to
     */
    public void setFileDisambiguation(boolean isRequired) {
        fileDisambiguation = isRequired;
    }

    /**
     * Simple setter for the rankDisambiguation boolean
     * @param isRequired state rankDisambiguation boolean is set to
     */
    public void setRankDisambiguation(boolean isRequired) {
        rankDisambiguation = isRequired;
    }

    /**
     * Simple setter for the isCastle boolean
     * @param castle state isCastle is set to
     */
    public void setCastle(boolean castle) {
        isCastle = castle;
    }

    /**
     * Simple setter for promotionType
     * @param promotionType the type of promotion
     */
    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
    }

    /**
     * Simple setter for isEnPassant
     * @param enPassant state isEnPassant is set to
     */
    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
    }

    /**
     * Simpler getter for the piece
     * @return the piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Simpler getter for the destination square
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Simple getter for isCheck
     * @return whether the move is a check
     */
    public boolean isCheck() {
        return isCheck;
    }

    /**
     * Simple getter for isCapture
     * @return whether the move is a capture
     */
    public boolean isCapture() {
        return isCapture;
    }

    /**
     * Simple getter for isCheckmate
     * @return whether the move is checkmate
     */
    public boolean isCheckmate() {
        return isCheckmate;
    }

    /**
     * Simple getter for fileDisambiguation
     * @return whether fileDisambiguation is needed
     */
    public boolean isFileDisambiguation() {
        return fileDisambiguation;
    }

    /**
     * Simple getter for rankDisambiguation
     * @return whether fileDisambiguation is needed
     */
    public boolean isRankDisambiguation() {
        return rankDisambiguation;
    }

    /**
     * Simple getter for promotionType
     * @return the promotionType
     */
    public PieceType getPromotionType() {
        return promotionType;
    }

    /**
     * Simple getter for isEnPassant
     * @return whether the move is an en passant capture
     */
    public boolean isEnPassant() {
        return isEnPassant;
    }

    /**
     * Takes the move and converts it to notation
     * @return the move in standard algebraic notation as a String
     */
    public String getAlgebraicNotation() {
        StringBuilder move =  new StringBuilder();
        switch (piece.getType()) {
            case KING:
                if (isCastle) {
                    if (destination.charAt(0) == 'g') {
                        move.append("O-O");
                    } else if (destination.charAt(0) == 'c') {
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
            move.append(piece.getFile());
        }
        if (rankDisambiguation) {
            move.append(piece.getRank());
        }
        if (isCapture) {
            if (piece.getType() == PieceType.PAWN) {
                move.append(piece.getFile());
            }
            move.append(AlgebraicNotation.CAPTURE);
        }
        move.append(destination);
        if (isCheckmate) {
            move.append(AlgebraicNotation.CHECKMATE);
        }
        if (isCheck) {
            move.append(AlgebraicNotation.CHECK);
        }
        return move.toString();
    }

    @Override
    /**
     * Compares move to a given move to check if they are equal
     * @return boolean object for if the moves are the same
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        }
        if (!(object instanceof Move other)) {
            return false;
        }
        return other.piece == this.piece && other.destination.equals(this.destination) &&
                other.promotionType == this.promotionType && other.isEnPassant == this.isEnPassant;
    }
}
