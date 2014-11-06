#ifndef __SERVER_HPP__
#define __SERVER_HPP__

#include <iostream>
#include <cstdlib>
#include <unistd.h>
#include <string>
#include <cstring>
#include <tuple>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "callback_table.hpp"
#include "grid.hpp"

using namespace std;

#define CONTINUE_SERVER 0
#define EXIT_SERVER 1

/* 
 * Function to deduce which command is from `c` 
 */
Command getCommand(char c);

/*
 * Function to shutdown and clean up before exiting
 */
int exitFunc(int sockfd, void* arg);

/*
 * Function to send data to the client
 * arg: any user defined data
 */
int sendFunc(int sockfd, void* arg);

/*
 * Function that sends an error message
 */
int errorFunc(int sockfd, void* arg);

/*
 * Function to receive a grid of information from the client
 */
int receiveFunc(int sockfd, void* arg);

/*
 * Function to update the grid to it's next generation
 */
int updateFunc(int sockfd, void* arg);

/*
 * Function to clear the grid
 */
int clearFunc(int sockfd, void* arg);

/* 
 * Procedure to parse the string and populate the grid
 */
void unserialize(string s, Grid *g);

/*
 * Function to read all bytes from the client
 */
string readAll(int sockfd);

/*
 * Procedure to send all the bytes to the client
 */
void sendAll(int sockfd, string str);
 
/*
 * Procedure to handle all client requests
 */
void handleClient(Grid *grid, int sockfd);

/*
 * Function to start the server
 */
int start_server(Grid *grid, sockaddr_in servAddr, int port);

#endif //__SERVER_HPP__
