lines = open('test.txt')

part1 = 0
part2 = 0
copies = {}
cardNr = 1
for line in lines:
    if cardNr in copies:
        copies[cardNr] += 1
    else:
        copies[cardNr] = 1

    [winNrs, yourNrs] = [line for line in line.split(":", 1)[1].strip().split("|", 1)]
    winNrs = winNrs.split()
    yourNrs = yourNrs.split()
    matches = 0
    for nr in yourNrs:
        if nr in winNrs:
            matches += 1

    for card in range(cardNr + 1, cardNr + 1 + matches):
        if card in copies:
            copies[card] += copies[cardNr]
        else:
            copies[card] = copies[cardNr]

    if matches != 0:
        part1 += 2**(matches-1)
    cardNr += 1

for card in copies:
    if card < cardNr:
        part2 += copies[card]
        
print("Part 1:", part1)
print("Part 2:", part2)

