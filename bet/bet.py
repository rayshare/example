import math

# 求出保底倍数
def current(primary, bottom, multiple):
    if primary <= 1 and bottom <= 1 and multiple <= 0:
        raise "Error"
    multiple = int(multiple)
    _multiple = math.ceil(multiple / (bottom - 1))

    if (primary-1) * multiple - _multiple < 0:
        raise "Error"
    return _multiple

#半全场转换基础赔率
def tranfer(w, d, l, multiple):
    len = multiple
    list = []
    for i in range(1, len - 1):
        for j in range(1, len - i):
            k = len - i - j
            wd = (w*i) / len
            dd = (d*j) / len
            ld = (l*k) / len
            if wd > 1 and  dd > 1 and  ld > 1:
                list.append((i,j,k,wd,dd,ld))
    return list

