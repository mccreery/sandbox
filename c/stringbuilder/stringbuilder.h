#ifndef STRINGBUILDER_H_
	#define STRINGBUILDER_H_

	typedef struct {
		char *text;
		unsigned length, capacity;
	} string_builder;

	void add_char(string_builder *builder, const char c);
	void reset(string_builder *builder);
#endif
