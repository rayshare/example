# w: 胜赔 d: 平赔 l: 负赔 s: 总投入
def invest(w, d, l, s):
    _var1 = sorted([w, d, l], reverse=True)
    a = _var1[0]
    b = _var1[1]
    c = _var1[2]

    pa = 1 / a
    pb = 1 / b
    pc = 1 / c
    psum = pa + pb + pc

    pa = pa / psum
    pb = pb / psum
    pc = pc / psum

    print(f"P: {1 / w / psum} {1 / d / psum} {1 / l / psum}")
    print(f"R: {psum - 1}")

    invc = s * pc

    pab = pa + pb
    _pa = pb / pab
    _pb = pa / pab

    inputa = s * pa + invc * _pa
    inputb = s * pb + invc * _pb
    inva = inputa * a
    invb = inputb * b

    profita = inva - s
    profitb = invb - s

    lable = None
    odds = None
    if a == w:
        lable = "W"
        odds = w
    elif a == d:
        lable = "D"
        odds = d
    elif a == l:
        lable = "L"
        odds = l
    print(f"{lable}: {odds} I: {inputa} GET: {profita}")

    if b == w:
        lable = "W"
        odds = w
    elif b == d:
        lable = "D"
        odds = d
    elif b == l:
        lable = "L"
        odds = l
    print(f"{lable}: {odds} I: {inputb} GET: {profitb}")
    print()
