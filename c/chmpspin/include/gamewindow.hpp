#ifndef GAMEWINDOW_H_
#define GAMEWINDOW_H_

#define UNICODE
#include <windows.h>
#include <util.hpp>

#define WS_WINDOWED   (WS_VISIBLE | WS_OVERLAPPED | WS_SYSMENU | WS_MINIMIZEBOX)
#define WS_BORDERLESS (WS_VISIBLE | WS_POPUP)

class GameWindow {
	private:
		Vec2<int> resolution;
	public:
		GameWindow(HINSTANCE hInstance, LPCWSTR title);
		void setStyle(DWORD style, BOOL update);
		void setResolution(int width, int height);
		Vec2<int> getResolution();

		int handleMessages();
		void swapBuffers();

		const HWND hWnd;
		const HDC hDC;
	private:
		static void registerClass(HINSTANCE hInstance);
		static LPCWSTR windowClass;

		HWND prepareHWnd(HINSTANCE hInstance, LPCWSTR title);
		void setPixelFormat();

		DWORD style;
};
LRESULT WINAPI WinProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);

#endif
