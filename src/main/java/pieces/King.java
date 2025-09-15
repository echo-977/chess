import it.unimi.dsi.fastutil.ints.IntArrayList;

public class King extends DirectionalPiece{
    private boolean check;

    /**
     * Constructs a king with the specified name, color, rank, and file.
     *
     * @param colour the colour of the king ("White" or "Black")
     * @param square the square the king is on.
     * @param check  whether the king is in check
     */
    public King(PieceColour colour, int square, boolean check) {
        super(PieceType.KING, colour, square);
        this.check = check;
    }

    /**
     * Generates all the legal moves the king can do.
     * @param position the position that we are searching for moves on.
     * @param moves array list legal moves are to be added to.
     */
    @Override
    public void generateMoves(Position position, IntArrayList moves) {
        int[] directions = {
                ChessDirections.NONE + ChessDirections.UP, ChessDirections.RIGHT + ChessDirections.UP,
                ChessDirections.RIGHT + ChessDirections.NONE, ChessDirections.RIGHT + ChessDirections.DOWN,
                ChessDirections.NONE + ChessDirections.DOWN,  ChessDirections.LEFT + ChessDirections.DOWN,
                ChessDirections.LEFT + ChessDirections.NONE, ChessDirections.LEFT + ChessDirections.UP
        };
        directionalMoveSearch(position, moves, directions);
        PieceColour colour = getColour();
        PieceColour enemyColour = colour.opponentColour();
        int castlingRights = position.getGameState().getCastlingRights();
        int square = getSquare();
        int rank = SquareMapUtils.getRankContribution(square);
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
        if ((castlingRights & kingsideCastleMask) != 0 && !board.getThreatMap(enemyColour)[getSquare()]) {
            if (canCastleSearch(board, ChessConstants.KINGSIDE_CASTLE_FILE)) {
                Move.createIfLegal(position, moves, ChessConstants.KINGSIDE_CASTLE_FILE + rank, square);
            }
        }
        if ((castlingRights & queensideCastleMask) != 0 && !board.getThreatMap(enemyColour)[getSquare()]) {
            if (canCastleSearch(board, ChessConstants.QUEENSIDE_CASTLE_FILE) && board.pieceSearch(Files.B + rank) == null) {
                Move.createIfLegal(position, moves, ChessConstants.QUEENSIDE_CASTLE_FILE + rank, square);
            }
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
        return Math.abs(moveRank - squareRank) <= ChessConstants.RANK_OFFSET &&
                Math.abs(moveFile - squareFile) <= ChessConstants.FILE_OFFSET;
    }

    /**
     * Check if the king can capture a given square.
     * Used for detection of checks.
     * @param board the board the capture is searched for on.
     * @param targetSquare the square we are checking.
     * @return false.
     */
    @Override
    public boolean canCaptureSquare(Board board, int targetSquare) {
        return isLegalMove(targetSquare); //if the king could move there it could capture a king on that square
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
    public Piece copyToSquare(int square) {
        return new King(getColour(), square, check);
    }

    /**
     * Utility function to find the next available empty slot for a move.
     * @param moves the array of moves.
     * @return index of first empty slot in the array.
     */
    public int findNextIndex(int[] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks whether a king can castle to a given file without passing through or ending up in check.
     * @param board the board in play.
     * @param targetFile the target file for the king to castle in.
     * @return boolean for if it is legal to castle.
     */
    public boolean canCastleSearch(Board board, int targetFile) {
        boolean[] threatMap = board.getThreatMap(getColour().opponentColour());
        int square = getSquare();
        int squareRank = SquareMapUtils.getRankContribution(square);
        int squareFile = SquareMapUtils.getFileContribution(square);
        int start, end;
        if (targetFile > squareFile) {
            start = square + ChessDirections.RIGHT;
            end = squareRank + targetFile;
        } else {
            start = squareRank + targetFile;
            end = square + ChessDirections.LEFT;
        }
        for (int squareCheck = start; squareCheck <= end; squareCheck++ ) {
            if (threatMap[squareCheck] || board.pieceSearch(squareCheck) != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Searches each given direction from the king, checking if it is valid to move to that square.
     * Updated to not allow moving to a square threatened by the opponent.
     * @param position the board the king is moving on.
     * @param moves array list legal moves are to be added to.
     * @param directions array of 2d directions the king can go in.
     */
    @Override
    public void directionalMoveSearch(Position position, IntArrayList moves, int[] directions) {
        int square = getSquare();
        int candidateMove;
        Piece piece;
        Board board = position.getBoard();
        boolean[] threatMap = board.getThreatMap(getColour().opponentColour());
        for (int i = 0; i < ChessConstants.NUM_DIRECTIONS; i++) {
            candidateMove = square + directions[i];
            if (isLegalMove(candidateMove) && !threatMap[candidateMove]) {
                piece = board.pieceSearch(candidateMove);
                if (piece == null || piece.getColour() != getColour()) { //opposite coloured piece so capture
                   Move.createIfLegal(position, moves, candidateMove, square);
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




