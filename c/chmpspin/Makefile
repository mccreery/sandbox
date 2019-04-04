.DUMMY: all clean

CXX := g++

#CXX_FLAGS := -Iinclude -lgdi32 -lopengl32 -lglu32
CXX_FLAGS := -Iinclude -lopengl32 -lWs2_32 -lole32 -lcomctl32 -lgdi32 -lcomdlg32 -luuid
CXX_FILES := $(patsubst src/%.cpp,%,$(shell find src/ -name '*.cpp'))
RES_FILES := $(shell find res/ -name '*.*')

all: $(patsubst %,build/%.o,$(CXX_FILES))
	$(CXX) $(CXX_FLAGS) $^ -o chmpspin

clean:
	rm -rf build/

build/res/%.o: res/$(patsubst %.c,%.o,%)
	mkdir -p $(dir $@)
	ld -r -b binary -o $@ $<

build/%.o: src/%.cpp
	mkdir -p $(dir $@)
	$(CXX) -c $(CXX_FLAGS) -o $@ $<
