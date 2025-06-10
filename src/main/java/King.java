public class King extends Piece{
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
    public King(String colour, char file, int rank, boolean check) {
        super("King", colour, file, rank);
        this.check = check;
    }

    /**
     * Generates all the legal moves the queen can do (without considering other pieces).
     * @return an array of all the squares the queen can move to as strings.
     */
    @Override
    public String[] generateMoves() {
        char file = getFile();
        int rank = getRank();
        char checkFile;
        int checkRank;
        String[] moves = new String[8]; //number of king moves in any position
        int movesIndex = 0;
        int[][] directions = {
                {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1},  {-1, -1}, {-1, 0}, {-1, 1}
        };
        String move;
        for (int i = 0; i < 8; i++) {
            checkFile = (char) (file + directions[i][0]);
            checkRank = rank + directions[i][1];
            move = String.valueOf(checkFile) + String.valueOf(checkRank);
            if (isLegalMove(move)) {
                moves[movesIndex] = move;
                movesIndex++;
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
        int moveRank = Integer.parseInt(move.substring(1, 2));
        return Math.abs(moveRank - rank) <= 1 && Math.abs(moveFile - file) <= 1;
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
}




