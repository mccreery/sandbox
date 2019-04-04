#include <gl33.h>
#include <ioutils.hpp>
#include <modelreader.hpp>
#include <stddef.h>
#include <stdlib.h>
#include <winsock2.h>
#include <cstdint>
#include <cstdio>
#include <cstring>
#include <fstream>
#include <stdexcept>
#include <string>
#include <vector>
#include <sstream>

void split(const std::string &s, char delim, std::vector<std::string> &elems) {
	std::stringstream ss;
	ss.str(s);
	std::string item;
	while(std::getline(ss, item, delim)) {
		elems.push_back(item);
	}
}

#define PLY_NONE   0
#define PLY_VERTEX 1
#define PLY_FACE 2

PlyReader::PlyReader(std::ifstream &in) {
	// Here's hoping my boys
	std::string line;
	std::getline(in, line);

	if(line != "ply") {
		throw std::runtime_error("Magic identifier \"ply\" missing");
	}

	int element = PLY_NONE;
	vertexCount = 0;
	int faceCount = 0;

	// Header
	while(std::getline(in, line)) {
		puts(line.c_str());
		std::vector<std::string> parts;
		split(line, ' ', parts);

		// Move on to data
		if(parts[0] == "end_header") {
			break;
		} else if(parts[0] == "comment") {
			continue;
		} else if(parts[0] == "format") {
			if(parts[1] != "ascii" || parts[2] != "1.0") {
				std::stringstream build;
				build << "PLY format \"" << parts[1] << ' ' << parts[2] << "\" is incorrect.";
				throw std::runtime_error(build.str());
			}
		} else if(parts[0] == "element") {
			std::istringstream count(parts[2]);

			if(parts[1] == "vertex") {
				element = PLY_VERTEX;
				count >> vertexCount;
			} else {
				element = PLY_FACE;
				count >> faceCount;
			}
		} else if(parts[0] == "property") {
			if(element == PLY_VERTEX) {
				attrib_t attrib;
				attrib.count = parts[2] == "u" ? 2 : 3;
				if(parts[1] == "float") {
					attrib.type = GL_FLOAT;
				} else {
					std::stringstream build;
					build << "Invalid type \"" << parts[1] << "\".";
					throw std::runtime_error(build.str());
				}
				format.push_back(attrib);

				// Skip y and z (or v), we already know about them.
				std::getline(in, line);
				if(attrib.count == 3) std::getline(in, line);
			} else if(element == PLY_FACE) {
				if(parts[1] != "list") throw std::runtime_error("Face property must be a list.");
				if(parts[4] != "vertex_indices") throw std::runtime_error("Only face indices are allowed.");
			}
		}
	}

	// Body
	std::stringstream out;

	// Vertices
	for(int i = 0; i < vertexCount; i++) {
		for(std::vector<attrib_t>::size_type j = 0; j < format.size(); j++) {
			float x;

			for(int k = 0; k < format[j].count; k++) {
				in >> x;
				std::cout << x << ", ";
				out.write(reinterpret_cast<char *>(&x), 4);
			}
			std::cout << std::endl;
		}
	}
	std::string data = out.str();
	vertices = new char[data.length()];
	memcpy(vertices, data.c_str(), data.length());

	out.str(std::string());

	// Indices
	indexCount = 0;
	for(int i = 0; i < faceCount; i++) {
		int faceIndices;
		in >> faceIndices;
		indexCount += faceIndices;

		uint16_t a, b, c;
		in >> a >> b >> c;

		std::cout << faceIndices << ": ";

		std::cout << a << ", " << b << ", " << c;
		out.write(reinterpret_cast<char *>(&a), 2);
		out.write(reinterpret_cast<char *>(&b), 2);
		out.write(reinterpret_cast<char *>(&c), 2);

		// Faces with multiple tris must be split into tris
		for(int j = 0; j < faceIndices - 3; j++) {
			// Rotate indices to move to new triangle
			b = c;
			in >> c;

			std::cout << ", " << a << ", " << b << ", " << c;
			out.write(reinterpret_cast<char *>(&a), 2);
			out.write(reinterpret_cast<char *>(&b), 2);
			out.write(reinterpret_cast<char *>(&c), 2);
		}
		std::cout << std::endl;
	}
	data = out.str();
	indices = new char[data.length()];
	memcpy(indices, data.c_str(), data.length());
}

ModelReader::ModelReader(std::istream &in) {
	char buf[256];

	// Error checking
	in.read(buf, 4);

	if(buf[0] != 'M' || buf[1] != 'D' || buf[2] != 'L') {
		fputs("The file provided is an invalid MDL file.\n", stderr);
		return;
	}
	if(buf[3] != 0) {
		fprintf(stderr, "The file provided has an invalid version number %u.\n", buf[3]);
		return;
	}

	// Read name
	in.read(buf, 1);
	int name_length = buf[0];
	in.read(buf, name_length);
	buf[name_length] = '\0';

	//printf("Loading model : \"%s\" {\n", buf);

	// Read format
	in.read(buf, 1);
	int format_length = buf[0] * 2; // 2 bytes per format specifier
	in.read(buf, format_length);

	//fputs("\tFormat: { ", stdout);
	for(int i = 0; i < format_length; i += 2) {
		attrib_t attrib;
		attrib.type = getType(buf[i]);
		attrib.count = getSize(buf[i + 1]);
		format.push_back(attrib);
		//printAttrib(attrib);
		/*if(i != format_length - 2) {
			fputs(", ", stdout);
		}*/
	}
	//puts(" }");
	const GLsizei stride = calculateStride();

	// Read vertex count
	vertexCount = read16(in);
	//printf("\tVertices: %u\n", vertexCount);

	// Read vertices
	if((vertices = malloc(stride * vertexCount)) == NULL) {
		fprintf(stderr, "Vertices is NULL, attempted to malloc %d bytes", stride * vertexCount);
	}
	size_t start = 0;
	for(int i = 0; i < vertexCount; i++) {
		start = loadVertex(in, start);
	}

	// Read index count
	indexCount = read16(in) * 3; // Model format stores triangle count
	//printf("\tTriangles: %u\n", indexCount / 3);

	// Read indices
	if((indices = malloc(2 * indexCount)) == NULL) {
		fprintf(stderr, "Indices is NULL, attempted to malloc %d bytes", 2 * indexCount);
	}
	start = 0;
	for(int i = 0; i < indexCount; i++) {
		uint16_t x = read16(in);
		memcpy(reinterpret_cast<char *>(indices) + start, &x, 2);
		start += 2;

		/*uint16_t x;
		in.read(reinterpret_cast<char *>(&x), 2);
		x = ntohs(x);
		memcpy(reinterpret_cast<char *>(indices) + start, &x, 2);*/

		/*in.read((char *)(indices) + start, 2);
		*(uint16_t *)((char *)(indices) + start) = ntohs(*(uint16_t *)((char *)(indices) + start));
		start += 2;*/
	}
	//puts("}");
}

size_t ModelReader::loadVertex(std::istream &in, size_t start) {
	unsigned char * const data = reinterpret_cast<unsigned char *>(vertices);

	for(std::vector<attrib_t>::size_type i = 0; i < format.size(); i++) {
		for(int j = 0; j < format[i].count; j++) {
			switch(format[i].type) {
				case GL_BYTE:
				case GL_UNSIGNED_BYTE:
					in.read(reinterpret_cast<char *>(data) + start++, 1);
					break;
				case GL_SHORT:
				case GL_UNSIGNED_SHORT: {
					uint16_t s = read16(in);
					memcpy(data + start, &s, 2);
					start += 2;
					break;
				}
				case GL_FLOAT:
				case GL_INT:
				case GL_UNSIGNED_INT:
				default: {
					uint32_t l = read32(in);
					memcpy(data + start, &l, 4);
					start += 4;
					break;
				}
				/*case GL_DOUBLE: not supported lol
					in.read(data + start, 8);
					*reinterpret_cast<uint64_t *>(data + start) = ntohll(*reinterpret_cast<uint64_t *>(data + start));
					start += 8;
					break;*/
			}
		}
	}
	return start;
}

GLenum ModelReader::getType(const char c) {
	switch(c) {
		case 'b':
			return GL_BYTE;
		case 'B':
			return GL_UNSIGNED_BYTE;
		case 's':
			return GL_SHORT;
		case 'S':
			return GL_UNSIGNED_SHORT;
		case 'i':
			return GL_INT;
		case 'I':
		case 'u':
		case 'U':
			return GL_UNSIGNED_INT;
		case 'f':
		case 'F':
		default:
			return GL_FLOAT;
		case 'd':
		case 'D':
			return GL_DOUBLE;
	}
}

GLint ModelReader::getSize(const char c) {
	if(c >= '0' && c <= '9') {
		return c - '0';
	} else {
		return 3;
	}
}
