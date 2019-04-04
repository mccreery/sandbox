#include <gl33.h>
#include <math.h>
#include <util.hpp>
#include <wgl.h>
#include <iostream>

template<typename T> Mat3<T> Mat3<T>::IDENTITY(1, 0, 0, 0, 1, 0, 0, 0, 1);
template<typename T> Mat4<T> Mat4<T>::IDENTITY(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

Mat4<float> model = Mat4<float>::IDENTITY,
	projection = Mat4<float>::IDENTITY;

Mat4<float> translate(float x, float y, float z) {
	return Mat4<float>(
		1, 0, 0, x,
		0, 1, 0, y,
		0, 0, 1, z,
		0, 0, 0, 1);
}

Mat4<float> scale(float x, float y, float z) {
	return Mat4<float>(
		x, 0, 0, 0,
		0, y, 0, 0,
		0, 0, z, 0,
		0, 0, 0, 1);
}

Mat4<float> rotate(float x, float y, float z) {
	const float a = cos(x),
		b = sin(x),
		c = cos(y),
		d = sin(y),
		e = cos(z),
		f = sin(z),
		ad = a*d,
		bd = b*d;

	return Mat4<float>(
		c*e,       -c*f,      d,    0,
		bd*e+a*f,  -bd*f+a*e, -b*c, 0,
		-ad*e+b*f, ad*f+b*e,  a*c,  0,
		0,         0,         0,    1);
}

void setup3D(Vec2<int> resolution, double fov, double n, double f) {
	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LEQUAL);
	glEnable(GL_CULL_FACE);
	glCullFace(GL_BACK);

	wglSwapIntervalEXT(1);
	glViewport(0, 0, resolution.x, resolution.y);
	glClearColor(0, 0, 0, 1);
	glClearDepth(1);

	const float r = tan(fov * M_PI / 360.) * n;
	const float t = r * resolution.y / resolution.x;

	projection = Mat4<float>(
		n/r, 0, 0, 0,
		0, n/t, 0, 0,
		0, 0, (n+f)/(n-f), (2*f*n)/(n-f),
		0, 0, -1, 0);
	model = Mat4<float>::IDENTITY;
}
