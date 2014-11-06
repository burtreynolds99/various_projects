# MAKEFILE FOR GAME OF LIFE
# this program utilizes c++ 0x features,
#  so it will not compile before gcc 4.3
CC=g++
CFLAGS = -std=c++0x -c
OBJS = main.o grid.o callback_table.o server.o
EXE = gol_server

all: $(EXE)

$(EXE): $(OBJS)
	$(CC) $(OBJS) -o $(EXE)

main.o: main.cpp 
	$(CC) $(CFLAGS) main.cpp

grid.o: grid.cpp
	$(CC) $(CFLAGS) grid.cpp

callback_table.o: callback_table.cpp
	$(CC) $(CFLAGS) callback_table.cpp

server.o: server.cpp
	$(CC) $(CFLAGS) server.cpp

.PHONY: clean
clean:
	rm -rf $(OBJS) $(EXE)
