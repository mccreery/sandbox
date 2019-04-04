#include <gl33.h>
#include <stdint.h>
#include <vaotest.hpp>
#include <cstdio>

GLuint generateSampleVAO() {
	float min = -0.5f;
	float max = 0.5f;
	float z = 0;

	const float vertices[] = {
		min, min, z, max, min, z, min, max, z, max, max, z
	};
	const uint16_t indices[] = {
		0, 1, 2, 2, 1, 3
	};
	GLuint array, vertex, index;

	glGenVertexArrays(1, &array);
	glGenBuffers(1, &vertex);
	glGenBuffers(1, &index);

	glBindVertexArray(array);
	glBindBuffer(GL_ARRAY_BUFFER, vertex);
	glBufferData(GL_ARRAY_BUFFER, 12 * sizeof(float), vertices, GL_STATIC_DRAW);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, index);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, 6 * sizeof(uint16_t), indices, GL_STATIC_DRAW);

	glEnableVertexAttribArray(0);
	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, 0);
	glBindVertexArray(0);

	return array;
}
