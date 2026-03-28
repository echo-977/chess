import java.util.Arrays;

public class Board {
    private final Piece[] pieces;
    private King whiteKing;
    private King blackKing;
    private long[] ATKTO;
    private long[] ATKFR;
    private final long[] occupancyBitboards;

    /**
     * Constructs a board based on all the boards attributes.
     *
     * @param pieces length 64 array of all the pieces on the board.
     */
    public Board(Piece[] pieces) {
        this.pieces = pieces;
        occupancyBitboards = new long[ChessConstants.NUM_OCCUPANCIES];
        for (Piece piece : pieces) {
            if (piece != null) {
                if (piece.getColour() == PieceColour.WHITE) {
                    occupancyBitboards[ChessConstants.WHITE_BITBOARD] ^= 1L << piece.getSquare();
                    if (piece.getType() == PieceType.KING) {
                        whiteKing = (King) piece;
                    }
                } else {
                    occupancyBitboards[ChessConstants.BLACK_BITBOARD] ^= 1L << piece.getSquare();
                    if (piece.getType() == PieceType.KING) {
                        blackKing = (King) piece;
                    }
                }
            }
        }
        updateCombinedOccupancyBitboard();
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
            occupancyBitboards[ChessConstants.WHITE_BITBOARD] ^= (1L << sourceSquare) | (1L << destinationSquare);
        } else {
            occupancyBitboards[ChessConstants.BLACK_BITBOARD] ^= (1L << sourceSquare) | (1L << destinationSquare);
        }
        pieces[sourceSquare] = null; //take the pawn off the board
    }

    /**
     * Undoes a promotion move.
     * @param destinationSquare the destination of the promotion move.
     * @param sourceSquare the source of the promotion move.
     */
    public void unDoPromotion(int destinationSquare, int sourceSquare) {
        pieces[sourceSquare] = new Pawn(pieces[destinationSquare].getColour(), sourceSquare);
        pieces[destinationSquare] = null;
        if (destinationSquare < Ranks.SEVEN) { //white is promoting
            occupancyBitboards[ChessConstants.WHITE_BITBOARD] ^= (1L << destinationSquare) | (1L << sourceSquare);
        } else {
            occupancyBitboards[ChessConstants.BLACK_BITBOARD] ^= (1L << destinationSquare) | (1L << sourceSquare);
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
        return new Board(clonedPieces);
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
        return Arrays.equals(other.pieces, this.pieces) && Arrays.equals(other.ATKFR, this.ATKFR) &&
                Arrays.equals(other.ATKTO, this.ATKTO) &&
                Arrays.equals(other.occupancyBitboards, this.occupancyBitboards);
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
        if (colour == PieceColour.WHITE) { //white piece matching square that attacks the given square
            return (ATKTO[square] & occupancyBitboards[ChessConstants.WHITE_BITBOARD]) != 0;
        } else {
            return (ATKTO[square] & occupancyBitboards[ChessConstants.BLACK_BITBOARD]) != 0;
        }
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
            occupancyBitboards[ChessConstants.WHITE_BITBOARD] ^= (1L << square);
        } else {
            occupancyBitboards[ChessConstants.BLACK_BITBOARD] ^= (1L << square);
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
            occupancyBitboards[ChessConstants.WHITE_BITBOARD] ^= (1L << square);
        } else {
            occupancyBitboards[ChessConstants.BLACK_BITBOARD] ^= (1L << square);
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
            occupancyBitboards[ChessConstants.WHITE_BITBOARD] ^= (1L << sourceSquare) | (1L << destinationSquare);
        } else {
            occupancyBitboards[ChessConstants.BLACK_BITBOARD] ^= (1L << sourceSquare) | (1L << destinationSquare);
        }
    }

    /**
     * Simple getter for an occupancy bitboard.
     * @param bitboardIndex the index of the desired occupancy bitboard.
     * @return long where each bit equal to 1 is a white piece.
     */
    public long getOccupancyBitboard(int bitboardIndex) {
        return occupancyBitboards[bitboardIndex];
    }

    /**
     * Sets the bitboard for the union of both pieces.
     */
    public void updateCombinedOccupancyBitboard() {
        occupancyBitboards[ChessConstants.BOTH_BITBOARD] =
                occupancyBitboards[ChessConstants.WHITE_BITBOARD] | occupancyBitboards[ChessConstants.BLACK_BITBOARD];
    }
}