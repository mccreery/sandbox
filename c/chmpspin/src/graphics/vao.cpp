#include <gl33.h>
#include <vao.hpp>
#include <cstdio>
#include <iostream>

VAO::VAO(VertexGenerator &generator, int mode)
		: buffers(getBuffers()), arrayId(nextArrayId()), mode(mode) {
	vertexCount = generator.getVertexCount();
	indexCount = generator.getIndexCount();
	const GLsizei stride = generator.calculateStride();

	glBindVertexArray(arrayId);
	glBindBuffer(GL_ARRAY_BUFFER, buffers.vertex);
	glBufferData(GL_ARRAY_BUFFER, vertexCount * stride, generator.getData(), GL_STATIC_DRAW);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers.index);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexCount * 2, generator.getIndices(), GL_STATIC_DRAW);

	char *pointer = 0;
	std::vector<attrib_t> format = generator.getFormat();
	for(std::vector<attrib_t>::size_type i = 0; i < format.size(); i++) {
		glEnableVertexAttribArray(i);
		glVertexAttribPointer(i, format[i].count, format[i].type, GL_FALSE, stride, pointer);
		pointer += VertexGenerator::getElementSize(format[i].type) * format[i].count;
	}
	glBindVertexArray(0);
}

VAO::~VAO() {
	glDeleteVertexArrays(1, &arrayId);
	glDeleteBuffers(1, &buffers.vertex);
	glDeleteBuffers(1, &buffers.index);
}

void VAO::draw(GLenum mode, GLint first, GLsizei count) {
	glBindVertexArray(arrayId);
	glDrawElements(mode, count, GL_UNSIGNED_SHORT, (void *)first);
	glBindVertexArray(0);
}

GLuint VAO::nextArrayId() {
	GLuint id;
	glGenVertexArrays(1, &id);
	return id;
}
buffers_t VAO::getBuffers() {
	buffers_t ids;
	GLuint x[2];
	glGenBuffers(2, x);
	ids.vertex = x[0];
	ids.index = x[1];
	return ids;
}

GLsizei VertexGenerator::calculateStride() {
	GLsizei stride = 0;

	for(std::vector<attrib_t>::size_type i = 0; i < format.size(); i++) {
		stride += getElementSize(format[i].type) * format[i].count;
	}
	return stride;
}

GLsizei VertexGenerator::getElementSize(GLenum type) {
	switch(type) {
		case GL_BYTE:
		case GL_UNSIGNED_BYTE:
			return 1;
		case GL_SHORT:
		case GL_UNSIGNED_SHORT:
			return 2;
		case GL_INT:
		case GL_UNSIGNED_INT:
		case GL_FLOAT:
			return 4;
		case GL_DOUBLE:
			return 8;
		default:
			return 0;
	}
}

void printAttrib(attrib_t &attrib) {
	switch(attrib.type) {
		case GL_BYTE: fputs("{ GL_BYTE, ", stdout); break;
		case GL_UNSIGNED_BYTE: fputs("{ GL_UNSIGNED_BYTE, ", stdout); break;
		case GL_SHORT: fputs("{ GL_SHORT, ", stdout); break;
		case GL_UNSIGNED_SHORT: fputs("{ GL_UNSIGNED_SHORT, ", stdout); break;
		case GL_INT: fputs("{ GL_INT, ", stdout); break;
		case GL_UNSIGNED_INT: fputs("{ GL_UNSIGNED_INT, ", stdout); break;
		case GL_FLOAT: fputs("{ GL_FLOAT, ", stdout); break;
		case GL_DOUBLE: fputs("{ GL_DOUBLE, ", stdout); break;
		default: printf("{ UNKNOWN (%u), ", attrib.type); break;
	}
	printf("%u }", attrib.count);
}
