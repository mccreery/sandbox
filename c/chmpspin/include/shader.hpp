#ifndef SHADER_HPP_
#define SHADER_HPP_

#include <gl33.h>
#define MIN_SHADER_TYPE   (0x8B30)
#define MAX_SHADER_TYPE   (MIN_SHADER_TYPE | 0x000F)
#define SHADER_TYPE_COUNT (MAX_SHADER_TYPE - MIN_SHADER_TYPE)

class Shader {
public:
	Shader(GLenum type);
	~Shader();

	void source(const char * const src);
	bool compile();

	const GLenum type;
	const GLuint id;
private:
	GLint compiled;
};

class ShaderProgram {
public:
	ShaderProgram();
	ShaderProgram(Shader &vertex, Shader &fragment);
	~ShaderProgram();

	void attach(Shader &shader);
	void attrib(GLuint index, const GLchar *name);
	bool prepare();
	bool use();

	const GLuint id;
private:
	Shader *shaders[SHADER_TYPE_COUNT];
	GLint prepared;
};

#endif
