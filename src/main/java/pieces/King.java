public class King extends DirectionalPiece{
    private boolean check;

    /**
     * Constructs a king with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     * @param check  whether the king is in check
     */
    public King(PieceColour colour, char file, int rank, boolean check) {
        super(PieceType.KING, colour, file, rank);
        this.check = check;
    }

    /**
     * Generates all the (pseudo)legal moves the king can do.
     * @param position the position that we are searching for moves on.
     * @return an array of all the squares the king can move to as strings.
     */
    @Override
    public Move[] generateMoves(Position position) {
        Move[] moves = new Move[ChessConstants.MAX_KING_MOVES];
        int[][] directions = {
                {ChessDirections.NONE, ChessDirections.UP}, {ChessDirections.RIGHT, ChessDirections.UP},
                {ChessDirections.RIGHT, ChessDirections.NONE}, {ChessDirections.RIGHT, ChessDirections.DOWN},
                {ChessDirections.NONE, ChessDirections.DOWN},  {ChessDirections.LEFT, ChessDirections.DOWN},
                {ChessDirections.LEFT, ChessDirections.NONE}, {ChessDirections.LEFT, ChessDirections.UP}
        };
        directionalMoveSearch(position, moves, directions);
        PieceColour colour = getColour();
        PieceColour enemyColour = colour.opponentColour();
        int castlingRights = position.getGameState().getCastlingRights();
        int nextIndex;
        int rank = getRank();
        int kingsideCastleMask;
        int queensideCastleMask;
        if (colour == PieceColour.WHITE) {
            kingsideCastleMask = FENConstants.WHITE_KINGSIDE_CASTLE_MASK;
            queensideCastleMask = FENConstants.WHITE_QUEENSIDE_CASTLE_MASK;
        } else {
            kingsideCastleMask = FENConstants.BLACK_KINGSIDE_CASTLE_MASK;
            queensideCastleMask = FENConstants.BLACK_QUEENSIDE_CASTLE_MASK;
        }
        Board board = position.getBoard();
        if ((castlingRights & kingsideCastleMask) != 0 &&
                !board.getThreatMap(enemyColour)[SquareMapUtils.mapSquareToInt(getSquare())]) {
            if (canCastleSearch(board, 'g')) {
                nextIndex = findNextIndex(moves);
                moves[nextIndex] = Move.createIfLegal(position, "g" + rank, this);
                if (moves[nextIndex] != null) {
                    moves[nextIndex].setCastleMask(kingsideCastleMask);
                }
            }
        }
        if ((castlingRights & queensideCastleMask) != 0 &&
                !board.getThreatMap(enemyColour)[SquareMapUtils.mapSquareToInt(getSquare())]) {
            if (canCastleSearch(board, 'c') && board.pieceSearch("b" + rank) == null) {
                nextIndex = findNextIndex(moves);
                moves[nextIndex] = Move.createIfLegal(position, "c" + rank, this);
                if (moves[nextIndex] != null) {
                    moves[nextIndex].setCastleMask(queensideCastleMask);
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
        char moveFile = SquareMapUtils.getFile(move);
        int moveRank = SquareMapUtils.getRank(move);
        return Math.abs(moveRank - rank) <= 1 && Math.abs(moveFile - file) <= 1;
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
        char targetFile = SquareMapUtils.getFile(targetSquare);
        int targetRank = SquareMapUtils.getRank(targetSquare);
        char file = getFile();
        int rank = getRank();
        return (Math.abs(targetFile - file) <= 1 && Math.abs(targetRank - rank) <= 1);

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
        char file = SquareMapUtils.getFile(square);
        int rank = SquareMapUtils.getRank(square);
        return new King(getColour(), file, rank, isCheck());
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
        char file = SquareMapUtils.getFile(square);
        if (file == 'g') {
            board.pieceSearch("h" + getRank()).move("f" + getRank());
        } else if (file == 'c') {
            board.pieceSearch("a" + getRank()).move("d" + getRank());
        }

    }

    /**
     * Searches each given direction from the king, checking if it is valid to move to that square.
     * Updated to not allow moving to a square threatened by the opponent.
     * @param position the board the king is moving on.
     * @param moves the current string of moves generated (legal moves are added to this).
     * @param directions array of 2d directions the king can go in.
     */
    @Override
    public void directionalMoveSearch(Position position, Move[] moves, int[][] directions) {
        char file = getFile();
        int rank = getRank();
        char checkFile;
        int checkRank;
        String candidateMove;
        Piece piece;
        int movesIndex = 0;
        boolean[] threatMap;
        Board board = position.getBoard();
        if (getColour() == PieceColour.WHITE) {
            threatMap = board.getThreatMap(PieceColour.BLACK);
        } else {
            threatMap = board.getThreatMap(PieceColour.WHITE);
        }
        for (int i = 0; i < 8; i++) {
            checkFile = (char) (file + directions[i][0]);
            checkRank = rank + directions[i][1];
            candidateMove = checkFile + String.valueOf(checkRank);
            if (isLegalMove(candidateMove) && !threatMap[SquareMapUtils.mapSquareToInt(candidateMove)]) {
                piece = board.pieceSearch(candidateMove);
                if (piece == null || piece.getColour() != getColour()) { //opposite coloured piece so capture
                    moves[movesIndex] = Move.createIfLegal(position, candidateMove, this);
                    if (moves[movesIndex] != null) {
                        movesIndex++;
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            King other = (King) object;
            return other.check == this.check;
        } else {
            return false;
        }
    }
}




