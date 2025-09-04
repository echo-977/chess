public class FENUtils {
    /**
     * Constructs a board from a given FEN string.
     *
     * @param FEN  FEN string for the position.
     */
    public static Board boardFromFEN(String FEN) {
        String[] fen = FEN.split(FENConstants.SPACE);
        int moveCount = Integer.parseInt(fen[FENConstants.FULLMOVE_CLOCK_FIELD]);
        int halfMoveClock = Integer.parseInt(fen[FENConstants.HALFMOVE_CLOCK_FIELD]);
        PieceColour turn;
        if (fen[FENConstants.TURN_FIELD].equals(FENConstants.WHITE)) {
            turn = PieceColour.WHITE;
        } else {
            turn = PieceColour.BLACK;
        }
        fen[FENConstants.PIECE_FIELD] = fen[FENConstants.PIECE_FIELD].replaceAll(FENConstants.NEW_RANK, "");
        String currentSquare;
        int skip = 0;
        int current = 0;
        PieceColour colour;
        Piece[] pieces = new Piece[ChessConstants.NUM_PIECES];
        int pieceCounter = 0;
        boolean moved;
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
                colour = PieceColour.WHITE;
            } else {
                colour = PieceColour.BLACK;
            }
            currentSquare = SquareMapUtils.mapIntToSquare(i);
            char file = SquareMapUtils.getFile(currentSquare);
            int rank = SquareMapUtils.getRank(currentSquare);
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
                    if (rank == 7 && colour == PieceColour.BLACK) {
                        moved = false;
                    } else {
                        moved = !(rank == 2 && colour == PieceColour.WHITE);
                    }
                    pieces[pieceCounter] = new Pawn(colour, file, rank, moved, false);
                    break;
                case FENConstants.KING_CHAR:
                    pieces[pieceCounter] = new King(colour, file, rank, true, false);
                    break;
                case FENConstants.ROOK_CHAR:
                    pieces[pieceCounter] = new Rook(colour, file, rank, true);
                    break;
            }
            pieceCounter++;
            current++;
        }
        Piece[] whitePieces = new Piece[ChessConstants.NUM_PIECES / 2];
        Piece[] blackPieces = new Piece[ChessConstants.NUM_PIECES / 2];
        int whiteCounter = 0;
        int blackCounter = 0;
        for (Piece piece: pieces) {
            if (piece != null) {
                if (piece.getColour() == PieceColour.WHITE) {
                    whitePieces[whiteCounter] = piece;
                    whiteCounter++;
                } else {
                    blackPieces[blackCounter] = piece;
                    blackCounter++;
                }
            }
        }
        Board board = new Board(whitePieces, blackPieces, turn, moveCount, halfMoveClock, new boolean[64], new boolean[64]);
        board.setCastlingRights(parseCastlingRights(fen[FENConstants.CASTLING_FIELD]));
        board.setEnPassantFlag(fen[FENConstants.EN_PASSANT_FIELD]);
        board.updateThreatMap(PieceColour.WHITE);
        board.updateThreatMap(PieceColour.BLACK);
        return board;
    }

    /**
     * Gets the FEN string for the current position
     *
     * @return the current positions FEN string
     */
    public static String getFEN(Board board) {
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
            square = SquareMapUtils.mapIntToSquare(i);
            piece = board.pieceSearch(square);
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
                if (piece.getColour() == PieceColour.WHITE) {
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
        if (board.getTurn() == PieceColour.WHITE) {
            fen.append(FENConstants.WHITE);
        } else {
            fen.append(FENConstants.BLACK);
        }
        fen.append(FENConstants.SPACE);
        boolean anyCastling = false;
        if (board.pieceSearch("e1") != null) {
            if (board.pieceSearch("e1").getType() == PieceType.KING && (!((King) board.pieceSearch("e1")).getMoved())) {  //potential for castling
                if (board.pieceSearch("h1") != null) {
                    if (board.pieceSearch("h1").getType() == PieceType.ROOK && (!((Rook) board.pieceSearch("h1")).getMoved())) {
                        fen.append(FENConstants.WHITE_KINGSIDE_CASTLE_CHAR);
                        anyCastling = true;
                    }
                }
                if (board.pieceSearch("a1") != null) {
                    if (board.pieceSearch("a1").getType() == PieceType.ROOK && (!((Rook) board.pieceSearch("a1")).getMoved())) {
                        fen.append(FENConstants.WHITE_QUEENSIDE_CASTLE_CHAR);
                        anyCastling = true;
                    }
                }
            }
        }
        if (board.pieceSearch("e8") != null) {
            if (board.pieceSearch("e8").getType() == PieceType.KING && (!((King) board.pieceSearch("e8")).getMoved())) {  //potential for castling
                if (board.pieceSearch("h8") != null) {
                    if (board.pieceSearch("h8").getType() == PieceType.ROOK && (!((Rook) board.pieceSearch("h8")).getMoved())) {
                        fen.append(FENConstants.BLACK_KINGSIDE_CASTLE_CHAR);
                        anyCastling = true;
                    }
                }
                if (board.pieceSearch("a8") != null) {
                    if (board.pieceSearch("a8").getType() == PieceType.ROOK && (!((Rook) board.pieceSearch("a8")).getMoved())) {
                        fen.append(FENConstants.BLACK_QUEENSIDE_CASTLE_CHAR);
                        anyCastling = true;
                    }
                }
            }
        }
        if (!anyCastling) {
            fen.append(FENConstants.NONE);
        }
        String enPassantTarget = FENConstants.NONE;
        Piece[] whitePieces = board.getWhitePieces();
        Piece[] blackPieces = board.getBlackPieces();
        for (int i = 0; i < 16; i++) {
            if (whitePieces[i] != null && whitePieces[i].getType() == PieceType.PAWN) {
                if (((Pawn) whitePieces[i]).getEnPassantable()) {
                    enPassantTarget = whitePieces[i].getFile() + String.valueOf(whitePieces[i].getRank() + ChessDirections.DOWN);
                    break;
                }
            }
            if (blackPieces[i] != null && blackPieces[i].getType() == PieceType.PAWN) {
                if (((Pawn) blackPieces[i]).getEnPassantable()) {
                    enPassantTarget = blackPieces[i].getFile() + String.valueOf(blackPieces[i].getRank() + ChessDirections.UP);
                    break;
                }
            }
        }
        fen.append(FENConstants.SPACE).append(enPassantTarget).append(FENConstants.SPACE);
        fen.append(board.getHalfMoveClock()).append(FENConstants.SPACE);
        fen.append(board.getMoveCount());
        return fen.toString();
    }

    /**
     * Converts the castling rights field of a FEN string to the bitwise mask used to set them.
     * @param castlingField the castling rights field of a FEN string.
     * @return 4 bit mask where each bit refers to a different castling condition
     */
    public static int parseCastlingRights(String castlingField) {
        int whiteKingsideCastle = castlingField.contains(FENConstants.WHITE_KINGSIDE_CASTLE_CHAR) ? FENConstants.WHITE_KINGSIDE_CASTLE_MASK : FENConstants.NO_CASTLE_MASK;
        int whiteQueensideCastle = castlingField.contains(FENConstants.WHITE_QUEENSIDE_CASTLE_CHAR) ? FENConstants.WHITE_QUEENSIDE_CASTLE_MASK : FENConstants.NO_CASTLE_MASK;
        int blackKingsideCastle = castlingField.contains(FENConstants.BLACK_KINGSIDE_CASTLE_CHAR) ? FENConstants.BLACK_KINGSIDE_CASTLE_MASK : FENConstants.NO_CASTLE_MASK;
        int blackQueensideCastle = castlingField.contains(FENConstants.BLACK_QUEENSIDE_CASTLE_CHAR) ? FENConstants.BLACK_QUEENSIDE_CASTLE_MASK : FENConstants.NO_CASTLE_MASK;
        return (whiteKingsideCastle | whiteQueensideCastle | blackKingsideCastle | blackQueensideCastle);
    }
}
