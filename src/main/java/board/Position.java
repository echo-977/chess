public class Position {
    private final Board board;
    private final GameState gameState;

    /**
     * Creates a position with the given board and game state.
     * @param board the pieces on the board for the given position.
     * @param gameState data about the position that cannot be shown with pieces alone.
     */
    public Position(Board board, GameState gameState) {
        this.board = board;
        this.gameState = gameState;
    }

    /**
     * Simple getter for the board.
     * @return the board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Simple getter for the game state.
     * @return the game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Plays out a given move on the board.
     * @param move the move to be played.
     * @return the state of the board before the move.
     */
    public State doMove(Move move){
        Piece capturedPiece = null;
        Piece movePiece = move.getPiece();
        PieceType movePieceType = movePiece.getType();
        Piece[] pieces = board.getPieces();
        int castlingRights = gameState.getCastlingRights();
        if (move.isCapture()) {
            capturedPiece = handleCaptureMove(move);
        }
        State stateBeforeMove = new State(move, capturedPiece, gameState.getEnPassantTarget(), castlingRights,
                gameState.getHalfMoveClock(), gameState.getMoveCount(), board.getThreatMap(PieceColour.WHITE),
                board.getThreatMap(PieceColour.BLACK));
        if (movePieceType == PieceType.PAWN &&
                Math.abs(movePiece.getRank() - SquareMapUtils.getRank(move.getDestination())) == 2) {
            int direction = (gameState.getTurn() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
            String enPassantTarget = movePiece.getFile() + String.valueOf(movePiece.getRank() + direction);
            gameState.setEnPassantTarget(enPassantTarget);
        } else {
            gameState.setEnPassantTarget(FENConstants.NONE);
        }
        if (move.getCastleMask() != FENConstants.NO_CASTLING_MASK) {
            board.handleCastleMovement(move);
        }
        if (movePieceType == PieceType.KING || movePieceType == PieceType.ROOK) {
            gameState.removeCastlingRights(move);
        }
        if (move.getPromotionType() != null) {
            board.handlePromotion(move);
        } else {
            move.getPiece().move(move.getDestination());
            pieces[SquareMapUtils.mapSquareToInt(move.getDestination())] = move.getPiece();
            pieces[SquareMapUtils.mapSquareToInt(move.getSource())] = null;
        }
        gameState.incrementMoveClocks(move);
        gameState.changeTurn();
        board.updateThreatMap(PieceColour.WHITE);
        board.updateThreatMap(PieceColour.BLACK);
        return stateBeforeMove;
    }

    /**
     * Handles the logic that is specific to a capture, removes the captured piece from the relevant pieces array.
     * If the captured piece is a rook it removes the relevant castling right.
     * @param move the move being played.
     * @return boolean flag for if the capture logic was completed successfully.
     */
    public Piece handleCaptureMove(Move move) {
        String captureDestination = board.getCaptureDestination(move);
        Piece capturedPiece = board.pieceSearch(captureDestination);
        if (capturedPiece.getType() == PieceType.ROOK) {
            int castlingRights = gameState.getCastlingRights();
            switch(capturedPiece.getSquare()) {
                case "a1" -> castlingRights &= ~FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
                case "h1" -> castlingRights &= ~FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
                case "a8" -> castlingRights &= ~FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
                case "h8" -> castlingRights &= ~FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
            }
            gameState.setCastlingRights(castlingRights);
        }
        board.getPieces()[SquareMapUtils.mapSquareToInt(captureDestination)] = null;
        return capturedPiece;
    }

    /**
     * Returns the board to the state before a move.
     * @param state the state the board is to be returned to.
     */
    public void unDoMove(State state) {
        Piece movePiece = state.move().getPiece();
        Piece[] pieces = board.getPieces();
        pieces[SquareMapUtils.mapSquareToInt(state.move().getDestination())] = null;
        if (state.move().getPromotionType() == null) {
            movePiece.move(state.move().getSource());
        }
        if (state.capturedPiece() != null) {
            pieces[SquareMapUtils.mapSquareToInt(state.capturedPiece().getSquare())] = state.capturedPiece();
        }
        if (state.move().getCastleMask() != FENConstants.NO_CASTLING_MASK) {
            board.unDoCastleMovement(state);
        }
        pieces[SquareMapUtils.mapSquareToInt(state.move().getSource())] = state.move().getPiece();
        if (movePiece.getType() == PieceType.PAWN &&
                (SquareMapUtils.getRank(state.move().getSource()) == 2 ||
                        SquareMapUtils.getRank(state.move().getSource()) == 7)) {
            ((Pawn) movePiece).setMoved(false);
        }

        board.resetThreatMaps(state.whiteThreatMap(), state.blackThreatMap());
        gameState.resetState(state);
    }

    /**
     * Returns an identical, independent copy of the position.
     *
     * @return a functionally identical position.
     */
    public Position copy() {
        return new Position(board.copy(), gameState.copy());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (object == this) {
            return true;
        }
        if (!(object instanceof Position other)) {
            return false;
        }
        return board.equals(other.board) && gameState.equals(other.gameState);
    }
}
