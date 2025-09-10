import java.util.Arrays;

public class Board {
    private final Piece[] pieces;
    private King whiteKing;
    private King blackKing;
    private boolean[] whiteThreatMap;
    private boolean[] blackThreatMap;

    /**
     * Constructs a board based on all the boards attributes.
     *
     * @param pieces         length 64 array of all the pieces on the board.
     * @param whiteThreatMap a boolean array of all squares white threatens.
     * @param blackThreatMap a boolean array of all squares black threatens.
     */
    public Board(Piece[] pieces, boolean[] whiteThreatMap, boolean[] blackThreatMap) {
        this.pieces = pieces;
        this.whiteThreatMap = whiteThreatMap;
        this.blackThreatMap = blackThreatMap;
        for (Piece piece : pieces) {
            if (piece != null && piece.getType() == PieceType.KING) {
                if (piece.getColour() == PieceColour.WHITE) {
                    whiteKing = (King) piece;
                    if (blackKing != null) {
                        break;
                    }
                } else {
                    blackKing = (King) piece;
                    if (whiteKing != null) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Search for if a piece exists on a given square
     *
     * @param square the target square
     * @return the piece if there is a piece on the square, otherwise null
     */
    public Piece pieceSearch(int square) {
        return pieces[square];
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
     * Utility method to return the king of the given colour.
     *
     * @param colour the king we want to return.
     * @return king of the given colour.
     */
    public King findKing(PieceColour colour) {
        return (colour == PieceColour.WHITE) ? whiteKing : blackKing;
    }

    /**
     * Finds the square of the piece being captured.
     * @param move the move of the capture.
     * @return the square of the captured piece.
     */
    public int getCaptureDestination(Move move) {
        int captureDestination;
        if (move.isEnPassant()) {
            int targetDirection = (move.getPiece().getColour() == PieceColour.WHITE) ? ChessDirections.DOWN : ChessDirections.UP;
            captureDestination = move.getDestination() + targetDirection;
        } else {
            captureDestination = move.getDestination();
        }
        return captureDestination;
    }

    /**
     * Handles the logic that is specific to castling.
     * Moves the rook to the correct square.
     * @param move the move being played.
     */
    public void handleCastleMovement(Move move) {
        int destination = move.getDestination();
        int file = SquareMapUtils.getFileContribution(destination);
        int rank = SquareMapUtils.getRankContribution(destination);
        int rookSquare;
        Rook rook;
        if (file == ChessConstants.KINGSIDE_CASTLE_FILE) {
            rookSquare = Files.H + rank;
            rook = (Rook) pieceSearch(rookSquare);
            pieces[rookSquare] = null;
            rook.move(ChessConstants.KINGSIDE_CASTLE_ROOK_FILE + rank);
            pieces[rook.getSquare()] = rook;
        } else if (file == ChessConstants.QUEENSIDE_CASTLE_FILE) {
            rookSquare = Files.A + rank;
            rook = (Rook) pieceSearch(rookSquare);
            pieces[rookSquare] = null;
            rook.move(ChessConstants.QUEENSIDE_CASTLE_ROOK_FILE + rank);
            pieces[rook.getSquare()] = rook;
        }
    }

    /**
     * Handles a promotion, replacing the pawn with a piece of the given type.
     *
     * @param move the promotion move to be carried out.
     */
    public void handlePromotion(Move move) {
        PieceColour colour = move.getPiece().getColour();
        Piece promotedPiece = switch (move.getPromotionType()) {
            case QUEEN -> new Queen(colour, move.getDestination());
            case ROOK -> new Rook(colour, move.getDestination());
            case BISHOP -> new Bishop(colour, move.getDestination());
            case KNIGHT -> new Knight(colour, move.getDestination());
            default -> null;
        };
        pieces[move.getDestination()] = promotedPiece; //put promoted piece on board
        pieces[move.getSource()] = null; //take the pawn off the board
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
                clonedPieces[squareIndex] = pieces[squareIndex].copyToSquare(squareIndex);
            }
        }
        return new Board(clonedPieces, whiteThreatMap.clone(), blackThreatMap.clone());
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
        return Arrays.equals(other.pieces, this.pieces) && Arrays.equals(other.whiteThreatMap, this.whiteThreatMap) &&
                Arrays.equals(other.blackThreatMap, this.blackThreatMap);
    }

    /**
     * Undoes the rook movement that occurs during castling.
     * @param state the state of the board before the move was called.
     */
    public void unDoCastleMovement(State state) {
        Piece movePiece = state.move().getPiece();
        int castleMask = state.move().getCastleMask();
        if ((castleMask & (FENConstants.WHITE_KINGSIDE_CASTLE_MASK | FENConstants.BLACK_KINGSIDE_CASTLE_MASK))
                != FENConstants.NO_CASTLING_MASK) {
            Rook rook =  ((Rook) pieceSearch(Files.F + SquareMapUtils.getRankContribution(movePiece.getSquare())));
            pieces[rook.getSquare()] = null;
            rook.move(Files.H + SquareMapUtils.getRankContribution(movePiece.getSquare()));
            pieces[rook.getSquare()] = rook;
        }
        else if ((castleMask & (FENConstants.WHITE_QUEENSIDE_CASTLE_MASK | FENConstants.BLACK_QUEENSIDE_CASTLE_MASK))
                != FENConstants.NO_CASTLING_MASK) {
            Rook rook =  ((Rook) pieceSearch(Files.D + SquareMapUtils.getRankContribution(movePiece.getSquare())));
            pieces[rook.getSquare()] = null;
            rook.move(Files.A + SquareMapUtils.getRankContribution(movePiece.getSquare()));
            pieces[rook.getSquare()] = rook;
        }
    }

    /**
     * Resets the threat maps to given threat maps to avoid having to recompute them when undoing a move.
     * @param whiteThreatMap boolean array for all pieces capturable by white.
     * @param blackThreatMap boolean array for all pieces capturable by black.
     */
    public void resetThreatMaps(boolean[] whiteThreatMap, boolean[] blackThreatMap) {
        this.whiteThreatMap = whiteThreatMap;
        this.blackThreatMap = blackThreatMap;
    }
}