#include <gamewindow.hpp>
#include <winbase.h>
#include <windef.h>
#include <wingdi.h>
#include <winnt.h>
#include <winuser.h>
#include <cstdio>
#include <iostream>

#define UNICODE
#include <windows.h>

LPCWSTR GameWindow::windowClass = NULL;

#include <stdlib.h>

void GameWindow::registerClass(HINSTANCE hInstance) {
	WNDCLASSEX wClass = {0};
	wClass.cbSize = sizeof(WNDCLASSEX);
	wClass.style = CS_OWNDC;
	wClass.lpfnWndProc = WinProc;
	wClass.hInstance = hInstance;
	wClass.hCursor = LoadCursor(NULL, IDC_ARROW);
	wClass.lpszClassName = TEXT("GL");
	wClass.cbClsExtra = wClass.cbWndExtra = 0;

	windowClass = MAKEINTATOM(RegisterClassEx(&wClass));
	if(!windowClass) {
		fprintf(stderr, "Error: %ld\n", GetLastError());
		exit(0);
	}
}

GameWindow::GameWindow(HINSTANCE hInstance, LPCWSTR title)
		: resolution(Vec2<int>(0, 0)), hWnd(prepareHWnd(hInstance, title)), hDC(GetDC(hWnd)) {
	setPixelFormat();
	wglMakeCurrent(hDC, wglCreateContext(hDC));
}

HWND GameWindow::prepareHWnd(HINSTANCE hInstance, LPCWSTR title) {
	if(windowClass == 0) registerClass(hInstance);
	setStyle(WS_WINDOWED, FALSE);
	setResolution(640, 360);

	return CreateWindowEx(
		0, windowClass, title, style, CW_USEDEFAULT, CW_USEDEFAULT,
		resolution.x, resolution.y, NULL, NULL, hInstance, NULL);
}

void GameWindow::setPixelFormat() {
	PIXELFORMATDESCRIPTOR pixelFormat = {0};
	pixelFormat.nSize = sizeof(pixelFormat);
	pixelFormat.nVersion = 1;
	pixelFormat.dwFlags = PFD_DRAW_TO_WINDOW | PFD_SUPPORT_OPENGL | PFD_DOUBLEBUFFER;
	pixelFormat.iPixelType = PFD_TYPE_RGBA;
	pixelFormat.cColorBits = 32;
	pixelFormat.cDepthBits = 24;
	pixelFormat.cStencilBits = 8;
	pixelFormat.iLayerType = PFD_MAIN_PLANE;

	SetPixelFormat(hDC, ChoosePixelFormat(hDC, &pixelFormat), &pixelFormat);
}

void GameWindow::setStyle(DWORD style, BOOL update = TRUE) {
	this->style = style;

	if((this->style & WS_POPUP)) {
		this->resolution.x = GetSystemMetrics(SM_CXSCREEN);
		this->resolution.y = GetSystemMetrics(SM_CYSCREEN);
	}
	// TODO do something more here
}

void GameWindow::setResolution(int width, int height) {
	if(!(style & WS_POPUP)) {
		resolution.x = width;
		resolution.y = height;
		// TODO do something more here
	}
}
Vec2<int> GameWindow::getResolution() {
	return resolution;
}

int GameWindow::handleMessages() {
	static BOOL code;
	static MSG message;

	while((code = PeekMessage(&message, NULL, 0, 0, PM_REMOVE)) != 0) {
		if(code == -1) return 1;
		TranslateMessage(&message);
		DispatchMessage(&message);
		if(message.message == WM_QUIT) return 1;
	}
	return 0;
}

void GameWindow::swapBuffers() {
	SwapBuffers(hDC);
}

LRESULT WINAPI WinProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	switch(message) {
		case WM_DESTROY:
			PostQuitMessage(0);
			break;
	}
	return DefWindowProc(hWnd, message, wParam, lParam);
}
