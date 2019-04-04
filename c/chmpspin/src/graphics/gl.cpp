#include <gl.hpp>
#include <gl33.h>
#include <GL/gl.h>
#include <util.hpp>
#include <wgl.h>
#include <windef.h>

void gl::loadExtensions(HDC hdc) {
	ogl_LoadFunctions();
	wgl_LoadFunctions(hdc);
}
