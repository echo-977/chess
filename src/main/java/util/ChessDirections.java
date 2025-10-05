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
    public static final int[] ALL = {ChessDirections.UP, ChessDirections.RIGHT, ChessDirections.DOWN,
            ChessDirections.LEFT, ChessDirections.UP_RIGHT, ChessDirections.DOWN_RIGHT, ChessDirections. DOWN_LEFT,
            ChessDirections.UP_LEFT};
    public static final int[] OPPOSITE_INDEX = {2, 3, 0, 1, 6, 7, 4, 5};
    public static final int RIGHT_INDEX = 1;
    public static final int LEFT_INDEX = 3;
}
