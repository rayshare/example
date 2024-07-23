def P(w, d, l):

    _pw = 1 / w
    _pd = 1 / d
    _pl = 1 / l
    psum = _pw + _pd + _pl

    pw = _pw / psum
    pd = _pd / psum
    pl = _pl / psum

    return (round(pw, 2), round(pd, 2), round(pl, 2), round(psum - 1, 2))
