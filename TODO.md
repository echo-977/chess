//TODO have an enum for current game state
//TODO set check move to put king in check and remove check detection from constructor
//TODO move clears whether the king was in check
//TODO write evaluation function
//TODO change threat map updates to only consider effected squares
//TODO move to bitboard based system
//TODO change move class to only contain source and destination, 
    any flags can be computed when the move is played to reduce computation
    (instead of using an object use an integer with masks to access movetype flags and start, end squares etc)