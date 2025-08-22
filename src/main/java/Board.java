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
     *
     * @param whitePieces    array of all the white pieces on the board.
     * @param blackPieces    array of all the black pieces on the board.
     * @param turn           the current colour whose turn it is.
     * @param moveCount      the current move.
     * @param halfMoveClock  number of half moves since a capture or pawn move.
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
        for (Piece piece : whitePieces) {
            if (piece != null && piece.getSquare().equals(square)) {
                return piece;
            }
        }
        for (Piece piece : blackPieces) {
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
     *
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
     * @return the state of the board before the move.
     */
    public State doMove(Move move) {
        Piece capturedPiece = null;
        String enPassantTarget = getEnPassantTarget();
        int castlingRights = getCastlingRights();
        int halfMoveClock = getHalfMoveClock();
        int moveCount = getMoveCount();
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
            capturedPiece = handleCaptureMove(move);
        }
        if (move.isCastle()) {
            handleCastleMove(move);
        }
        if (move.getPromotionType() != null) {
            handlePromotion(move);
        } else {
            move.getPiece().move(move.getDestination());
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
        return new State(move, capturedPiece, enPassantTarget, castlingRights, halfMoveClock, moveCount);
    }

    /**
     * Handles the logic that is specific to a capture.
     * Removes the captured piece from the relevant pieces array.
     * @param move the move being played.
     * @return boolean flag for if the capture logic was completed successfully.
     */
    public Piece handleCaptureMove(Move move) {
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
            }
        }
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
     */
    public void handleCastleMove(Move move) {
        String destination = move.getDestination();
        char file = destination.charAt(0);
        int rank = destination.charAt(1) - '0';
        String rookSquare;
        Rook rook;
        if (file == 'g') {
            rookSquare = "h" + rank;
            rook = (Rook) pieceSearch(rookSquare);
            rook.move("f" + rank);
        } else if (file == 'c') {
            rookSquare = "a" + rank;
            rook = (Rook) pieceSearch(rookSquare);
            rook.move("d" + rank);
        }
    }

    /**
     * Handles a promotion, replacing the pawn with a piece of the given type.
     *
     * @param move the promotion move to be carried out.
     */
    public void handlePromotion(Move move) {
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
        Piece promotingPiece = pieceSearch(move.getPiece().getSquare());
        if (colour == PieceColour.WHITE) {
            for (int i = 0; i < ChessConstants.NUM_PIECES / 2; i++) {
                if (whitePieces[i] == promotingPiece) {
                    whitePieces[i] = promotedPiece;
                }
            }
        } else {
            for (int i = 0; i < ChessConstants.NUM_PIECES / 2; i++) {
                if (blackPieces[i] == promotingPiece) {
                    blackPieces[i] = promotedPiece;
                }
            }
        }
    }

    /**
     * Generates all the moves for a given colour.
     *
     * @param colour the colour we want to generate moves for.
     * @return an array of all the moves the colour can do.
     */
    public Move[] generateMoves(PieceColour colour) {
        int moveCount = 0;
        Piece[] pieces;
        if (colour == PieceColour.WHITE) {
            pieces = whitePieces;
        } else {
            pieces = blackPieces;
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
        Piece[] clonedWhitePieces = new Piece[ChessConstants.NUM_PIECES / 2];
        Piece[] clonedBlackPieces = new Piece[ChessConstants.NUM_PIECES / 2];
        for (int i = 0; i < ChessConstants.NUM_PIECES / 2; i++) {
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

    /**
     * Updates the moved booleans for relevant pieces to see if they can castle.
     *
     * @param rights 4 bit mask for each castling right.
     */
    public void setCastlingRights(int rights) {
        if ((rights & FENConstants.WHITE_KINGSIDE_CASTLE_MASK) != FENConstants.NO_CASTLE_MASK) {
            ((King) pieceSearch("e1")).setMoved(false);
            ((Rook) pieceSearch("h1")).setMoved(false);
        }
        if ((rights & FENConstants.WHITE_QUEENSIDE_CASTLE_MASK) != FENConstants.NO_CASTLE_MASK) {
            ((King) pieceSearch("e1")).setMoved(false);
            ((Rook) pieceSearch("a1")).setMoved(false);
        }
        if ((rights & FENConstants.BLACK_KINGSIDE_CASTLE_MASK) != FENConstants.NO_CASTLE_MASK) {
            ((King) pieceSearch("e8")).setMoved(false);
            ((Rook) pieceSearch("h8")).setMoved(false);
        }
        if ((rights & FENConstants.BLACK_QUEENSIDE_CASTLE_MASK) != FENConstants.NO_CASTLE_MASK) {
            ((King) pieceSearch("e8")).setMoved(false);
            ((Rook) pieceSearch("a8")).setMoved(false);
        }
    }

    /**
     * Updates the en passantable flag of the pawn that can currently be taken by en passant.
     *
     * @param enPassantTarget the en passant target square.
     */
    public void setEnPassantFlag(String enPassantTarget) {
        if (enPassantTarget.equals(FENConstants.NONE)) {
            return;
        }
        char enPassantTargetFile = enPassantTarget.charAt(0);
        int enPassantTargetRank = enPassantTarget.charAt(1) - '0';
        int direction;
        if (enPassantTargetRank == 3) { //it must be white if target square is the third rank
            direction = ChessDirections.UP;
        } else {
            direction = ChessDirections.DOWN;
        }
        String square = enPassantTargetFile + String.valueOf(enPassantTargetRank + direction);
        ((Pawn) pieceSearch(square)).setEnPassantable(true);
    }

    /**
     * Calculates whether a given side can castle on king and or queenside.
     *
     * @param colour the colour the castling rights are for.
     * @return Returns a 2 bit mask of the castling rights for a given colour.
     */
    public int getColourCastlingRights(PieceColour colour) {
        King king = findKing(colour);
        if (king == null) {
            return FENConstants.NO_CASTLE_MASK; //if there is no king there is no castling (only occurs in testing positions)
        }
        int kingsideCastle = FENConstants.NO_CASTLE_MASK;
        int queensideCastle = FENConstants.NO_CASTLE_MASK;
        if (!king.getMoved()) {
            int rank = king.getRank();
            Piece piece = pieceSearch("a" + rank);
            if (piece instanceof Rook rook && !rook.getMoved()) {
                queensideCastle = (colour == PieceColour.WHITE ? FENConstants.WHITE_QUEENSIDE_CASTLE_MASK : FENConstants.BLACK_QUEENSIDE_CASTLE_MASK);
            }
            piece = pieceSearch("h" + rank);
            if (piece instanceof Rook rook && !rook.getMoved()) {
                kingsideCastle = (colour == PieceColour.WHITE ? FENConstants.WHITE_KINGSIDE_CASTLE_MASK : FENConstants.BLACK_KINGSIDE_CASTLE_MASK);
            }
        }
        return (kingsideCastle | queensideCastle);
    }

    /**
     * Gets the whole boards castling rights.
     *
     * @return 4 bit mask of the board's castling rights.
     */
    public int getCastlingRights() {
        return (getColourCastlingRights(PieceColour.WHITE) | getColourCastlingRights(PieceColour.BLACK));
    }

    /**
     * Calculates the en passant target square in the position.
     * @return the current en passant target square according to FEN notation.
     */
    public String getEnPassantTarget() {
        for (Piece piece : whitePieces) {
            if (piece == null || piece.getType() != PieceType.PAWN) {
                continue;
            }
            if (((Pawn) piece).getEnPassantable()) {
                return piece.getFile() + String.valueOf(piece.getRank() + ChessDirections.DOWN);
            }
        }
        for (Piece piece : blackPieces) {
            if (piece == null || piece.getType() != PieceType.PAWN) {
                continue;
            }
            if (((Pawn) piece).getEnPassantable()) {
                return piece.getFile() + String.valueOf(piece.getRank() + ChessDirections.UP);
            }
        }
        return FENConstants.NONE;
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
        if (state.capturedPiece() != null) {
            Piece[] opponentPieces;
            if (state.capturedPiece().getColour() == PieceColour.WHITE) {
                opponentPieces = getWhitePieces();
            } else {
                opponentPieces = getBlackPieces();
            }
            for (int i = 0; i < ChessConstants.NUM_PIECES/2; i++) { //add captured piece back to relevant piece array
                if (opponentPieces[i] == null) {
                    opponentPieces[i] = state.capturedPiece();
                }
            }
        }
        if (state.move().getPromotionType() != null) {
            Piece[] pieces;
            if (movePiece.getColour() == PieceColour.WHITE) {
                pieces = getWhitePieces();
            } else {
                pieces = getBlackPieces();
            }
            for (int i = 0; i < ChessConstants.NUM_PIECES/2; i++) {
                if (pieces[i] != null && state.move().getDestination().equals(pieces[i].getSquare())) {
                    Piece oldPiece = state.move().getPiece();
                    pieces[i] = new Pawn(oldPiece.getColour(), oldPiece.getFile(), oldPiece.getRank(), true, false);
                }
            }
        }
        if (state.move().isCastle()) {
            if (state.move().getDestination().charAt(0) == 'g') { //kingside
                Rook rook =  ((Rook) pieceSearch("f" + movePiece.getRank()));
                rook.move("h" + movePiece.getRank());
                rook.setMoved(false);

            }
            else if (state.move().getDestination().charAt(0) == 'c') { //queenside
                Rook rook =  ((Rook) pieceSearch("d" + movePiece.getRank()));
                rook.move("a" + movePiece.getRank());
                rook.setMoved(false);
            }
        }
        movePiece.move(state.move().getSource());
        if (!(movePiece.getType() != PieceType.PAWN || (state.move().getSource().charAt(1) - '0' != 2 || state.move().getSource().charAt(1) - '0' != 7))) {
            ((Pawn) movePiece).setMoved(false);
        }
        setHalfMoveClock(state.halfMoveClock());
        setMoveCount(state.moveCount());
        setCastlingRights(state.castlingRights());
        setEnPassantFlag(state.enPassantSquare());
        turn = turn.opponentColour();
    }
}