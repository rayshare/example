import bet

# 比利时VS斯洛伐克
list = bet.tranfer(2.15, 4.25, 24, 20)
list = sorted(list, key=lambda x: x[3], reverse=True)
for i in range(0, len(list)):
    w = list[i][0]
    d = list[i][1]
    l = list[i][2]
    wd = list[i][3]
    dd = list[i][4]
    ld = list[i][5]
    print(f"{w} {d} {l}")
    print(f"{wd} {dd} {ld}\n")
