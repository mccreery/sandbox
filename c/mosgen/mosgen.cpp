#include <iostream>
#include <iomanip>
#include <cmath>
#include <string>

using namespace std;
struct Params { unsigned coeff, offset, shift; };

Params find_best(Params & max, unsigned source, unsigned target);

int main(int argc, char ** argv) {
    if(argc < 5) return 1;

    unsigned max_depth = atoi(argv[1]);
    Params max;
    max.coeff  = atoi(argv[2]);
    max.offset = atoi(argv[3]);
    max.shift  = atoi(argv[4]);

    cout << "const unsigned TABLE_" << max_depth << '['<< max_depth << "][" << max_depth << "][3] = {" << endl;

    for(unsigned source = 1; source <= max_depth; source++) {
        cout << "    { ";
        for(unsigned target = 1; target <= max_depth; target++) {
            Params best = find_best(max, source, target);

            cout << '{' << best.coeff << ", " << best.offset << ", " << best.shift << "}";
            if(target != max_depth) cout << ", ";
        }
        cout << " }";
        if(source != max_depth) cout << ',';
        cout << endl;
    }
    cout << "};" << endl;
}

Params find_best(Params & max, unsigned source, unsigned target) {
    Params best;
    float best_se = -1;

    Params p;
    for(p.coeff = 1; p.coeff <= max.coeff; p.coeff++) {
        for(p.offset = 0; p.offset <= max.offset; p.offset++) {
            for(p.shift = 0; p.shift <= max.shift; p.shift++) {
                const float coeff = (float)((1 << target) - 1) / (float)((1 << source) - 1);
                float se = 0;

                for(unsigned x = 0; x < (1u << source); x++) {
                    unsigned actual   = (p.coeff * x + p.offset) >> p.shift;
                    if(actual >= (1u << target)) goto nope;

                    unsigned expected = round(x * coeff);

                    se += (expected - actual) * (expected - actual);
                }

                if(best_se < 0 || se < best_se) {
                    best = p;
                    best_se = se;
                }
                nope:;
            }
        }
    }
    return best;
}
