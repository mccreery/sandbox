#include <stdint.h>

uint32_t spreadBits2(uint32_t x) {
    // Divide and conquer approach
                                   // ---- ---- ---- ---- fedc ba98 7654 3210
    x = (x | x << 8) & 0x00ff00ff; // ---- ---- fedc ba98 ---- ---- 7654 3210
    x = (x | x << 4) & 0x0f0f0f0f; // ---- fedc ---- ba98 ---- 7654 ---- 3210
    x = (x | x << 2) & 0x33333333; // --fe --dc --ba --98 --76 --54 --32 --10
    x = (x | x << 1) & 0x55555555; // -f-e -d-c -b-a -9-8 -7-6 -5-4 -3-2 -1-0

    return x;
}
