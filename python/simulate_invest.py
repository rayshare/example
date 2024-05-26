import random


def dice():
    n = int(random.random() * 10)
    return n % 2 == 0

# final
s = 0
# start
start = 10
# multiply
m = 1
# expect rate
rate = 1.56
# all input
all = 0
# days
n = 1000

i = 0
while i < n:
    a = m * start

    if s < a:
        if s > 0:
            all = all + a - s
        else:
            all = all + a

    if dice():
        s = s + a * rate
        m = 1
    else:
        s = s - a
        m = m + 1

    if i == n - 1 and s <= 0:
        n = n + 1
    i = i + 1

print(n)
print(s)
print(all)
print(s / all)
print(s / all / n)
