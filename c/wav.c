#include <stdio.h>
#include <stdint.h>
#include <memory.h>
#include <stdlib.h>

#define CHUNK 0
#define BYTE  1
#define SHORT 2
#define INT   4
#define LONG  8
#define WORD  SHORT
#define DWORD INT
#define QWORD LONG

#define WAVE_FORMAT_PCM  0x0001
#define IBM_FORMAT_MULAW 0x0101
#define IBM_FORMAT_ADPCM 0x0103

inline void fwrite_little16(FILE *file, uint16_t x) {
	fputc(x & 0xFF, file);
	fputc(x >> 8  , file);
}
inline void fwrite_big16(FILE *file, uint16_t x) {
	fputc(x >> 8  , file);
	fputc(x & 0xFF, file);
}
inline void fwrite_little32(FILE *file, uint32_t x) {
	fputc(x       & 0xFF, file);
	fputc(x >> 8  & 0xFF, file);
	fputc(x >> 16 & 0xFF, file);
	fputc(x >> 24,        file);
}
inline void fwrite_big32(FILE *file, uint32_t x) {
	fputc(x >> 24,        file);
	fputc(x >> 16 & 0xFF, file);
	fputc(x >> 8  & 0xFF, file);
	fputc(x       & 0xFF, file);
}

#define SQUARE    0
#define SAWTOOTH  1
#define ISAWTOOTH 2
#define TRIANGLE  3
#define SINE      4
#define NOISE     5

uint8_t wave(uint8_t i, uint8_t wave_type) {
	switch(wave_type) {
		case SAWTOOTH:
			return i;
		case ISAWTOOTH:
			return ~i;
		case SQUARE:
			return (i >> 7) - 1;
		case TRIANGLE:
			return (i << 1) ^ ((i >> 7) - 1);
		case NOISE:
			i ^= i >> 6;
			i ^= i << 5;
			i ^= i << 3;
			return i;
		case SINE:;
			uint8_t mask = (i >> 7) - 1;
			i &= 127;
			i = ((i * (i - 128)) >> 5) + 128;
			i ^= mask;
			return i;
		default:
			return 0;
	}
}

typedef struct {
	void **data;
	uint8_t *data_types;
	uint32_t data_length;

	uint8_t id[4];
} riff_chunk;

uint32_t write_riff(FILE *file, riff_chunk *chunk) {
	printf("Starting chunk %4.4s\n", chunk->id);
	fwrite(chunk->id, 1, 4, file);
	long size_pos = ftell(file);
	fseek(file, 4, SEEK_CUR);
	uint32_t size = 0;

	int i;
	for(i = 0; i < chunk->data_length; i++) {
		uint32_t value;
		switch(chunk->data_types[i]) {
			case BYTE:
				fputc(*(uint8_t *)(chunk->data[i]), file);
				size++;
				break;
			case SHORT:
				value = *(uint16_t *)(chunk->data[i]);
				fwrite_little16(file, value);
				size += 2;
				break;
			case INT:
				value = *(uint32_t *)(chunk->data[i]);
				fwrite_little32(file, value);
				size += 4;
				break;
			case CHUNK:
				size += write_riff(file, (riff_chunk *)(chunk->data[i]));
				break;
			default: break;
		}
	}

	fseek(file, size_pos, SEEK_SET);
	printf("Size: ");
	fwrite_little32(file, size);
	fseek(file, size, SEEK_CUR);
	puts("Ending chunk");
	return size + 8;
}

#define TWRT_2 1.059463094359295264561825294F

/** Finds the frequency of a given musical note, n,
    where A0 corresponds to n=0 and A4 (A440)
    corresponds to n=48

    @return The frequency of note n, in Hz */
float freq(unsigned note) {
	float val = 27.5F;  // A0
	float exp = TWRT_2; // 12th root of 2

	// Exponentiate by squaring
	for(; note > 0; note /= 2) {
		if(note % 2 == 1) {
			val *= exp;
		}
		exp *= exp;
	}
	return val;
}

int main(int argc, char **argv) {
	if(argc >= 3) {
		uint8_t wave_type;
		if(argc >= 4) {
			wave_type = argv[3][0] - '0';
		} else {
			wave_type = 5;
		}

		riff_chunk wav, fmt, data;

		// fmt chunk
		memcpy(fmt.id, "fmt ", 4);
		fmt.data_length = 6;
		uint16_t format_tag      = WAVE_FORMAT_PCM;
		uint16_t channels        = 1;
		uint32_t sample_rate     = 44100;
		uint32_t bits_per_sample = 8;
		uint32_t block_align     = channels * (bits_per_sample / 8);
		uint32_t byte_rate       = sample_rate * block_align;

		fmt.data = malloc(fmt.data_length * sizeof(void *));
		fmt.data[0] = &format_tag;
		fmt.data[1] = &channels;
		fmt.data[2] = &sample_rate;
		fmt.data[3] = &byte_rate;
		fmt.data[4] = &block_align;
		fmt.data[5] = &bits_per_sample;

		fmt.data_types = malloc(fmt.data_length);
		fmt.data_types[0] = fmt.data_types[1] = fmt.data_types[4] = SHORT;
		fmt.data_types[2] = fmt.data_types[3] = fmt.data_types[5] = INT;

		// data chunk
		memcpy(data.id, "data", 4);
		data.data_length = atoi(argv[2]) * sample_rate;
		data.data = malloc(data.data_length * sizeof(void *));

		const float k = 256.0F / sample_rate;

		#define G3 46
		#define A4 48
		#define B4 50
		#define C4 51
		#define D4 53
		#define E4 55
		#define F4 56
		#define G4 58

		#define NOTE_COUNT 18
		uint8_t notes[NOTE_COUNT] = {
			E4,0,  E4,0, E4,0,0, C4,0, E4,0, G4,G4,0, G3,G3,0
		};

		uint32_t i;
		for(i = 0; i < data.data_length; i++) {
			uint8_t *ptr = malloc(1);

			unsigned note_no = (int)(i / (float)sample_rate * 10.0F);
			if(notes[note_no % NOTE_COUNT]) {
				float f = freq(notes[note_no % NOTE_COUNT]);

				uint8_t normalized = fmod(k*i*f, 256.0F);

				*ptr = wave(normalized, wave_type);
			} else {
				*ptr = 0;
			}
			data.data[i] = ptr; // Fill with 1 tone with period 256 samples
		}
		data.data_types = malloc(data.data_length);
		memset(data.data_types, BYTE, data.data_length);

		// RIFF chunk (main chunk)
		memcpy(wav.id, "RIFF", 4);
		wav.data_length = 6;
		wav.data_types = malloc(6);
		wav.data_types[0] = wav.data_types[1] = wav.data_types[2] = wav.data_types[3] = BYTE;
		wav.data_types[4] = wav.data_types[5] = CHUNK;

		wav.data = malloc(6 * sizeof(void *));
		char *wave = "WAVE";
		wav.data[0] = wave;
		wav.data[1] = wave + 1;
		wav.data[2] = wave + 2;
		wav.data[3] = wave + 3;
		wav.data[4] = &fmt;
		wav.data[5] = &data;

		FILE *file = fopen(argv[1], "w");
		write_riff(file, &wav);
		fclose(file);

		for(i = 0; i < data.data_length; i++) {
			free(data.data[i]);
		}
		free(data.data);
	}
}
