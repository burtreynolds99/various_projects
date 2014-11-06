#include "server.hpp"

Command getCommand(char c)
{  
  switch (c)
  {
  case 'e':
    return EXIT;
  case 's':
    return SEND;
  case 'p':
    return RCV;
  case 'u':
    return UPDATE;
  case 'c':
    return CLEAR;
  default:
    return ERROR;
  }
}

/*
 * Simple function to shutdown and clean up before exiting
 */
int exitFunc(int sockfd, void* arg)
{
   printf("Shutting down server...\n");
   close(sockfd); 
   exit(0); 

   return EXIT_SERVER;
}

/* simple function to send data to the client
 * sockfd: socket file descriptor
 * arg: any user definened value and type
 */
int sendFunc(int sockfd, void* arg)
{
   return 0;
}

/* simple function that sends an error message
 * sockfd: socket file descriptor
 * arg: any user definened value and type.
 */
int errorFunc(int sockfd, void* arg)
{
   string s = *(string*)arg;

   cout << s << endl;
   
   return 0;
}


/* procedure to parse the string and populate the grid.*/
void unserialize(string s, Grid *g)
{
   
   for (int i = 0; i < s.length(); i++)
   {
      if (s[i] == 't')
      {
         g->setAlive(i);
      }
      else if (s[i] == 'f')
      {
         g->setDead(i);
      }
      else
      {
         return;
      }
   } 
}

/* Function to reall all bytes from the client*/
string readAll(int sockfd)
{
   char buff[256];
   stringstream ss;
   char c = '\n';
   int n = 0;

   while (c != '\0')
   {
      n = read(sockfd, buff, 255);
      if (n == 0)
      {
	      break;
      }
      
      for (int i = 0; i < n; i++)
      {
	      ss << buff[i];
	      c = buff[i];
      }
   }
   return ss.str();
}

/* Simple func to receive a payload from the client*/
int receiveFunc(int sockfd, void* arg)
{
   tuple<string, Grid*> in = *(tuple<string, Grid*>*)arg;
   string input = get<0>(in);
   Grid *grid = get<1>(in);

   unserialize(input, grid);
   
   return 0;
}

void sendAll(int sockfd, string str)
{
  int len = str.length();
  int upperBnd = 256;
  char buff[256];
  int strIndex = 0;
  int buffIndex = 0;
  while(true)
  {
    upperBnd = (len <= upperBnd) ? len : upperBnd;
    for (buffIndex = 0; buffIndex < upperBnd; buffIndex++)
    {
      buff[buffIndex] = str[strIndex];
      strIndex++;
    }
    
    len -= 256;

    if (len > 0)
    {
      if (send(sockfd, buff, upperBnd, 0) != upperBnd)
      {
	      perror("Failed to send to client");
	      exit(1);
      }
    }
    else
    {
      buff[buffIndex] = '\0';
      if (send(sockfd, buff, upperBnd + 1, 0) != upperBnd + 1)
      {
         perror("Failed to send to client");
         exit(1);
      }
      break;
    }
  }
}


/* simple proc to update the grid to it's next generation*/
int updateFunc(int sockfd, void* arg)
{
   Grid *grid = (Grid*)arg;
   grid->update();
   string msg = grid->toString();
   sendAll(sockfd, msg);
   return 0;
}

int clearFunc(int sockfd, void* arg)
{
   Grid *grid = (Grid*)arg;
   grid->clear();
   
   return 0;
}

/* procedure to aid in handling the clients requests*/
void handleClient(Grid *grid, int sockfd)
{
   char buff[256];
   int received = 0;
   Command cmd;
   string input;
   CallbackTable *table = CallbackTable::getCallbackTable();

   /* Receive a message */
   if ((received = recv(sockfd, buff, 256, 0)) < 0)
   {
      perror("Failed to receive initial bytes from client");
      exit(1);
   }
   
   if (memcmp(buff, "wake", 4))
   {
     printf("Error, invalid start up command. Expected `wake` was given : %s.", buff);
     exit(1);
   }

   /* Send bytes and check for more incoming data */
   while (received > 0)
   {
          
      input = readAll(sockfd);
      received = input.length();
      if (input.compare("") == 0)
      {
	      perror("Failed to receive additional bytes from client");
	      exit(1);
      }
      
      cmd = getCommand(input[0]);

      switch (cmd)
      {
      case EXIT:
	      (*table)(EXIT, sockfd, NULL);
	      break;
      case RCV:
	      (*table)(RCV, sockfd, new tuple<string, Grid*>(input.substr(1), grid));
	      break;
      case SEND:
	      (*table)(ERROR, sockfd, &input);
	      break;
      case UPDATE:
	      (*table)(UPDATE, sockfd, grid);
	      break;
      case CLEAR:
         (*table)(CLEAR, sockfd, grid);
         break;
      default:
	      (*table)(ERROR, sockfd, &input);
	      break;
      }
   }
   
   close(sockfd);
}

int start_server(Grid *grid, sockaddr_in servAddr, int port)
{
   int sockfd, newsockfd, clientsockfd;
   char buffer[256];
   sockaddr_in cliAddr;
   int  n;
   Command cmd;
   string read_in;
  
   /* First call to socket() function */
   sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
   if (sockfd < 0) 
   {
       perror("ERROR opening socket");
       exit(1);
   }
    
   /* Initialize socket structure */
   memset((char *) &servAddr, 0, sizeof(servAddr));
   servAddr.sin_family = AF_INET;
   servAddr.sin_addr.s_addr = INADDR_ANY;
   servAddr.sin_port = htons(port);
 
   /* Now bind the host address using bind() call.*/
   if (bind(sockfd, (struct sockaddr *) &servAddr, sizeof(servAddr)) < 0)
   {
        perror("ERROR on binding");
        exit(1);
   }
   
   /* Now start listening for the clients, here process will
   *     * go in sleep mode and will wait for the incoming connection
   *         */
   if (listen(sockfd, 5) < 0)
   {  
      perror("Failed to listen to server socket");
      exit(1);
   }
   
   while (true)
   {
      socklen_t clilen = sizeof(cliAddr);

      /* Accept actual connection from the client */
      newsockfd = accept(sockfd, (sockaddr*)&cliAddr, &clilen);
      
      if (newsockfd < 0) 
      {
          perror("ERROR on accept");
          exit(1);
      }
      
      fprintf(stdout, "Client connected: %s\n", inet_ntoa(cliAddr.sin_addr));
      handleClient(grid, newsockfd);
   }
      return 0; 
}



