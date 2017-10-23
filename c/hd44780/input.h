#include <stdint.h>

#ifndef BUTTONINPUT_H
	#define BUTTONINPUT_H
	#define IN_DOWN_MASK 0x01

	#define IN_RELEASE   0x02
	#define IN_PRESS     (0x04|IN_DOWN_MASK)
	#define IN_REPEAT    (IN_PRESS|0x02)

	struct in_state {
		int pin;
		uint8_t state, repeat, duration, cooldown;
	};
#endif

void tick_input(struct in_state *button);
