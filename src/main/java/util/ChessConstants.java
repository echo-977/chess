public class ChessConstants {
    public static final int BOARD_SIZE = 8;

    public static final int NUM_PIECES = 32;
    public static final int NUM_SQUARES = 64;
    public static final int NUM_FILES = BOARD_SIZE;
    public static final int NUM_RANKS = BOARD_SIZE;
    public static final int NUM_DIRECTIONS = 8;
    public static final int NUM_PROMOTIONS = 4;

    public static final int MAX_PAWN_MOVES = 12;
    public static final int MAX_KING_MOVES = 8;
    public static final int MAX_QUEEN_MOVES = 27;
    public static final int MAX_ROOK_MOVES = 14;
    public static final int MAX_BISHOP_MOVES = 13;
    public static final int MAX_KNIGHT_MOVES = 8;

    public static final int KINGSIDE_CASTLE_FILE = Files.G;
    public static final int QUEENSIDE_CASTLE_FILE = Files.C;
    public static final int KINGSIDE_CASTLE_ROOK_FILE = Files.F;
    public static final int QUEENSIDE_CASTLE_ROOK_FILE = Files.D;
    public static final int KINGSIDE_ROOK_SOURCE_FILE = Files.H;
    public static final int QUEENSIDE_ROOK_SOURCE_FILE = Files.A;
    public static final int WHITE_KINGSIDE_ROOK_SQUARE = Squares.H1;
    public static final int WHITE_QUEENSIDE_ROOK_SQUARE = Squares.A1;
    public static final int BLACK_KINGSIDE_ROOK_SQUARE = Squares.H8;
    public static final int BLACK_QUEENSIDE_ROOK_SQUARE = Squares.A8;

    public static final int FILE_OFFSET = 1;
    public static final int RANK_OFFSET = 8;

    public static final int ROOK_DIRECTIONS = 4;
    public static final int BISHOP_DIRECTIONS = 4;

    public static final int NO_TRANSPOSITION = -1;
    public static final int PERFT_TABLE_SIZE = 2^24;

    public static final int NUM_OCCUPANCIES = 3;
    public static final int WHITE_BITBOARD = 0;
    public static final int BLACK_BITBOARD = 1;
    public static final int BOTH_BITBOARD = 2;
}
