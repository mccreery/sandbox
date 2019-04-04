#include <shader.hpp>
#include <cstdio>

Shader::Shader(GLenum type) : type(type), id(glCreateShader(type)), compiled(false) {}
Shader::~Shader() {glDeleteShader(id);}

void Shader::source(const char * const src) {
	glShaderSource(id, 1, &src, 0);
}

bool Shader::compile() {
	if(compiled) return true;

	glCompileShader(id);

	glGetShaderiv(id, GL_COMPILE_STATUS, &compiled);
	if(!compiled) {
		GLint maxLength = 0;
		glGetShaderiv(id, GL_INFO_LOG_LENGTH, &maxLength);

		char infoLog[maxLength];
		glGetShaderInfoLog(id, maxLength, &maxLength, infoLog);
		puts(infoLog);
	}
	return compiled;
}

ShaderProgram::ShaderProgram() : id(glCreateProgram()), shaders(), prepared(false) {}
ShaderProgram::ShaderProgram(Shader &vertex, Shader &fragment) : ShaderProgram() {
	attach(vertex);
	attach(fragment);
}
ShaderProgram::~ShaderProgram() {glDeleteProgram(id);}

void ShaderProgram::attach(Shader &shader) {
	shaders[shader.type - MIN_SHADER_TYPE] = &shader;
	glAttachShader(id, shader.id);
}

void ShaderProgram::attrib(GLuint index, const GLchar *name) {
	glBindAttribLocation(id, index, name);
}

bool ShaderProgram::prepare() {
	if(prepared) return true;

	for(Shader *shader : shaders) {
		if(shader != NULL) {
			if(!shader->compile()) return false;
		}
	}
	glLinkProgram(id);

	glGetProgramiv(id, GL_LINK_STATUS, &prepared);
	return prepared;
}

bool ShaderProgram::use() {
	if(!prepare()) return false;
	glUseProgram(id);
	return true;
}
