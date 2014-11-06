#include "grid.hpp"
#include <iostream>
using namespace std;

Grid::Grid(int width, int height)
{
   m_width = width;
   m_height = height;
   m_cells = new bool*[m_width];
   
   for (int i = 0; i < m_width; i++)
   {
      m_cells[i] = new bool[m_height];
   }
   
   // set all elements to false (dead)
   for (int i = 0; i < m_width; i++)
   {
      for (int j = 0; j < m_height; j++)
      {
         m_cells[i][j] = false;
      }
   }
}

Grid::~Grid()
{
   for (int i = 0; i < m_height; i++)
   {
      delete[] m_cells[i];
   }
   
   delete[] m_cells;
}

int Grid::getWidth()
{
   return m_width;
}

int Grid::getHeight()
{
   return m_height;
}

/* given `p` it will determine if the element at that location is alive
 * (it will return false if not in the grid)
 */
bool Grid::alive(Pos *p)
{
   int x = p->x;
   int y = p->y;
   
   if (x < 0 || y < 0 || x >= m_width || y >= m_height)
      return false;
   else
      return m_cells[x][y];
}

/* given `p` it will determine if the element at that location is alive
 * (it will return false if not in the grid)
 */
bool Grid::alive(int x, int y)
{
   if (x < 0 || y < 0 || x >= m_width || y >= m_height)
      return false;
   else
      return m_cells[x][y];
}

/* given `p` it will determine if the element at that location is alive
 * (it will return false if not in the grid)
 */
bool Grid::alive(int p)
{
   int x = p % m_width;
   int y = p / m_height;
   
   if (x < 0 || y < 0 || x >= m_width || y >= m_height)
      return false;
   else
      return m_cells[x][y];
}

/* given `p` it will determine if the element at that location is dead
 * (it will return false if not in the grid)
 */ 
bool Grid::dead(Pos *p)
{
   int x = p->x;
   int y = p->y;
   
   if (x < 0 || y < 0 || x >= m_width || y >= m_height)
      return false;
   else
      return !m_cells[x][y];
}

/* given `p` it will determine if the element at that location is dead
 * (it will return false if not in the grid)
 */ 
bool Grid::dead(int x, int y)
{
   if (x < 0 || y < 0 || x >= m_width || y >= m_height)
      return false;
   else
      return !m_cells[x][y];
}

/* given `p` it will determine if the element at that location is dead
 * (it will return false if not in the grid)
 */ 
bool Grid::dead(int p)
{
   int x = p % m_width;
   int y = p / m_height;
   
   if (x < 0 || y < 0 || x >= m_width || y >= m_height)
      return false;
   else
      return !m_cells[x][y];
}


/* given `p` it will toggle the state of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::toggleState(Pos *p)
{
   m_cells[p->x][p->y] != m_cells[p->x][p->y];
}

/* given `p` it will toggle the state of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::toggleState(int x, int y)
{
   m_cells[x][y] != m_cells[x][y];
}

/* given `p` it will toggle the state of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::toggleState(int p)
{
   int x = p % m_width;
   int y = p / m_height;
   m_cells[x][y] != m_cells[x][y];
}


/* given `p` it will set the state to alive of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::setAlive(Pos *p)
{
   m_cells[p->x][p->y] = true;
}

/* given `p` it will set the state to alive of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::setAlive(int x, int y)
{
   m_cells[x][y] = true;
}

/* given `p` it will set the state to alive of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::setAlive(int p)
{
   int x = p % m_width;
   int y = p / m_height;

   m_cells[x][y] = true;
}


/* given `p` it will set the state to dead of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::setDead(Pos *p)
{
   m_cells[p->x][p->y] = false;
}

/* given `p` it will set the state to dead of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::setDead(int x, int y)
{
   m_cells[x][y] = false;
}

/* given `p` it will set the state to dead of the element at that location
 * (if `p` is not in the grid it will not do anything)
 */
void Grid::setDead(int p)
{
   int x = p % m_width;
   int y = p / m_height;

   m_cells[x][y] = false;
}


string Grid::toString()
{
   stringstream ss;
   Pos *p = new Pos(0,0);
   char c;
   for (int i = 0; i < m_width; i++)
   {
      for (int j = 0; j < m_height; j++)
      {
         p->x = j;
         p->y = i;
         c = (alive(p)) ? 't' : 'f';
         ss << c;
      }
   }
   return ss.str();
}

void Grid::update()
{
   Pos p(0, 0);
   Grid *g = new Grid(m_width, m_height);
   NeighborList *nl = new NeighborList();

   for (int i = 0; i < m_width; i++)
   {
      for (int j = 0; j < m_height; j++)
      {
         
         p.x = i;
         p.y = j;
         nl = findAliveNeighbors(this, &p);
         
         // rules to evolve from  
         if (alive(&p) && (nl->size() < 2))
            g->setDead(&p);
         else if (alive(&p) && (nl->size() > 3))
            g->setDead(&p);
         else if (alive(&p) && ((nl->size() == 2) || (nl->size() == 3)))
            g->setAlive(&p);
         else if (dead(&p) && (nl->size() == 3))
            g->setAlive(&p); 
        
         delete nl;
      }   
   }
   
   /* copy over the cells so we can delete the temporary grid */
   for (int i = 0; i < g->m_width; i++)
   {
      for (int j = 0; j < g->m_height; j++)
      {
         m_cells[i][j] = g->m_cells[i][j];
      }
   }
  
   delete g; 
}

void Grid::clear()
{
   for (int i = 0; i < m_width; i++)
   {
      for (int j = 0; j < m_height; j++)
      {
         m_cells[i][j] = false;
      }  
   }
}

void aliveNeighbors(Grid *g, NeighborList *nl)
{
   bool is_alive; 
   NeighborList *newNl = new NeighborList();
   
   for (NeighborList::iterator i = nl->begin(); i != nl->end(); i++)
   {     
    
      if (!(g->alive(*i))) 
         newNl->push_back((*i));
   }

   delete nl;

   nl = newNl;
}

NeighborList* findNeighbors(Pos *p)
{
   NeighborList *nl = new NeighborList();
   nl->push_back( new Pos( (p->x - 1), p->y)); // left neighbor
   nl->push_back( new Pos( (p->x + 1), p->y)); // right neighbor
   nl->push_back( new Pos( (p->x - 1), (p->y - 1))); // upper left neighbor
   nl->push_back( new Pos( p->x, (p->y - 1))); // neighbor above
   nl->push_back( new Pos( (p->x + 1), (p->y - 1))); // upper right neighbor
   nl->push_back( new Pos( (p->x - 1), (p->y + 1))); // lower left neighbor
   nl->push_back( new Pos( p->x, (p->y + 1))); // neighbor below
   nl->push_back( new Pos( (p->x + 1), (p->y + 1))); // lower right neighbor
  
   return nl;
}

NeighborList* findAliveNeighbors(Grid *g, Pos *p)
{
   NeighborList *nl = new NeighborList();
   int x = p->x;
   int y = p->y;
    
   if (g->alive(x - 1, y))
      nl->push_front( new Pos( (x - 1), y)); // left neighbor
   if (g->alive(x + 1, y))
      nl->push_front( new Pos( (x + 1), y)); // right neighbor
   if (g->alive(x - 1, y - 1))
      nl->push_front( new Pos( (x - 1), (y - 1))); // upper left neighbor
   if (g->alive(x, y - 1))
      nl->push_front( new Pos( x, (y - 1))); // neighbor above
   if (g->alive(x + 1, y - 1))
      nl->push_front( new Pos( (x + 1), (y - 1))); // upper right neighbor
   if (g->alive(x - 1, y + 1))
      nl->push_front( new Pos( (x - 1), (y + 1))); // lower left neighbor
   if (g->alive(x, y + 1))
      nl->push_front( new Pos(x, (y + 1))); // neighbor below
   if (g->alive(x + 1, y + 1))
      nl->push_front( new Pos( (x + 1), (y + 1))); // lower right neighbor
   
  
   return nl;
}




   
