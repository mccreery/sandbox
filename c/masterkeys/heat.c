#include "setup.h"
#include <signal.h>

int running = 1;
void halter(int signum) {running = 0;}

void function(int i);
KEY_COLOR map(float index);
void CALLBACK callback(int iRow, int iColumn, bool bPressed);

int main(int argc, char **argv) {
	srand(time(NULL));
	setup(DEV_MKeys_S, 0);
	signal(SIGINT, halter);

	SetKeyCallBack(callback);
	EnableKeyInterrupt(1);

	KEY_COLOR color = map(0.0F);
	SetFullLedColor(color.r, color.g, color.b);
	RefreshLed(0);
	while(running); // Keep the program alive
}

void CALLBACK callback(int iRow, int iColumn, bool bPressed) {
	static int max = 0;
	static struct {
		int n;
		float index;
	} freq[MAX_LED_ROW][MAX_LED_COLUMN] = {0};

	if(!bPressed) return;

	if(++freq[iRow][iColumn].n > max) {
		max = freq[iRow][iColumn].n;
	}

	for(int row = 0; row < MAX_LED_ROW; row++) {
		for(int col = 0; col < MAX_LED_COLUMN; col++) {
			float index = (float)freq[row][col].n / max;

			//if(iRow == row && iColumn == col || index < freq[row][col].index - 0.1F) {
				freq[row][col].index = index;
				SetLedColorC(row, col, map(index));
			//}
		}
	}
	RefreshLed(0);
}

KEY_COLOR map(float index) {
	index *= 4;

	float r, g, b;
	if(index <= 1) {
		r = 0; g = index; b = 1;
	} else if(index <= 2) {
		r = 0; g = 1; b = 1 - (index - 1);
	} else if(index <= 3) {
		r = index - 2; g = 1; b = 0;
	} else {
		r = 1; g = 1 - (index - 3); b = 0;
	}

	KEY_COLOR color = {r*255, g*255, b*255};
	return color;
}
