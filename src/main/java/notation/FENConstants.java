public class FENConstants {
    public static final String SPACE = " ";
    public static final String NONE = "-";
    public static final String NEW_RANK = "/";
    public static final String WHITE = "w";
    public static final String BLACK = "b";

    public static final int PIECE_FIELD = 0;
    public static final int TURN_FIELD = 1;
    public static final int CASTLING_FIELD = 2;
    public static final int EN_PASSANT_FIELD = 3;
    public static final int HALFMOVE_CLOCK_FIELD = 4;
    public static final int FULLMOVE_CLOCK_FIELD = 5;

    public static final char PAWN_CHAR = 'p';
    public static final char QUEEN_CHAR = 'q';
    public static final char KING_CHAR = 'k';
    public static final char KNIGHT_CHAR = 'n';
    public static final char ROOK_CHAR = 'r';
    public static final char BISHOP_CHAR = 'b';

    public static final String WHITE_KINGSIDE_CASTLE_CHAR = "K";
    public static final String WHITE_QUEENSIDE_CASTLE_CHAR = "Q";
    public static final String BLACK_KINGSIDE_CASTLE_CHAR = "k";
    public static final String BLACK_QUEENSIDE_CASTLE_CHAR = "q";
    public static final int WHITE_KINGSIDE_CASTLE_MASK = 1;
    public static final int WHITE_QUEENSIDE_CASTLE_MASK = 2;
    public static final int BLACK_KINGSIDE_CASTLE_MASK = 4;
    public static final int BLACK_QUEENSIDE_CASTLE_MASK = 8;
    public static final int NO_CASTLING_MASK = 0;

    public static final char WHITE_PAWN = 'P';
    public static final char WHITE_QUEEN = 'Q';
    public static final char WHITE_KING = 'K';
    public static final char WHITE_KNIGHT = 'N';
    public static final char WHITE_ROOK = 'R';
    public static final char WHITE_BISHOP = 'B';

    public static final char BLACK_PAWN = 'p';
    public static final char BLACK_QUEEN = 'q';
    public static final char BLACK_KING = 'k';
    public static final char BLACK_KNIGHT = 'n';
    public static final char BLACK_ROOK = 'r';
    public static final char BLACK_BISHOP = 'b';
}
