#ifndef __CALLBACK_TABLE_HPP__
#define __CALLBACK_TABLE_HPP__

#include <cstdlib>

// function pointer for callbacks
// int: used for socket file descriptor
// void*: used for implementors args
typedef int (*ServerCallback)(int, void*);

enum Command
{
   EXIT,
   RCV,
   SEND,
   UPDATE,
   CLEAR,
   ERROR
};

#define NUM_OF_CMDS 6 // update when adding to Command

class CallbackTable
{
private:
   ServerCallback *m_table;
   static CallbackTable *m_instance;
   CallbackTable();

public:
   ~CallbackTable();
   static CallbackTable* getCallbackTable();
   void setFuncInTable(int index, ServerCallback cb);
   ServerCallback getFuncInTable(int index);
   int operator()(int index, int sockfd, void *arg);
};

#endif // __CALLBACK_TABLE_HPP__
