public class Move {
    private final Piece piece;
    private final String destination;
    private boolean isCheck;
    private boolean isCapture;
    private boolean isCheckmate;
    private boolean isCastle;
    private boolean fileDisambiguation;
    private boolean rankDisambiguation;

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
     * Simple getter for the isCastle boolean
     * @param castle state isCastle is set to
     */
    public void setCastle(boolean castle) {
        isCastle = castle;
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
     *
     * @return the move in standard algebraic notation
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
}
