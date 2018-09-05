#!/usr/bin/env python
import os, sys
path = os.path.abspath(sys.argv[1]) if len(sys.argv) >= 2 else os.getcwd()

for root, _, files in os.walk(path):
    for f in files:
        i = f.rfind(".") + 1

        if i > 0:
            lower = f[:i] + f[i:].lower()
            if f != lower:
                print(os.path.join(root, "{%s -> %s}" % (f, lower)))

                os.rename(os.path.join(root, f),
                    os.path.join(root, lower))
