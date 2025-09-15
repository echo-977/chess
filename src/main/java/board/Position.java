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
    public State doMove(int move){
        int moveFlag = move >> MoveFlags.FLAG_SHIFT;
        int destinationSquare = (move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT;
        int sourceSquare = move & MoveFlags.SOURCE_MASK;
        Piece capturedPiece;
        int castlingRights = gameState.getCastlingRights();
        if ((moveFlag & MoveFlags.CAPTURE_BIT) != 0) { //capture
            capturedPiece = handleCaptureMove(moveFlag, destinationSquare, sourceSquare);
        } else {
            capturedPiece = null;
        }
        State stateBeforeMove = new State(move, capturedPiece, gameState.getEnPassantTarget(), castlingRights,
                gameState.getHalfMoveClock(), gameState.getMoveCount(), board.getThreatMap(PieceColour.WHITE),
                board.getThreatMap(PieceColour.BLACK));
        Piece[] pieces = board.getPieces();
        Piece movePiece = pieces[sourceSquare];
        gameState.setEnPassantTarget(ChessConstants.NO_EN_PASSANT_TARGET);
        if ((moveFlag & MoveFlags.PROMOTION_BIT) == MoveFlags.PROMOTION_BIT) {
            board.handlePromotion(moveFlag, destinationSquare, sourceSquare);
        } else {
            if ((moveFlag & MoveFlags.QUEENSIDE_CASTLE) == MoveFlags.QUEENSIDE_CASTLE) {
                board.handleCastleMovement(moveFlag, destinationSquare);
            } else if ((moveFlag & MoveFlags.KINGSIDE_CASTLE) == MoveFlags.KINGSIDE_CASTLE) {
                board.handleCastleMovement(moveFlag, destinationSquare);
            } else {
                if (((moveFlag & MoveFlags.CAPTURE_BIT) != MoveFlags.CAPTURE_BIT) &&
                        ((moveFlag & MoveFlags.DOUBLE_PAWN_PUSH) == MoveFlags.DOUBLE_PAWN_PUSH)) {
                    int direction = (gameState.getTurn() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
                    int enPassantTarget = sourceSquare + direction;
                    gameState.setEnPassantTarget(enPassantTarget);
                }
            }
            PieceType movePieceType = movePiece.getType();
            if (movePieceType == PieceType.KING || movePieceType == PieceType.ROOK) {
                gameState.removeCastlingRights(movePiece);
            }
            movePiece.move(destinationSquare);
            pieces[destinationSquare] = movePiece;
            pieces[sourceSquare] = null;
        }
        gameState.incrementMoveClocks(movePiece, moveFlag);
        gameState.changeTurn();
        board.updateThreatMap(PieceColour.WHITE);
        board.updateThreatMap(PieceColour.BLACK);
        return stateBeforeMove;
    }

    /**
     * Handles the logic that is specific to a capture, removes the captured piece from the relevant pieces array.
     * If the captured piece is a rook it removes the relevant castling right.
     * @param moveFlag flag containing whether a move is a capture or en passant and other move information.
     * @param destinationSquare the destination of a move.
     * @param sourceSquare the square of the piece moving.
     * @return the captured piece.
     */
    public Piece handleCaptureMove(int moveFlag, int destinationSquare, int sourceSquare) {
        int captureDestination = board.getCaptureDestination(moveFlag, destinationSquare, sourceSquare);
        Piece capturedPiece = board.pieceSearch(captureDestination);
        if (capturedPiece.getType() == PieceType.ROOK) {
            int castlingRights = gameState.getCastlingRights();
            switch(capturedPiece.getSquare()) {
                case ChessConstants.WHITE_QUEENSIDE_ROOK_SQUARE -> castlingRights &= ~FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
                case ChessConstants.WHITE_KINGSIDE_ROOK_SQUARE -> castlingRights &= ~FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
                case ChessConstants.BLACK_QUEENSIDE_ROOK_SQUARE -> castlingRights &= ~FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
                case ChessConstants.BLACK_KINGSIDE_ROOK_SQUARE -> castlingRights &= ~FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
            }
            gameState.setCastlingRights(castlingRights);
        }
        board.getPieces()[captureDestination] = null;
        return capturedPiece;
    }

    /**
     * Returns the board to the state before a move.
     * @param state the state the board is to be returned to.
     */
    public void unDoMove(State state) {
        int move = state.move();
        int moveFlag = move >> MoveFlags.FLAG_SHIFT;
        int destinationSquare = (move & MoveFlags.DESTINATION_MASK) >> MoveFlags.DESTINATION_SHIFT;
        int sourceSquare = move & MoveFlags.SOURCE_MASK;
        Piece[] pieces = board.getPieces();
        if ((moveFlag & MoveFlags.PROMOTION_BIT) ==  MoveFlags.PROMOTION_BIT) {
            pieces[sourceSquare] = new Pawn(pieces[destinationSquare].getColour(), sourceSquare, true);
        } else {
            pieces[destinationSquare].move(sourceSquare);
            pieces[sourceSquare] = pieces[destinationSquare];
            if (((moveFlag & MoveFlags.QUEENSIDE_CASTLE) == MoveFlags.QUEENSIDE_CASTLE) ||
                    ((moveFlag & MoveFlags.KINGSIDE_CASTLE) == MoveFlags.KINGSIDE_CASTLE)) {
                board.unDoCastleMovement(moveFlag, destinationSquare);
            }
        }
        pieces[destinationSquare] = null;
        if (state.capturedPiece() != null) {
            pieces[state.capturedPiece().getSquare()] = state.capturedPiece();
        }
        int sourceRank = SquareMapUtils.getRankContribution(sourceSquare);
        if (pieces[sourceSquare].getType() == PieceType.PAWN && (sourceRank == Ranks.TWO || sourceRank == Ranks.SEVEN)) {
            ((Pawn) pieces[sourceSquare]).setMoved(false);
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
