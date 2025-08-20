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
                movesIndex = addMove(board, moves, movesIndex, candidateMove);
            } else {
                break; //if single push is blocked then double push is blocked
            }
        }
        Piece piece = board.pieceSearch((char) (getFile() + ChessDirections.LEFT)+ String.valueOf(getRank() + moveDirection));
        if (piece != null && piece.getColour() != getColour()) {
            movesIndex = addMove(board, moves, movesIndex, (char) (getFile() + ChessDirections.LEFT) + String.valueOf(getRank() + moveDirection));
        }
        piece = board.pieceSearch((char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank() + moveDirection));
        if (piece != null && piece.getColour() != getColour()) {
            movesIndex = addMove(board, moves, movesIndex, (char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank() + moveDirection));
        }
        piece = board.pieceSearch((char) (getFile() + ChessDirections.LEFT) + String.valueOf(getRank()));
        if (piece != null && piece.getColour() != getColour() && piece.getType() == PieceType.PAWN && ((Pawn) piece).getEnPassantable()) {
            movesIndex = addMove(board, moves, movesIndex, (char) (getFile() + ChessDirections.LEFT) + String.valueOf(getRank() + moveDirection));
            moves[movesIndex - 1].setEnPassant(true);
        }
        piece = board.pieceSearch((char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank()));
        if (piece != null && piece.getColour() != getColour() && piece.getType() == PieceType.PAWN && ((Pawn) piece).getEnPassantable()) {
            movesIndex = addMove(board, moves, movesIndex, (char) (getFile() + ChessDirections.RIGHT) + String.valueOf(getRank() + moveDirection));
            try {
                moves[movesIndex - 1].setEnPassant(true);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(movesIndex + " " + moves[movesIndex]);
                throw e;
            }
        }
        return moves;
    }

    /**
     * Helper function to add moves for the generateMoves method
     * @param board the board we are looking for moves on
     * @param moves the array of moves from generateMoves
     * @param movesIndex index of next available spot in moves
     * @param destination where the piece is going to
     * @return updated value of movesIndex
     */
    public int addMove(Board board, Move[] moves, int movesIndex, String destination) {
        int rank = destination.charAt(1) - '0';
        Move move;
        if ((getColour() == PieceColour.WHITE && rank == 8) || (getColour() == PieceColour.BLACK && rank == 1)) {
            for (PieceType type : PieceType.values()) {
                if (!canPromoteTo(type)) {
                    continue;
                }
                move = Move.createIfLegal(board, this, destination);
                if (move != null) {
                    move.setPromotionType(type);
                    moves[movesIndex] = move;
                    movesIndex++;
                } else {
                    break;
                }
            }
        } else {
            moves[movesIndex] = Move.createIfLegal(board, this, destination);
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
            int moveRank = move.charAt(1) - '0';
            if (Math.abs(moveRank - getRank()) == 2) {
                enPassantable = true;
            }
        } else if (enPassantable) {
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
    public boolean canCaptureSquare(Board board, String targetSquare) {
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

    /**
     * Simple setter for the boolean enPassantable.
     * @param enPassant whether the pawn can be captured by en passant.
     */
    public void setEnPassantable(boolean enPassant) {
        enPassantable = enPassant;
    }

    /**
     * Creates a copy of the pawn at a given square.
     * @param square the square the piece copy will be at.
     * @return a pawn object at the given square with the same properties.
     */
    @Override
    public Piece copyToSquare(String square) {
        char file = square.charAt(0);
        int rank = square.charAt(1) - '0';
        return new Pawn(getColour(), file, rank, getMoved(), getEnPassantable());
    }
}


