public class GameState {
    private PieceColour turn;
    private int moveCount;
    private int halfMoveClock;
    private int castlingRights;
    private int enPassantTarget;

    /**
     * Stores the data about a position that cannot be saved with pieces alone.
     * @param turn           the current colour whose turn it is.
     * @param moveCount      the current move.
     * @param halfMoveClock  number of half moves since a capture or pawn move.
     */
    public GameState(PieceColour turn, int moveCount, int halfMoveClock) {
        this.turn = turn;
        this.moveCount = moveCount;
        this.halfMoveClock = halfMoveClock;
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
     * Saves the given castling rights to the game state.
     *
     * @param rights 4 bit mask for each castling right.
     */
    public void setCastlingRights(int rights) {
        castlingRights = rights;
    }

    /**
     * Saves the given en passant target to the game state.
     *
     * @param enPassantTarget the en passant target square.
     */
    public void setEnPassantTarget(int enPassantTarget) {
        this.enPassantTarget = enPassantTarget;
    }

    /**
     * Simple getter for the castling rights.
     *
     * @return 4 bit mask of the board's castling rights.
     */
    public int getCastlingRights() {
        return castlingRights;
    }

    /**
     * Simple getter for the en passant target square.
     * @return the current en passant target square according to FEN notation.
     */
    public int getEnPassantTarget() {
        return enPassantTarget;
    }

    /**
     * Simple setter for halfmove clock.
     * @param halfMoveClock the value the halfMoveClock will be set to.
     */
    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    /**
     * Simple setter for moveCount.
     * @param moveCount the value moveCount will be set to.
     */
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    /**
     * Simple toggle for the turn.
     * Swaps the turn to the other colour.
     */
    public void changeTurn() {
        turn = turn.opponentColour();
    }

    /**
     * Handle the removal of castling rights if the move uses a king or rook.
     * @param move the move that causes castling rights to need removal.
     */
    public void removeCastlingRights(Move move) {
        Piece movePiece = move.getPiece();
        PieceType movePieceType = movePiece.getType();
        if (movePieceType == PieceType.KING) {
            if (movePiece.getColour() == PieceColour.WHITE) {
                castlingRights &= ~FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
                castlingRights &= ~FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
            } else {
                castlingRights &= ~FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
                castlingRights &= ~FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
            }
        } else if (movePieceType == PieceType.ROOK) {
            if (SquareMapUtils.getFileContribution(movePiece.getSquare()) == Files.H) {
                if (movePiece.getColour() == PieceColour.WHITE) {
                    castlingRights &= ~FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
                } else {
                    castlingRights &= ~FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
                }
            } else if (SquareMapUtils.getRankContribution(movePiece.getSquare()) == Files.A) {
                if (movePiece.getColour() == PieceColour.WHITE) {
                    castlingRights &= ~FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
                } else {
                    castlingRights &= ~FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
                }
            }
        }
    }

    /**
     * Increments the move and half move clocks if they are meant to be.
     * Move clock increments with each black move.
     * Half move clock increments with every capture or non pawn move, otherwise it is reset to zero.
     * @param move the move being played.
     */
    public void incrementMoveClocks(Move move) {
        if (move.getPiece().getType() == PieceType.PAWN || move.isCapture()) {
            this.halfMoveClock = 0;
        } else {
            this.halfMoveClock++;
        }
        if (getTurn() == PieceColour.BLACK) {
            this.moveCount++;
        }
    }

    /**
     * Sets the game state to the state before a move occurred.
     * @param state the state that game state is reset to.
     */
    public void resetState(State state) {
        halfMoveClock = state.halfMoveClock();
        moveCount = state.moveCount();
        castlingRights = state.castlingRights();
        enPassantTarget = state.enPassantSquare();
        turn = turn.opponentColour();
    }

    /**
     * Returns a copy of the board in the same position.
     *
     * @return a functionally identical board.
     */
    public GameState copy() {
        GameState gameState = new GameState(turn, moveCount, halfMoveClock);
        gameState.setCastlingRights(castlingRights);
        gameState.setEnPassantTarget(enPassantTarget);
        return gameState;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        }
        if (!(object instanceof GameState other)) {
            return false;
        }
        return this.turn == other.turn && this.moveCount == other.moveCount &&
                this.halfMoveClock == other.halfMoveClock && this.castlingRights == other.castlingRights &&
                this.enPassantTarget == other.enPassantTarget;
    }
}
