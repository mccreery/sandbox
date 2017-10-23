#ifndef GPIO_H
	#define GPIO_H

	#define DIR_IN 0
	#define DIR_OUT (!DIR_IN)
	#define LOW 0
	#define HIGH (!LOW)
#endif

int exportp(int pin);
int unexportp(int pin);
int directp(int pin, int direction);
int writep(int pin, int value);
int readp(int pin);
