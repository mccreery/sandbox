#ifndef DIALOGUE_H_
	#define DIALOGUE_H_

	#include <stdio.h>

	typedef struct dialogue {
		char *entry;
		int *syllables;
		int syllableCount;
	} dialogue;

	#define NAME_SIZE 16
	#define ENTRY_SIZE 32
	#define SYLLABLE_COUNT 16

	void *loadDialogue(FILE *file);
	dialogue getDialogue(char *key);

	void processEntry(dialogue *dialogue);
	char *readUntil(FILE *file, const char *end, const int initialSize);
	int isNewline(char x);
#endif
