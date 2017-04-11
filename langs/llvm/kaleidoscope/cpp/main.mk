
TARGET       := kcc
TARGET_DIR   := build/dist

CXXFLAGS     := -std=c++11 -g -I/usr/include/llvm-3.8
SOURCES      := $(shell find src -name '*.cpp')
TGT_LDFLAGS  :=
TGT_LDLIBS   :=

SRC_INCDIRS  :=
