#include <glob.h>
#include <stdio.h>

// Shouldn't need lossless images
#define STBI_NO_JPEG
#include "stb_image.h"

#define PCF_PROPERTIES   1
#define PCF_ACCELERATORS 2
#define PCF_BITMAPS      8

void write_pcf(FILE *output, char **paths, int pathc);
void write_32l(FILE *output, int x);
void write_table(FILE *output, pcf_table table);

typedef struct {
	int type, format, size, offset;
} pcf_table;

int main(int argc, char **argv) {
	if(argc < 3) {
		fputs("At least one input and an output are required.\n", stderr);
		return 1;
	}

	glob_t *files;
	int i;

	glob(argv[1], GLOB_NOSORT, 0, files);
	for(i = 0; i < argc - 1; i++)
		glob(argv[i], GLOB_NOSORT | GLOB_APPEND, 0, files);

	FILE *output = fopen(argv[argc - 1], "wb");
	write_pcf(output, files.gl_pathv, files.gl_pathc);
	globfree(files);

	return 0;
}

void write_pcf(FILE *output, char **paths, int pathc) {
	fputs("\1fcp", output);
	write_32l(output, pathc);

	write_table(output, {
		PCF_PROPERTIES
	});
}

void write_table(FILE *output, pcf_table table) {
	write_32l(output, table.type);
	write_32l(output, table.format);
	write_32l(output, table.size);
	write_32l(output, table.offset);
}

void write_32l(FILE *output, int x) {
	putc(x         & 0xFF, output);
	putc((x >> 8)  & 0xFF, output);
	putc((x >> 16) & 0xFF, output);
	putc((x >> 24) & 0xFF, output);
}
