.PHONY: all clean dirs

CC := x86_64-w64-mingw32-gcc
CPPFLAGS := -Iinclude
LDFLAGS := lib/SDKDLL64.lib

SDKURL := https://makerhub.coolermaster.com/custom-lighting/assets/sdk/coolermaster-sdk.zip
SDKZIP := sdk.zip

all: dirs bin/blink.exe bin/bounce.exe bin/heat.exe bin/laser.exe bin/matrix.exe
clean:
	rm -rf bin/ lib/ include/ $(SDKZIP)

dirs:
	mkdir -p bin/ lib/ include/

lib/SDKDLL64.dll: dirs $(SDKZIP)
	unzip -oj $(SDKZIP) Src/SDK/x64/SDKDLL.dll

lib/SDKDLL64.lib: dirs $(SDKZIP)
	unzip -oj $(SDKZIP) Src/SDK/x64/SDKDLL64.lib -d lib/

include/SDKDLL.h: dirs $(SDKZIP)
	unzip -oj $(SDKZIP) Src/SDK/x64/SDKDLL.h -d include/

$(SDKZIP):
	wget $(SDKURL) -O $(SDKZIP)

bin/%.exe: bin/%.o
	$(CC) $(LDFLAGS) $(CFLAGS) $^ -o $@

bin/%.o: %.c include/SDLDLL.h
	$(CC) $(CPPFLAGS) $(CFLAGS) -c $< -o $@
