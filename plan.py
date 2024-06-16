import math

# 计算回本倍数
def plan(start, invest, profit):
    start = int(start)
    profit = int(profit)
    invest = int(invest)
    if start <= 0 and profit <= 0 and profit <= invest:
        raise "Error"
    return math.ceil(start / (profit - invest))

# 计算盈利
def profit(start, invest, profit, multiple):
    start = int(start)
    profit = int(profit)
    invest = int(invest)
    multiple = int(multiple)
    if start <= 0 and profit <= 0 and profit <= invest and multiple <= 0:
        raise "Error"
    bottomMultiple = plan(start, invest, profit)
    if(bottomMultiple < multiple):
        raise "Error"
    return (profit - invest) * multiple - start
