public class ChessConstants {
    public static final int BOARD_SIZE = 8;

    public static final int NUM_PIECES = 32;
    public static final int NUM_SQUARES = 64;
    public static final int NUM_FILES = 8;
    public static final int NUM_RANKS = 8;
    public static final int NUM_DIRECTIONS = 8;
    public static final int NUM_PROMOTIONS = 4;

    public static final int MAX_PAWN_MOVES = 12;
    public static final int MAX_KING_MOVES = 8;
    public static final int MAX_QUEEN_MOVES = 27;
    public static final int MAX_ROOK_MOVES = 14;
    public static final int MAX_BISHOP_MOVES = 13;
    public static final int MAX_KNIGHT_MOVES = 8;

    public static final char KINGSIDE_CASTLE_FILE = Files.G;
    public static final char QUEENSIDE_CASTLE_FILE = Files.C;
    public static final char KINGSIDE_CASTLE_ROOK_FILE = Files.F;
    public static final char QUEENSIDE_CASTLE_ROOK_FILE = Files.D;

    public static final int NO_EN_PASSANT_TARGET = -1;

    public static final int FILE_OFFSET = 1;
    public static final int RANK_OFFSET = 8;
}
