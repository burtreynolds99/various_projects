#include "callback_table.hpp"

CallbackTable* CallbackTable::m_instance = NULL;

CallbackTable::CallbackTable()
{
   m_table = new ServerCallback[NUM_OF_CMDS];

}

CallbackTable::~CallbackTable()
{
   delete[] m_table;
}

CallbackTable* CallbackTable::getCallbackTable()
{
   if (m_instance == NULL)
      m_instance = new CallbackTable();

   return m_instance;
}

void CallbackTable::setFuncInTable(int index, ServerCallback cb)
{
   m_table[index] = cb;
}

ServerCallback CallbackTable::getFuncInTable(int index)
{
   return m_table[index];
}

int CallbackTable::operator()(int index, int sockfd, void* arg)
{
   return (*m_table[index])(sockfd, arg);
}
