#include <time.h>

#ifndef AUDIO_H
	#define AUDIO_H

	// Function: 1000000/(440(2^((x-33) / 12)))
	#define REST -1

	#define C0 15289
	#define Db0 14430
	#define D0 13620
	#define Eb0 12856
	#define E0 12134
	#define F0 11453
	#define Gb0 10810
	#define G0 10204
	#define Ab0 9631
	#define A0 9090
	#define Bb0 8580
	#define B0 8099
	#define C1 7644
	#define Db1 7215
	#define D1 6810
	#define Eb1 6428
	#define E1 6067
	#define F1 5726
	#define Gb1 5405
	#define G1 5102
	#define Ab1 4815
	#define A1 4545
	#define Bb1 4290
	#define B1 4049
	#define C2 3822
	#define Db2 3607
	#define D2 3405
	#define Eb2 3214
	#define E2 3033
	#define F2 2863
	#define Gb2 2702
	#define G2 2551
	#define Ab2 2407
	#define A2 2272
	#define Bb2 2145
	#define B2 2024
	#define C3 1911
	#define Db3 1803
	#define D3 1702
	#define Eb3 1607
	#define E3 1516
	#define F3 1431
	#define Gb3 1351
	#define G3 1275
	#define Ab3 1203
	#define A3 1136
	#define Bb3 1072
	#define B3 1012
	#define C4 955
	#define Db4 901
	#define D4 851
	#define Eb4 803
	#define E4 758
	#define F4 715
	#define Gb4 675
	#define G4 637
	#define Ab4 601
	#define A4 568
	#define Bb4 536
	#define B4 506
	#define C5 477
	#define Db5 450
	#define D5 425
	#define Eb5 401
	#define E5 379
	#define F5 357
	#define Gb5 337
	#define G5 318
	#define Ab5 300
	#define A5 284
	#define Bb5 268
	#define B5 253
	#define C6 238
	#define Db6 225
	#define D6 212
	#define Eb6 200
	#define E6 189
	#define F6 178
	#define Gb6 168
	#define G6 159
	#define Ab6 150
	#define A6 142
	#define Bb6 134
	#define B6 126
	#define C7 119
	#define Db7 112
	#define D7 106
	#define Eb7 100
	#define E7 94
	#define F7 89
	#define Gb7 84
	#define G7 79
	#define Ab7 75
	#define A7 71
	#define Bb7 67
	#define B7 63
	#define C8 59
	#define Db8 56
	#define D8 53
	#define Eb8 50
	#define E8 47
	#define F8 44
	#define Gb8 42
	#define G8 39
	#define Ab8 37
	#define A8 35
	#define Bb8 33
	#define B8 31
#endif

#include <stdint.h>

void set_bpm(const uint32_t bpm);
void flush_audio();
void push_audio(const uint32_t *notes, uint8_t length);
void play_audio(const uint32_t *notes, uint8_t length);
void tick_audio();
