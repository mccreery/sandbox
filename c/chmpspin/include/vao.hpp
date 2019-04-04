#ifndef VBO_H_
#define VBO_H_

#include <gl33.h>
#include <vector>

typedef struct {
	GLenum type;
	GLint count;
} attrib_t;

void printAttrib(attrib_t &attrib);

typedef struct {
	GLuint vertex, index;
} buffers_t;

class VertexGenerator;

class VAO {
	public:
		VAO(VertexGenerator &generator, int mode);
		virtual ~VAO();

		void draw() {draw(mode, 0, indexCount);}
		void draw(GLenum mode) {draw(mode, 0, indexCount);}
		void draw(GLint first, GLsizei count) {draw(mode, first, count);}
		void draw(GLenum mode, GLint first, GLsizei count);
	private:
		buffers_t buffers;
		const GLuint arrayId;

		buffers_t getBuffers();
		GLuint nextArrayId();

		GLsizei vertexCount;
		GLsizei indexCount;
		const GLenum mode;
};

// TODO this pattern is bad and you should feel bad
class VertexGenerator {
	public:
		virtual ~VertexGenerator() {}

		virtual GLvoid *getData() = 0;
		virtual GLsizei getVertexCount() = 0;
		virtual GLvoid *getIndices() = 0;
		virtual GLsizei getIndexCount() = 0;

		GLsizei calculateStride();
		static GLsizei getElementSize(GLenum type);
		std::vector<attrib_t> getFormat() {return format;}
	protected:
		std::vector<attrib_t> format;
};

class CubeGenerator : public VertexGenerator {
public:
	CubeGenerator() {
		format.push_back({GL_FLOAT, 3}); // Position
		format.push_back({GL_FLOAT, 3}); // Normal
	}

	GLvoid *getData() {return data;}
	GLsizei getVertexCount() {return 24;};
	GLvoid *getIndices() {return indices;}
	GLsizei getIndexCount() {return 36;}
private:
	float data[144] = {
		1, 1, -1, 0, 0, -1,
		1, -1, -1, 0, 0, -1,
		-1, -1, -1, 0, 0, -1,
		-1, 1, -1, 0, 0, -1,
		1, 1, 1, 0, 0, 1,
		-1, 1, 1, 0, 0, 1,
		-1, -1, 1, 0, 0, 1,
		1, -1, 1, 0, 0, 1,
		1, 1, -1, 1, 0, 0,
		1, 1, 1, 1, 0, 0,
		1, -1, 1, 1, 0, 0,
		1, -1, -1, 1, 0, 0,
		1, -1, -1, 0, -1, 0,
		1, -1, 1, 0, -1, 0,
		-1, -1, 1, 0, -1, 0,
		-1, -1, -1, 0, -1, 0,
		-1, -1, -1, -1, 0, 0,
		-1, -1, 1, -1, 0, 0,
		-1, 1, 1, -1, 0, 0,
		-1, 1, -1, -1, 0, 0,
		1, 1, 1, 0, 1, 0,
		1, 1, -1, 0, 1, 0,
		-1, 1, -1, 0, 1, 0,
		-1, 1, 1, 0, 1, 0
	};
	short indices[36] = {
		0, 1, 2, 0, 2, 3,
		4, 5, 6, 4, 6, 7,
		8, 9, 10, 8, 10, 11,
		12, 13, 14, 12, 14, 15,
		16, 17, 18, 16, 18, 19,
		20, 21, 22, 20, 22, 23
	};
};

#endif
