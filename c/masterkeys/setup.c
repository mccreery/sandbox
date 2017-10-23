#include "setup.h"
#include <stdio.h>
#include <signal.h>

int interval(void (*function)(int), cond_t condition, long interval) {
	clock_t frame = clock();

	for(int i = 0;;) {
		for(clock_t now = clock(); now >= frame + interval; frame += interval, i++) {
			if(!condition(i)) return i;
			function(i);
		}
	}
}

bool SetLedColorC(int iRow, int iColumn, KEY_COLOR color) {
	return SetLedColor(iRow, iColumn, color.r, color.g, color.b);
}

void cleanup();

int setup(DEVICE_INDEX device/*, int refresh*/) {
	printf("MasterKeys SDK %d\n", GetCM_SDK_DllVer());
	fputs("Setting up...", stdout);
	fflush(stdout);

	SetControlDevice(device);

	if(!EnableLedControl(1)) {
		fputs("\rUnable to grab LED control\n", stdout);
		return 0;
	}
	if(!IsDevicePlug()) {
		fputs("\rDevice is not plugged in\n", stdout);
		return 0;
	}

	atexit(cleanup);
	fputs("\rSetup complete\n", stdout);
}

void cleanup() {
	puts("Goodbye!");
	EnableLedControl(0);
}

static int running = 1;
static void handle(int signum) {running = 0;}
static int condition(int i) {return running;}

cond_t halter() {
	signal(SIGINT, handle);
	return &condition;
}
