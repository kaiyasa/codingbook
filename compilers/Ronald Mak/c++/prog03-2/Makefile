
OBJS = buffer.o \
	common.o \
	error.o \
	misc.o \
	parser.o \
	scanner.o \
	tknnum.o \
	tknstrsp.o \
	tknword.o \
	tokeniz2.o

TARGET = tokeniz2

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $(OBJS)

clean:
	rm -f $(TARGET) $(OBJS)
