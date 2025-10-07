import java.util.Arrays;

public class Board {
    private final Piece[] pieces;
    private King whiteKing;
    private King blackKing;
    private long whiteThreatMap;
    private long blackThreatMap;
    private long[] ATKTO;
    private long[] ATKFR;
    private long whiteBitboard;
    private long blackBitboard;

    /**
     * Constructs a board based on all the boards attributes.
     *
     * @param pieces         length 64 array of all the pieces on the board.
     * @param whiteThreatMap long of all squares white threatens.
     * @param blackThreatMap long of all squares black threatens.
     */
    public Board(Piece[] pieces, long whiteThreatMap, long blackThreatMap) {
        this.pieces = pieces;
        this.whiteThreatMap = whiteThreatMap;
        this.blackThreatMap = blackThreatMap;
        this.whiteBitboard = 0L;
        this.blackBitboard = 0L;
        for (Piece piece : pieces) {
            if (piece != null) {
                if (piece.getColour() == PieceColour.WHITE) {
                    this.whiteBitboard |= 1L << piece.getSquare();
                    if (piece.getType() == PieceType.KING) {
                        whiteKing = (King) piece;
                    }
                } else {
                    this.blackBitboard |= 1L << piece.getSquare();
                    if (piece.getType() == PieceType.KING) {
                        blackKing = (King) piece;
                    }
                }
            }
        }
        ATKTO = new long[ChessConstants.NUM_SQUARES];
        ATKFR = new long[ChessConstants.NUM_SQUARES];
        AttackTables.setUpAttackTables(this);
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
     * @param moveFlag flag containing whether a move is a capture or en passant and other move information.
     * @param destinationSquare the destination of a move.
     * @param sourceSquare the square of the piece moving.
     * @return the square of the captured piece.
     */
    public int getCaptureDestination(int moveFlag, int destinationSquare, int sourceSquare) {
        int captureDestination;
        if ((moveFlag & MoveFlags.EN_PASSANT) == MoveFlags.EN_PASSANT &&
                (moveFlag & MoveFlags.PROMOTION_BIT) != MoveFlags.PROMOTION_BIT) { //move is an en passant capture
            int targetDirection = (pieces[sourceSquare].getColour() == PieceColour.WHITE) ? ChessDirections.DOWN : ChessDirections.UP;
            captureDestination = destinationSquare + targetDirection;
        } else {
            captureDestination = destinationSquare;
        }
        return captureDestination;
    }

    /**
     * Handles the logic that is specific to castling.
     * Moves the rook to the correct square.
     * @param moveFlag contains a flag bit for if the move is a king or queenside castle.
     * @param destinationSquare resultant square of the king.
     */
    public void handleCastleMovement(int moveFlag, int destinationSquare) {
        int rank = SquareMapUtils.getRankContribution(destinationSquare);
        int rookSourceSquare, rookDestinationSquare;
        if ((moveFlag & MoveFlags.QUEENSIDE_CASTLE) == MoveFlags.QUEENSIDE_CASTLE) { //queenside castle
            rookSourceSquare = ChessConstants.QUEENSIDE_ROOK_SOURCE_FILE + rank;
            rookDestinationSquare = ChessConstants.QUEENSIDE_CASTLE_ROOK_FILE + rank;
        } else { //kingside castle
            rookSourceSquare = ChessConstants.KINGSIDE_ROOK_SOURCE_FILE + rank;
            rookDestinationSquare = ChessConstants.KINGSIDE_CASTLE_ROOK_FILE + rank;
        }
        movePiece(rookSourceSquare, rookDestinationSquare);
    }

    /**
     * Handles a promotion, replacing the pawn with a piece of the given type.
     * @param moveFlag contains a flag bit for which piece the pawn promotes to.
     * @param destinationSquare the square the pawn is promoted on.
     * @param sourceSquare the square the pawn moved from.
     */
    public void handlePromotion(int moveFlag, int destinationSquare, int sourceSquare) {
        pieces[destinationSquare] = switch (moveFlag & MoveFlags.PROMOTION_MASK) {
            case MoveFlags.QUEEN -> new Queen(pieces[sourceSquare].getColour(), destinationSquare);
            case MoveFlags.ROOK -> new Rook(pieces[sourceSquare].getColour(), destinationSquare);
            case MoveFlags.BISHOP -> new Bishop(pieces[sourceSquare].getColour(), destinationSquare);
            default -> new Knight(pieces[sourceSquare].getColour(), destinationSquare); //knight flag is equal to 0 so this is our base case
        };
        if (destinationSquare < Ranks.SEVEN) { //white is promoting
            whiteBitboard &= ~(1L << sourceSquare);
            whiteBitboard |= (1L << destinationSquare);
        } else {
            blackBitboard &= ~(1L << sourceSquare);
            blackBitboard |= (1L << destinationSquare);
        }
        pieces[sourceSquare] = null; //take the pawn off the board
    }

    /**
     * Undoes a promotion move.
     * @param destinationSquare the destination of the promotion move.
     * @param sourceSquare the source of the promotion move.
     */
    public void unDoPromotion(int destinationSquare, int sourceSquare) {
        pieces[sourceSquare] = new Pawn(pieces[destinationSquare].getColour(), sourceSquare, true);
        pieces[destinationSquare] = null;
        if (destinationSquare < Ranks.SEVEN) { //white is promoting
            whiteBitboard &= ~(1L << destinationSquare);
            whiteBitboard |= (1L << sourceSquare);
        } else {
            blackBitboard &= ~(1L << destinationSquare);
            blackBitboard |= (1L << sourceSquare);
        }
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
     * @return long of whether the given colour threatens each square.
     */
    public long getThreatMap(PieceColour colour) {
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
        return new Board(clonedPieces, whiteThreatMap, blackThreatMap);
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
        return Arrays.equals(other.pieces, this.pieces) && other.whiteThreatMap == this.whiteThreatMap &&
                other.blackThreatMap == this.blackThreatMap && Arrays.equals(other.ATKFR, this.ATKFR) &&
                Arrays.equals(other.ATKTO, this.ATKTO) && this.whiteBitboard == other.whiteBitboard &&
                this.blackBitboard == other.blackBitboard;
    }

    /**
     * Undoes the rook movement that occurs during castling.
     * @param moveFlag 4 bit flag containing a bit for whether the castle is king or queenside.
     * @param destinationSquare the square the king moved to.
     */
    public void unDoCastleMovement(int moveFlag, int destinationSquare) {
        int rank  = SquareMapUtils.getRankContribution(destinationSquare);
        int rookSourceSquare, rookDestinationSquare;
        if ((moveFlag & MoveFlags.QUEENSIDE_CASTLE) ==  MoveFlags.QUEENSIDE_CASTLE) { //queenside castle
            rookDestinationSquare = ChessConstants.QUEENSIDE_CASTLE_ROOK_FILE + rank;
            rookSourceSquare = ChessConstants.QUEENSIDE_ROOK_SOURCE_FILE + rank;
        }
        else { //kingside castle
            rookDestinationSquare = ChessConstants.KINGSIDE_CASTLE_ROOK_FILE + rank;
            rookSourceSquare = ChessConstants.KINGSIDE_ROOK_SOURCE_FILE + rank;
        }
        movePiece(rookDestinationSquare, rookSourceSquare);
    }

    /**
     * Resets the threat maps to given threat maps to avoid having to recompute them when undoing a move.
     * @param whiteThreatMap long for all squares threatened by white.
     * @param blackThreatMap long for all squares threatened by black.
     */
    public void resetThreatMaps(long whiteThreatMap, long blackThreatMap) {
        this.whiteThreatMap = whiteThreatMap;
        this.blackThreatMap = blackThreatMap;
    }

    /**
     * Simple getter for the attack from table.
     * @return long for each square of the board where each bit denotes a piece attacking the square.
     */
    public long[] getATKFR() {
        return ATKFR;
    }

    /**
     * Simple getter for the attack to table.
     * @return long for each square of the board where each bit denotes a square the piece there can attack.
     */
    public long[] getATKTO() {
        return ATKTO;
    }

    /**
     * Simple setter for the attack from and attack to tables.
     * @param ATKFR table of all squares each square is attacked by.
     * @param ATKTO table of all squares the each square attacks.
     */
    public void setAttackTables(long[] ATKFR, long[] ATKTO) {
        this.ATKFR = ATKFR;
        this.ATKTO = ATKTO;
    }

    /**
     * Checks to see if a square is attacked by a piece of the given colour.
     * @param square the square to check.
     * @param colour the colour to look for attacks from.
     * @return true if the colour attacks the square, otherwise false.
     */
    public boolean isAttackedBy(int square, PieceColour colour) {
        if (ATKTO[square] == 0) { //no attacks
            return false;
        }
        long attackedBy = ATKTO[square];
        int attackSourceSquare;
        while (attackedBy != 0) { //for each bit in the attacks to the square we look to see the colour of the piece
            attackSourceSquare = Long.numberOfTrailingZeros(attackedBy);
            if (pieces[attackSourceSquare].getColour() == colour) {
                return true;
            }
            attackedBy &= attackedBy - 1;
        }
        return false;
    }

    /**
     * Simple helper method to take a piece off of the board.
     * @param square the square of the piece to be removed.
     * @return the piece (so it can be added back if needed).
     */
    public Piece removePiece(int square) {
        Piece piece = pieces[square];
        pieces[square] = null;
        if (piece.getColour() == PieceColour.WHITE) {
            whiteBitboard &= ~(1L << square);
        } else {
            blackBitboard &= ~(1L << square);
        }
        return piece;
    }

    /**
     * Simple helper method to add a piece to the board.
     * @param square the square of the piece to be added.
     * @param piece the piece to be added.
     */
    public void addPiece(int square, Piece piece) {
        pieces[square] = piece;
        if (piece.getColour() == PieceColour.WHITE) {
            whiteBitboard |= (1L << square);
        } else {
            blackBitboard |= (1L << square);
        }
    }

    /**
     * Simple helper method to move a piece from one square to another.
     * @param sourceSquare where the piece is moved from.
     * @param destinationSquare where the piece is moved to.
     */
    public void movePiece(int sourceSquare, int destinationSquare) {
        pieces[sourceSquare].move(destinationSquare);
        pieces[destinationSquare] = pieces[sourceSquare];
        pieces[sourceSquare] = null;
        if (pieces[destinationSquare].getColour() == PieceColour.WHITE) {
            whiteBitboard &= ~(1L << sourceSquare);
            whiteBitboard |= (1L << destinationSquare);
        } else {
            blackBitboard &= ~(1L << sourceSquare);
            blackBitboard |= (1L << destinationSquare);
        }
    }

    /**
     * Simple getter for the white bit board.
     * @return long where each bit equal to 1 is a white piece.
     */
    public long getWhiteBitboard() {
        return whiteBitboard;
    }

    /**
     * Simple getter for the black bit board.
     * @return long where each bit equal to 1 is a black piece.
     */
    public long getBlackBitboard() {
        return blackBitboard;
    }
}