public class Pawn extends Piece{
    private boolean moved;
    private boolean enPassantable;

    /**
     * Constructs a pawn with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour         the colour of the piece ("White" or "Black")
     * @param file           the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank           the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param moved          whether the pawn has moved
     * @param enPassantable  whether the pawn can be taken by en passant
     */
    public Pawn(PieceColour colour, char file, int rank, boolean moved, boolean enPassantable) {
        super(PieceType.PAWN, colour, file, rank);
        this.moved = moved;
        this.enPassantable = enPassantable;
    }

    /**
     * Generates all the legal moves the pawn can do.
     * @param board the board that we are searching for moves on.
     * @return an array of moves of all the squares the pawn can move.
     */
    @Override
    public Move[] generateMoves(Board board) {
        char file = getFile();
        int checkRank = getRank();
        Move[] moves = new Move[ChessConstants.MAX_PAWN_MOVES];
        String candidateMove;
        int moveDirection;
        int movesIndex = 0;
        if (getColour() == PieceColour.WHITE) {
            moveDirection = ChessDirections.UP;
        } else {
            moveDirection = ChessDirections.DOWN;
        }
        for (int i = 0; i < 2; i++) { //pawns can move forward either 1 or 2 squares
            checkRank = checkRank + moveDirection;
            candidateMove = file + String.valueOf(checkRank);
            if (isLegalMove(candidateMove) && board.pieceSearch(candidateMove) == null) {
                moves[movesIndex] = new Move(this, candidateMove);
                movesIndex++;
            } else {
                break; //if single push is blocked then double push is blocked
            }
        }
        Piece piece = board.pieceSearch((char) (getFile() + ChessDirections.LEFT)+ String.valueOf(getRank() + moveDirection));
        if (piece != null && piece.getColour() != getColour()) {
            moves[movesIndex] = new Move(this, (char) (getFile() + ChessDirections.LEFT) + String.valueOf(getRank() + moveDirection));
            movesIndex++;
        }
        piece = board.pieceSearch((char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank() + moveDirection));
        if (piece != null && piece.getColour() != getColour()) {
            moves[movesIndex] = new Move(this, (char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank() + moveDirection));
            movesIndex++;
        }
        piece = board.pieceSearch((char) (getFile() + ChessDirections.LEFT) + String.valueOf(getRank()));
        if (piece != null && piece.getColour() != getColour() && piece.getType() == PieceType.PAWN && ((Pawn) piece).getEnPassantable()) {
            moves[movesIndex] = new Move(this, (char) (getFile() + ChessDirections.LEFT) + String.valueOf(getRank() + moveDirection));
            movesIndex++;
        }
        piece = board.pieceSearch((char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank()));
        if (piece != null && piece.getColour() != getColour() && piece.getType() == PieceType.PAWN && ((Pawn) piece).getEnPassantable()) {
            moves[movesIndex] = new Move(this, (char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank() + moveDirection));
        }
        return moves;
    }

    /**
     * Check if a given move is legal (without considering other pieces).
     * @param move the move to be validated.
     * @return boolean value denoting if the move is theoretically legal.
     */
    @Override
    public boolean isLegalMove(String move) {
        if (!super.isLegalMove(move)) {
            return false;
        }
        char file = getFile();
        int rank = getRank();
        char moveFile = move.charAt(0);
        int moveRank = move.charAt(1) - '0';
        if (Math.abs(moveRank - rank) == 1 && moveFile == file) {
            return true;
        }
        else {
            return (!getMoved() && Math.abs(moveRank - rank) == 2 && moveFile == file);
        }

    }

    /**
     * Overrides the move method in the piece class to update the moved flag.
     * @param move the square to move the piece to.
     */
    @Override
    public void move(String move) {
        if (!moved) {
            moved = true;
            enPassantable = true;
        } else {
            enPassantable = false;
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
    public boolean canCaptureKing(Board board, String targetSquare) {
        if (!super.isLegalMove(targetSquare)) {
            return false;
        }
        char file = getFile();
        int rank = getRank();
        char targetFile = targetSquare.charAt(0);
        int targetRank = targetSquare.charAt(1) - '0';
        int direction;
        if (getColour() == PieceColour.WHITE) {
            direction = ChessDirections.UP;
        } else {
            direction = ChessDirections.DOWN;
        }
        return targetRank == rank + direction &&
                (targetFile == (char) (file + ChessDirections.LEFT) || targetFile == (char) (file + ChessDirections.RIGHT));
    }

    /**
     * Simple getter for the boolean moved
     * @return the moved boolean
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * Simple getter for the boolean enPassantable
     * @return the enPassantable boolean
     */
    public boolean getEnPassantable() {
        return enPassantable;
    }
}


