#include <gamewindow.hpp>
#include <gl.hpp>
#include <gl33.h>
#include <GL/gl.h>
#include <ioutils.hpp>
#include <modelreader.hpp>
#include <stdlib.h>
#include <util.hpp>
#include <vao.hpp>
#include <vaotest.hpp>
#include <windef.h>
#include <winnt.h>
#include <cstdio>
#include <fstream>
#include <iostream>
#include <shader.hpp>
#include <sstream>

void render(GameWindow &window);

VAO *monkey;
GLuint test;
float theta;

ShaderProgram *program;

VAO *cube;

// TEMP STUFF FOR PACKING

const char * const VERT_SOURCE = "#version 150\n"

"uniform mat4 model;"
"uniform mat4 projection;"

"in vec4 pos;"
"in vec3 normal;"

"out vec4 f_position;"
"out vec3 f_normal;"

"void main() {"
	"gl_Position = projection * model * pos;"

	// Pass-through
	"f_position = pos;"
	"f_normal = normal;"
"}";

const char * const FRAG_SOURCE = "#version 150\n"

"uniform mat4 model;"
"uniform vec3 light;"

"in vec4 f_position;"
"in vec3 f_normal;"

"void main() {"
	"mat3 normalMatrix = transpose(inverse(mat3(model)));"

	"vec3 normal = normalize(normalMatrix * f_normal);"
	"vec4 position = model * f_position;"

	"vec3 dist = normalize(light - vec3(position));"
	"vec4 diffuse = vec4(1, 1, 1, 1) * max(0.1, dot(normal, dist));"
	"vec4 specular = vec4(1, 1, 1, 1) * clamp(pow(max(0.0, dot(reflect(normal, dist), vec3(position))), 0.9), 0, 1);"

	"gl_FragColor = diffuse;"
"}";

#include <monkeymodel.h>

// END TEMP STUFF

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE prev, LPSTR cmd, int show) {
	GameWindow window(hInstance, L"CHMPSPIN");
	gl::loadExtensions(window.hDC);
	setup3D(window.getResolution(), 90, .1, 500.);

	test = generateSampleVAO();

	std::string ah(reinterpret_cast<const char *>(MONKEY_HIGH), 70773);
	std::istringstream stream(ah);
	//std::ifstream stream;
	//stream.open("resources/models/monkey_high.mdl", std::ifstream::in | std::ifstream::binary);
	ModelReader loader(stream);
	VAO _monkey(loader, GL_TRIANGLES);
	monkey = &_monkey;
	//stream.close();

	CubeGenerator temp;
	cube = new VAO(temp, GL_TRIANGLES);

	//char *source;

	Shader vertex(GL_VERTEX_SHADER);
	//readRaw(source, "resources/shaders/main.vert", true);
	vertex.source(VERT_SOURCE/*source*/);

	Shader fragment(GL_FRAGMENT_SHADER);
	//readRaw(source, "resources/shaders/main.frag", true);
	fragment.source(FRAG_SOURCE/*source*/);

	program = new ShaderProgram(vertex, fragment);
	program->attrib(0, "pos");
	program->attrib(1, "normal");
	program->use();

	GLint light = glGetUniformLocation(program->id, "light");
	glUniform3f(light, 5, 5, 5);

	theta = 0;
	while(1) {
		if(window.handleMessages()) {
			break;
		}

		render(window);
		theta += .01f;
	}
	return 0;
}

void render(GameWindow &window) {
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	//model = rotate(.5, 0, 0) * translate(0, -1.25, -1.5) * rotate(0, theta, 0); // TEAPOT
	model = rotate(.5, 0, 0) * translate(0, -0.75, -2) * rotate(0, theta, 0); // MONKEY
	//model = rotate(.5, 0, 0) * translate(0, -1, -5) * rotate(0, theta, 0); // CUBE
	//model = rotate(.75, 0, 0) * translate(0, -10, -10) * rotate(0, theta, 0); // CHAIR

	GLint modelU = glGetUniformLocation(program->id, "model");
	glUniformMatrix4fv(modelU, 1, GL_FALSE, model.toArray().data());
	GLint projectionU = glGetUniformLocation(program->id, "projection");
	glUniformMatrix4fv(projectionU, 1, GL_FALSE, projection.toArray().data());

	monkey->draw();
	//cube->draw();

	window.swapBuffers();
}
