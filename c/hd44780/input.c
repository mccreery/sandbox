#include "gpio.h"
#include "input.h"

void tick_input(struct in_state *button) {
	button->state &= IN_DOWN_MASK; // Decay special state

	if(readp(button->pin)) {
		button->cooldown = 5;
		if(button->state) {
			if(!++button->duration && button->repeat) {
				button->state = IN_REPEAT; // Special state 5
			}
		} else {
			button->state = IN_PRESS; // Special state 3
			button->duration = 0;
		}
	} else if(button->state && !--button->cooldown) {
		button->state = IN_RELEASE; // Special state 2
	}
}
