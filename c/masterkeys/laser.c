#include "setup.h"
#include <time.h>

struct ball {
	int x, y, xVel, yVel;
	KEY_COLOR color;
};

void function(int i);

int main(int argc, char **argv) {
	srand(time(NULL));
	setup(DEV_MKeys_S);
	interval(function, halter(), (CLOCKS_PER_SEC / 20));
}

void update(struct ball *ball) {
	ball->x += ball->xVel;
	ball->y += ball->yVel;
	SetLedColorC(ball->y, ball->x, ball->color);

	if(ball->x <= 0) ball->xVel = 1;
	else if(ball->x >= MAX_LED_COLUMN - 1) ball->xVel = -1;
	if(ball->y <= 0) ball->yVel = 1;
	else if(ball->y >= MAX_LED_ROW - 1) ball->yVel = -1;
}

void function(int i) {
	static struct ball head = {0, 0, 1, 1, {255,127,0}}, tail = {0, 0, 1, 1, {0,0,0}};

	update(&head);
	if(i >= 8) update(&tail);
	RefreshLed(0);
}
