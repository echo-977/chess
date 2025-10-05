//TODO have an enum for current game state
//TODO set check move to put king in check and remove check detection from constructor
//TODO move clears whether the king was in check
//TODO write evaluation function
//TODO change threat map updates to only consider effected squares 
//TODO add method for checking if a colour attacks a square based on the AFKTO table 
(check the colour of each case where the bit is 1)
the attack tables will be updated in doMove and can also be reset in unDoMove by saving them to the state before the move
//TODO move to bitboard based system
//TODO add method to compute whether a specific square is targeted
//TODO store attack mask as a piece attribute and update on move in some way (needs to update on other piece moves too potentially)