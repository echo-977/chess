public class ChessDirections {
    public static final int UP = -ChessConstants.RANK_OFFSET;
    public static final int DOWN = ChessConstants.RANK_OFFSET;
    public static final int LEFT = -ChessConstants.FILE_OFFSET;
    public static final int RIGHT = ChessConstants.FILE_OFFSET;
    public static final int UP_RIGHT = UP + RIGHT;
    public static final int UP_LEFT = UP + LEFT;
    public static final int DOWN_RIGHT = DOWN + RIGHT;
    public static final int DOWN_LEFT = DOWN + LEFT;
    public static final int NONE = 0;
}
