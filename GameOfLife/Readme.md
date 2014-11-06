How to get started with GameOfLife:

1) First run make
2) start gol_server
3) run game_of_life_client.rkt

Controls for the game:

Toggling a cell - just click on the screen to toggle any cell you want.

Pushing to the server - to push the configuration of cells to the server press the 'p' key.

Send an update request - press 'u' to get the next generation from the server

Send a clear request - press 'c' to tell the server to clear its configuration.  ** Note you must update manually afterward to see results

Loop updates - press 'l' to toggle whether to loop updates.  If the game is idle pressing 'l' will make the game update each tick.  If the game is already updating every tick, and you press 'l' it will make the game go idle. 

Exiting the game - to exit and shutdown the server at the same time, press 'e'.
