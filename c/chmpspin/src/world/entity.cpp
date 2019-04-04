#include <world.hpp>

Object::Object(VAO &vao, Vec3<float> pos) : vao(vao), pos(pos) {}

void Object::tick() {
	// nop
}

void Object::render() {
	vao.draw();
}
