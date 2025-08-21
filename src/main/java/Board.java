import java.util.Arrays;

public class Board {
    private final Piece[] whitePieces;
    private final Piece[] blackPieces;
    private PieceColour turn;
    private int moveCount;
    private int halfMoveClock;
    private boolean[] whiteThreatMap;
    private boolean[] blackThreatMap;

    /**
     * Constructs a board based on all the boards attributes.
     * @param whitePieces array of all the white pieces on the board.
     * @param blackPieces array of all the black pieces on the board.
     * @param turn the current colour whose turn it is.
     * @param moveCount the current move.
     * @param halfMoveClock number of half moves since a capture or pawn move.
     * @param whiteThreatMap boolean array of all squares white threatens.
     * @param blackThreatMap boolean array of all squares black threatens.
     */
    public Board(Piece[] whitePieces, Piece[] blackPieces, PieceColour turn, int moveCount, int halfMoveClock,
                 boolean[] whiteThreatMap, boolean[] blackThreatMap) {
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.turn = turn;
        this.moveCount = moveCount;
        this.halfMoveClock = halfMoveClock;
        this.whiteThreatMap = whiteThreatMap;
        this.blackThreatMap = blackThreatMap;
    }

    /**
     * Search for if a piece exists on a given square
     *
     * @param square the target square
     * @return the piece if there is a piece on the square, otherwise null
     */
    public Piece pieceSearch(String square) {
        for (Piece piece: whitePieces) {
            if (piece != null && piece.getSquare().equals(square)) {
                return piece;
            }
        }
        for (Piece piece: blackPieces) {
            if (piece != null && piece.getSquare().equals(square)) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Simple getter for the white pieces
     *
     * @return piece array of all the white pieces
     */
    public Piece[] getWhitePieces() {
        return whitePieces;
    }

    /**
     * Simple getter for the black pieces
     *
     * @return piece array of all the black pieces
     */
    public Piece[] getBlackPieces() {
        return blackPieces;
    }

    /**
     * Simple getter for the turn boolean
     *
     * @return boolean value (true for White move, false for Black move)
     */
    public PieceColour getTurn() {
        return turn;
    }

    /**
     * Simple getter for the current move
     *
     * @return int value for the current move
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Simple getter for the half move clock
     *
     * @return int value for the number of half moves since a pawn move or capture
     */
    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    /**
     * Utility method to return the king of the given colour.
     * @param colour the king we want to return.
     * @return king of the given colour.
     */
    public King findKing(PieceColour colour) {
        Piece[] pieces = new Piece[0];
        if (colour == PieceColour.WHITE) {
            pieces = whitePieces;
        } else if (colour == PieceColour.BLACK) {
            pieces = blackPieces;
        }
        for (Piece piece : pieces) {
            if (piece != null && piece.getType() == PieceType.KING) {
                return (King) piece;
            }
        }
        return null;
    }

    /**
     * Plays out a given move on the board.
     * @param move the move to be played.
     * @return a boolean flag for if the move was completed successfully.
     */
    public boolean doMove(Move move) {
        boolean handleCapture = true;
        boolean handleCastle = true;
        boolean handlePromotion = true;
        Piece[] pieces;
        if (turn == PieceColour.WHITE) {
            pieces = blackPieces;
        } else {
            pieces = whitePieces;
        }
        for (Piece piece : pieces) {
            if (piece != null && piece.getType() == PieceType.PAWN) {
                ((Pawn) piece).setEnPassantable(false);
            }
        }
        if (move.isCapture()) {
            handleCapture = handleCaptureMove(move);
        }
        if (move.isCastle()) {
            handleCastle = handleCastleMove(move);
        }
        if (move.getPromotionType() != null) {
            handlePromotion = handlePromotion(move);
        } else {
            move.getPiece().move(move.getDestination());
        }
        if (!(move.getPiece().getType() == PieceType.PAWN || move.isCapture())) {
            halfMoveClock++;
        } else {
            halfMoveClock = 0;
        }
        if (getTurn() == PieceColour.BLACK) {
            moveCount++;
        }
        turn = turn.opponentColour();
        whiteThreatMap = ThreatMapGenerator.getThreatMap(this, PieceColour.WHITE);
        blackThreatMap = ThreatMapGenerator.getThreatMap(this, PieceColour.BLACK);
        return handleCapture && handleCastle && handlePromotion;
    }

    /**
     * Handles the logic that is specific to a capture.
     * Removes the captured piece from the relevant pieces array.
     * @param move the move being played.
     * @return boolean flag for if the capture logic was completed successfully.
     */
    public boolean handleCaptureMove(Move move) {
        String captureDestination = getCaptureDestination(move);
        Piece capturedPiece = pieceSearch(captureDestination);
        Piece[] pieces;
        if (move.getPiece().getColour() == PieceColour.WHITE) {
            pieces = blackPieces;
        } else {
            pieces = whitePieces;
        }
        for (int i = 0; i < (ChessConstants.NUM_PIECES / 2); i++) {
            if (pieces[i] == capturedPiece) {
                pieces[i] = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the square of the piece being captured.
     * @param move the move of the capture.
     * @return the square of the captured piece.
     */
    public String getCaptureDestination(Move move) {
        String captureDestination = move.getDestination();
        if (move.isEnPassant()){
            int targetDirection;
            if (move.getPiece().getColour() == PieceColour.WHITE) {
                targetDirection = ChessDirections.DOWN;
            } else {
                targetDirection = ChessDirections.UP;
            }
            char file = move.getDestination().charAt(0);
            int rank = move.getDestination().charAt(1) - '0';
            int captureRank = rank + targetDirection;
            captureDestination = file + String.valueOf(captureRank);
        }
        return captureDestination;
    }

    /**
     * Handles the logic that is specific to castling.
     * Moves the rook to the correct square.
     * @param move the move being played.
     * @return boolean flag for if the castle logic was completed successfully.
     */
    public boolean handleCastleMove(Move move) {
        String destination = move.getDestination();
        char file = destination.charAt(0);
        int rank = destination.charAt(1) - '0';
        String rookSquare;
        Rook rook;
        if (file == 'g') {
            rookSquare = "h" + rank;
            rook = (Rook) pieceSearch(rookSquare);
            rook.move("f" + rank);
            return true;
        } else if (file == 'c') {
            rookSquare = "a" + rank;
            rook = (Rook) pieceSearch(rookSquare);
            rook.move("d" + rank);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Handles a promotion, replacing the pawn with a piece of the given type.
     * @param move the promotion move to be carried out.
     * @return boolean flag confirming if the move was successfully carried out.
     */
    public boolean handlePromotion(Move move) {
        PieceColour colour = move.getPiece().getColour();
        char file = move.getDestination().charAt(0);
        int rank = move.getDestination().charAt(1) - '0';
        Piece promotedPiece = switch (move.getPromotionType()) {
            case QUEEN -> new Queen(colour, file, rank);
            case ROOK -> new Rook(colour, file, rank, true);
            case BISHOP -> new Bishop(colour, file, rank);
            case KNIGHT -> new Knight(colour, file, rank);
            default -> null;
        };
        if (promotedPiece == null) {
            return false;
        }
        Piece promotingPiece = pieceSearch(move.getPiece().getSquare());
        if (colour == PieceColour.WHITE) {
            for (int i = 0; i < ChessConstants.NUM_PIECES / 2; i++) {
                if (whitePieces[i] == promotingPiece) {
                    whitePieces[i] = promotedPiece;
                    return true;
                }
            }
        } else {
            for (int i = 0; i < ChessConstants.NUM_PIECES / 2; i++) {
                if (blackPieces[i] == promotingPiece) {
                    blackPieces[i] = promotedPiece;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Generates all the moves for a given colour.
     * @param colour the colour we want to generate moves for.
     * @return an array of all the moves the colour can do.
     */
    public Move[] generateMoves(PieceColour colour) {
        int moveCount = 0;
        Piece[] pieces;
        if (colour == PieceColour.WHITE) {
            pieces =  whitePieces;
        } else {
            pieces =  blackPieces;
        }
        for (Piece piece : pieces) {
            if (piece == null) {
                continue;
            }
            for (Move move : piece.generateMoves(this)) {
                if (move != null) {
                    moveCount++;
                }
            }
        }
        Move[] moves = new Move[moveCount];
        moveCount = 0;
        for (Piece piece : pieces) {
            if (piece == null) {
                continue;
            }
            for (Move move : piece.generateMoves(this)) {
                if (move != null) {
                    moves[moveCount] = move;
                    moveCount++;
                }
            }
        }
        DisambiguationUtils.handleDisambiguation(moves);
        return moves;
    }

    /**
     * Update the saved threat map with a new one.
     * @param colour the colour of the threat map to be updated.
     */
    public void updateThreatMap(PieceColour colour) {
        if (colour == PieceColour.WHITE) {
            whiteThreatMap = ThreatMapGenerator.getThreatMap(this, PieceColour.WHITE);
        } else {
            blackThreatMap = ThreatMapGenerator.getThreatMap(this, PieceColour.BLACK);
        }
    }

    /**
     * Returns the current cached threat map for the colour.
     * @param colour the threat map required.
     * @return boolean array of whether the given colour threatens each square.
     */
    public boolean[] getThreatMap(PieceColour colour) {
        if (colour == PieceColour.WHITE) {
            return whiteThreatMap;
        } else {
            return blackThreatMap;
        }
    }

    /**
     * Returns a copy of the board in the same position.
     * @return a functionally identical board.
     */
    public Board copy() {
        Piece[] clonedWhitePieces = new Piece[ChessConstants.NUM_PIECES/2];
        Piece[] clonedBlackPieces = new Piece[ChessConstants.NUM_PIECES/2];
        for (int i = 0; i < ChessConstants.NUM_PIECES/2; i++) {
            if (whitePieces[i] != null) {
                clonedWhitePieces[i] = whitePieces[i].copyToSquare(whitePieces[i].getSquare());
            }
            if (blackPieces[i] != null) {
                clonedBlackPieces[i] = blackPieces[i].copyToSquare(blackPieces[i].getSquare());
            }
        }
        return new Board(clonedWhitePieces, clonedBlackPieces, turn, moveCount, halfMoveClock, whiteThreatMap.clone(), blackThreatMap.clone());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        }
        if (!(object instanceof Board other)) {
            return false;
        }
        return Arrays.equals(other.whitePieces, this.whitePieces) && Arrays.equals(other.blackPieces, this.blackPieces) && other.turn == this.turn
                && other.moveCount == this.moveCount && other.halfMoveClock == this.halfMoveClock &&
                Arrays.equals(other.whiteThreatMap, this.whiteThreatMap) && Arrays.equals(other.blackThreatMap, this.blackThreatMap);
    }
}
