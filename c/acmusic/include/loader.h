#ifndef LOADER_H_
#define LOADER_H_

int read_format(FILE *file, audio_format_t *format);
int read_data(FILE *file, audio_stream_t *format);
int read_cues(FILE *file, audio_stream_t *format);

#endif
