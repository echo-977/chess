public class FENUtils {
    /**
     * Constructs a board from a given FEN string.
     *
     * @param FEN  FEN string for the position.
     * @return the board representing the FEN position.
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
        Piece[] pieces = new Piece[ChessConstants.NUM_SQUARES];
        boolean moved;
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
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
            currentSquare = SquareMapUtils.mapIntToSquare(squareIndex);
            char file = SquareMapUtils.getFile(currentSquare);
            int rank = SquareMapUtils.getRank(currentSquare);
            switch (Character.toLowerCase(fen[FENConstants.PIECE_FIELD].charAt(current))) {
                case FENConstants.QUEEN_CHAR:
                    pieces[squareIndex] = new Queen(colour, file, rank);
                    break;
                case FENConstants.BISHOP_CHAR:
                    pieces[squareIndex] = new Bishop(colour, file, rank);
                    break;
                case FENConstants.KNIGHT_CHAR:
                    pieces[squareIndex] = new Knight(colour, file, rank);
                    break;
                case FENConstants.PAWN_CHAR:
                    if (rank == 7 && colour == PieceColour.BLACK) {
                        moved = false;
                    } else {
                        moved = !(rank == 2 && colour == PieceColour.WHITE);
                    }
                    pieces[squareIndex] = new Pawn(colour, file, rank, moved, false);
                    break;
                case FENConstants.KING_CHAR:
                    pieces[squareIndex] = new King(colour, file, rank, true, false);
                    break;
                case FENConstants.ROOK_CHAR:
                    pieces[squareIndex] = new Rook(colour, file, rank, true);
                    break;
            }
            current++;
        }
        Board board = new Board(pieces, turn, moveCount, halfMoveClock, new boolean[64], new boolean[64]);
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
                switch (piece.getType()) {
                    case PieceType.PAWN -> fen.append(piece.getColour() == PieceColour.WHITE ? FENConstants.WHITE_PAWN : FENConstants.BLACK_PAWN);
                    case PieceType.KING -> fen.append(piece.getColour() == PieceColour.WHITE ? FENConstants.WHITE_KING : FENConstants.BLACK_KING);
                    case PieceType.QUEEN -> fen.append(piece.getColour() == PieceColour.WHITE ? FENConstants.WHITE_QUEEN : FENConstants.BLACK_QUEEN);
                    case PieceType.ROOK -> fen.append(piece.getColour() == PieceColour.WHITE ? FENConstants.WHITE_ROOK : FENConstants.BLACK_ROOK);
                    case PieceType.BISHOP -> fen.append(piece.getColour() == PieceColour.WHITE ? FENConstants.WHITE_BISHOP : FENConstants.BLACK_BISHOP);
                    case PieceType.KNIGHT -> fen.append(piece.getColour() == PieceColour.WHITE ? FENConstants.WHITE_KNIGHT : FENConstants.BLACK_KNIGHT);
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
        fen.append(FENConstants.SPACE).append(board.getEnPassantTarget()).append(FENConstants.SPACE);
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
