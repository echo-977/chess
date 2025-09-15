public class MoveFlags {
    public static final int QUIET_MOVE = 0b0000;
    public static final int DOUBLE_PAWN_PUSH = 0b0001;
    public static final int KINGSIDE_CASTLE = 0b0010;
    public static final int QUEENSIDE_CASTLE = 0b0011;
    public static final int CAPTURE_BIT = 0b0100;
    public static final int EN_PASSANT = 0b0001;
    public static final int PROMOTION_BIT = 0b1000;
    public static final int KNIGHT = 0b0000;
    public static final int BISHOP = 0b0001;
    public static final int ROOK = 0b0010;
    public static final int QUEEN = 0b0011;
    public static final int FLAG_SHIFT = 12;
    public static final int DESTINATION_SHIFT = 6;
    public static final int DESTINATION_MASK = 0b0000111111000000;
    public static final int SOURCE_MASK = 0b0000000000111111;
    public static final int NO_MOVE = 0;
    public static final int FORCE_16_BIT = 0xFFFF;
    public static final int PROMOTION_MASK = 0b0011;
}
