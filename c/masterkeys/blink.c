#include <SDKDLL.h>
#include <stdbool.h>
#include <time.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <memory.h>

#define STARS_MAX 8
bool setup();

typedef struct {
	int x, y;
	float stage, speed;
} STAR;

bool update_star(STAR * const star, KEY_COLOR color);

float brightness(float stage) {
	float val = stage < 0.5 ? stage*2.f : 2.f - stage*2.f;
	return val < 0 ? 0 : val > 1 ? 1 : val;
}

int randint(int max) {
	return ((float)rand()/(float)RAND_MAX) * max;
}

//COLOR_MATRIX keys;

bool SetLedColorC(int iRow, int iColumn, KEY_COLOR color) {
	/*int r = keys.KeyColor[iRow][iColumn].r + color.r;
	int g = keys.KeyColor[iRow][iColumn].g + color.g;
	int b = keys.KeyColor[iRow][iColumn].b + color.b;

	keys.KeyColor[iRow][iColumn].r = r>255?255:r;
	keys.KeyColor[iRow][iColumn].g = g>255?255:g;
	keys.KeyColor[iRow][iColumn].b = b>255?255:b;
	return 1;*/
	return SetLedColor(iRow, iColumn, color.r, color.g, color.b);
}

volatile int run = 1;
void halter(int signum) {run = 0;}

int main(int argc, char **argv) {
	if(!setup()) return 1;
	signal(SIGINT, halter);

	STAR stars[16];

	// Must not be dealloc'd
	static const KEY_COLOR STAR_COLOR = {0x00, 0x55, 0xFF};

	clock_t frame = clock();
	for(int n = 0; run; n++) {
		for(clock_t now = clock(); now >= frame + INTERVAL; frame += INTERVAL) {
			int new_stars = randint(3);

			for(int i = 0; i < STARS_MAX; i++) {
				if(stars[i].x >= 0) {
					if(!update_star(&stars[i], STAR_COLOR)) {
						stars[i].x = -1;
					}
				} else if(new_stars > 0) {
					stars[i].x = randint(MAX_LED_COLUMN);
					stars[i].y = randint(MAX_LED_ROW);
					stars[i].stage = 0.f;
					stars[i].speed = 0.03f + ((float)rand()/(float)RAND_MAX) * 0.07f;

					--new_stars;
				}
			}
		}
	}
}

/** @return true if star is alive */
bool update_star(STAR * const star, KEY_COLOR color) {
	const float mult = brightness(star->stage);
	color.r *= mult;
	color.g *= mult;
	color.b *= mult;
	SetLedColorC(star->y, star->x, color);

	const KEY_COLOR dim = {color.r*.3f, color.g*.3f, color.b*.3f};
	SetLedColorC(star->y, star->x - 1, dim);
	SetLedColorC(star->y, star->x + 1, dim);
	SetLedColorC(star->y - 1, star->x, dim);
	SetLedColorC(star->y + 1, star->x, dim);

	return (star->stage += star->speed) <= 1.1f;
}
