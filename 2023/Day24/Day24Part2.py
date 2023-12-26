import z3

hailstones = [tuple(map(int, line.replace("@", ",").split(","))) for line in open("test.txt")]

rock = z3.RealVector('r', 6)
time = z3.RealVector('t', 3)
s = z3.Solver()
s.add(*[rock[d] + rock[d+3] * t == hail[d] + hail[d+3] * t
        for t, hail in zip(time, hailstones) for d in range(3)])
s.check()
part_2 = (s.model().eval(sum(rock[:3]))
print(part_2)
