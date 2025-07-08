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
     * Search for if a piece exists on a given square
     *
     * @param square the target square
     * @return the piece if there is a piece on the square, otherwise null
     */
    public Piece pieceSearch(String square) {
        for (Piece piece: whitePieces) {
            if (piece != null && piece.getSquare().equals(square)) {
                return piece;
            }
        }
        for (Piece piece: blackPieces) {
            if (piece != null && piece.getSquare().equals(square)) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Gets the FEN string for the current position
     *
     * @return the current positions FEN string
     */
    public String getFEN() {
        char current = '0';
        String square;
        Piece piece;
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            if (((i) % 8 == 0) && i != 0) {
                if (Character.isDigit(current) && current > '0') {
                    fen.append(current);
                    current = '0';
                }
                fen.append('/');
            }
            square = mapIntToSquare(i);
            piece = pieceSearch(square);
            if (piece == null) {
                if (Character.isDigit(current)) {
                    current++;
                } else {
                    current = 1;
                }
            } else {
                if (Character.isDigit(current) && current > '0') {
                    fen.append(current);
                    current = '0';
                }
                if (piece.getColour().equals("White")) {
                    if (piece.getName().equals("Knight")) {
                        fen.append("N");
                    } else {
                        fen.append(Character.toUpperCase(piece.getName().charAt(0)));
                    }
                } else {
                    if (piece.getName().equals("Knight")) {
                        fen.append("n");
                    } else {
                        fen.append(Character.toLowerCase(piece.getName().charAt(0)));
                    }
                }
            }
            if (i == 63 && Character.isDigit(current) && current > '0') {
                fen.append(current);
            }
        }
        if (getTurn()) {
            fen.append(" w ");
        } else {
            fen.append(" b ");
        }
        boolean anyCastling = false;
        if (pieceSearch("e1") != null) {
            if (pieceSearch("e1").getName().equals("King") && (!((King) pieceSearch("e1")).getMoved())) ;
            {  //potential for castling
                if (pieceSearch("h1") != null) {
                    if (pieceSearch("h1").getName().equals("Rook") && (!((Rook) pieceSearch("h1")).getMoved())) {
                        fen.append("K");
                        anyCastling = true;
                    }
                }
                if (pieceSearch("a1") != null) {
                    if (pieceSearch("a1").getName().equals("Rook") && (!((Rook) pieceSearch("a1")).getMoved())) {
                        fen.append("Q");
                        anyCastling = true;
                    }
                }
            }
        }
        if (pieceSearch("e8") != null) {
            if (pieceSearch("e8").getName().equals("King") && (!((King) pieceSearch("e8")).getMoved())) ;
            {  //potential for castling
                if (pieceSearch("h8") != null) {
                    if (pieceSearch("h8").getName().equals("Rook") && (!((Rook) pieceSearch("h8")).getMoved())) {
                        fen.append("k");
                        anyCastling = true;
                    }
                }
                if (pieceSearch("a8") != null) {
                    if (pieceSearch("a8").getName().equals("Rook") && (!((Rook) pieceSearch("a8")).getMoved())) {
                        fen.append("q");
                        anyCastling = true;
                    }
                }
            }
        }
        if (!anyCastling) {
            fen.append('-');
        }
        String enPassantTarget = "-";
        for (int i = 0; i < 16; i++) {
            if (whitePieces[i].getName().equals("Pawn")) {
                if (((Pawn) whitePieces[i]).getEnPassantable()) {
                    enPassantTarget = String.valueOf(whitePieces[i].getFile()) + String.valueOf(whitePieces[i].getRank() - 1);
                    break;
                }
            }
            if (blackPieces[i].getName().equals("Pawn")) {
                if (((Pawn) blackPieces[i]).getEnPassantable()) {
                    enPassantTarget = String.valueOf(blackPieces[i].getFile()) + String.valueOf(blackPieces[i].getRank() + 1);
                    break;
                }
            }
        }
        fen.append(" ").append(enPassantTarget).append(" ");
        fen.append(getHalfMoveClock()).append(" ");
        fen.append(getMoveCount());
        return fen.toString();
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
