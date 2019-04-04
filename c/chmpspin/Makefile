.DUMMY: all clean

CXX := g++

#CXX_FLAGS := -Iinclude -lgdi32 -lopengl32 -lglu32
CXX_FLAGS := -Iinclude -lopengl32 -lWs2_32 -lole32 -lcomctl32 -lgdi32 -lcomdlg32 -luuid
CXX_FILES := $(patsubst src/%.cpp,%,$(shell find src/ -name '*.cpp'))
RES_FILES := $(shell find res/ -name '*.*')

all: $(patsubst %,build/%.o,$(CXX_FILES) $(RES_FILES))
	$(CXX) $(CXX_FLAGS) $^ -o chmpspin

clean:
	rm -rf build/

build/%.o: src/%.cpp
	mkdir -p $(dir $@)
	$(CXX) -c $(CXX_FLAGS) -o $@ $<

build/res/%.o: res/%
	mkdir -p $(dir $@)

	# Append NUL terminator to text
	cp $< build/$<
	printf '%b' '\0' >> build/$<

	# Output the object file containing the NUL terminated string
	cd build/res; ld -r -b binary -o $(patsubst res/%,%.o,$<) $(patsubst res/%,%,$<)
	# ld produces a .data section by default
	objcopy --rename-section .data=.rodata,alloc,load,readonly,data,contents $@ $@
