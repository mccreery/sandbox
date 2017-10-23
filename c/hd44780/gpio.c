#include "gpio.h"
#include <stdio.h>
#include <errno.h>

int exportp(int pin) {
	FILE *handle = fopen("/sys/class/gpio/export", "w");
	if(handle == NULL) return errno;

	if(fprintf(handle, "%d", pin) < 0) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : 0;
}

int unexportp(int pin) {
	FILE *handle = fopen("/sys/class/gpio/unexport", "w");
	if(handle == NULL) return errno;

	if(fprintf(handle, "%d", pin) < 0) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : 0;
}

int directp(int pin, int direction) {
	static char fname[33];
	sprintf(fname, "/sys/class/gpio/gpio%d/direction", pin);

	FILE *handle = fopen(fname, "w");
	if(handle == NULL) return errno;

	if(fputs(direction ? "out" : "in", handle) == EOF) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : 0;
}

int writep(int pin, int value) {
	static char fname[29];
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

int readp(int pin) {
	static char fname[29];
	sprintf(fname, "/sys/class/gpio/gpio%d/value", pin);

	FILE *handle = fopen(fname, "r");
	if(handle == NULL) return errno;

	int c;
	if((c = fgetc(handle)) == EOF) {
		int err = ferror(handle);
		fclose(handle);
		return err;
	}

	return fclose(handle) ? errno : c - '0';
}
