#include "stringbuilder.h"
#include <memory.h>
#include <stdlib.h>

void reset(string_builder *builder) {
	if(builder->text) free(builder->text);
	builder->text = malloc(builder->capacity = 16);
	builder->length = 0;
}

void add_char(string_builder *builder, const char c) {
	if(builder->length >= builder->capacity) {
		builder->capacity <<= 1;
		builder->text = realloc(builder->text, builder->capacity);
	}
	builder->text[builder->length] = c;
	builder->length += 1;
}
