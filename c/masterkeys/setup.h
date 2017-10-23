#ifndef SETUP_H_
#define SETUP_H_

typedef int bool;
#include <windows.h>
#include <time.h>
#include "SDKDLL.h"

typedef bool (*cond_t)(int);

bool SetLedColorC(int iRow, int iColumn, KEY_COLOR color);
int setup(DEVICE_INDEX device);
int interval(void (*function)(int), bool (*condition)(int), long interval);
cond_t halter();

#endif
