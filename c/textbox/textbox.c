#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "textbox.h"

int main() {
	textbox tb;
	initTextbox(&tb);

	puts("Setting text");
	setText(&tb, "Hello, world!", 13);
	printTextbox(&tb);
	puts("\nAppending text");
	append(&tb, " How are you?", 13);
	printTextbox(&tb);
	puts("\nDeleting text");
	delete(&tb, 5, 7);
	printTextbox(&tb);
	puts("\nInserting text");
	insert(&tb, 18, ", mate", 6);
	printTextbox(&tb);
	puts("\nForcing shrink");
	tb.shrinkDelay = 1;
	delete(&tb, 4, tb.length - 4);
	printTextbox(&tb);
	puts("\nClearing");
	clear(&tb);
	printTextbox(&tb);
	puts("\nGrowing from zero");
	setText(&tb, "And we're back!", 15);
	printTextbox(&tb);

	cleanupTextbox(&tb);
	return 0;
}

void initTextbox(textbox *ptr) {
	ptr->selectStart = ptr->selectEnd = ptr->length = 0;
	ptr->text = malloc(ptr->capacity = MIN_SIZE);
	ptr->shrinkDelay = SHRINK_DELAY;
}
void cleanupTextbox(textbox *ptr) {
	free(ptr->text);
}

/*void keyTyped(textbox *ptr, uint_fast8_t key) {
	switch(key) {
		case CTRL_A:
			select(ptr, 0, ptr->length);
			break;
		case CTRL_C:
			// TODO Set clipboard string
			break;
		case CTRL_V:
			const uint_least8_t *text = ""; // get from clipboard
			write(ptr, text);
			break;
		case '\b':
			deleteSelection(ptr);
			if(FALSE) { // CTRL key is down
				uint_fast32_t last = lastWord(ptr, ptr->selectStart);
				delete(ptr, last, ptr->selectStart - last);
				moveCursor(ptr, last);
			} else {
				moveCursor(ptr, ptr->selectStart - 1);
				delete(ptr, ptr->selectStart, 1);
			}
		case HOME:
			if(FALSE) { // SHIFT key is down
				ptr->selectEnd = 0;
			} else {
				moveCursor(ptr, 0);
			}
		case LEFT:
			// TODO
		case RIGHT:
			// TODO
		case END:
			if(FALSE) { // SHIFT key is down
				ptr->selectEnd = ptr->length;
			} else {
				moveCursor(ptr, ptr->length);
			}
	}
}*/

void moveCursor(textbox *ptr, uint_fast32_t pos) {
	select(ptr, pos, pos);
}
void select(textbox *ptr, uint_fast32_t start, uint_fast32_t end) {
	ptr->selectStart = start < ptr->capacity ? start : ptr->capacity - 1;
	ptr->selectEnd = end < ptr->capacity ? end : ptr->capacity - 1;
}
bool hasSelection(textbox *ptr) {
	return ptr->selectStart != ptr->selectEnd;
}
uint_least8_t *getSelection(textbox *ptr) {
	if(ptr->selectStart < ptr->selectEnd) {
		return ptr->text + ptr->selectStart;
	} else {
		return ptr->text + ptr->selectEnd;
	}
}
uint_fast32_t getSelectionLength(textbox *ptr) {
	if(ptr->selectStart < ptr->selectEnd) {
		return ptr->selectEnd - ptr->selectStart;
	} else {
		return ptr->selectStart - ptr->selectEnd;
	}
}
void clear(textbox *ptr) {
	enforceLength(ptr, 0);
	ptr->length = ptr->selectStart = ptr->selectEnd = 0;
}
bool setText(textbox *ptr, const uint_least8_t *text, uint_fast32_t length) {
	if(enforceLength(ptr, length)) {
		memcpy(ptr->text, text, length);
		ptr->length = length;
		return TRUE;
	}
	return FALSE;
}
bool append(textbox *ptr, const uint_least8_t *text, uint_fast32_t length) {
	if(enforceLength(ptr, ptr->length + length)) {
		memcpy(ptr->text + ptr->length, text, length);
		ptr->length += length;
		return TRUE;
	}
	return FALSE;
}
bool insert(textbox *ptr, uint_fast32_t i, const uint_least8_t *text, uint_fast32_t length) {
	if(enforceLength(ptr, ptr->length + length)) {
		// Shift old text
		memmove(ptr->text + i + length, ptr->text + i, ptr->length - i);
		// Insert new text
		memcpy(ptr->text + i, text, length);
		ptr->length += length;
		return TRUE;
	}
	return FALSE;
}
void delete(textbox *ptr, uint_fast32_t i, uint_fast32_t length) {
	// Shift text
	memmove(ptr->text + i, ptr->text + i + length, ptr->length - (i + length));
	ptr->length -= length;
	// Since we've removed text, we may be able to shrink
	enforceLength(ptr, ptr->length);
}
void deleteSelection(textbox *ptr) {
	if(hasSelection(ptr)) {
		if(ptr->selectStart < ptr->selectEnd) {
			delete(ptr, ptr->selectStart, ptr->selectEnd - ptr->selectStart);
			moveCursor(ptr, ptr->selectStart);
		} else {
			delete(ptr, ptr->selectEnd, ptr->selectStart - ptr->selectEnd);
			moveCursor(ptr, ptr->selectEnd);
		}
	}
}
bool write(textbox *ptr, const uint_least8_t *text, uint_fast32_t length) {
	deleteSelection(ptr);
	return insert(ptr, ptr->selectStart, text, length);
}

uint_fast32_t nextWord(textbox *ptr, uint_fast32_t i) {
	--i;
	while(++i != ptr->length && !IS_WHITESPACE(ptr->text[i])); // Find next whitespace
	--i;
	while(++i != ptr->length && IS_WHITESPACE(ptr->text[i])); // Find next word
	return i;
}
uint_fast32_t lastWord(textbox *ptr, uint_fast32_t i) {
	while(--i != -1 && IS_WHITESPACE(ptr->text[i])); // Find previous word
	++i;
	while(--i != -1 && !IS_WHITESPACE(ptr->text[i])); // Find previous whitespace
	++i;
	return i;
}

/** @return TRUE if the textbox is capable
    of holding the given length
    Shrinks or grows capacity when necessary */
bool enforceLength(textbox *ptr, uint_fast32_t length) {
	if(ptr->capacity > MIN_SIZE && length <= (ptr->capacity >> 1)) {
		if(!--ptr->shrinkDelay) {
			// Shrink
			uint_fast32_t capacity = ptr->capacity >> 1;
			for(; capacity > MIN_SIZE && length <= capacity >> 1; capacity >>= 1);

			uint_least8_t *temp = ptr->text;
			ptr->text = realloc(ptr->text, capacity);
			if(ptr->text != NULL) {
				ptr->shrinkDelay = SHRINK_DELAY;
				ptr->capacity = capacity;

				if(ptr->length > ptr->capacity) {
					ptr->length = ptr->capacity;
				}
			} else {
				ptr->text = temp;
				ptr->shrinkDelay = 1; // Try again next time
			}
		}
	} else {
		ptr->shrinkDelay = SHRINK_DELAY;

		if(length > ptr->capacity) {
			// Grow
			uint_fast32_t capacity = ptr->capacity << 1;
			for(; length > capacity; capacity <<= 1);

			uint_least8_t *temp = ptr->text;
			ptr->text = realloc(ptr->text, capacity);
			if(ptr->text != NULL) {
				ptr->capacity = capacity;
			} else {
				ptr->text = temp;
				return FALSE;
			}
		}
	}
	return TRUE;
}

void printTextbox(textbox *ptr) {
	if(ptr->length != 0) {
		uint_least8_t *text = malloc(ptr->length + 1);
		memcpy(text, ptr->text, ptr->length);
		text[ptr->length] = '\0';

		printf("Text:     %s\nLength:   %u\nCapacity: %u\n", text, ptr->length, ptr->capacity);
		free(text);
	} else {
		printf("Length:   %u\nCapacity: %u\n", ptr->length, ptr->capacity);
	}
}
