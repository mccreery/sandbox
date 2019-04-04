#ifndef WORLD_HPP_
#define WORLD_HPP_

#include <util.hpp>
#include <vector>
#include <vao.hpp>

class Entity {
public:
	virtual ~Entity() {}
	virtual void tick() = 0;
	virtual void render() = 0;
};

class Object : Entity {
public:
	Object(VAO &vao, Vec3<float> pos);
	void tick();
	void render();
private:
	VAO &vao;
	Vec3<float> pos;
};

class World {
public:
	void tick();
private:
	std::vector<Entity> entities;
};

#endif
