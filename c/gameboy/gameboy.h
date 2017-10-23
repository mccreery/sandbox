#ifndef GAMEBOY_H_
#define GAMEBOY_H_

#define WIDTH (48)
#define HEIGHT (8)
#define SCALE (16)

#define WINDOW_STYLE (WS_OVERLAPPED | WS_VISIBLE | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX)

HDC hdc;
unsigned char *image_data;
int painting;

typedef struct {
	BITMAPINFO info;
	RGBQUAD extra;
} BITMAP_1BPP;

extern unsigned char DEFAULT[48];
extern BITMAP_1BPP _INFO;
#define info (*((BITMAPINFO *)&_INFO))

LRESULT WINAPI WinProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
void load_image(unsigned char *dest, unsigned char *source);
void save_image(unsigned char *dest, unsigned char *source);
void set_pixel(unsigned char *dest, unsigned x, unsigned y, unsigned value);

#endif
