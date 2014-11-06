#include <iostream>
#include <netinet/in.h>
#include "grid.hpp"
#include "callback_table.hpp"
#include "server.hpp"

#define BOARD_WIDTH 64
#define BOARD_HEIGHT 64

using namespace std;

#define PORT_NO 10001

int main( int argc, char *argv[] )
{
   CallbackTable *table = CallbackTable::getCallbackTable();
   Grid *grid = new Grid(BOARD_WIDTH, BOARD_HEIGHT);
   sockaddr_in servAddr;
   int port = PORT_NO;

   /* set all the functions to the defaults in server.hpp */
   table->setFuncInTable(EXIT, exitFunc);
   table->setFuncInTable(ERROR, errorFunc);
   table->setFuncInTable(RCV, receiveFunc);
   table->setFuncInTable(SEND, sendFunc);
   table->setFuncInTable(UPDATE, updateFunc);
   table->setFuncInTable(CLEAR, clearFunc);
   
   start_server(grid, servAddr, port);
   
   delete grid;
   return 0;
}
