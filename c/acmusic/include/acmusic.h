#ifndef AC_MUSIC_H_
#define AC_MUSIC_H_

#include <stdint.h>
#include <stdio.h>

typedef struct {
	uint16_t format, channels;
	uint32_t sample_rate, byte_rate;
	uint16_t block_align, bit_depth;
} audio_format_t;

typedef struct {
	union {
		int16_t s16;
	} left, right;
} sample_t;

typedef struct {
	uint32_t name;
	uint32_t index;
	uint8_t chunk[4];
	uint32_t chunk_start;
	uint32_t block_start;
	uint32_t position;
} cue_point_t;

typedef struct {
	audio_format_t format;
	uint32_t data_length;
	uint8_t *data;
	uint32_t cue_point_count;
	cue_point_t *cue_points;
} audio_stream_t;

uint16_t read_u16le(uint8_t *buf);
uint32_t read_u32le(uint8_t *buf);
void write_u16le(uint8_t *buf, uint16_t x);
void write_u32le(uint8_t *buf, uint32_t x);

int id_equals(uint8_t * const buf, const char * const name);
int read_wav(FILE *file, audio_stream_t *stream);
void play_sample(sample_t *sample);
int get_sample(sample_t *sample, audio_stream_t *add);

#endif
