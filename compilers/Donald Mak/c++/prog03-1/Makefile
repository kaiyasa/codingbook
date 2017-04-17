
OBJS = buffer.o \
	common.o \
	error.o \
	misc.o \
	parser.o \
	scanner.o \
	token.o \
	tokeniz1.o

TARGET = tokeniz1

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $(OBJS)

clean:
	rm -f $(TARGET) $(OBJS)
