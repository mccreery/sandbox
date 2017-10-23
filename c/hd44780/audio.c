#include "gpio.h"
#include "audio.h"
#include <time.h>
#include <stdint.h>
#include <stdio.h>

static uint32_t sfx_buf[256];
static uint8_t current = 0;
static uint8_t buf_end = 0;

static uint32_t beat_delay;

void set_bpm(const uint32_t bpm) {
	beat_delay = (60 * CLOCKS_PER_SEC) / bpm;
}

void flush_audio() {
	current = buf_end;
}
void push_audio(const uint32_t *notes, uint8_t length) {
	while(length--) sfx_buf[buf_end++] = *(notes++);
}
void play_audio(const uint32_t *notes, uint8_t length) {
	uint8_t temp_current = current + 1;
	while(length--) sfx_buf[temp_current++] = *(notes++);
}

void tick_audio() {
	uint8_t i;
	static clock_t last_tick, last_ntick, next;
	if(!last_tick) {
		last_ntick = last_tick = clock();
	}

	if(current != buf_end) {
		// Wave generation
		if(sfx_buf[current] != REST) {
			uint32_t delay = sfx_buf[current] * CLOCKS_PER_SEC / 1000000;

			for(next = clock();
				next >= last_tick + delay;
				last_tick += delay, i++) {

				writep(22, i & 1);
			}
		}

		// Note buffer incrementation
		for(next = clock();
			next >= last_ntick + beat_delay;
			last_ntick += beat_delay, current++);
	}
}
