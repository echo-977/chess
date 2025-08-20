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
    private final Board board;

    /**
     * Simple constructor
     * @param piece the piece that will do the move
     * @param destination square the piece moves to
     */
    public Move(Board board, Piece piece, String destination) {
        this.piece = piece;
        this.destination = destination;
        this.board = board;
        if (board.pieceSearch(destination) != null) {
            isCapture = true;
        }
        if (piece.getType() == PieceType.KING) {
            char destinationFile = destination.charAt(0);
            if (Math.abs(piece.getFile() - destinationFile) == 2) {
                isCastle = true;
            }
        }
        isCheck = causesCheck(board, piece, destination);
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
     * Checks again whether the move is a check since the rook movement wouldn't have been accounted for.
     * @param castle state isCastle is set to
     */
    public void setCastle(boolean castle) {
        isCastle = castle;
        if (castle) {
            isCheck = causesCheck(board, piece, destination);
        }
    }

    /**
     * Simple setter for promotionType
     * @param promotionType the type of promotion
     */
    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
        isCheck = causesCheck(board, piece, destination);
    }

    /**
     * Simple setter for isEnPassant
     * @param enPassant state isEnPassant is set to
     */
    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
        if (enPassant) {
            isCapture = true;
        }
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
     * Simple getter for isCastle
     * @return whether the move is a castle
     */
    public boolean isCastle() {
        return isCastle;
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
     * Calculates whether moving a piece to a certain square will cause a check.
     * @param board the board the piece is being moved on.
     * @param piece the piece being moved.
     * @param destination the square the piece is moved to.
     * @return boolean stating whether the move is a check on the enemy king.
     */
    public boolean causesCheck(Board board, Piece piece, String destination) {
        Piece checkTest = piece.copyToSquare(destination);
        PieceColour enemyKingColour;
        if (piece.getColour() == PieceColour.WHITE) {
            enemyKingColour = PieceColour.BLACK;
        } else {
            enemyKingColour = PieceColour.WHITE;
        }
        King enemyKing = board.findKing(enemyKingColour);
        if (enemyKing == null) {
            return false;
        }
        if (checkTest.canCaptureSquare(board, enemyKing.getSquare())) {
            return true; //direct check
        } //discovered checks
        String currentPosition = board.getFEN();
        Board boardAfterMove = new Board(currentPosition);
        Piece pieceOnNewBoard = boardAfterMove.pieceSearch(piece.getSquare());
        char destinationFile = destination.charAt(0);
        if (pieceOnNewBoard instanceof King king && Math.abs(destinationFile - pieceOnNewBoard.getFile()) == 2) {
            king.castleMove(boardAfterMove, destination);
        }
        if (promotionType != null) {
            boardAfterMove.handlePromotion(this);
        } else {
            pieceOnNewBoard.move(destination);
        }
        if (piece.getColour() == PieceColour.WHITE) {
            for (Piece updatedBoardPiece : boardAfterMove.getWhitePieces()) {
                if (updatedBoardPiece != null &&
                        updatedBoardPiece.canCaptureSquare(boardAfterMove, enemyKing.getSquare())) {
                    return true;
                }
            }
        }
        else {
            for (Piece updatedBoardPiece : boardAfterMove.getBlackPieces()) {
                if (updatedBoardPiece != null &&
                        updatedBoardPiece.canCaptureSquare(boardAfterMove, enemyKing.getSquare())) {
                    return true;
                }
            }
        }
        return false;
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

    /**
     * Static factory method to filter out illegal moves (moves that leave the king in check).
     * In the event that the move is illegal a null object is returned, otherwise the move is returned.
     * @param board the board the move will be on.
     * @param piece the piece that is being moved.
     * @param destination the square the piece is moved to.
     * @return null if the move is illegal, otherwise the move object.
     */
    public static Move createIfLegal(Board board, Piece piece, String destination) {
        Board boardAfterMove = new Board(board.getFEN());
        Piece pieceCopy = boardAfterMove.pieceSearch(piece.getSquare());
        Move moveCopy = new Move(boardAfterMove, pieceCopy, destination);
        boardAfterMove.doMove(moveCopy);
        PieceColour colour;
        PieceColour enemyColour;
        if (piece.getColour() == PieceColour.WHITE) {
            colour = PieceColour.WHITE;
            enemyColour = PieceColour.BLACK;
        } else {
            colour = PieceColour.BLACK;
            enemyColour = PieceColour.WHITE;
        }
        King king = boardAfterMove.findKing(colour);
        if (king == null) { //only occurs in test positions in which case there is no check to worry about
            return new Move(board, piece, destination);
        }
        boolean[] threatMap = boardAfterMove.getThreatMap(enemyColour);
        String kingSquare = king.getSquare();
        if (threatMap[board.mapSquareToInt(kingSquare)]) { //king is in check so move is invalid
            return null;
        } else {
            return new Move(board, piece, destination);
        }
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
        return other.piece == this.piece && other.destination.equals(this.destination) &&
                other.promotionType == this.promotionType && other.isEnPassant == this.isEnPassant &&
                other.isCapture == this.isCapture;
    }

    @Override
    public String toString() {
        return piece + destination;
    }
}
