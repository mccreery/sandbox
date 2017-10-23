#include <stdio.h>
#include <time.h>
#include <errno.h>
#include <signal.h>
#include <fcntl.h>
#include <sys/stat.h>

#define IN 0
#define OUT 1

int export(int pin);
int direct(int pin, int direction);
int value(int pin, int value);

int filter(unsigned char t, int (*func)(char, unsigned char));

int alternate(char i, unsigned char t);
int loop     (char i, unsigned char t);
int bloop    (char i, unsigned char t);
int flash    (char i, unsigned char t);
int bounce   (char i, unsigned char t);
int dist     (char i, unsigned char t);
int fan      (char i, unsigned char t);
int change   (char i, unsigned char t);

void exit_handle(int a);
int quit(int error);
