.PHONY: all clean

CHROME ?= /c/Program Files (x86)/Google/Chrome/Application/chrome.exe
PAGE := keys.html

all: keys.png

clean:
	rm keys.png

keys.png: keys.html keys.css
	'$(CHROME)' --headless --default-background-color=0 \
	--screenshot='$(CURDIR)/keys.png' '$(CURDIR)/keys.html'
