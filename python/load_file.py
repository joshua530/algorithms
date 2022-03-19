import sys
import random

def read_file(filename):
    try:
        with open(filename) as f:
            contents = f.read()
    except IOError:
        print("The file %s does not exist", filename)

    return contents
