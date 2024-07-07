#真实概率计算
def P(w, d, l):

    _pw = 1 / w
    _pd = 1 / d
    _pl = 1 / l
    psum = _pw + _pd + _pl

    pw = _pw / psum
    pd = _pd / psum
    pl = _pl / psum

    return (round(pw, 2), round(pd, 2), round(pl, 2), round(psum - 1, 2))

# pc all in A
def InvestA(pa, pb, pc, S):
    a = S * pa
    b = S * pb
    c = S * pc
    return (a + c, b)

# pc all in B
def InvestB(pa, pb, pc, S):
    a = S * pa
    b = S * pb
    c = S * pc
    return (a, b + c)

# pc all in AB
def InvestAB(pa, pb, pc, S):
    a = S * pa
    b = S * pb
    c = S * pc
    rateA = a / (a + b)
    rateB = b / (a + b)
    return (a + c * (rateA), b + c * (rateB))

# pc all in BA
def InvestBA(pa, pb, pc, S):
    a = S * pa
    b = S * pb
    c = S * pc
    rateA = a / (a + b)
    rateB = b / (a + b)
    return (round(a + c * (rateB)), round(b + c * (rateA)))
