//TODO have an enum for current game state
//TODO set check move to put king in check and remove check detection from constructor
//TODO move clears whether the king was in check
//TODO write evaluation function
//TODO change threat map updates to only consider effected squares 
//TODO add attack tables updating in doMove and resetting in unDoMove by saving them to the state before the move
//TODO move to bitboard based system
//TODO store attack mask as a piece attribute and update on move in some way (needs to update on other piece moves too potentially)