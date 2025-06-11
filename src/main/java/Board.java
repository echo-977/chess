import java.util.Arrays;

public class Board {
    private Piece[] whitePieces;
    private Piece[] blackPieces;
    private boolean turn;
    private int moveCount;
    private int halfMoveClock;

    /**
     * Constructs a board from a given FEN string
     *
     * @param FEN  FEN string for the position
     */
    public Board(String FEN) {
        String[] fen = FEN.split(" ");
        moveCount = Integer.parseInt(fen[5]);
        halfMoveClock = Integer.parseInt(fen[4]);
        turn = fen[1].equals("w");
        fen[0] = fen[0].replaceAll("/", "");
        String currentSquare;
        int skip = 0;
        int current = 0;
        String colour;
        Piece[] pieces = new Piece[32];
        int pieceCounter = 0;
        boolean moved;
        boolean enPassantable;
        for (int i = 0; i < 64; i++) {
            if (skip > 0) { //used for numbers in FEN string representing sequential empty squares
                skip--;
            } else {
                if (Character.isDigit(fen[0].charAt(current))) {
                    skip = Character.getNumericValue(fen[0].charAt(current));
                    //current += skip;
                    skip--;
                } else {
                    if (Character.isUpperCase(fen[0].charAt(current))) {
                        colour = "White";
                    } else {
                        colour = "Black";
                    }
                    currentSquare = mapIntToSquare(i);
                    char file = currentSquare.charAt(0);
                    int rank = Integer.parseInt(currentSquare.substring(1, 2));
                    switch (Character.toLowerCase(fen[0].charAt(current))) {
                        case 'q':
                            pieces[pieceCounter] = new Queen(colour, file, rank);
                            break;
                        case 'b':
                            pieces[pieceCounter] = new Bishop(colour, file, rank);
                            break;
                        case 'n':
                            pieces[pieceCounter] = new Knight(colour, file, rank);
                            break;
                        case 'p':
                            if (rank == 7 && colour.equals("Black")) {
                                moved = false;
                            } else {
                                moved = !(rank == 2 && colour.equals("White"));
                            }
                            if (fen[3].equals("-")) {
                                enPassantable = false;
                            } else if (colour.equals("White")) {
                                enPassantable = (file == fen[3].charAt(0) && rank - 1 == Integer.parseInt(fen[3].substring(1, 2)));
                            } else {
                                enPassantable = (file == fen[3].charAt(0) && rank + 1 == Integer.parseInt(fen[3].substring(1, 2)));
                            }
                            pieces[pieceCounter] = new Pawn(colour, file, rank, moved, enPassantable);
                            break;
                        case 'k':
                            if (fen[2].equals(fen[2].toLowerCase()) && colour.equals("White")) {
                                moved = true;
                            } else moved = fen[2].equals(fen[2].toUpperCase()) && colour.equals("Black");
                            pieces[pieceCounter] = new King(colour, file, rank, moved, false);
                            break;
                        case 'r':
                            if (colour.equals("White")) {
                                if (currentSquare.equals("a1") && fen[2].contains("Q")) {
                                    moved = false;
                                } else moved = !(currentSquare.equals("h1") && fen[2].contains("K"));
                            } else {
                                if (currentSquare.equals("a8") && fen[2].contains("q")) {
                                    moved = false;
                                } else moved = !(currentSquare.equals("h8") && fen[2].contains("k"));
                            }
                            pieces[pieceCounter] = new Rook(colour, file, rank, moved);
                            break;
                    }
                    pieceCounter++;
                }
                current++;
            }
        }
        whitePieces = new Piece[16];
        blackPieces = new Piece[16];
        int whiteCounter = 0;
        int blackCounter = 0;
        for (Piece piece: pieces) {
            if (piece != null) {
                if (piece.getColour().equals("White")) {
                    whitePieces[whiteCounter] = piece;
                    whiteCounter++;
                } else {
                    blackPieces[blackCounter] = piece;
                    blackCounter++;
                }
            }
        }
    }


    /**
    * Give the square that an integer from 0 to 63 relates to
    * 0 refers to a8 then it reads to the right and down
    * 63 refers to h1
    *
    * @param location the integer that a square returns to
    * @return a string of the square
     */
    public String mapIntToSquare(int location) {
        int rank = 8 - location / 8;
        char file = (char) ('a' + (location % 8));
        return file + String.valueOf(rank);
    }

    /**
     * Simple getter for the white pieces
     *
     * @return piece array of all the white pieces
     */
    public Piece[] getWhitePieces() {
        return whitePieces;
    }

    /**
     * Simple getter for the black pieces
     *
     * @return piece array of all the black pieces
     */
    public Piece[] getBlackPieces() {
        return blackPieces;
    }

    /**
     * Simple getter for the turn boolean
     *
     * @return boolean value (true for White move, false for Black move)
     */
    public boolean getTurn() {
        return turn;
    }

    /**
     * Simple getter for the current move
     *
     * @return int value for the current move
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Simple getter for the half move clock
     *
     * @return int value for the number of half moves since a pawn move or capture
     */
    public int getHalfMoveClock() {
        return halfMoveClock;
    }
}
