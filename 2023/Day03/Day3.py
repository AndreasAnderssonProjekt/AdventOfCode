lines = open('test.txt')
lines = [line.strip() for line in lines]
gears = {}

def isPartNumber(lines, lineNr, start, end):
    for i in range(lineNr - 1, lineNr + 2):
        for j in range(start - 1, end + 2):
            if 0 <= i < len(lines) and 0 <= j < len(lines[i]) and lines[i][j] != '.' and not lines[i][j].isdigit():
                return True
    return False

def addGearNeighbours(lines, lineNr, start, end):
    for i in range(lineNr - 1, lineNr + 2):
        for j in range(start - 1, end + 2):
            if 0 <= i < len(lines) and 0 <= j < len(lines[i]) and lines[i][j] != '.' and not lines[i][j].isdigit():
                if (i,j) in gears:
                    gears[(i,j)].append([lineNr, start, end])
                else:
                    gears[(i,j)] = [[lineNr, start, end]]
    return False

part1 = 0
for lineNr, line in enumerate(lines):
    start = 0
    while start < len(line):
        if not line[start].isdigit():
            start += 1
        else:
            end = start
            while True:
                if end + 1 < len(line) and line[end + 1].isdigit():
                    end += 1
                else:
                    break
            nr = int(line[start:end + 1])
            addGearNeighbours(lines, lineNr, start, end)
            if isPartNumber(lines, lineNr, start, end):
                part1 += nr
            start = end + 1

part2 = 0
for gear in gears:
    if len(gears[gear]) == 2:
        p1 = gears[gear][0]
        p2 = gears[gear][1]
        part2 += int(lines[p1[0]][p1[1]:p1[2]+1]) * int(lines[p2[0]][p2[1]:p2[2]+1])


print("Part 1:", part1)
print("Part 2:", part2)
