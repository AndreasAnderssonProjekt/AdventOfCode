from functools import cache
import os


@cache
def arrangements(sequence, seqIndex, conditions, condIndex, nrCompleted):
    sum = 0
    if seqIndex >= len(sequence):
        return 1 if (condIndex >= len(conditions) and nrCompleted == 0) or (condIndex == len(conditions) - 1 and int(conditions[condIndex]) == nrCompleted) else 0
    if sequence[seqIndex] == '?':
        translations = ['.', '#']
    else:
        translations = [sequence[seqIndex]]
    for i in range(len(translations)):
        if translations[i] == '#':
            sum += arrangements(sequence, seqIndex+1, conditions, condIndex, nrCompleted + 1)
        else:
            if nrCompleted > 0:
                if condIndex < len(conditions) and int(conditions[condIndex]) == nrCompleted:
                    sum += arrangements(sequence, seqIndex + 1, conditions, condIndex + 1, 0)
            else:
                sum += arrangements(sequence, seqIndex + 1, conditions, condIndex, 0)
    return sum



with open("test.txt") as f:
    ws = [l.split() for l in f.read().strip().split("\n")]

rows = [(w1, tuple(map(int, w2.split(",")))) for w1, w2 in ws]


print(sum(arrangements(sequence + ".", 0, conditions, 0, 0) for sequence, conditions in rows))

print(sum(arrangements("?".join([sequence] * 5) + ".", 0, conditions * 5, 0, 0) for sequence, conditions in rows))




