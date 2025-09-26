public class FENUtils {
    /**
     * Constructs a board from a given FEN string.
     *
     * @param FEN FEN string for the position.
     * @return the board representing the FEN position.
     */
    public static Position positionFromFEN(String FEN) {
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
        int square = 0;
        PieceColour colour;
        Piece[] pieces = new Piece[ChessConstants.NUM_SQUARES];
        boolean moved;
        for (char fenCharacter : fen[FENConstants.PIECE_FIELD].toCharArray()) {
            if (Character.isDigit(fenCharacter)) {
                square += (fenCharacter - '0');
            } else {
                if (Character.isUpperCase(fenCharacter)) {
                    colour = PieceColour.WHITE;
                } else {
                    colour = PieceColour.BLACK;
                }
                switch (Character.toLowerCase(fenCharacter)) {
                    case FENConstants.QUEEN_CHAR:
                        pieces[square] = new Queen(colour, square);
                        break;
                    case FENConstants.BISHOP_CHAR:
                        pieces[square] = new Bishop(colour, square);
                        break;
                    case FENConstants.KNIGHT_CHAR:
                        pieces[square] = new Knight(colour, square);
                        break;
                    case FENConstants.KING_CHAR:
                        pieces[square] = new King(colour, square, false);
                        break;
                    case FENConstants.ROOK_CHAR:
                        pieces[square] = new Rook(colour, square);
                        break;
                    case FENConstants.PAWN_CHAR:
                        if (square >= Files.A + Ranks.SEVEN && square <= Files.H + Ranks.SEVEN && colour == PieceColour.BLACK) {
                            moved = false;
                        } else {
                            moved = !(square >= Files.A + Ranks.TWO && square <= Files.H + Ranks.TWO && colour == PieceColour.WHITE);
                        }
                        pieces[square] = new Pawn(colour, square, moved);
                        break;
                }
                square += FENConstants.SQUARE_OFFSET;
            }


        }
        Board board = new Board(pieces, new boolean[64], new boolean[64]);
        GameState gameState = new GameState(turn, moveCount, halfMoveClock);
        gameState.setCastlingRights(parseCastlingRights(fen[FENConstants.CASTLING_FIELD]));
        if (fen[FENConstants.EN_PASSANT_FIELD].equals(FENConstants.NONE)) {
            gameState.setEnPassantTarget(Squares.NONE);
        } else {
            gameState.setEnPassantTarget(SquareMapUtils.mapSquareToInt(fen[FENConstants.EN_PASSANT_FIELD]));
        }
        board.updateThreatMap(PieceColour.WHITE);
        board.updateThreatMap(PieceColour.BLACK);
        return new Position(board, gameState);
    }

    /**
     * Gets the FEN string for the current position
     *
     * @return the current positions FEN string
     */
    public static String getFEN(Position position) {
        Board board = position.getBoard();
        GameState gameState = position.getGameState();
        char current = '0';
        Piece piece;
        StringBuilder fen = new StringBuilder();
        for (int squareIndex = 0; squareIndex < ChessConstants.NUM_SQUARES; squareIndex++) {
            if (((squareIndex) % ChessConstants.NUM_FILES == 0) && squareIndex != 0) {
                if (Character.isDigit(current) && current > '0') {
                    fen.append(current);
                    current = '0';
                }
                fen.append(FENConstants.NEW_RANK);
            }
            piece = board.pieceSearch(squareIndex);
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
            if (squareIndex == 63 && Character.isDigit(current) && current > '0') {
                fen.append(current);
            }
        }
        fen.append(FENConstants.SPACE);
        if (gameState.getTurn() == PieceColour.WHITE) {
            fen.append(FENConstants.WHITE);
        } else {
            fen.append(FENConstants.BLACK);
        }
        fen.append(FENConstants.SPACE);
        boolean anyCastling = false;
        int castlingRights = gameState.getCastlingRights();
        if ((castlingRights & FENConstants.WHITE_KINGSIDE_CASTLE_MASK) != FENConstants.NO_CASTLING_MASK) {
            fen.append(FENConstants.WHITE_KINGSIDE_CASTLE_CHAR);
            anyCastling = true;
        }
        if ((castlingRights & FENConstants.WHITE_QUEENSIDE_CASTLE_MASK) != FENConstants.NO_CASTLING_MASK) {
            fen.append(FENConstants.WHITE_QUEENSIDE_CASTLE_CHAR);
            anyCastling = true;
        }
        if ((castlingRights & FENConstants.BLACK_KINGSIDE_CASTLE_MASK) != FENConstants.NO_CASTLING_MASK) {
            fen.append(FENConstants.BLACK_KINGSIDE_CASTLE_CHAR);
            anyCastling = true;
        }
        if ((castlingRights & FENConstants.BLACK_QUEENSIDE_CASTLE_MASK) != FENConstants.NO_CASTLING_MASK) {
            fen.append(FENConstants.BLACK_QUEENSIDE_CASTLE_CHAR);
            anyCastling = true;
        }
        if (!anyCastling) {
            fen.append(FENConstants.NONE);
        }
        fen.append(FENConstants.SPACE);
        if (gameState.getEnPassantTarget() == Squares.NONE) {
            fen.append(FENConstants.NONE);
        } else {
            fen.append(SquareMapUtils.mapIntToSquare(gameState.getEnPassantTarget()));
        }
        fen.append(FENConstants.SPACE);
        fen.append(gameState.getHalfMoveClock()).append(FENConstants.SPACE);
        fen.append(gameState.getMoveCount());
        return fen.toString();
    }

    /**
     * Converts the castling rights field of a FEN string to the bitwise mask used to set them.
     * @param castlingField the castling rights field of a FEN string.
     * @return 4 bit mask where each bit refers to a different castling condition
     */
    public static int parseCastlingRights(String castlingField) {
        int whiteKingsideCastle = castlingField.contains(FENConstants.WHITE_KINGSIDE_CASTLE_CHAR) ? FENConstants.WHITE_KINGSIDE_CASTLE_MASK : FENConstants.NO_CASTLING_MASK;
        int whiteQueensideCastle = castlingField.contains(FENConstants.WHITE_QUEENSIDE_CASTLE_CHAR) ? FENConstants.WHITE_QUEENSIDE_CASTLE_MASK : FENConstants.NO_CASTLING_MASK;
        int blackKingsideCastle = castlingField.contains(FENConstants.BLACK_KINGSIDE_CASTLE_CHAR) ? FENConstants.BLACK_KINGSIDE_CASTLE_MASK : FENConstants.NO_CASTLING_MASK;
        int blackQueensideCastle = castlingField.contains(FENConstants.BLACK_QUEENSIDE_CASTLE_CHAR) ? FENConstants.BLACK_QUEENSIDE_CASTLE_MASK : FENConstants.NO_CASTLING_MASK;
        return (whiteKingsideCastle | whiteQueensideCastle | blackKingsideCastle | blackQueensideCastle);
    }
}
