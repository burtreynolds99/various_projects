#ifndef __GRID_HPP__
#define __GRID_HPP__

#include <string>
#include <sstream>
#include <list>

using namespace std;
struct Pos
{
   int x;
   int y;
   Pos(int x1, int y1): x(x1), y(y1){}
};

typedef std::list<Pos*> NeighborList;

class Grid
{
private:
   bool **m_cells;
   int   m_width;
   int   m_height;
   
public:
   Grid(int width, int height);
   ~Grid();
   int getWidth();
   int getHeight();
   bool alive(Pos *p);
   bool alive(int x, int y);
   bool alive(int p);
   bool dead(Pos *p);
   bool dead(int p);
   bool dead(int x, int y);
   void toggleState(Pos *p);
   void toggleState(int p);
   void toggleState(int x, int y);
   void setAlive(Pos *p);
   void setAlive(int p);
   void setAlive(int x, int y);
   void setDead(Pos *p);
   void setDead(int p);
   void setDead(int x, int y);
   string toString();
   void update();
   void clear(); 
};

void aliveNeighbors(Grid *g, NeighborList *nl);
NeighborList* findAliveNeighbors(Grid *g, Pos *p);
NeighborList* findNeighbors(Pos *p);

#endif //__GRID_HPP__
