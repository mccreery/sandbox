#include "gpio.h"
#include <stdint.h>

#ifndef HD44780_H
	#define HD44780_H

	#define GROUND       0x00
	#define POWER        0x01        // 5V
	#define CONTRAST     0x02
	#define RS           0x03
	#define RW           0x04
	#define ENABLE       0x05        // Pulse for 1us to send
	#define DATA0        0x06        // LSB
	#define BUSY         (DATA0 + 7) // Flag (don't send when active)
	#define LIGHT        0x0E
	#define LIGHT_GROUND 0x0F

	#define RS_COMMAND   0x00
	#define RS_DATA      0x01
	#define RW_WRITE     0x00
	#define RW_READ      0x01

	#define CLEAR        0x01
	#define RESET        0x02

	#define ENTRY_MODE   0x04        // Controls behaviour on data entry
	#define CUR_RIGHT    0x02
	#define SHIFT        0x01

	#define DISPLAY      0x08        // Toggles display features
	#define DISPLAY_T    0x04
	#define CURSOR_T     0x02
	#define BLINK_T      0x01

	#define M_SHIFT      0x10        // Manually shift cursor or display
	#define SHIFT_TYPE   0x08        // 0: Cursor,  1: Screen
	#define SHIFT_RIGHT  0x04

	#define SETUP        0x20        // Configures display
	#define BYTE_INPUT   0x10
	#define TWO_LINE     0x08
	#define TALL_CHARS   0x04

	#define CGRAM        0x40        // Sets CGRAM address
	#define CGRAM_MASK   0x3F

	#define DDRAM        0x80        // Sets DDRAM address
	#define DDRAM_MASK   0x7F
#endif

void block();
void write_data(uint8_t val);
void pulse();

void send_data(uint8_t c, int rw, int rs);
void register_char(uint8_t c, uint64_t data);
void setup(uint8_t setup, uint8_t entry_mode, uint8_t display);
