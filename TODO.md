//TODO have an enum for current game state
//TODO set check move to put king in check and remove check detection from constructor
//TODO move clears whether the king was in check
//TODO move to using integers for the squares throughout rather than strings
//TODO write evaluation function
//TODO change threat map updates to only consider effected squares
//TODO move to bitboard based system
//TODO remove any useage of causesCheck in Move class and replace with a doMove unDoMove strategy
//TODO move the generateMoves method in the board/position class to be done by an external moveGenerator class
//TODO change move class to only contain source and destination, 
    any flags can be computed when the move is played to reduce computation