#ifndef DEBOUNCE_H
#define DEBOUNCE_H

#include <stddef.h>
#include <stdint.h>

#ifndef DEBOUNCE_BITS
#define DEBOUNCE_BITS 2
#endif

typedef struct {
    uint8_t set_bits;
    uint8_t changed_bits;
    uint8_t counter_planes[DEBOUNCE_BITS];
} debounce_state;

static void debounce_bits(debounce_state *const state, uint8_t const set_bits) {
    uint8_t changed = state->set_bits ^ set_bits;

    for (size_t i = DEBOUNCE_BITS - 1; i > 0; --i) {
        state->counter_planes[i] ^= state->counter_planes[i - 1];
    }
    state->counter_planes[0] = ~state->counter_planes[0];

    /* increment counters by  */
    state->counters_1 ^= state->counters_0; /* carry 1 to 0 */
    state->counters_0 = ~state->counters_0; /* finally toggle bit 0 */

    /* reset counters if the inputs match the stored inputs */
    state->counters_1 &= changed;
    state->counters_0 &= changed;

    state->counters_1 = (state->counters_1 ^ state->counters_0) & changed;
    state->counters_0 = ~state->counters_0 & changed;

    state->changed_bits = state->counters_0 & state->counters_1;
    state->set_bits ^= state->changed_bits;
}

#endif /* DEBOUNCE_H */
