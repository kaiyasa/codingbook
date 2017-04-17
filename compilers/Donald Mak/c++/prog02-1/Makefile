
OBJS = buffer.o common.o error.o list.o
TARGET = list

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $(OBJS)

clean:
	rm -f $(TARGET) $(OBJS)
