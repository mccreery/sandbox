#include <stdio.h>
#include <stdlib.h>
#include <termios.h>

void draw_template();
int cell;
int turn = {0};

struct {
	int rows[3][2];
	int cols[3][2];
	int diag[2][2];
} score;

void reposition() {
	char command[4] = "\e[2E";
	command[2] += ((8 - cell) / 3) * 2;
	fputs(command, stdout);
	fflush(stdout);
}

void absoluteMove(int dest) {
	int delta = dest - cell;
	if(delta > 0) {
		printf("\e[%dB\e[%dC", delta/3, delta%3);
	} else {
		delta = -delta;
		printf("\e[%dA\e[%dD", delta/3, delta%3);
	}
	cell = dest;
}

int main(int argc, char **argv) {
	atexit(reposition);

	struct termios term;
	tcgetattr(0, &term);
	term.c_lflag &= ~(ICANON|ECHO);
	tcsetattr(0, TCSAFLUSH, &term);

	draw_template();
	fputs("\e[G\e[4A", stdout);
	fflush(stdout);

	cell = 0;
	turn = 0;

	char c;
	while(c = getchar()) {
		if(c == '\e' && getchar() == '[') {
			switch(getchar()) {
				case 'A':
					if(cell >= 3) {
						cell -= 3;
						fputs("\e[2A", stdout);
					} else {
						cell = 0;
						fputs("\e[G", stdout);
					}
					break;
				case 'B':
					if(cell < 6) {
						cell += 3;
						fputs("\e[2B", stdout);
					} else {
						cell = 8;
						fputs("\e[9G", stdout);
					}
					break;
				case 'C':
					if(cell < 8) {
						if(++cell == 3 || cell == 6) {
							fputs("\e[2E", stdout);
						} else {
							fputs("\e[4C", stdout);
						}
					}
					break;
				case 'D':
					if(cell > 0) {
						if(--cell == 2 || cell == 5) {
							fputs("\e[9G\e[2A", stdout);
						} else {
							fputs("\e[4D", stdout);
						}
					}
					break;
			}
		} else if(c == ' ') {
			turn = !turn;
			putchar("OX"[turn]);
			fputs("\b", stdout);
		}
	}
}

void draw_template() {
	char box[43];

	for(int i = 0, j = 2; i < 43; i++, j++) {
		int c = 0;

		if((j & 3) == 0) c |= 1;
		if(j > 9) c |= 2;
		else if(j == 9) c = 4;
		if(j == 19) {
			j = 1;
			c = 4;
		}
		box[i] = " |-+\n"[c];
	}
	fputs(box, stdout);
}
