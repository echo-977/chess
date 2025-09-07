import java.util.Arrays;

public class Board {
    private final Piece[] pieces;
    private PieceColour turn;
    private int moveCount;
    private int halfMoveClock;
    private boolean[] whiteThreatMap;
    private boolean[] blackThreatMap;
    private int castlingRights;
    private String enPassantTarget;

    /**
     * Constructs a board based on all the boards attributes.
     *
     * @param pieces         length 64 array of all the pieces on the board.
     * @param turn           the current colour whose turn it is.
     * @param moveCount      the current move.
     * @param halfMoveClock  number of half moves since a capture or pawn move.
     * @param whiteThreatMap boolean array of all squares white threatens.
     * @param blackThreatMap boolean array of all squares black threatens.
     */
    public Board(Piece[] pieces, PieceColour turn, int moveCount, int halfMoveClock,
                 boolean[] whiteThreatMap, boolean[] blackThreatMap) {
        this.pieces = pieces;
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
        return pieces[SquareMapUtils.mapSquareToInt(square)];
    }

    /**
     * Simple getter for the pieces
     *
     * @return piece array of all the white pieces
     */
    public Piece[] getPieces() {
        return pieces;
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
     *
     * @param colour the king we want to return.
     * @return king of the given colour.
     */
    public King findKing(PieceColour colour) {
        for (int index = 0; index < ChessConstants.NUM_SQUARES; index++) {
            if (pieces[index] != null && pieces[index].getType() == PieceType.KING && pieces[index].getColour() == colour) {
                return (King) pieces[index];
            }
        }
        return null;
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
        String  enPassantSquare = getEnPassantTarget();
        int castlingRights = getCastlingRights();
        int halfMoveClock = getHalfMoveClock();
        int moveCount = getMoveCount();
        boolean[] threatMapWhite = whiteThreatMap;
        boolean[] threatMapBlack = blackThreatMap;
        if (movePieceType == PieceType.PAWN &&
                Math.abs(movePiece.getRank() - SquareMapUtils.getRank(move.getDestination())) == 2) {
            int direction = (turn == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
            String enPassantTarget = movePiece.getFile() + String.valueOf(movePiece.getRank() + direction);
            setEnPassantTarget(enPassantTarget);
        } else {
            setEnPassantTarget(FENConstants.NONE);
        }
        if (move.isCapture()) {
            capturedPiece = handleCaptureMove(move);
        }
        if (move.getCastleMask() != FENConstants.NO_CASTLING_MASK) {
            handleCastleMove(move);
        }
        if (movePieceType == PieceType.KING | movePieceType == PieceType.ROOK) {
            removeCastlingRights(move);
        }
        if (move.getPromotionType() != null) {
            handlePromotion(move);
        } else {
            move.getPiece().move(move.getDestination());
            pieces[SquareMapUtils.mapSquareToInt(move.getDestination())] = move.getPiece();
            pieces[SquareMapUtils.mapSquareToInt(move.getSource())] = null;
        }
        if (!(move.getPiece().getType() == PieceType.PAWN || move.isCapture())) {
            this.halfMoveClock++;
        } else {
            this.halfMoveClock = 0;
        }
        if (getTurn() == PieceColour.BLACK) {
            this.moveCount++;
        }
        turn = turn.opponentColour();
        whiteThreatMap = ThreatMapGenerator.getThreatMap(this, PieceColour.WHITE);
        blackThreatMap = ThreatMapGenerator.getThreatMap(this, PieceColour.BLACK);
        return new State(move, capturedPiece, enPassantSquare, castlingRights, halfMoveClock, moveCount,
                threatMapWhite, threatMapBlack);
    }

    /**
     * Handle the removal of castling rights if the move uses a king or rook.
     * @param move the move that causes castling rights to need removal.
     */
    private void removeCastlingRights(Move move) {
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
            if (movePiece.getFile() == 'h') {
                if (movePiece.getColour() == PieceColour.WHITE) {
                    castlingRights &= ~FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
                } else {
                    castlingRights &= ~FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
                }
            } else if (movePiece.getFile() == 'a') {
                if (movePiece.getColour() == PieceColour.WHITE) {
                    castlingRights &= ~FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
                } else {
                    castlingRights &= ~FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
                }
            }
        }
    }

    /**
     * Handles the logic that is specific to a capture.
     * Removes the captured piece from the relevant pieces array.
     * If the captured piece is a rook it removes the relevant castling right.
     * @param move the move being played.
     * @return boolean flag for if the capture logic was completed successfully.
     */
    public Piece handleCaptureMove(Move move) {
        String captureDestination = getCaptureDestination(move);
        Piece capturedPiece = pieceSearch(captureDestination);
        if (capturedPiece.getType() == PieceType.ROOK) {
            switch(capturedPiece.getSquare()) {
                case "a1" -> castlingRights &= ~FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
                case "h1" -> castlingRights &= ~FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
                case "a8" -> castlingRights &= ~FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
                case "h8" -> castlingRights &= ~FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
            }
        }
        pieces[SquareMapUtils.mapSquareToInt(captureDestination)] = null;
        return capturedPiece;
    }

    /**
     * Finds the square of the piece being captured.
     * @param move the move of the capture.
     * @return the square of the captured piece.
     */
    public String getCaptureDestination(Move move) {
        String captureDestination = move.getDestination();
        if (move.isEnPassant()) {
            int targetDirection;
            if (move.getPiece().getColour() == PieceColour.WHITE) {
                targetDirection = ChessDirections.DOWN;
            } else {
                targetDirection = ChessDirections.UP;
            }
            char file = SquareMapUtils.getFile(move.getDestination());
            int rank = SquareMapUtils.getRank(move.getDestination());
            int captureRank = rank + targetDirection;
            captureDestination = file + String.valueOf(captureRank);
        }
        return captureDestination;
    }

    /**
     * Handles the logic that is specific to castling.
     * Moves the rook to the correct square.
     * @param move the move being played.
     */
    public void handleCastleMove(Move move) {
        String destination = move.getDestination();
        char file = SquareMapUtils.getFile(destination);
        int rank = SquareMapUtils.getRank(destination);
        String rookSquare;
        Rook rook;
        if (file == 'g') {
            rookSquare = "h" + rank;
            rook = (Rook) pieceSearch(rookSquare);
            pieces[SquareMapUtils.mapSquareToInt(rookSquare)] = null;
            rook.move("f" + rank);
            pieces[SquareMapUtils.mapSquareToInt(rook.getSquare())] = rook;
        } else if (file == 'c') {
            rookSquare = "a" + rank;
            rook = (Rook) pieceSearch(rookSquare);
            pieces[SquareMapUtils.mapSquareToInt(rookSquare)] = null;
            rook.move("d" + rank);
            pieces[SquareMapUtils.mapSquareToInt(rook.getSquare())] = rook;
        }
    }

    /**
     * Handles a promotion, replacing the pawn with a piece of the given type.
     *
     * @param move the promotion move to be carried out.
     */
    public void handlePromotion(Move move) {
        PieceColour colour = move.getPiece().getColour();
        char file = SquareMapUtils.getFile(move.getDestination());
        int rank = SquareMapUtils.getRank(move.getDestination());
        Piece promotedPiece = switch (move.getPromotionType()) {
            case QUEEN -> new Queen(colour, file, rank);
            case ROOK -> new Rook(colour, file, rank);
            case BISHOP -> new Bishop(colour, file, rank);
            case KNIGHT -> new Knight(colour, file, rank);
            default -> null;
        };
        pieces[SquareMapUtils.mapSquareToInt(move.getDestination())] = promotedPiece; //put promoted piece on board
        pieces[SquareMapUtils.mapSquareToInt(move.getSource())] = null; //take the pawn off the board
    }

    /**
     * Generates all the moves for a given colour.
     *
     * @param colour the colour we want to generate moves for.
     * @return an array of all the moves the colour can do.
     */
    public Move[] generateMoves(PieceColour colour) {
        int moveCount = 0;
        Move[][] allMoves = new Move[ChessConstants.NUM_SQUARES][];
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != colour) {
                allMoves[squareIndex] = new Move[0];
                continue;
            }
            allMoves[squareIndex] = pieces[squareIndex].generateMoves(this);
            for (Move move : allMoves[squareIndex]) {
                if (move != null) {
                    moveCount++;
                }
            }
        }
        Move[] moves = new Move[moveCount];
        moveCount = 0;
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES ; squareIndex++) {
            if (pieces[squareIndex] == null || pieces[squareIndex].getColour() != colour) {
                continue;
            }
            for (Move move : allMoves[squareIndex]) {
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
     *
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
     *
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
     *
     * @return a functionally identical board.
     */
    public Board copy() {
        Piece[] clonedPieces = new Piece[ChessConstants.NUM_SQUARES];
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (pieces[squareIndex] != null) {
                clonedPieces[squareIndex] = pieces[squareIndex].copyToSquare(pieces[squareIndex].getSquare());
            }
        }
        Board board = new Board(clonedPieces, turn, moveCount, halfMoveClock, whiteThreatMap.clone(), blackThreatMap.clone());
        board.setCastlingRights(castlingRights);
        board.setEnPassantTarget(enPassantTarget);
        return board;
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
        return Arrays.equals(other.pieces, this.pieces) && other.turn == this.turn
                && other.moveCount == this.moveCount && other.halfMoveClock == this.halfMoveClock &&
                Arrays.equals(other.whiteThreatMap, this.whiteThreatMap) &&
                Arrays.equals(other.blackThreatMap, this.blackThreatMap);
    }

    /**
     * Saves the given castling rights to the board.
     *
     * @param rights 4 bit mask for each castling right.
     */
    public void setCastlingRights(int rights) {
        castlingRights = rights;
    }

    /**
     * Saves the given en passant target to the board.
     *
     * @param enPassantTarget the en passant target square.
     */
    public void setEnPassantTarget(String enPassantTarget) {
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
    public String getEnPassantTarget() {
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
     * Returns the board to the state before a move.
     * @param state the state the board is to be returned to.
     */
    public void unDoMove(State state) {
        Piece movePiece = state.move().getPiece();
        pieces[SquareMapUtils.mapSquareToInt(state.move().getDestination())] = null;
        if (state.move().getPromotionType() == null) {
            movePiece.move(state.move().getSource());
            if (movePiece.getType() == PieceType.PAWN &&
                    (SquareMapUtils.getRank(state.move().getSource()) == 2 ||
                            SquareMapUtils.getRank(state.move().getSource()) == 7)) {
                ((Pawn) movePiece).setMoved(false);
            }
        }
        if (state.capturedPiece() != null) {
            pieces[SquareMapUtils.mapSquareToInt(state.capturedPiece().getSquare())] = state.capturedPiece();
        }
        if (state.move().getCastleMask() != FENConstants.NO_CASTLING_MASK) {
            unDoCastle(state);
        }
        pieces[SquareMapUtils.mapSquareToInt(state.move().getSource())] = state.move().getPiece();
        setHalfMoveClock(state.halfMoveClock());
        setMoveCount(state.moveCount());
        setCastlingRights(state.castlingRights());
        setEnPassantTarget(state.enPassantSquare());
        turn = turn.opponentColour();
        whiteThreatMap = state.whiteThreatMap();
        blackThreatMap = state.blackThreatMap();
    }

    /**
     * Undoes the rook movement that occurs during castling.
     * @param state the state of the board before the move was called.
     */
    private void unDoCastle(State state) {
        Piece movePiece = state.move().getPiece();
        int castleMask = state.move().getCastleMask();
        if ((castleMask & (FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.BLACK_KINGSIDE_CASTLE_MASK))
                != FENConstants.NO_CASTLING_MASK) {
            Rook rook =  ((Rook) pieceSearch("f" + movePiece.getRank()));
            pieces[SquareMapUtils.mapSquareToInt(rook.getSquare())] = null;
            rook.move("h" + movePiece.getRank());
            pieces[SquareMapUtils.mapSquareToInt(rook.getSquare())] = rook;
        }
        else if ((castleMask & (FENConstants.WHITE_QUEENSIDE_CASTLE_MASK | FENConstants.BLACK_QUEENSIDE_CASTLE_MASK))
                != FENConstants.NO_CASTLING_MASK) {
            Rook rook =  ((Rook) pieceSearch("d" + movePiece.getRank()));
            pieces[SquareMapUtils.mapSquareToInt(rook.getSquare())] = null;
            rook.move("a" + movePiece.getRank());
            pieces[SquareMapUtils.mapSquareToInt(rook.getSquare())] = rook;
        }
    }
}