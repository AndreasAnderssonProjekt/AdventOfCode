games = open('test.txt')
red = 12
green = 13
blue = 14

sum = 0
powerSum = 0
for game in games:
    [idNr, rounds] = game.split(": ", 1)
    idNr = int(idNr.split(" ")[1])
    rounds = rounds.split("; ")
    impossibleOutcome = False
    largest = [0, 0, 0]
    for r in rounds:
        for component in r.split(", "):
            [nr, color] = component.split(" ", 1)
            nr = int(nr)
            color = color.strip()

            if color == "red":
                if largest[0] < nr:
                    largest[0] = nr
                if red < nr:
                    impossibleOutcome = True

            elif color == "green":
                if largest[1] < nr:
                    largest[1] = nr
                if green < nr:
                    impossibleOutcome = True

            else:
                if largest[2] < nr:
                    largest[2] = nr
                if blue < int(nr):
                    impossibleOutcome = True

    if not impossibleOutcome:
        sum += idNr

    powerSum += largest[0] * largest[1] * largest[2]

print(sum)
print(powerSum)
