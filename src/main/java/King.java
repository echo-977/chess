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
        PieceColour enemyColour;
        if (getColour() == PieceColour.WHITE) {
            enemyColour = PieceColour.BLACK;
        } else {
            enemyColour = PieceColour.WHITE;
        }
        if (!moved && !board.getThreatMap(enemyColour)[SquareMapUtils.mapSquareToInt(getSquare())]) {
            int nextIndex;
            int rank = getRank();
            Piece piece = board.pieceSearch("a" + rank);
            if (piece instanceof Rook rook) {
                if (!rook.getMoved() && canCastleSearch(board, 'c') && board.pieceSearch("b" + rank) == null) {
                    nextIndex = findNextIndex(moves);
                    moves[nextIndex] = Move.createIfLegal(board, this, "c" + rank);
                    moves[nextIndex].setCastle(true);
                }
            }
            piece = board.pieceSearch("h" + rank);
            if (piece instanceof Rook rook) {
                if (!rook.getMoved() && canCastleSearch(board, 'g')) {
                    nextIndex = findNextIndex(moves);
                    moves[nextIndex] = Move.createIfLegal(board, this, "g" + rank);
                    moves[nextIndex].setCastle(true);
                }
            }
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
     * Used for detection of checks.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return false.
     */
    @Override
    public boolean canCaptureSquare(Board board, String targetSquare) {
        char targetFile = targetSquare.charAt(0);
        int targetRank = targetSquare.charAt(1) - '0';
        char file = getFile();
        int rank = getRank();
        return (Math.abs(targetFile - file) <= 1 && Math.abs(targetRank - rank) <= 1);

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

    /**
     * Utility function to find the next available empty slot for a move.
     * @param moves the array of moves.
     * @return index of first empty slot in the array.
     */
    public int findNextIndex(Move[] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks whether a king can castle to a given file without passing through or ending up in check.
     * @param board the board in play.
     * @param file the target file of the king.
     * @return boolean for if it is legal to castle.
     */
    public boolean canCastleSearch(Board board, char file) {
        boolean[] threatMap;
        if (getColour() == PieceColour.WHITE) {
            threatMap = board.getThreatMap(PieceColour.BLACK);
        } else {
            threatMap = board.getThreatMap(PieceColour.WHITE);
        }
        int rank = getRank();
        char start, end;
        if (file > getFile()) {
            start = (char) (getFile() + 1);
            end = file;
        } else {
            start = file;
            end = (char) (getFile() - 1);
        }
        String square;
        for (char f = start; f <= end; f++ ) {
            square = f + String.valueOf(rank);
            if (threatMap[SquareMapUtils.mapSquareToInt(square)] || board.pieceSearch(square) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs a castling move in the most basic way.
     * Used for checking if castling will result in a check on the enemy king.
     * @param board the board the castling will take place on.
     * @param square square the king is moved to.
     */
    public void castleMove(Board board, String square) {
        super.move(square);
        char file = square.charAt(0);
        if (file == 'g') {
            board.pieceSearch("h" + getRank()).move("f" + getRank());
        } else if (file == 'c') {
            board.pieceSearch("a" + getRank()).move("d" + getRank());
        }

    }

    /**
     * Searches each given direction from the king, checking if it is valid to move to that square.
     * Updated to not allow moving to a square threatened by the opponent.
     * @param board the board the king is moving on.
     * @param moves the current string of moves generated (legal moves are added to this).
     * @param directions array of 2d directions the king can go in.
     */
    @Override
    public void directionalMoveSearch(Board board, Move[] moves, int[][] directions) {
        char file = getFile();
        int rank = getRank();
        char checkFile;
        int checkRank;
        String candidateMove;
        Piece piece;
        int movesIndex = 0;
        for (int i = 0; i < 8; i++) {
            checkFile = (char) (file + directions[i][0]);
            checkRank = rank + directions[i][1];
            candidateMove = checkFile + String.valueOf(checkRank);
            boolean[] threatMap;
            if (getColour() == PieceColour.WHITE) {
                threatMap = board.getThreatMap(PieceColour.BLACK);
            } else {
                threatMap = board.getThreatMap(PieceColour.WHITE);
            }
            if (isLegalMove(candidateMove) && !threatMap[SquareMapUtils.mapSquareToInt(candidateMove)]) {
                piece = board.pieceSearch(candidateMove);
                if (piece == null || piece.getColour() != getColour()) { //opposite coloured piece so capture
                    moves[movesIndex] = Move.createIfLegal(board, this, candidateMove);
                    if (moves[movesIndex] != null) {
                        movesIndex++;
                    }
                }
            }
        }
    }
}




