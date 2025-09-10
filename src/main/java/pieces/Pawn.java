public class Pawn extends Piece{
    private boolean moved;

    /**
     * Constructs a pawn with the specified name, color, rank, and file.
     *
     * @param colour the colour of the pawn ("White" or "Black")
     * @param square the square the pawn is on.
     * @param moved  whether the pawn has moved
     */
    public Pawn(PieceColour colour, int square, boolean moved) {
        super(PieceType.PAWN, colour, square);
        this.moved = moved;
    }

    /**
     * Generates all the legal moves the pawn can do.
     * @param position the board that we are searching for moves on.
     * @return an array of moves of all the squares the pawn can move.
     */
    @Override
    public Move[] generateMoves(Position position) {
        int square = getSquare();
        int candidateMove = square;
        Move[] moves = new Move[ChessConstants.MAX_PAWN_MOVES];
        int moveDirection = (getColour() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
        int movesIndex = 0;
        for (int i = 0; i < 2; i++) { //pawns can move forward either 1 or 2 squares
            candidateMove += moveDirection;
            if (isLegalMove(candidateMove) && position.getBoard().pieceSearch(candidateMove) == null) {
                movesIndex = addMove(position, moves, movesIndex, candidateMove);
            } else {
                break; //if single push is blocked then double push is blocked
            }
        }
        int captureTarget;
        if (SquareMapUtils.getFileContribution(square) > Files.A) { //pawns on the a-file cannot capture to the left
            captureTarget = square + ChessDirections.LEFT + moveDirection;
            movesIndex = addCaptureMove(position, captureTarget, movesIndex, moves);
        }
        if (SquareMapUtils.getFileContribution(square) < Files.H) { //pawns on the h-file cannot capture to the right
            captureTarget = square + ChessDirections.RIGHT + moveDirection;
            addCaptureMove(position, captureTarget, movesIndex, moves);
        }
        return moves;
    }

    /**
     * Helper function for adding capture moves on either side of the pawn.
     *
     * @param position      the position we are looking for moves in.
     * @param captureTarget the square the pawn can capture.
     * @param movesIndex    the index of the next available space in the moves array.
     * @param moves         the array of moves.
     * @return updated value of movesIndex.
     */
    private int addCaptureMove(Position position, int captureTarget, int movesIndex, Move[] moves) {
        Piece piece = position.getBoard().pieceSearch(captureTarget);
        if (piece != null && piece.getColour() != getColour()) {
            movesIndex = addMove(position, moves, movesIndex, captureTarget);
        }
        int enPassantTarget = position.getGameState().getEnPassantTarget();
        if (enPassantTarget != ChessConstants.NO_EN_PASSANT_TARGET) {
            if (enPassantTarget == captureTarget) {
                int index = movesIndex;
                movesIndex = addMove(position, moves, movesIndex, captureTarget);
                if (movesIndex == index + 1) { //en passant move was added so we need to set it as such
                    moves[movesIndex - 1].setEnPassant(true);
                }
            }
        }
        return movesIndex;
    }

    /**
     * Helper function to add moves for the generateMoves method.
     *
     * @param position    the position we are looking for moves in.
     * @param moves       the array of moves from generateMoves.
     * @param movesIndex  index of next available spot in moves.
     * @param destination where the piece is going to.
     * @return updated value of movesIndex.
     */
    public int addMove(Position position, Move[] moves, int movesIndex, int destination) {
        int rank = SquareMapUtils.getRank(destination);
        Move move;
        if ((getColour() == PieceColour.WHITE && rank == 8) || (getColour() == PieceColour.BLACK && rank == 1)) {
            for (PieceType type : PieceType.values()) {
                if (!canPromoteTo(type)) {
                    continue;
                }
                move = Move.createIfLegal(position, this, destination);
                if (move != null) {
                    move.setPromotionType(type);
                    moves[movesIndex] = move;
                    movesIndex++;
                } else {
                    break;
                }
            }
        } else {
            moves[movesIndex] = Move.createIfLegal(position, this, destination);
            if (moves[movesIndex] != null) {
                movesIndex++;
            }
        }
        return movesIndex;
    }

    /**
     * Filters piece types based on whether they can be promoted to
     * @param type the piece type
     * @return true if the piece can be promoted to, false otherwise
     */
    public boolean canPromoteTo(PieceType type) {
        return switch (type) {
            case QUEEN, ROOK, BISHOP, KNIGHT -> true;
            default -> false;
        };
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
        int moveDirection = (getColour() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
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
        int moveDirection = (getColour() == PieceColour.WHITE) ? ChessDirections.UP : ChessDirections.DOWN;
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
}


