#include <SDL2/SDL.h>
#include <SDL2/SDL_Surface.h>
#include <string.h>
#include "graphics.h"

void drawButton(int x, int y, int width, int height, char *str) {
	SDL_Rect src =  {0, 0, 13, 22};
	SDL_Rect dest = {x, y, width, height};
	SDL_Point center = {6, 12};
	drawScalableRect(&src, &dest, &center);

	// TODO improve string width thingy
	int strWidth = strlen(str) * (FONT_WIDTH + 1);
	drawString(x + (width - strWidth) / 2, y + (height - FONT_HEIGHT) / 2, str);
}

namespace graphics {
	static SDL_Surface *windowSurface, *fontSurface;
}

void drawScalableRect(SDL_Rect *src, SDL_Rect *dest, SDL_Point *center) {
	SDL_Rect subSrc, subDest;

	SDL_Surface *fontSurface = graphics::fontSurface;

	subSrc.x = src->x; subSrc.y = src->y;
	subDest.x = dest->x; subDest.y = dest->y;
	subSrc.w = subDest.w = center->x - subSrc.x;
	subSrc.h = subDest.h = center->y - subSrc.y;
	SDL_BlitSurface(graphics::fontSurface, &subSrc, graphics::windowSurface, &subDest);

	subSrc.x += src->w - subSrc.w;
	subDest.x += dest->w - subDest.w;
	SDL_BlitSurface(graphics::fontSurface, &subSrc, graphics::windowSurface, &subDest);

	subSrc.y = center->y + 1;
	subSrc.h = subDest.h = src->y + src->h - subSrc.y;
	subDest.y += dest->h - subDest.h;
	SDL_BlitSurface(graphics::fontSurface, &subSrc, graphics::windowSurface, &subDest);

	subSrc.x = src->x;
	subDest.x = dest->x;
	SDL_BlitSurface(graphics::fontSurface, &subSrc, graphics::windowSurface, &subDest);
}

void drawString(int x0, int y, char *str) {
	int x = x0;

	for(; *str; str++) {
		SDL_Rect src, dest;
		switch(*str) {
			case '\n':
				y += FONT_HEIGHT + 1;
				// No break
			case '\r':
				x = x0;
				break;
			case '\b':
				x -= FONT_WIDTH + 1;
				break;
			case '\t':
				x -= x0;
				x = (x / TAB_WIDTH + 1) * TAB_WIDTH;
				x += x0;
				break;
			default:
				if(*str < 32 || *str >= 128) continue;

				src.x = (*str & 15) * FONT_WIDTH;
				src.y = (*str >> 4) * FONT_HEIGHT;
				dest.x = x;
				dest.y = y;
				src.w = dest.w = FONT_WIDTH;
				src.h = dest.h = FONT_HEIGHT;
				SDL_BlitSurface(graphics::fontSurface, &src, graphics::windowSurface, &dest);
				// No break
			case ' ':
				x += FONT_WIDTH + 1;
				break;
		}
	}
}
