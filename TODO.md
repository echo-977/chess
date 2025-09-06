//TODO have an enum for current game state
//TODO set check move to put king in check and remove check detection from constructor
//TODO move clears whether the king was in check
//TODO move en passant target and castling data to be part of the board
//TODO move to using integers for the squares throughout rather than strings
//TODO write evaluation function
//TODO change threat map updates to only consider effected squares
//TODO move to bitboard based system
//TODO decompose board to only contain piece related things; 
    add a gamestate class for things like castling;
    and a position class containing an instance of each