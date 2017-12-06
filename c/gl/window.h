#ifndef WINDOW_H_
#define WINDOW_H_

#define _WIN32_WINNT 0x0500
#define UNICODE
#include <windows.h>

#define M_PI  (3.14159265358979323846)
#define FOV   (70)
#define ZNEAR (0.1)
#define ZFAR  (64.0)

#define NO_FILTER (0)

BOOL (*wglSwapIntervalEXT)(int);
void loadGlExtensions();

LRESULT WINAPI WinProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
unsigned long error(const char * const action);
#endif
