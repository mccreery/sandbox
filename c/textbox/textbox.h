#ifndef TEXTBOX_H_
	#define TEXTBOX_H_

	#define IS_WHITESPACE(c) (c == ' ' || c == '\t' || c == '\n' || c == '\r')
	#define SHRINK_DELAY 8
	#define MIN_SIZE 16

	typedef int bool;
	#define FALSE (0)
	#define TRUE  (!FALSE)

	typedef struct {
		uint_least8_t *text;
		uint_fast32_t length, capacity;
		uint_fast32_t selectStart, selectEnd;

		uint_fast8_t shrinkDelay;
	} textbox;

	void initTextbox(textbox *ptr);
	void cleanupTextbox(textbox *ptr);
	void moveCursor(textbox *ptr, uint_fast32_t pos);
	void select(textbox *ptr, uint_fast32_t start, uint_fast32_t end);
	bool hasSelection(textbox *ptr);
	uint_least8_t *getSelection(textbox *ptr);
	uint_fast32_t getSelectionLength(textbox *ptr);
	void clear(textbox *ptr);
	bool setText(textbox *ptr, const uint_least8_t *text, uint_fast32_t length);
	bool append(textbox *ptr, const uint_least8_t *text, uint_fast32_t length);
	bool insert(textbox *ptr, uint_fast32_t i, const uint_least8_t *text, uint_fast32_t length);
	void delete(textbox *ptr, uint_fast32_t i, uint_fast32_t length);
	void deleteSelection(textbox *ptr);
	bool write(textbox *ptr, const uint_least8_t *text, uint_fast32_t length);
	uint_fast32_t nextWord(textbox *ptr, uint_fast32_t i);
	uint_fast32_t lastWord(textbox *ptr, uint_fast32_t i);
	bool enforceLength(textbox *ptr, uint_fast32_t length);
	void printTextbox(textbox *ptr);
#endif
