/*#include "gpio.h"
#include <stdint.h>
#include <time.h>
#include <stdio.h>*/

/*#define C1 ((1.0 / 32.703) * CLOCKS_PER_SEC) / 2
#define D1 ((1.0 / 36.708) * CLOCKS_PER_SEC) / 2
#define E1 ((1.0 / 41.203) * CLOCKS_PER_SEC) / 2
#define F1 ((1.0 / 43.654) * CLOCKS_PER_SEC) / 2
#define G1 ((1.0 / 48.999) * CLOCKS_PER_SEC) / 2
#define A1 ((1.0 / 55.000) * CLOCKS_PER_SEC) / 2
#define B1 ((1.0 / 61.735) * CLOCKS_PER_SEC) / 2

#define G3 ((1.0 / 195.998) * CLOCKS_PER_SEC) / 2

#define C4 ((1.0 / 261.626) * CLOCKS_PER_SEC) / 2
#define Db4 ((1.0 / 277.183) * CLOCKS_PER_SEC) / 2
#define D4 ((1.0 / 293.665) * CLOCKS_PER_SEC) / 2
#define Eb4 ((1.0 / 311.127) * CLOCKS_PER_SEC) / 2
#define E4 ((1.0 / 329.628) * CLOCKS_PER_SEC) / 2
#define F4 ((1.0 / 349.228) * CLOCKS_PER_SEC) / 2
#define Gb4 ((1.0 / 369.994) * CLOCKS_PER_SEC) / 2
#define G4 ((1.0 / 391.995) * CLOCKS_PER_SEC) / 2
#define Ab4 ((1.0 / 415.305) * CLOCKS_PER_SEC) / 2
#define A4 ((1.0 / 440.000) * CLOCKS_PER_SEC) / 2
#define Bb4 ((1.0 / 466.164) * CLOCKS_PER_SEC) / 2
#define B4 ((1.0 / 493.883) * CLOCKS_PER_SEC) / 2

#define C5 ((1.0 / 523.251) * CLOCKS_PER_SEC) / 2
#define Db5 ((1.0 / 554.365) * CLOCKS_PER_SEC) / 2*/

#include <stdint.h>
#include "gpio.h"
#include "audio.h"

int main() {
	const int frere[] = {
		C4,C4, D4,D4, E4,E4, C4,C4, C4,C4, D4,D4, E4,E4, C4,C4,
		E4,E4, F4,F4, G4,G4, G4,G4, E4,E4, F4,F4, G4,G4, G4,G4,
		G4, A4, G4, F4, E4,E4, C4,C4,
		G4, A4, G4, F4, E4,E4, C4,C4,
		C4,C4, G3,G3, C4,C4, C4,C4, C4,C4, G3,G3, C4,C4, C4,C4, 0
	};
	const int pacman[] = {
		C4,-1, C5,-1, G4,-1, E4,-1, C5, G4, -1,-1, E4,E4,E4,E4,
		Db4,-1, Db5,-1, Ab4,-1, F4,-1, Db5, Ab4, -1,-1, F4,F4,F4,F4,
		C4,-1, C5,-1, G4,-1, E4,-1, C5, G4, -1,-1, E4,E4,E4,E4,
		Eb4, E4, F4, -1,
		F4, Gb4, G4, -1,
		G4, Ab4, A4, -1,
		C5, -1,-1,-1
	};
	const int coin[] = {
		B5, E6, E6, E6
	};
	const int h3h3[] = {
		B - Bb7 - Ebm7 - Dbm7 F#7
	};

	exportp(22);
	directp(22, DIR_OUT);

	set_bpm(800);
	push_audio(pacman, 64);
	while(1) tick_audio();
}
