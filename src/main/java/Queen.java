public class Queen extends Piece{

    /**
     * Constructs a queen with the specified name, color, rank, and file.
     * Ensures inputs for a piece are valid.
     *
     * @param colour the colour of the piece ("White" or "Black")
     * @param file   the file (column) position on the board in algebraic notation (e.g., "e")
     * @param rank   the rank (row) position on the board in algebraic notation (e.g., "2")
     */
    public Queen(String colour, char file, int rank) {
        super("Queen", colour, file, rank);
    }

    /**
     * Generates all the legal moves the queen can do (without considering other pieces).
     * @param board the board that we are searching for moves on.
     * @return an array of all the squares the queen can move to as strings.
     */
    @Override
    public String[] generateMoves(Board board) {
        Rook rook = new Rook(getColour(), getFile(), getRank(), true);
        Bishop bishop = new Bishop(getColour(), getFile(), getRank());
        String[] moves = new String[27]; //max number of bishop moves in any position
        String[] rookMoves = rook.generateMoves(board);
        String[] bishopMoves = bishop.generateMoves(board);
        System.arraycopy(rookMoves, 0, moves, 0, rookMoves.length);
        System.arraycopy(bishopMoves, 0, moves, rookMoves.length, bishopMoves.length);
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
        //since a queen behaves either like a rook or bishop
        Rook rook = new Rook(getColour(), getFile(), getRank(), true);
        Bishop bishop = new Bishop(getColour(), getFile(), getRank());
        return rook.isLegalMove(move) ^ bishop.isLegalMove(move);
    }
}