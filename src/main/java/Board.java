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
        String[] fen = FEN.split(FENConstants.SPACE);
        moveCount = Integer.parseInt(fen[FENConstants.FULLMOVE_CLOCK_FIELD]);
        halfMoveClock = Integer.parseInt(fen[FENConstants.HALFMOVE_CLOCK_FIELD]);
        turn = fen[FENConstants.TURN_FIELD].equals(FENConstants.WHITE);
        fen[FENConstants.PIECE_FIELD] = fen[FENConstants.PIECE_FIELD].replaceAll(FENConstants.NEW_RANK, "");
        String currentSquare;
        int skip = 0;
        int current = 0;
        String colour;
        Piece[] pieces = new Piece[ChessConstants.NUM_PIECES];
        int pieceCounter = 0;
        boolean moved;
        boolean enPassantable;
        for (int i = 0; i < ChessConstants.NUM_SQUARES; i++) {
            skip--;
            if (skip > 0) { //used for numbers in FEN string representing sequential empty squares
                continue;
            }
            if (Character.isDigit(fen[FENConstants.PIECE_FIELD].charAt(current))) {
                skip = Character.getNumericValue(fen[FENConstants.PIECE_FIELD].charAt(current));
                current++;
                continue;
            }
            if (Character.isUpperCase(fen[FENConstants.PIECE_FIELD].charAt(current))) {
                colour = ChessConstants.WHITE;
            } else {
                colour = ChessConstants.BLACK;
            }
            currentSquare = mapIntToSquare(i);
            char file = currentSquare.charAt(0);
            int rank = Integer.parseInt(currentSquare.substring(1, 2));
            switch (Character.toLowerCase(fen[FENConstants.PIECE_FIELD].charAt(current))) {
                case FENConstants.QUEEN_CHAR:
                    pieces[pieceCounter] = new Queen(colour, file, rank);
                    break;
                case FENConstants.BISHOP_CHAR:
                    pieces[pieceCounter] = new Bishop(colour, file, rank);
                    break;
                case FENConstants.KNIGHT_CHAR:
                    pieces[pieceCounter] = new Knight(colour, file, rank);
                    break;
                case FENConstants.PAWN_CHAR:
                    if (rank == 7 && colour.equals(ChessConstants.BLACK)) {
                        moved = false;
                    } else {
                        moved = !(rank == 2 && colour.equals(ChessConstants.WHITE));
                    }
                    if (fen[FENConstants.EN_PASSANT_FIELD].equals(FENConstants.NONE)) {
                        enPassantable = false;
                    } else if (colour.equals(ChessConstants.WHITE)) {
                        enPassantable = (file == fen[FENConstants.EN_PASSANT_FIELD].charAt(0) && rank + ChessDirections.DOWN == Integer.parseInt(fen[FENConstants.EN_PASSANT_FIELD].substring(1, 2)));
                    } else {
                        enPassantable = (file == fen[FENConstants.EN_PASSANT_FIELD].charAt(0) && rank + ChessDirections.UP == Integer.parseInt(fen[FENConstants.EN_PASSANT_FIELD].substring(1, 2)));
                    }
                    pieces[pieceCounter] = new Pawn(colour, file, rank, moved, enPassantable);
                    break;
                case FENConstants.KING_CHAR:
                    if (fen[FENConstants.CASTLING_FIELD].equals(fen[FENConstants.CASTLING_FIELD].toLowerCase()) && colour.equals(ChessConstants.WHITE)) {
                        moved = true;
                    } else
                        moved = fen[FENConstants.CASTLING_FIELD].equals(fen[FENConstants.CASTLING_FIELD].toUpperCase()) && colour.equals(ChessConstants.BLACK);
                    pieces[pieceCounter] = new King(colour, file, rank, moved, false);
                    break;
                case FENConstants.ROOK_CHAR:
                    if (colour.equals(ChessConstants.WHITE)) {
                        if (currentSquare.equals("a1") && fen[FENConstants.CASTLING_FIELD].contains(FENConstants.WHITE_QUEENSIDE_CASTLE)) {
                            moved = false;
                        } else
                            moved = !(currentSquare.equals("h1") && fen[FENConstants.CASTLING_FIELD].contains(FENConstants.WHITE_KINGSIDE_CASTLE));
                    } else {
                        if (currentSquare.equals("a8") && fen[FENConstants.CASTLING_FIELD].contains(FENConstants.BLACK_QUEENSIDE_CASTLE)) {
                            moved = false;
                        } else
                            moved = !(currentSquare.equals("h8") && fen[FENConstants.CASTLING_FIELD].contains(FENConstants.BLACK_KINGSIDE_CASTLE));
                    }
                    pieces[pieceCounter] = new Rook(colour, file, rank, moved);
                    break;
            }
            pieceCounter++;
            current++;
        }
        whitePieces = new Piece[ChessConstants.NUM_PIECES / 2];
        blackPieces = new Piece[ChessConstants.NUM_PIECES / 2];
        int whiteCounter = 0;
        int blackCounter = 0;
        for (Piece piece: pieces) {
            if (piece != null) {
                if (piece.getColour().equals(ChessConstants.WHITE)) {
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
                fen.append(FENConstants.NEW_RANK);
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
                if (piece.getColour().equals(ChessConstants.WHITE)) {
                    switch (piece.getType()) {
                        case PieceType.PAWN -> fen.append(FENConstants.WHITE_PAWN);
                        case PieceType.KING -> fen.append(FENConstants.WHITE_KING);
                        case PieceType.QUEEN -> fen.append(FENConstants.WHITE_QUEEN);
                        case PieceType.ROOK -> fen.append(FENConstants.WHITE_ROOK);
                        case PieceType.BISHOP -> fen.append(FENConstants.WHITE_BISHOP);
                        case PieceType.KNIGHT -> fen.append(FENConstants.WHITE_KNIGHT);
                    }
                } else {
                    switch (piece.getType()) {
                        case PieceType.PAWN -> fen.append(FENConstants.BLACK_PAWN);
                        case PieceType.KING -> fen.append(FENConstants.BLACK_KING);
                        case PieceType.QUEEN -> fen.append(FENConstants.BLACK_QUEEN);
                        case PieceType.ROOK -> fen.append(FENConstants.BLACK_ROOK);
                        case PieceType.BISHOP -> fen.append(FENConstants.BLACK_BISHOP);
                        case PieceType.KNIGHT -> fen.append(FENConstants.BLACK_KNIGHT);
                    }
                }
            }
            if (i == 63 && Character.isDigit(current) && current > '0') {
                fen.append(current);
            }
        }
        fen.append(FENConstants.SPACE);
        if (getTurn()) {
            fen.append(FENConstants.WHITE);
        } else {
            fen.append(FENConstants.BLACK);
        }
        fen.append(FENConstants.SPACE);
        boolean anyCastling = false;
        if (pieceSearch("e1") != null) {
            if (pieceSearch("e1").getType() == PieceType.KING && (!((King) pieceSearch("e1")).getMoved())) {  //potential for castling
                if (pieceSearch("h1") != null) {
                    if (pieceSearch("h1").getType() == PieceType.ROOK && (!((Rook) pieceSearch("h1")).getMoved())) {
                        fen.append(FENConstants.WHITE_KINGSIDE_CASTLE);
                        anyCastling = true;
                    }
                }
                if (pieceSearch("a1") != null) {
                    if (pieceSearch("a1").getType() == PieceType.ROOK && (!((Rook) pieceSearch("a1")).getMoved())) {
                        fen.append(FENConstants.WHITE_QUEENSIDE_CASTLE);
                        anyCastling = true;
                    }
                }
            }
        }
        if (pieceSearch("e8") != null) {
            if (pieceSearch("e8").getType() == PieceType.KING && (!((King) pieceSearch("e8")).getMoved())) {  //potential for castling
                if (pieceSearch("h8") != null) {
                    if (pieceSearch("h8").getType() == PieceType.ROOK && (!((Rook) pieceSearch("h8")).getMoved())) {
                        fen.append(FENConstants.BLACK_KINGSIDE_CASTLE);
                        anyCastling = true;
                    }
                }
                if (pieceSearch("a8") != null) {
                    if (pieceSearch("a8").getType() == PieceType.ROOK && (!((Rook) pieceSearch("a8")).getMoved())) {
                        fen.append(FENConstants.BLACK_QUEENSIDE_CASTLE);
                        anyCastling = true;
                    }
                }
            }
        }
        if (!anyCastling) {
            fen.append(FENConstants.NONE);
        }
        String enPassantTarget = FENConstants.NONE;
        for (int i = 0; i < 16; i++) {
            if (whitePieces[i].getType() == PieceType.PAWN) {
                if (((Pawn) whitePieces[i]).getEnPassantable()) {
                    enPassantTarget = whitePieces[i].getFile() + String.valueOf(whitePieces[i].getRank() - 1);
                    break;
                }
            }
            if (blackPieces[i].getType() == PieceType.PAWN) {
                if (((Pawn) blackPieces[i]).getEnPassantable()) {
                    enPassantTarget = blackPieces[i].getFile() + String.valueOf(blackPieces[i].getRank() + 1);
                    break;
                }
            }
        }
        fen.append(FENConstants.SPACE).append(enPassantTarget).append(FENConstants.SPACE);
        fen.append(getHalfMoveClock()).append(FENConstants.SPACE);
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
