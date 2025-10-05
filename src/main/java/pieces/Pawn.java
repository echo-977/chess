import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Pawn extends Piece{
    private boolean moved;
    private final int moveDirection;

    /**
     * Constructs a pawn with the specified name, color, rank, and file.
     * Stores the direction the pawn moves in.
     *
     * @param colour the colour of the pawn ("White" or "Black")
     * @param square the square the pawn is on.
     * @param moved  whether the pawn has moved
     */
    public Pawn(PieceColour colour, int square, boolean moved) {
        super(PieceType.PAWN, colour, square);
        this.moved = moved;
        this.moveDirection = (colour == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
    }

    /**
     * Generates all the legal moves the pawn can do.
     * @param position the board that we are searching for moves on.
     * @param moves array list legal moves are to be added to.
     */
    @Override
    public void generateMoves(Position position, IntArrayList moves) {
        int square = getSquare();
        int candidateMove = square;
        for (int i = 0; i < 2; i++) { //pawns can move forward either 1 or 2 squares
            candidateMove += moveDirection;
            if (isLegalMove(candidateMove) && position.getBoard().pieceSearch(candidateMove) == null) {
                if (i == 1) { //this is a double pawn push
                    moves.add(Move.encodeMove(position, candidateMove, square) |
                            MoveFlags.DOUBLE_PAWN_PUSH << MoveFlags.FLAG_SHIFT);
                } else {
                    addMove(position, moves, candidateMove);
                }
            } else {
                break; //if single push is blocked then double push is blocked
            }
        }
        int captureTarget;
        if (SquareMapUtils.getFileContribution(square) > Files.A) { //pawns on the a-file cannot capture to the left
            captureTarget = square + ChessDirections.LEFT + moveDirection;
            addCaptureMove(position, captureTarget, moves);
        }
        if (SquareMapUtils.getFileContribution(square) < Files.H) { //pawns on the h-file cannot capture to the right
            captureTarget = square + ChessDirections.RIGHT + moveDirection;
            addCaptureMove(position, captureTarget, moves);
        }
    }

    /**
     * Helper function for adding capture moves on either side of the pawn.
     *
     * @param position the position we are looking for moves in.
     * @param captureTarget the square the pawn can capture.
     * @param moves array list legal moves are to be added to.
     */
    private void addCaptureMove(Position position, int captureTarget, IntArrayList moves) {
        Piece piece = position.getBoard().pieceSearch(captureTarget);
        if (piece != null && piece.getColour() != getColour()) {
            addMove(position, moves, captureTarget);
            return;
        }
        int enPassantTarget = position.getGameState().getEnPassantTarget();
        if (enPassantTarget != Squares.NONE && enPassantTarget == captureTarget) {
            int moveFlag = (MoveFlags.CAPTURE_BIT | MoveFlags.EN_PASSANT) << MoveFlags.FLAG_SHIFT;
            moves.add(moveFlag | Move.encodeMove(position, captureTarget, getSquare()));
        }
    }

    /**
     * Helper function to add moves for the generateMoves method.
     *
     * @param position    the position we are looking for moves in.
     * @param moves array list legal moves are to be added to.
     * @param destination where the piece is going to.
     */
    public void addMove(Position position, IntArrayList moves, int destination) {
        PieceColour colour = getColour();
        if (destination < Ranks.SEVEN && colour == PieceColour.WHITE || destination >= Ranks.ONE && colour == PieceColour.BLACK) {
            int move = Move.encodeMove(position, destination, getSquare());
            int[] promotionPieceFlags = {MoveFlags.QUEEN, MoveFlags.ROOK, MoveFlags.BISHOP, MoveFlags.KNIGHT};
            for (int promotionPiece : promotionPieceFlags) {
                moves.add(move | ((promotionPiece | MoveFlags.PROMOTION_BIT) << MoveFlags.FLAG_SHIFT));
            }
        } else {
            moves.add(Move.encodeMove(position, destination, getSquare()));
        }
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(int move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        int square = getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        int squareRank = SquareMapUtils.getRankContribution(square);
        int moveFile = SquareMapUtils.getFileContribution(move);
        int moveRank = SquareMapUtils.getRankContribution(move);
        if (squareRank + moveDirection == moveRank && squareFile == moveFile) {
            return true;
        }
        else {
            return (!getMoved() && squareRank + 2 * moveDirection == moveRank && squareFile == moveFile);
        }
    }

    /**
     * Overrides the move method in the piece class to update the moved flag.
     * @param move the square to move the piece to.
     */
    @Override
    public void move(int move) {
        if (!moved) {
            moved = true;
        }
        super.move(move);
    }

    /**
     * Check if the pawn can capture a given square.
     * Used for detection of checks so en passant is excluded here.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return a boolean for whether the piece can capture that square.
     */
    @Override
    public boolean canCaptureSquare(Board board, int targetSquare) {
        if (!super.isLegalMove(targetSquare)) {
            return false;
        }
        int square = getSquare();
        int squareFile = SquareMapUtils.getFileContribution(square);
        boolean leftCapture = false;
        boolean rightCapture = false;
        if (squareFile > Files.A) { //when on the A file the pawn cannot capture left
            leftCapture = square + moveDirection + ChessDirections.LEFT == targetSquare;
        }
        if (squareFile < Files.H) { //when on the H file the pawn cannot capture left
            rightCapture = square + moveDirection + ChessDirections.RIGHT == targetSquare;
        }
        return leftCapture || rightCapture;
    }

    /**
     * Simple getter for the boolean moved
     * @return the moved boolean
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * Creates a copy of the pawn at a given square.
     * @param square the square the piece copy will be at.
     * @return a pawn object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(int square) {
        return new Pawn(getColour(), square, getMoved());
    }

    /**
     * Simple setter for the moved boolean.
     * @param moved value moved will be set to.
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            Pawn other = (Pawn) object;
            return other.moved == this.moved;
        }
        return false;
    }

    /**
     * Gets a bitmask of all the squares the pawn attacks.
     * @return long where each bit represents whether the pawn attacks that square or not.
     */
    @Override
    public long getAttackMask(Board board) {
        long attackMask = 0L;
        int square = getSquare();
        int file = SquareMapUtils.getFileContribution(square);
        if (file > Files.A && file < Files.H) { //pawn can attack on either side
            attackMask |= 1L << (square + moveDirection + ChessDirections.LEFT);
            attackMask |= 1L << (square + moveDirection + ChessDirections.RIGHT);
        } else if (file > Files.A) { //pawn can attack on left only
            attackMask |= 1L << (square + moveDirection + ChessDirections.LEFT);
        } else { //pawn can attack on right only
            attackMask |= 1L << (square + moveDirection + ChessDirections.RIGHT);
        }
        return attackMask;
    }
}


