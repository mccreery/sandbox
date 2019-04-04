#ifndef MODELREADER_H_
#define MODELREADER_H_

#include <vao.hpp>
#include <iostream>

class ModelReader : public VertexGenerator {
	public:
		ModelReader(std::istream &in);
		static GLenum getType(const char c);
		static GLint getSize(const char c);

		GLvoid *getData() {return vertices;}
		GLsizei getVertexCount() {return vertexCount;}
		GLvoid *getIndices() {return indices;}
		GLsizei getIndexCount() {return indexCount;}
	private:
		int vertexCount;
		int indexCount;
		GLvoid *vertices;
		GLvoid *indices;
		size_t loadVertex(std::istream &in, size_t start);
};

class PlyReader : public VertexGenerator {
	public:
		PlyReader(std::ifstream &in);

		GLvoid *getData() {return vertices;}
		GLsizei getVertexCount() {return vertexCount;}
		GLvoid *getIndices() {return indices;}
		GLsizei getIndexCount() {return indexCount;}
	private:
		int vertexCount;
		int indexCount;
		GLvoid *vertices;
		GLvoid *indices;
};

#endif
