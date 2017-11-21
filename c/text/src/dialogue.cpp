#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <map>
#include "dialogue.h"

std::map<char *, dialogue> dialogueMap;

dialogue getDialogue(char *key) {
	return dialogueMap[key];
}

void loadDialogue(FILE *file) {
	char *name, *entry;
	char c = '\0';

	while(c != EOF) {
		char *name = readUntil(file, "=", NAME_SIZE);

		dialogue *d;
		d->entry = readUntil(file, "\n\r", ENTRY_SIZE);
		processEntry(d);
		dialogueMap[name] = &d;

		while(isNewline(c = fgetc(file)));
		ungetc(c, file);
	}
}

void processEntry(dialogue *dialogue) {
	int len = strlen(dialogue->entry);
	int i = 0, j = 0, k = 0;

	int size = SYLLABLE_COUNT;
	dialogue->syllables = malloc(size * sizeof(int));

	for(; i < len; i++) {
		int start = i;
		for(; i < len && dialogue->entry[i] != ' ' && dialogue->entry[i] != '-'; i++);

		// Add new syllable index
		if(k >= size) {
			dialogue->syllables = realloc(dialogue->syllables, size *= 2);
		}
		dialogue->syllables[k++] = j;

		// Copy over part of string we need
		if(dialogue->entry[i] == '-') {
			memmove(dialogue->entry + j, dialogue->entry + start, i - start);
			j += i - start;
		} else {
			memmove(dialogue->entry + j, dialogue->entry + start, i - start + 1);
			j += i - start + 1;
		}
	}
	dialogue->syllableCount = k;
	dialogue->entry = realloc(dialogue->entry, j + 1);
	dialogue->entry[j] = '\0';
}

int isNewline(char x) {
	return x == '\n' || x == '\r';
}

char *readUntil(FILE *file, const char *end, const int initialSize) {
	int size = initialSize;
	char *buf = malloc(size);

	int i; char cur;
	for(i = 0; !strchr(end, cur = fgetc(file)) && cur != EOF; i++) {
		if(i >= size) { // We need more memory
			buf = realloc(buf, size *= 2);
		}
		buf[i] = cur;
	}
	buf = realloc(buf, i + 1);
	buf[i] = '\0';
	return buf;
}
