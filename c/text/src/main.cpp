#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <SDL2/SDL.h>
#include <SDL2/SDL_surface.h>
#include "graphics.h"
#include "dialogue.h"

void onExit();
SDL_Color getPixel(SDL_Surface *surface, int x, int y);
SDL_Window *createWindow(int width, int height, int bpp, int refresh, int fullscreen, const char *title);

SDL_Window *window;

extern "C" int main(int argc, char **argv) {
	FILE *f = fopen("res/dialogue/tutorial.txt", "r");
	loadDialogue(f);
	fclose(f);

	if(SDL_Init(SDL_INIT_VIDEO/* | SDL_INIT_AUDIO*/)) {
		fputs("Unable to initialize SDL.", stderr);
		exit(1);
	}
	atexit(onExit);
	window = createWindow(480, 360, 32, 60, 0, "Test Window");
	graphics::windowSurface = SDL_GetWindowSurface(window);
	SDL_FillRect(graphics::windowSurface, NULL, 0xFFFFFFFF);

	graphics::fontSurface = SDL_LoadBMP("res/img/gui.bmp");
	if(graphics::fontSurface == NULL) fprintf(stderr, "Unable to load font surface: %s\n", SDL_GetError());
	SDL_SetColorKey(graphics::fontSurface, SDL_TRUE, SDL_MapRGB(graphics::fontSurface->format, 0xFF, 0x00, 0xFF));

	drawString(5, 5, getDialogue("tutorial").entry);
	//drawString(5, 5, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam sodales in\norci id aliquet. Fusce massa felis, rhoncus ac arcu ac, consectetur congue\ntortor. In tempor a ante at faucibus. Suspendisse laoreet congue tellus quis\nvenenatis. Praesent efficitur porttitor mauris, sed semper tortor rhoncus ut.\nProin dignissim turpis porta magna commodo ultrices. Morbi id suscipit nulla.\nAliquam ut varius mi, in bibendum risus. Aenean vel ipsum sem. Proin nec\naliquet metus. ");
	drawButton(10, 100, 96, 24, "Start");
	drawButton(10, 128, 96, 24, "Begin");
	drawButton(10, 156, 96, 40, "Go");

	SDL_Rect src = {32, 16, 5, 5};
	SDL_Rect dest = {5, 95, 106, 106};
	SDL_Point center = {34, 18};
	drawScalableRect(&src, &dest, &center);

	SDL_UpdateWindowSurface(window);

	SDL_Event event;
	while(1) {
		SDL_WaitEvent(&event);
		switch(event.type) {
			case SDL_QUIT:
				exit(0);
			default: break;
		}
	}
	return 0;
}

const SDL_Color WHITE = {0xFF, 0xFF, 0xFF, 0xFF};

SDL_Color getPixel(SDL_Surface *surface, int x, int y) {
	Uint32 pixel;
	switch(surface->format->BytesPerPixel) {
		case 1:
			pixel = ((Uint8 *)surface->pixels)[y * surface->pitch + x];
			return surface->format->palette->colors[pixel];
		default:
			return WHITE;
	}
}

SDL_Window *createWindow(int width, int height, int bpp, int refresh, int fullscreen, const char *title) {
	return SDL_CreateWindow(
		title, SDL_WINDOWPOS_UNDEFINED, SDL_WINDOWPOS_UNDEFINED,
		width, height, fullscreen ? SDL_WINDOW_FULLSCREEN : 0);
}

void onExit() {
	SDL_FreeSurface(windowSurface);
	SDL_FreeSurface(fontSurface);
	SDL_DestroyWindow(window);
	SDL_Quit();
}

/*void audioCallback(void *userdata, Uint8 *stream, int len);
static Uint8 *currentAudio;
static Uint32 currentLen;

static Uint32 len;
static Uint8 *buf;
static SDL_AudioSpec spec;

if(!SDL_LoadWAV("res/il.wav", &spec, &buf, &len)) {
	fprintf(stderr, "Unable to load sound effect: %s\n", SDL_GetError());
	exit(1);
}
spec.callback = audioCallback;
spec.userdata = NULL;
currentAudio = buf;
currentLen = len;

if(SDL_OpenAudio(&spec, NULL)) {
	fputs("Unable to open audio.", stderr);
	exit(1);
}
SDL_PauseAudio(0);
while(currentLen > 0) SDL_Delay(100);
SDL_CloseAudio();
SDL_FreeWAV(buf);*/

/*void audioCallback(void *userdata, Uint8 *stream, int len) {
	if(currentLen == 0) return;

	if(len > currentLen) len = currentLen;
	SDL_MixAudio(stream, currentAudio, len, SDL_MIX_MAXVOLUME);

	currentAudio += len;
	currentLen -= len;
}*/
