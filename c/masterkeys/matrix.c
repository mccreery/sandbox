#include "setup.h"
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <time.h>
#include <memory.h>
#include <math.h>

#define FRAMES   (10)
#define HIGH     (MAX_LED_ROW * (FRAMES - 1))

int running = 1;
void halter(int signum) {running = 0;}
int condition(int i) {return running;}

void function(int i);

int randint(int max) {
	return ((float)rand()/(float)RAND_MAX) * max;
}

typedef struct {
	KEY_COLOR KeyColor[MAX_LED_ROW * FRAMES][MAX_LED_COLUMN];
	int offset;
} ROLLING_MATRIX;

#define FRAME(m) (COLOR_MATRIX *)((unsigned char *)&m.KeyColor + m.offset * sizeof(KEY_COLOR[MAX_LED_COLUMN]))

int main(int argc, char **argv) {
	srand(time(NULL));
	setup(DEV_MKeys_S, 1);
	signal(SIGINT, halter);
	interval(function, condition, (CLOCKS_PER_SEC / 8));
}

void function(int i) {
	static ROLLING_MATRIX matrix = {{0}, HIGH};
	static COLOR_MATRIX *frame;

	// Roll
	if(--matrix.offset < 0) { // Bug: matrix repeats a frame when rolling around
		matrix.offset = HIGH;
		frame = FRAME(matrix);
		memcpy(frame->KeyColor[1], matrix.KeyColor, sizeof(KEY_COLOR[MAX_LED_ROW - 1][MAX_LED_COLUMN]));
	} else {
		frame = FRAME(matrix);
	}

	// generate row
	for(int j = 0; j < MAX_LED_COLUMN; j++) {
		frame->KeyColor[0][j].r = max((int)frame->KeyColor[1][j].r - 100, 0);
		frame->KeyColor[0][j].g = max((int)frame->KeyColor[1][j].g - 20, 0);
		frame->KeyColor[0][j].b = max((int)frame->KeyColor[1][j].b - 100, 0);
	}

	// Spawn new
	for(int new = randint(3); new > 0; new--) {
		int x = randint(MAX_LED_COLUMN);
		frame->KeyColor[0][x].r = 255;
		frame->KeyColor[0][x].g = 255;
		frame->KeyColor[0][x].b = 255;
	}

	// Update
	if(!SetAllLedColor(*frame)) {
		fputs("Unable to update keyboard", stderr);
		running = 0;
	}
}
