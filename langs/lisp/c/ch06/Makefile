

CFLAGS=-std=c99 -Wall
LIBS=-ledit
OBJS=parsing.o mpc.o

all: parsing

parsing: $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS) $(LIBS)

parsing.o: parsing.c
mpc.o: mpc.c mpc.h

clean:
	rm -f parsing $(OBJS)
