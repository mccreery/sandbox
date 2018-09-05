#include <windows.h>
#include <windowsx.h>
#include <tchar.h>
#include <stdio.h>

#include "resources.h"
#include "gameboy.h"

unsigned char DEFAULT[48] = {
	0xCE, 0xED, 0x66, 0x66, 0xCC, 0x0D, 0X00, 0X0B,
	0X03, 0X73, 0X00, 0X83, 0X00, 0X0C, 0X00, 0X0D,
	0x00, 0X08, 0X11, 0X1F, 0X88, 0X89, 0X00, 0X0E,
	0XDC, 0XCC, 0X6E, 0XE6, 0XDD, 0XDD, 0XD9, 0X99,
	0XBB, 0XBB, 0X67, 0X63, 0X6E, 0X0E, 0XEC, 0XCC,
	0XDD, 0XDC, 0X99, 0X9F, 0XBB, 0XB9, 0X33, 0X3E
};

BITMAP_1BPP _INFO = {
	{
		{
			.biSize = sizeof(BITMAPINFOHEADER),
			.biWidth = 48, .biHeight = -8,
			.biPlanes = 1, .biBitCount = 1,
			.biCompression = BI_RGB
		},
		{{.rgbRed = 0x7F, .rgbGreen = 0x86, .rgbBlue = 0x0F}}
	}, {.rgbRed = 0x2A, .rgbGreen = 0x45, .rgbBlue = 0x3B}
};

wchar_t current_file[256];

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
	painting = -1;

	WNDCLASS class = {
		.lpfnWndProc = WinProc,
		.hInstance = hInstance,
		.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON)),
		.hCursor = LoadCursor(NULL, IDC_CROSS),
		.lpszMenuName = MAKEINTRESOURCE(IDM_MENU),
		.lpszClassName = TEXT("Gameboy")
	};
	LPCWSTR classId = MAKEINTATOM(RegisterClass(&class));

	RECT size = {0, 0, WIDTH * SCALE, HEIGHT * SCALE};
	AdjustWindowRect(&size, WINDOW_STYLE, TRUE);

	HWND hwnd = CreateWindow(classId, TEXT("Gameboy Logo Painter"), WINDOW_STYLE,
		CW_USEDEFAULT, CW_USEDEFAULT, size.right - size.left, size.bottom - size.top,
		NULL, NULL, hInstance, NULL);

	hdc = GetDC(hwnd);
	PIXELFORMATDESCRIPTOR format = {
		.nSize = sizeof(PIXELFORMATDESCRIPTOR),
		.nVersion = 1,
		.iPixelType = PFD_TYPE_COLORINDEX,
		.cColorBits = 8
	};
	int formatId = ChoosePixelFormat(hdc, &format);

	DescribePixelFormat(hdc, formatId, format.nSize, &format);
	if(format.iPixelType != PFD_TYPE_COLORINDEX || format.cColorBits != 8) {
		fputs("Unable to find valid pixel format.", stderr);
		return 1;
	}

	CreateDIBSection(hdc, &info, DIB_RGB_COLORS, (void **)&image_data, NULL, 0);
	load_image(image_data, DEFAULT);

	StretchDIBits(hdc, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, image_data, &info, DIB_RGB_COLORS, SRCCOPY);

	MSG msg;
	while(GetMessage(&msg, NULL, 0, 0)) {
		TranslateMessage(&msg);
		DispatchMessage(&msg);
	}
	return (int)msg.wParam;
}

LRESULT WINAPI WinProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam) {
	if(message == WM_DESTROY) {
		PostQuitMessage(0);
	} else if(message == WM_COMMAND) {
		if(wParam == 100) {
			OPENFILENAME open = {
				.lStructSize = sizeof(OPENFILENAME),
				.hwndOwner = hWnd,
				.lpstrFilter = TEXT("Gameboy ROM Images (*.gb)\0*.gb\0"),
				.Flags = OFN_FILEMUSTEXIST,
				.lpstrFile = current_file,
				.nMaxFile = 256
			};

			if(GetOpenFileName(&open)) {
				unsigned char new_image[48];

				// Grab the relevant part of the file
				FILE *file = (FILE *)_tfopen(open.lpstrFile, TEXT("rb"));
				fseek(file, 0x104, SEEK_SET);
				fread(new_image, 1, 48, file);
				fclose(file);

				load_image(image_data, new_image);
				StretchDIBits(hdc, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, image_data, &info, DIB_RGB_COLORS, SRCCOPY);
			}
		} else if(wParam == 101) {
			unsigned char new_image[48];
			save_image(new_image, image_data);

			FILE *file = (FILE *)_tfopen(current_file, TEXT("r+b"));
			fseek(file, 0x104, SEEK_SET);
			fwrite(new_image, 1, 48, file);
			fclose(file);
		}
	} else if(message == WM_LBUTTONDOWN || message == WM_RBUTTONDOWN) {
		painting = !((message >> 2) & 0x01);
	} else if(painting != -1 && message == (painting == 1 ? WM_LBUTTONUP : WM_RBUTTONUP)) {
		painting = -1;
	}

	if(painting != -1 && (message == WM_LBUTTONDOWN || message == WM_RBUTTONDOWN || message == WM_MOUSEMOVE)) {
		set_pixel(image_data, GET_X_LPARAM(lParam) / SCALE, GET_Y_LPARAM(lParam) / SCALE, painting);
		StretchDIBits(hdc, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, image_data, &info, DIB_RGB_COLORS, SRCCOPY);
	}
	return DefWindowProc(hWnd, message, wParam, lParam);
}

void set_pixel(unsigned char *dest, unsigned x, unsigned y, unsigned value) {
	unsigned i = y * 8 + (x >> 3);
	unsigned j = x & 0x07;

	dest[i] &= ~(0x80 >> j);     // Clear bit
	dest[i] |= value << (7 - j); // Set bit to value
}

void load_image(unsigned char *dest, unsigned char *source) {
	unsigned i = 0, j = 0;

	// Rows of pixels are padded to DWORD (4 bytes), hence 8 instead of 6
	for(unsigned block = 0; block < 12; block++, i += 4, j++) {
		if(j == 6) j = 32;
		dest[j     ] = (source[i] & 0xF0) | (source[i + 2] >> 4);
		dest[j +  8] = (source[i] << 4) | (source[i + 2] & 0x0F);
		dest[j + 16] = (source[i + 1] & 0xF0) | (source[i + 3] >> 4);
		dest[j + 24] = (source[i + 1] << 4) | (source[i + 3] & 0x0F);
	}
}

void save_image(unsigned char *dest, unsigned char *source) {
	unsigned i = 0, j = 0;

	// Rows of pixels are padded to DWORD (4 bytes), hence 8 instead of 6
	for(unsigned block = 0; block < 12; block++, i += 4, j++) {
		if(j == 6) j = 32;
		dest[i    ] = (source[j] & 0xF0) | (source[j + 8] >> 4);
		dest[i + 1] = (source[j + 16] & 0xF0) | (source[j + 24] >> 4);
		dest[i + 2] = (source[j] << 4) | (source[j + 8] & 0x0F);
		dest[i + 3] = (source[j + 16] << 4) | (source[j + 24] & 0x0F);
	}
}
