#include <cmath>
#include <array>

#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif

#ifndef UTIL_H_
#define UTIL_H_

template<typename T>
struct Vec2 {
	T x, y;
	Vec2<T>() : Vec2<T>(0, 0) {}
	Vec2<T>(T x, T y) : x(x), y(y) {}

	T operator[](int n) {
		switch(n) {
		case 0: return x;
		case 1: return y;
		}
	}

	Vec2<T> &operator=(Vec2<T> other) {
		x = other.x;
		y = other.y;
		return *this;
	}
	Vec2<T> &operator+=(Vec2<T> other) {return *this = *this + other;}
	Vec2<T> &operator-=(Vec2<T> other) {return *this = *this - other;}
	Vec2<T> &operator*=(float scale) {return *this = *this * scale;}
	Vec2<T> &operator/=(float scale) {return *this = *this / scale;}

	Vec2<T> operator+(Vec2<T> other) {return Vec2<T>(x + other.x, y + other.y);}
	Vec2<T> operator-(Vec2<T> other) {return Vec2<T>(x - other.x, y - other.y);}
	Vec2<T> operator*(float scale) {return Vec2<T>(x * scale, y * scale);}
	Vec2<T> operator/(float scale) {return Vec2<T>(x / scale, y / scale);}
	float dot(Vec2<T> other) {return x * other.x + y * other.y;}
	operator float() {return sqrt(dot(*this));}
};

template<typename T>
struct Vec3 {
	T x, y, z;
	Vec3<T>() : Vec3<T>(0, 0, 0) {}
	Vec3<T>(T x, T y, T z) : x(x), y(y), z(z) {}

	T operator[](int n) {
		switch(n) {
		case 0: return x;
		case 1: return y;
		case 2: return z;
		}
	}

	Vec3<T> &operator=(Vec3<T> other) {
		x = other.x;
		y = other.y;
		z = other.z;
		return *this;
	}
	Vec3<T> &operator+=(Vec3<T> other) {return *this = *this + other;}
	Vec3<T> &operator-=(Vec3<T> other) {return *this = *this - other;}
	Vec3<T> &operator*=(float scale) {return *this = *this * scale;}
	Vec3<T> &operator/=(float scale) {return *this = *this / scale;}
	Vec3<T> &operator*=(Vec3<T> other) {return *this = *this * other;}

	Vec3<T> operator+(Vec3<T> other) {return Vec3<T>(x + other.x, y + other.y, z + other.z);}
	Vec3<T> operator-(Vec3<T> other) {return Vec3<T>(x - other.x, y - other.y, z - other.z);}
	Vec3<T> operator*(float scale) {return Vec3<T>(x * scale, y * scale, z * scale);}
	Vec3<T> operator/(float scale) {return Vec3<T>(x / scale, y / scale, z / scale);}
	Vec3<T> operator*(Vec3<T> other) {return Vec3<T>(
		y * other.z - z * other.y,
		z * other.x - x * other.z,
		x * other.y - y * other.x);}

	float dot(Vec3<T> other) {return x * other.x + y * other.y + z * other.z;}
	operator float() {return sqrt(dot(*this));}
};

template<typename T>
struct Vec4 {
	T x, y, z, w;
	Vec4<T>() : Vec4<T>(0, 0, 0, 0) {}
	Vec4<T>(T x, T y, T z, T w) : x(x), y(y), z(z), w(w) {}

	T operator[](int n) {
		switch(n) {
		case 0: return x;
		case 1: return y;
		case 2: return z;
		case 3: return w;
		default: throw std::out_of_range("Vec4<T> index is out of range");
		}
	}

	Vec4<T> &operator=(Vec4<T> other) {
		x = other.x;
		y = other.y;
		z = other.z;
		w = other.w;
		return *this;
	}
	Vec4<T> &operator+=(Vec4<T> other) {return *this = *this + other;}
	Vec4<T> &operator-=(Vec4<T> other) {return *this = *this - other;}
	Vec4<T> &operator*=(float scale) {return *this = *this * scale;}
	Vec4<T> &operator/=(float scale) {return *this = *this / scale;}
	Vec4<T> &operator*=(Vec4<T> other) {return *this = *this * other;}

	Vec4<T> operator+(Vec4<T> other) {return Vec4<T>(x + other.x, y + other.y, z + other.z, w + other.w);}
	Vec4<T> operator-(Vec4<T> other) {return Vec4<T>(x - other.x, y - other.y, z - other.z, w - other.w);}
	Vec4<T> operator*(float scale) {return Vec4<T>(x * scale, y * scale, z * scale, w * scale);}
	Vec4<T> operator/(float scale) {return Vec4<T>(x / scale, y / scale, z / scale, w / scale);}

	float dot(Vec4<T> other) {return x * other.x + y * other.y + z * other.z + w * other.w;}
	operator float() {return sqrt(dot(*this));}
};

template<typename T>
struct Mat3 {
	static Mat3<T> IDENTITY;

	Vec3<T> a, b, c;
	Mat3<T>(Vec3<T> a, Vec3<T> b, Vec3<T> c) : a(a), b(b), c(c) {}
	Mat3<T>(T ax, T ay, T az, T bx, T by, T bz, T cx, T cy, T cz)
		: Mat3<T>(Vec3<T>(ax, ay, az), Vec3<T>(bx, by, bz), Vec3<T>(cx, cy, cz)) {}

	Vec3<T> &operator[](int n) {
		switch(n) {
		case 0: return a;
		case 1: return b;
		case 2: return c;
		}
	}

	Mat3<T> &operator=(Mat3<T> other) {
		a = other.a;
		b = other.b;
		c = other.c;
		return *this;
	}
	Mat3<T> &operator+=(Mat3<T> other) {return *this = *this + other;}
	Mat3<T> &operator*=(float scale) {return *this = *this * scale;}
	Mat3<T> &operator*=(Mat3<T> other) {return *this = *this * other;}

	Mat3<T> operator+(Mat3<T> other) {
		Mat3<T> mat(a, b, c);
		mat.a += other.a;
		mat.b += other.b;
		mat.c += other.c;
		return mat;
	}
	Mat3<T> operator*(float scale) {
		Mat3<T> mat(a, b, c);
		mat.a *= scale;
		mat.b *= scale;
		mat.c *= scale;
		return mat;
	}
	Mat3<T> operator*(Mat3<T> other) {
		return Mat3<T>(
			a.x * other.a.x + a.y * other.b.x + a.z * other.c.x,
			a.x * other.a.y + a.y * other.b.y + a.z * other.c.y,
			a.x * other.a.z + a.y * other.b.z + a.z * other.c.z,

			b.x * other.a.x + b.y * other.b.x + b.z * other.c.x,
			b.x * other.a.y + b.y * other.b.y + b.z * other.c.y,
			b.x * other.a.z + b.y * other.b.z + b.z * other.c.z,

			c.x * other.a.x + c.y * other.b.x + c.z * other.c.x,
			c.x * other.a.y + c.y * other.b.y + c.z * other.c.y,
			c.x * other.a.z + c.y * other.b.z + c.z * other.c.z);
	}
};

template<typename T>
struct Mat4 {
	static Mat4<T> IDENTITY;

	Vec4<T> a, b, c, d;
	Mat4<T>(Vec4<T> a, Vec4<T> b, Vec4<T> c, Vec4<T> d) : a(a), b(b), c(c), d(d) {}
	Mat4<T>(T ax, T ay, T az, T aw, T bx, T by, T bz, T bw, T cx, T cy, T cz, T cw, T dx, T dy, T dz, T dw)
		: Mat4<T>(Vec4<T>(ax, ay, az, aw), Vec4<T>(bx, by, bz, bw), Vec4<T>(cx, cy, cz, cw), Vec4<T>(dx, dy, dz, dw)) {}

	Vec4<T> &operator[](int n) {
		switch(n) {
		case 0: return a;
		case 1: return b;
		case 2: return c;
		case 3: return d;
		default: throw std::out_of_range("Mat4<T> index is out of range");
		}
	}

	Mat4<T> &operator=(Mat4<T> other) {
		a = other.a;
		b = other.b;
		c = other.c;
		d = other.d;
		return *this;
	}
	Mat4<T> &operator+=(Mat4<T> other) {return *this = *this + other;}
	Mat4<T> &operator*=(float scale) {return *this = *this * scale;}
	Mat4<T> &operator*=(Mat4<T> other) {return *this = *this * other;}

	Mat4<T> operator+(Mat4<T> other) {
		Mat4<T> mat(a, b, c, d);
		mat.a += other.a;
		mat.b += other.b;
		mat.c += other.c;
		mat.d += other.d;
		return mat;
	}
	Mat4<T> operator*(float scale) {
		Mat4<T> mat(a, b, c, d);
		mat.a *= scale;
		mat.b *= scale;
		mat.c *= scale;
		mat.d *= scale;
		return mat;
	}
	Mat4<T> operator*(Mat4<T> other) {
		return Mat4<T>(
			a.x * other.a.x + a.y * other.b.x + a.z * other.c.x + a.w * other.d.x,
			a.x * other.a.y + a.y * other.b.y + a.z * other.c.y + a.w * other.d.y,
			a.x * other.a.z + a.y * other.b.z + a.z * other.c.z + a.w * other.d.z,
			a.x * other.a.w + a.y * other.b.w + a.z * other.c.w + a.w * other.d.w,

			b.x * other.a.x + b.y * other.b.x + b.z * other.c.x + b.w * other.d.x,
			b.x * other.a.y + b.y * other.b.y + b.z * other.c.y + b.w * other.d.y,
			b.x * other.a.z + b.y * other.b.z + b.z * other.c.z + b.w * other.d.z,
			b.x * other.a.w + b.y * other.b.w + b.z * other.c.w + b.w * other.d.w,

			c.x * other.a.x + c.y * other.b.x + c.z * other.c.x + c.w * other.d.x,
			c.x * other.a.y + c.y * other.b.y + c.z * other.c.y + c.w * other.d.y,
			c.x * other.a.z + c.y * other.b.z + c.z * other.c.z + c.w * other.d.z,
			c.x * other.a.w + c.y * other.b.w + c.z * other.c.w + c.w * other.d.w,

			d.x * other.a.x + d.y * other.b.x + d.z * other.c.x + d.w * other.d.x,
			d.x * other.a.y + d.y * other.b.y + d.z * other.c.y + d.w * other.d.y,
			d.x * other.a.z + d.y * other.b.z + d.z * other.c.z + d.w * other.d.z,
			d.x * other.a.w + d.y * other.b.w + d.z * other.c.w + d.w * other.d.w);
	}

	std::array<T, 16> toArray() {
		return {
			a.x, b.x, c.x, d.x,
			a.y, b.y, c.y, d.y,
			a.z, b.z, c.z, d.z,
			a.w, b.w, c.w, d.w
		};
	}
};

extern Mat4<float> model, projection;
void setup3D(Vec2<int> resolution, double fov, double n, double f);

Mat4<float> translate(float x, float y, float z);
Mat4<float> scale(float x, float y, float z);
Mat4<float> rotate(float x, float y, float z);

#endif
