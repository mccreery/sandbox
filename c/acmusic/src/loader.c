#include <acmusic.h>
#include <loader.h>
#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

int read_wav(FILE *file, audio_stream_t *stream) {
	uint8_t buf[12];
	unsigned long file_length = 8;

	fread(buf, 1, 12, file);
	if(!id_equals(buf, "RIFF") || !id_equals(buf + 8, "WAVE")) {
		fputs("Header ain't no good\n", stderr);
		return 1;
	}
	file_length += read_u32le(buf + 4);

	while(ftell(file) < file_length) {
		if(fread(buf, 1, 4, file) == 0) {
			fputs("File is longer than advertised\n", stderr);
			return 1;
		}

		if(id_equals(buf, "fmt ")) {
			if(read_format(file, &(stream->format))) {
				return 1;
			}
		} else if(id_equals(buf, "data")) {
			if(read_data(file, stream)) {
				return 1;
			}
		} else if(id_equals(buf, "cue ")) {
			if(read_cues(file, stream)) {
				return 1;
			}
		} else {
			// Skip unknown chunk
			fread(buf, 1, 4, file);
			fseek(file, read_u32le(buf), SEEK_CUR);
		}
	}
	return 0;
}

int read_format(FILE *file, audio_format_t *format) {
	uint8_t buf[20];
	fread(buf, 1, 20, file);

	if(read_u32le(buf) != 16) {
		fputs("\n", stderr);
		return 1;
	}
	format->format      = read_u16le(buf + 4);
	format->channels    = read_u16le(buf + 6);
	format->sample_rate = read_u32le(buf + 8);
	format->byte_rate   = read_u32le(buf + 12);
	format->block_align = read_u16le(buf + 16);
	format->bit_depth   = read_u16le(buf + 18);
	return 0;
}

int read_data(FILE *file, audio_stream_t *stream) {
	uint8_t buf[4];
	fread(buf, 1, 4, file);

	stream->data_length = read_u32le(buf);
	stream->data = malloc(stream->data_length);
	fread(stream->data, 1, stream->data_length, file);
	return 0;
}

int read_cues(FILE *file, audio_stream_t *stream) {
	uint8_t buf[8];
	fread(buf, 1, 8, file);

	uint32_t size = read_u32le(buf) - 4;
	stream->cue_point_count = read_u32le(buf + 4);
	stream->cue_points = malloc(sizeof(cue_point_t) * stream->cue_point_count);

	uint8_t *points_raw = malloc(size);
	fread(points_raw, 1, size, file);

	uint8_t *j = points_raw;
	for(long i = 0; i < stream->cue_point_count; i++) {
		cue_point_t *cue = &(stream->cue_points[i]);
		cue->name = read_u32le(j); j += 4;
		cue->index = read_u32le(j); j += 4;
		memcpy(cue->chunk, j, 4); j += 4;
		cue->chunk_start = read_u32le(j); j += 4;
		cue->block_start = read_u32le(j); j += 4;
		cue->position = read_u32le(j); j += 4;
	}
	free(points_raw);
	return 0;
}
