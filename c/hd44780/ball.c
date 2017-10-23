#include "gpio.h"
#include "hd44780.h"
#include "titles.h"
#include "audio.h"
#include "input.h"
#include <time.h>
#include <stdio.h>
#include <stdint.h>

#define PADDLE_DOWN 20
#define PADDLE_UP   21

const int pacman[] = {
	C4,-1, C5,-1, G4,-1, E4,-1, C5, G4, -1,-1, E4,E4,E4,E4,
	Db4,-1, Db5,-1, Ab4,-1, F4,-1, Db5, Ab4, -1,-1, F4,F4,F4,F4,
	C4,-1, C5,-1, G4,-1, E4,-1, C5, G4, -1,-1, E4,E4,E4,E4,
	Eb4, E4, F4, -1,
	F4, Gb4, G4, -1,
	G4, Ab4, A4, -1,
	C5, -1,-1,-1
};

#define TICK (CLOCKS_PER_SEC / 20)
clock_t tick_rate = 20;

struct ball {
	uint8_t direction;
	uint8_t x, y;
};
struct paddle {
	int8_t vel;
	uint8_t sub, y;
};

struct in_state A = {20, 0, 0, 0, 0}, B = {21, 0, 0, 0, 0};

void update_score(uint8_t p1, uint8_t p2) {
	send_data(DDRAM | 5 & DDRAM_MASK, RW_WRITE, RS_COMMAND);

	uint8_t digits = p1;
	p1 /= 10;
	digits -= p1 * 10;
	send_data('0' + p1,               RW_WRITE, RS_DATA);
	send_data('0' + digits,           RW_WRITE, RS_DATA);

	send_data(DDRAM | 9 & DDRAM_MASK, RW_WRITE, RS_COMMAND);

	digits = p2;
	p2 /= 10;
	digits -= p2 * 10;
	send_data('0' + p2,               RW_WRITE, RS_DATA);
	send_data('0' + digits,           RW_WRITE, RS_DATA);
}

void tick(struct ball *b, struct paddle *p1) {
	static uint8_t score1 = 0;
	static uint8_t score2 = 0;

	if((b->x == 5 || b->x == 74)
		&& b->y >= (p1->y & 15)
		&& b->y < ((p1->y & 15) + 4)
		&& b->x / 40 == (p1->y >> 4 & 1)) {
		//tick_rate += 5;
		/*if(b->x == 5) */update_score(++score1, score2);
		b->direction ^= 0x01;
	}

	if(b->y == 0 || b->y == 15) {
		b->direction ^= 0x02;
	}

	b->x += (b->direction & 0x01) * 2 - 1;
	b->y += (b->direction & 0x02) - 1;

	if(b->x < 5 || b->x >= 75) {
		//tick_rate = 10;
		update_score(score1 = 0, ++score2);
		b->x = 40;
		b->y = 8;
		b->direction ^= 0x03;
	}/* else if(b->x >= 75) {
		update_score(++score1, score2);
		b->x = 40;
		b->y = 8;
		b->direction ^= 0x03;
	}*/
}

void tick_paddle(struct paddle *p) {
	p->vel -= readp(PADDLE_DOWN) * 2;
	p->vel += readp(PADDLE_UP) * 2;
	if(p->vel > 16) p->vel = 16;
	if(p->vel < -16) p->vel = -16;

	p->sub += p->vel;
	if(p->vel < 0) {
		(p->vel)++;
	} else if(p->vel > 0) {
		(p->vel)--;
	};

	if(p->sub < 0) p->sub = 0;
	if(p->sub > 28 * 8) p->sub = 28 * 8;
	p->y = p->sub / 8;
}

int main() {
	struct ball b = {0, 40, 8};
	struct paddle p1 = {0, 0, 0};

	clock_t last, last2, next;
	uint8_t cindex;

	int i, j, k;
	for(i = RS; i <= BUSY; i++) {
		exportp(i);
		directp(i, DIR_OUT);
	}
	exportp(PADDLE_DOWN);
	directp(PADDLE_DOWN, DIR_IN);
	exportp(PADDLE_UP);
	directp(PADDLE_UP, DIR_IN);

	exportp(22);
	directp(22, DIR_OUT);

	send_data(SETUP | BYTE_INPUT | TWO_LINE, RW_WRITE, RS_COMMAND);
	send_data(CLEAR,                         RW_WRITE, RS_COMMAND);
	send_data(ENTRY_MODE | CUR_RIGHT,        RW_WRITE, RS_COMMAND);
	send_data(DISPLAY | DISPLAY_T,           RW_WRITE, RS_COMMAND);

	// Title begin
	/*send_data(CGRAM | 0x00 & CGRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 0; i < 64; i++) send_data(PONG_TITLE[i], RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x16 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 0; i < 4; i++) send_data(i, RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x56 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 4; i < 8; i++) send_data(i, RW_WRITE, RS_DATA);

	send_data(DDRAM | 0x14 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data('*', RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x1B & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data('*', RW_WRITE, RS_DATA);
	*/

	const uint8_t *titles[3];
	titles[0] = pong;
	titles[1] = snake;
	titles[2] = driver;

	k = 0;
	draw_title(titles[k], 0);
	draw_title(titles[k], 24);

	last = last2 = clock();

	i = j = 0;
	int menu = 1;
	while(menu) {
		if(i) {
			for(next = clock(); next >= last + (CLOCKS_PER_SEC / 10); last += (CLOCKS_PER_SEC / 10)) {
				send_data(M_SHIFT | SHIFT_TYPE, RW_WRITE, RS_COMMAND);
				if(i == 13) {
					k = (k + 1) % 3;
					draw_title(titles[k], 0);
					draw_title(titles[k], 24);
				}
				if(!--i) {
					send_data(RESET, RW_WRITE, RS_COMMAND);
					break;
				}
			}
		} else {
			for(next = clock(); next >= last + (CLOCKS_PER_SEC / 2); last += (CLOCKS_PER_SEC / 2), j++) {
				// Draw arrows
				send_data(DDRAM | 0x02 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
				send_data(j & 1 ? 0x7E : ' ', RW_WRITE, RS_DATA);
				send_data(DDRAM | 0x0D & DDRAM_MASK, RW_WRITE, RS_COMMAND);
				send_data(j & 1 ? 0x7F : ' ', RW_WRITE, RS_DATA);
			}
		}

		for(next = clock(); next >= last2 + (CLOCKS_PER_SEC / 10); last2 += (CLOCKS_PER_SEC / 10)) {
			tick_input(&B);
			tick_input(&A);

			if(!i && B.state == IN_RELEASE) {
				i = 24;
			}
			if(A.state == IN_RELEASE) {
				menu = 0;
			}
		}
	}

	send_data(CLEAR, RW_WRITE, RS_COMMAND);
	// Title end

	push_audio(pacman, 64);

	//const char *msg = "Choose mode";
	//send_data(DDRAM | 0x02 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	//for(i = 0; i < 11; i++) send_data(msg[i], RW_WRITE, RS_DATA);
	//sleep(5);

	send_data(DDRAM | 0x00 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data(0x01,                         RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x40 & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data(0x02,                         RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x0F & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data(0x03,                         RW_WRITE, RS_DATA);
	send_data(DDRAM | 0x4F & DDRAM_MASK, RW_WRITE, RS_COMMAND);
	send_data(0x04,                         RW_WRITE, RS_DATA);

	last = clock();

	send_data(CGRAM | 8 & CGRAM_MASK, RW_WRITE, RS_COMMAND);
	for(i = 0; i < 32; i++) send_data(0x00, RW_WRITE, RS_DATA);
	//for(i = 0; i < 16; i++) send_data(0x10, RW_WRITE, RS_DATA);

	update_score(0, 0);
	set_bpm(800);

	while(1) {
		tick_audio();

		for(next = clock(); next >= last + (CLOCKS_PER_SEC / tick_rate); last += (CLOCKS_PER_SEC / tick_rate)) {
			// Clear paddle
			send_data(CGRAM | (8 + p1.y) & CGRAM_MASK, RW_WRITE, RS_COMMAND);
			for(i = 0; i < 4; i++) send_data(0x00, RW_WRITE, RS_DATA);

			tick_paddle(&p1);

			// Write paddle
			send_data(CGRAM | (8 + p1.y) & CGRAM_MASK, RW_WRITE, RS_COMMAND);
			send_data(p1.y >= 16 ? 0x18 : 0x03, RW_WRITE, RS_DATA);
			send_data(p1.y >= 15 ? 0x10 : 0x01, RW_WRITE, RS_DATA);
			send_data(p1.y >= 14 ? 0x10 : 0x01, RW_WRITE, RS_DATA);
			send_data(p1.y >= 13 ? 0x18 : 0x03, RW_WRITE, RS_DATA);

			// Clear glyph
			send_data(CGRAM | b.y & 7 & CGRAM_MASK, 0, 0);
			send_data(0x00, 0, 1);

			cindex = (b.y / 8) * 0x40 + (b.x / 5);
			if(cindex != 5 && cindex != 6 && cindex != 9 && cindex != 10) {
				// Navigate to old tile
				send_data(DDRAM | cindex & DDRAM_MASK, RW_WRITE, RS_COMMAND);
				// Clear tile
				send_data(' ', RW_WRITE, RS_DATA);
			}

			tick(&b, &p1);

			// Write glyph
			send_data(CGRAM | b.y & 7 & CGRAM_MASK, 0, 0);
			send_data(0x10 >> (b.x % 5), 0, 1);

			cindex = (b.y / 8) * 0x40 + (b.x / 5);
			if(cindex != 5 && cindex != 6 && cindex != 9 && cindex != 10) {
				// Navigate to new tile
				send_data(DDRAM | cindex & DDRAM_MASK, RW_WRITE, RS_COMMAND);
				// Set tile
				send_data(0x00, RW_WRITE, RS_DATA);
			}
		}
	}
}
