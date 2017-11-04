#include <acmusic.h>
#include <stdio.h>
#include <stdint.h>
#include <io.h>
#include <fcntl.h>
#include <memory.h>
#include <time.h>
#include <windows.h>

int main(int argc, char **argv) {
	_setmode(1, _O_BINARY);

	audio_stream_t streams[24];
	for(int i = 0; i < 24; i++) {
		audio_stream_t *stream = &(streams[i]);
		memset(stream, 0, sizeof(audio_stream_t));

		char name[7];
		memcpy(name, "res/00.wav", 11);
		name[4] += i / 10;
		name[5] += i % 10;

		FILE *file = fopen(name, "rb");
		if(read_wav(file, stream)) {
			fputs("Invalid file somehow??\n", stderr);
			return 1;
		}
		fclose(file);
	}
	sample_t sample;
	int add_first = 1;

	uint64_t now = time(NULL);
	for(int i = 0; 1; now++) {
		audio_stream_t *add = NULL;

		// Wait if we're more than 10 seconds ahead of time
		if((now & 15) == 0) {
			uint64_t real = time(NULL);

			if(now > real + 10) {
				fprintf(stderr, "Waiting for %u seconds\n", (now - (real + 10)));
				Sleep(1000 * (now - (real + 10)));
			}
		}

		// Add a new hour song to the queue
		if(now % 3600 == 0 || add_first) {
			add = &(streams[(now % 86400) / 3600]);
			if(add_first) add_first = 0;
		}
		if(get_sample(&sample, add)) return 1;
		play_sample(&sample);

		// Process remaining samples for 1 second
		for(int j = 1; j < 32728; j++, i++) {
			if(get_sample(&sample, NULL)) return 1;
			play_sample(&sample);
		}
	}
	return 0;
}

int get_sample(sample_t *sample, audio_stream_t *add) {
	static audio_stream_t *queue[16] = {0};
	static int tail = 0, head = 0;
	static uint32_t position = 0;

	if(add != NULL) {
		queue[head] = add;
		head = (head + 1) & 15;
	}
	if(head == tail) return 0; // Weird case but it could happen

	if(position == queue[tail]->cue_points[0].position) {
		fputs("Hit loop start\n", stderr);
		if(tail != head - 1) {
			fputs("Skipping to end\n", stderr);
			position = queue[tail]->cue_points[1].position;
		}
	} else if(position == queue[tail]->cue_points[1].position) {
		fputs("Hit loop end\n", stderr);
		if(tail == head - 1) {
			fputs("Looping\n", stderr);
			position = queue[tail]->cue_points[0].position;
		}
	} else if(position == queue[tail]->data_length / 4) {
		//fputs("Hit end of song\n", stderr);
		if(tail != head - 1) {
			fputs("Changing song\n", stderr);
			tail = (tail + 1) & 15;
		} else {
			// Illegal state
			fputs("Queue exhausted.\n", stderr);
			return 1;
		}
		position = 0;
	}

	sample->left.s16  = read_u16le(queue[tail]->data + position*4);
	sample->right.s16 = read_u16le(queue[tail]->data + position*4 + 2);
	++position;

	return 0;
}

void play_sample(sample_t *sample) {
	uint8_t buf[2];

	write_u16le(buf, sample->left.s16);
	putchar(buf[0]); putchar(buf[1]);
	write_u16le(buf, sample->right.s16);
	putchar(buf[0]); putchar(buf[1]);
}
