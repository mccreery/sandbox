#include "window.h"
#include <gl/gl.h>
#include <stdio.h>

int WINAPI WinMain(HINSTANCE cur, HINSTANCE prev, LPSTR cmd, int show) {
	const WNDCLASS wClass = {
		.style = CS_OWNDC,
		.lpfnWndProc = WinProc,
		.hInstance = cur,
		.hCursor = LoadCursor(NULL, IDC_ARROW),
		.lpszClassName = TEXT("GL")
	};
	const LPTSTR wClassId = MAKEINTATOM(RegisterClass(&wClass));
	if(!wClassId) return error("registering window class");

	const int WINDOW_WIDTH = 640;
	const int WINDOW_HEIGHT = 480;

	const HWND hWnd = CreateWindowEx(
		0, wClassId, TEXT("Best Program on Earth"), WS_VISIBLE | WS_OVERLAPPED | WS_HSCROLL | WS_VSCROLL | WS_SYSMENU | WS_MINIMIZEBOX,
		CW_USEDEFAULT, CW_USEDEFAULT, WINDOW_WIDTH, WINDOW_HEIGHT, NULL, NULL, cur, NULL
	);
	if(!hWnd) return error("creating window");

	const SCROLLINFO scroll = {
		.cbSize = sizeof(SCROLLINFO),
		.nMax = 360,
		.fMask = SIF_ALL,
		.nPos = 180
	};
	SetScrollInfo(hWnd, SB_HORZ, &scroll, TRUE);
	SetScrollInfo(hWnd, SB_VERT, &scroll, TRUE);

	const HDC hDC = GetDC(hWnd);
	if(!hDC) return error("getting DC");

	const PIXELFORMATDESCRIPTOR pixelFormat = {
		.nSize = sizeof(pixelFormat),
		.nVersion = 1,
		.dwFlags = PFD_DRAW_TO_WINDOW | PFD_SUPPORT_OPENGL | PFD_DOUBLEBUFFER,
		.iPixelType = PFD_TYPE_RGBA,
		.cColorBits = 32,
		.cDepthBits = 24,
		.cStencilBits = 8,
		.iLayerType = PFD_MAIN_PLANE
	};
	SetPixelFormat(hDC, ChoosePixelFormat(hDC, &pixelFormat), &pixelFormat);

	const HGLRC gl = wglCreateContext(hDC);
	if(!gl) return error("creating OpenGL context");

	wglMakeCurrent(GetDC(hWnd), gl);
	loadGlExtensions();

	MSG message;
	BOOL code;

	glClearColor(0, 0, 0, 0);
	glClearDepth(1);
	glDepthFunc(GL_LEQUAL);
	/*glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);*/
	glViewport(0, 0, WINDOW_WIDTH, -WINDOW_HEIGHT);
	glEnable(GL_DEPTH_TEST);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	double h = tan(FOV * M_PI / 360.) * ZNEAR;
	double w = h * WINDOW_WIDTH / WINDOW_HEIGHT;
	glFrustum(-w, w, -h, h, ZNEAR, ZFAR);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glTranslatef(0, 0, -3.f);

	wglSwapIntervalEXT(1);
	//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

	const float min = -1.f, max = 1.f;
	const float verts[8][3] = {
		{min, max, min},
		{max, max, min},
		{min, max, max},
		{max, max, max},
		{min, min, min},
		{max, min, min},
		{min, min, max},
		{max, min, max}
	};
	const int strip[14] = {1, 0, 3, 2, 6, 0, 4, 1, 5, 3, 7, 6, 5, 4};
	// 14 vertices, 6 repetitions, 8 distinct vertices

	glColor3f(1.f, 0.f, 0.f);
	while(1) {
		while((code = PeekMessage(&message, NULL, NO_FILTER, NO_FILTER, PM_REMOVE)) != 0) {
			if(code == -1) return error("handling message");
			TranslateMessage(&message);
			DispatchMessage(&message);
			if(message.message == WM_QUIT) return message.wParam;
		}
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glPushMatrix();

		SCROLLINFO scroll;
		GetScrollInfo(hWnd, SB_HORZ, &scroll);
		glRotatef(scroll.nPos, 0, 1, 0);
		GetScrollInfo(hWnd, SB_VERT, &scroll);
		glRotatef(scroll.nPos, 1, 0, 0);

		glBegin(GL_TRIANGLE_STRIP);
		int i;
		for(i = 0; i < 14; i++) {
			const float *vertex = verts[strip[i]];
			glVertex3f(vertex[0], vertex[1], vertex[2]);
		}
		glEnd();

		glPopMatrix();

		SwapBuffers(hDC);
	}
	return 0;
}

LRESULT WINAPI WinProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	switch(message) {
		case WM_HSCROLL:
		case WM_VSCROLL:;
			SCROLLINFO scroll = {.fMask = SIF_POS};
			GetScrollInfo(hWnd, message & 1, &scroll);

			switch(LOWORD(wParam)) {
				case SB_THUMBPOSITION:
				case SB_THUMBTRACK:
					scroll.nPos = HIWORD(wParam);
					break;
				case SB_LINEDOWN:
					scroll.nPos += 10;
					break;
				case SB_LINEUP:
					scroll.nPos -= 10;
					break;
			}
			SetScrollInfo(hWnd, message & 1, &scroll, TRUE);
			break;
		case WM_DESTROY:
			PostQuitMessage(0);
			break;
	}
	return DefWindowProc(hWnd, message, wParam, lParam);
}

unsigned long error(const char * const action) {
	DWORD code = GetLastError();

	LPWSTR message = NULL;
	FormatMessage(FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS | FORMAT_MESSAGE_MAX_WIDTH_MASK
		| FORMAT_MESSAGE_ARGUMENT_ARRAY | FORMAT_MESSAGE_ALLOCATE_BUFFER,
		NULL, code, 0, (LPWSTR)&message, 0, NULL);

	fwprintf(stderr, L"Error %S: %s(%ld)\n", action, message, code);
	return code;
}

void loadGlExtensions() {
	wglSwapIntervalEXT = (BOOL (*)(int))wglGetProcAddress("wglSwapIntervalEXT");
}
