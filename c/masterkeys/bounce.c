#include "setup.h"
#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <time.h>

#define BITN(x, n)  (int)((x & (1<<n)) >> n)
#define MAX_BALLS   (8)
#define START_BALLS (3)

struct ball {
	int x, y, xVel, yVel;
	KEY_COLOR color;
	int enabled;
};

int running = 1;
void halter(int signum) {running = 0;}
int condition(int i) {return running;}

void function(int i);
void spawn(struct ball *ball);

int main(int argc, char **argv) {
	srand(time(NULL));
	setup(DEV_MKeys_S, 0);
	signal(SIGINT, halter);
	interval(function, condition, (CLOCKS_PER_SEC / 5));
}

void function(int i) {
	static struct ball balls[MAX_BALLS] = {0};
	static int need = START_BALLS;

	for(int i = 0; i < MAX_BALLS; i++) { // Cleaner, also spawns new balls
		if(balls[i].enabled) {
			SetLedColor(balls[i].y, balls[i].x, 0, 0, 0);
		} else if(need > 0) {
			spawn(&balls[i]);
			--need;
		}
	}
	need = rand() & 1; // Try spawn a new ball next time

	for(int i = 0; i < MAX_BALLS; i++) {
		if(!balls[i].enabled) continue;

		balls[i].x += balls[i].xVel;
		balls[i].y += balls[i].yVel;
		SetLedColorC(balls[i].y, balls[i].x, balls[i].color);

		int canDespawn = 3;

		if(balls[i].x <= 0) balls[i].xVel = 1;
		else if(balls[i].x >= MAX_LED_COLUMN - 1) balls[i].xVel = -1;
		else canDespawn ^= 2;

		if(balls[i].y <= 0) balls[i].yVel = 1;
		else if(balls[i].y >= MAX_LED_ROW - 1) balls[i].yVel = -1;
		else canDespawn ^= 1;

		if(canDespawn && rand() < (RAND_MAX >> 3)) {
			balls[i].enabled = 0;
		}
	}
	RefreshLed(0);

	// SAD
	for(int i = 0; i < MAX_BALLS; i++) {
		if(!balls[i].enabled) SetLedColor(balls[i].y, balls[i].x, 0, 0, 0);
	}
}

void spawn(struct ball *ball) {
	const KEY_COLOR colors[8] = {
		{255, 0, 0}, {0, 255, 0}, {0, 0, 255}, {255, 255, 0},
		{255, 127, 0}, {191, 0, 255}, {0, 255, 255}, {255, 127, 255}
	};
	const int mine = rand();

	if(BITN(mine, 14)) { // Varying X
		ball->x = mine % MAX_LED_COLUMN;
		ball->y = BITN(mine, 13) ? MAX_LED_ROW : 0;
	} else { // Varying Y
		ball->x = BITN(mine, 13) ? MAX_LED_COLUMN : 0;
		ball->y = mine % MAX_LED_ROW;
	}
	ball->xVel = BITN(mine, 12) ? 1 : -1;
	ball->yVel = BITN(mine, 11) ? 1 : -1;

	ball->color = colors[(mine >> 8) & 7];
	ball->enabled = 1;
}
