public class Knight extends Piece{

    /**
     * Constructs a knight with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Knight(String colour, char file, int rank) {
        super("Knight", colour, file, rank);
    }

    /**
     * Generates all the legal moves the knight can do (without considering other pieces).
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the knight can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        char file = getFile();
        int rank = getRank();
        char checkFile;
        int checkRank;
        String[] moves = new String[8]; //number of knight moves in any position
        int movesIndex = 0;
        int[][] directions = {
                {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
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
        if (Math.abs(moveRank - rank) == 1 && Math.abs(moveFile - file) == 2) {
            return true;
        } else {
            return Math.abs(moveRank - rank) == 2 && Math.abs(moveFile - file) == 1;
        }
    }
}

