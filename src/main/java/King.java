public class King extends DirectionalPiece{
    private boolean check;
    private boolean moved;

    /**
     * Constructs a king with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param moved  whether the king has moved yet
     * @param check  whether the king is in check
     */
    public King(PieceColour colour, char file, int rank, boolean moved, boolean check) {
        super(PieceType.KING, colour, file, rank);
        this.moved = moved;
        this.check = check;
    }

    /**
     * Generates all the (pseudo)legal moves the king can do.
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the king can move to as strings.
     */
    @Override
    public Move[] generateMoves(Board board) {
        Move[] moves = new Move[ChessConstants.MAX_KING_MOVES];
        int[][] directions = {
                {ChessDirections.NONE, ChessDirections.UP}, {ChessDirections.RIGHT, ChessDirections.UP},
                {ChessDirections.RIGHT, ChessDirections.NONE}, {ChessDirections.RIGHT, ChessDirections.DOWN},
                {ChessDirections.NONE, ChessDirections.DOWN},  {ChessDirections.LEFT, ChessDirections.DOWN},
                {ChessDirections.LEFT, ChessDirections.NONE}, {ChessDirections.LEFT, ChessDirections.UP}
        };
        directionalMoveSearch(board, moves, directions);
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
        return Math.abs(moveRank - rank) <= 1 && Math.abs(moveFile - file) <= 1;
    }

    /**
     * Overrides the move method in the piece class to update the moved flag.
     * @param move the square to move the piece to.
     */
    @Override
    public void move(String move) {
        if (!moved) {
            moved = true;
        }
        super.move(move);
    }

    /**
     * Check if the king can capture a given square.
     * Used for detection of checks so is always false.
     * (A king cannot capture another king as this would mean the other king could capture it first, hence illegal move).
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return false.
     */
    @Override
    public boolean canCaptureKing(Board board, String targetSquare) {
        return false;
    }

    /**
     * Simple getter for the boolean moved
     * @return the moved boolean
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * Simple getter for the boolean check
     * @return the check boolean
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * Simple setter for the boolean check
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    /**
     * Creates a copy of the king at a given square.
     * @param square the square the piece copy will be at.
     * @return a king object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(String square) {
        char file = square.charAt(0);
        int rank = square.charAt(1) - '0';
        return new King(getColour(), file, rank, getMoved(), isCheck());
    }
}




