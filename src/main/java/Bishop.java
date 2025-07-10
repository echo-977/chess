public class Bishop extends Piece{

    /**
     * Constructs a bishop with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Bishop(String colour, char file, int rank) {
        super("Bishop", colour, file, rank);
    }

    /**
     * Generates all the legal moves the bishop can do (without considering other pieces).
     * @return an array of all the squares the bishop can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        char file = getFile();
        int rank = getRank();
        String[] moves = new String[13]; //max number of bishop moves in any position
        int movesIndex = 0;
        String move;
        //bottom left diagonal
        int numMoves = Math.abs(Math.min(Math.abs(rank - 1), Math.abs(file - 'a')));
        char checkFile = (char) (file - numMoves);
        int checkRank = rank - numMoves;
        for (int i = 0; i < numMoves; i++) {
            move = String.valueOf(checkFile) + String.valueOf(checkRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            }
            checkFile = (char) (checkFile + 1);
            checkRank = checkRank + 1;
        }
        //top right diagonal
        numMoves = Math.abs(Math.min(Math.abs(rank - 8), Math.abs(file - 'h')));
        checkFile = (char) (file + 1);
        checkRank = rank + 1;
        for (int i = 0; i < numMoves; i++) {
            move = String.valueOf(checkFile) + String.valueOf(checkRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            }
            checkFile = (char) (checkFile + 1);
            checkRank = checkRank + 1;
        }
        //top left diagonal
        numMoves = Math.abs(Math.min(Math.abs(rank - 8), file - 'a'));
        checkFile = (char) (file - numMoves);
        checkRank = rank + numMoves;
        for (int i = 0; i < numMoves; i++) {
            move = String.valueOf(checkFile) + String.valueOf(checkRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            }
            checkFile = (char) (checkFile + 1);
            checkRank = checkRank - 1;
        }
        //bottom right diagonal
        numMoves = Math.abs(Math.min(rank - 1, Math.abs(file - 'h')));
        checkFile = (char) (file + 1);
        checkRank = rank - 1;
        for (int i = 0; i < numMoves; i++) {
            move = String.valueOf(checkFile) + String.valueOf(checkRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
            }
            checkFile = (char) (checkFile + 1);
            checkRank = checkRank - 1;
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
        char moveFile = move.charAt(0);
        int moveRank = Integer.parseInt(move.substring(1, 2));
        char file = getFile();
        int rank = getRank();
        return Math.abs(rank - moveRank) == Math.abs(file - moveFile);

    }
}
