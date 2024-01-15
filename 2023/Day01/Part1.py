lines = open('test.txt')

sum = 0
curr = 0
for line in lines:
    for i, c in enumerate(line):
        if c.isdigit():
            curr += 10 * int(c)
            break

    for i, c in enumerate(reversed(line)):
        if c.isdigit():
            curr += int(c)
            break

    sum += curr
    curr = 0

print(sum)



