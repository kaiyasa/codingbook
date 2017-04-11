

CFLAGS=-std=c99 -Wall
LIBS=-ledit

all: repl

repl: repl.c
	$(CC) $(CFLAGS) -o $@ $< $(LIBS)

clean:
	rm -f repl
