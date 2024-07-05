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

    if a == w:
        print(f"w: {w}\nin: {inputa}\nprofit: {profita}\n")
    elif a == d:
        print(f"d: {d}\nin: {inputa}\nprofit: {profita}\n")
    elif a == l:
        print(f"l: {l}\nin: {inputa}\nprofit: {profita}\n")

    if b == w:
        print(f"w: {w}\nin: {inputb}\nprofit: {profitb}\n")
    elif b == d:
        print(f"d: {d}\nin: {inputb}\nprofit: {profitb}\n")
    elif b == l:
        print(f"l: {l}\nin: {inputb}\nprofit: {profitb}\n")


invest(3.05, 3.40, 1.97, 100)
