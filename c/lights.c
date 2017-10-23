#include "lights.h"

int quit(int error) {
	printf("Quit! Error: %d\n", error);
	exit_handle(0);
	return error;
}

int export(int pin) {
	FILE *handle = fopen("/sys/class/gpio/export", "w");
	if(handle == NULL) return errno;

	if(fprintf(handle, "%d", pin) < 0) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : 0;
}

int direct(int pin, int direction) {
	char fname[33];
	sprintf(fname, "/sys/class/gpio/gpio%d/direction", pin);
	printf("%s\n", fname);

	FILE *handle = fopen(fname, "w");
	if(handle == NULL) return errno;

	if(fputs(direction ? "out" : "in", handle) == EOF) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	printf("%d\n", pin);
	return fclose(handle) ? errno : 0;
}

int value(int pin, int value) {
	char fname[29];
	sprintf(fname, "/sys/class/gpio/gpio%d/value", pin);

	FILE *handle = fopen(fname, "w");
	if(handle == NULL) return errno;

	if(fputc('0' + value, handle) == EOF) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : 0;
}

int unexport(int pin) {
	FILE *handle = fopen("/sys/class/gpio/unexport", "w");
	if(handle == NULL) return errno;

	if(fprintf(handle, "%d", pin) < 0) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : 0;
}

static int run = 1;
static int (*modes[])(char, unsigned char) = {
	&alternate, &loop, &bloop, &flash,
	&bounce, &dist, &fan, &change
};

// 8 lights [0, 8), 256 frames [0, 256)

int alternate(char i, unsigned char t) { // 2 frames
	return (i&1) ^ (t&1);
}
int loop(char i, unsigned char t) { // 8 frames
	return (t%11) == i;
}
int bloop(char i, unsigned char t) { // 8 frames
	return (~(t&7) + 8) == i;
}
int flash(char i, unsigned char t) { // 2 frames
	return t&1;
}
int bounce(char i, unsigned char t) { // 14 frames
	return i == 7 - abs(7 - (t % 15));
}
int dist(char i, unsigned char t) { // 6 frames
	return ((~i + 4) & 0x7F) < t % 7;
}
int fan(char i, unsigned char t) {
	return ((~i + 3) & 0x7F) == 2 - abs(2 - (t % 4));
}
int change(char i, unsigned char t) { // 256 frames
	return modes[((t >> 4) % 7)](i, t);
}

int filter(unsigned char t, int (*func)(char, unsigned char)) {
	char i;
	for(i = 0; i < 11; i++) {
		value(i + 3, func(i, t));
	}
	return 0;
}

void exit_handle(int a) {
	int i;
	for(i = 3; i < 14; i++) {
		unexport(i);
	}
	run = 0;
}

int main(int argc, char **argv) {
	signal(SIGINT, exit_handle);
	int i, c, error;

	for(i = 3; i < 14; i++) {
		if((error = export(i)) || (error = direct(i, OUT))) {
			return quit(error);
		}
	}

	const clock_t interval = CLOCKS_PER_SEC / 5;

	int (*mode)(char, unsigned char);
	mode = modes[**(argv + 1) - '0'];

	unsigned char t = 0;
	clock_t last = clock(), next;
	while(run) {
		for(next = clock(); next > last + interval; last += interval, t++) {
			c = filter(t, mode);
			if(c) return quit(c);
		}
	}
}
