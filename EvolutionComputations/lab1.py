import matplotlib.pyplot as plt
import numpy as np
from math import *


class Instance:
    def __init__(self, bits):
        self.bits = bits
        self.arg = toArg(bits)
        self.val = f(self.arg)
        self.norm_val = 0


def f(x):
    if abs(x) <= 0.3:
        return 0
    return cos(3 * x - 15) / abs(x)

# зависимость от мощности популяции
# 12 - 9-? итерации (часто в локальный минимум)
# 20 - 9-15 итерации (почти стабильно)
# 30 - 5-10 итераций
# 40 - 5-7 итераций
# Зафиксируем 40 особей

# зависимость от p_c
# < 0.4 - часто расходится
# 0.4 - за 18 итераций
# 0.5 - ~10 итераций
# 0.7 - от 3 итераций (но иногда расходится)
# 0.9 - от 3 итераций

# зависимость от p_m
# 0.9 - 20 итераций
# 0.5 - 10 итераций
# 0.1 - 8 итераций
# 0.01 - 12 итераций

fl, fr = -10, 10
eps = 1e-3
countGenerations = 20
p_mutation = 0.01
p_cross = 0.9
sz = ceil(log((fr - fl) / eps, 2))
n = 40


def toArg(a):
    res = 0
    for x in a:
        res = res * 2 + x
    return fl + res * (fr - fl) / (1 << len(a))

vf = np.vectorize(f)


def calc_fitness(xs):
    minval = min([inst.val for inst in xs])
    sum = 0
    for inst in xs:
        inst.norm_val = inst.val - minval
        sum += inst.norm_val
    for inst in xs:
        inst.norm_val /= sum
    res = sorted(xs, key=lambda x: -x.norm_val)
    return res

def choose_random(a, ps):
    p = np.random.rand() * sum(ps)
    cur = 0
    for inst, prob in zip(a, ps):
        cur += prob
        if cur > p:
            return inst
    return a[-1]

def get_wheel(a, cnt):
    res = []
    for _ in range(cnt):
        res.append(choose_random(a, [inst.norm_val for inst in a]))
    return res

xs = []
for i in range(n):
    xs.append(Instance(np.random.randint(2, size=sz)))

t = np.arange(-10, 10, 0.01)
plt.figure(figsize=(15, 10))

for gen in range(countGenerations):
    plt.subplot(4, 5, gen + 1)
    plt.title("subplot {}".format(gen + 1))
    plt.plot(t, vf(t))
    plt.plot([x.arg for x in xs], [x.val for x in xs], 'r*')

    ans = max([(x.val, x.arg) for x in xs])
    print(ans)
    xs = calc_fitness(xs)
    #print([x.norm_val for x in xs])
    # Crossingover
    # np.random.shuffle(nxs)
    ps = []
    A = 1.5
    B = 2 - A
    for idx, inst in enumerate(xs):
        ps.append(1.0 / n * (A - (A - B) * (idx / (n - 1))))
    nxs = []
    for _ in range(n // 2):
        inst1 = choose_random(xs, ps)
        inst2 = choose_random(xs, ps)
        if np.random.rand() < p_cross:
            k = np.random.randint(sz - 1) + 1
            nbits1 = list(inst1.bits[:k]) + list(inst2.bits[k:])
            nbits2 = list(inst2.bits[:k]) + list(inst1.bits[k:])
            inst1 = Instance(nbits1)
            inst2 = Instance(nbits2)
        nxs.append(inst1)
        nxs.append(inst2)

    # Mutation
    for idx, inst in enumerate(nxs):
        if np.random.rand() < p_mutation:
            k = np.random.randint(sz - 1) + 1
            nbits = inst.bits
            nbits[k] = 1 - nbits[k]
            nxs[idx] = Instance(nbits)
    to_add = int(0.3 * n)
    for _ in range(to_add):
        nxs.append(Instance(np.random.randint(2, size=sz)))
    nxs = calc_fitness(nxs)
    nxs = get_wheel(nxs, n - 1)
    nxs.append(xs[0])
    xs = nxs

plt.show()
