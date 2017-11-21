#include <SDL2/SDL.h>

#ifndef GRAPHICS_H_
	#define GRAPHICS_H_

	void drawScalableRect(SDL_Rect *src, SDL_Rect *dest, SDL_Point *center);
	void drawButton(int x, int y, int width, int height, char *str);
	void drawString(int x0, int y, char *str);

	#define FONT_WIDTH  5
	#define TAB_WIDTH   (4 * (FONT_WIDTH + 1))
	#define FONT_HEIGHT 11
#endif

namespace graphics {
	static extern SDL_Surface *windowSurface, *fontSurface;
}
