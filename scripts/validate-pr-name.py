import re
import sys


pattern = r"PR-[0-9]+(\s{1}[^\s]+)*$"
pat = re.compile(pattern)

test = sys.argv[1]

if ("NOJIRA" not in test) and not re.fullmatch(pat, test):
    print(f"'{test}' has invalid format. Please reformat to the following pattern {pattern} or have NOJIRA included.")