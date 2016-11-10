from abstract_scheme import AbstractScheme


class ImplicitDownstream(AbstractScheme):
    def do_iteration(self, left_temper, right_temper):
        n = len(self.temp)
        a = [-self.r for _ in range(n - 1)]
        b = [1 - self.s + 2 * self.r for _ in range(n)]
        c = [self.s - self.r for _ in range(n - 1)]
        d = self.temp[:]

        c[0] = 0
        b[0] = 1
        a[n - 2] = 0
        b[n - 1] = 1
        d[0] = left_temper
        d[n - 1] = right_temper

        self.temp = solve_tridiagonal(a, b, c, d)
        return self.temp


def solve_tridiagonal(a, b, c, d):
    n = len(d)
    for i in range(n - 1):
        d[i + 1] -= d[i] * a[i] / b[i]
        b[i + 1] -= c[i] * a[i] / b[i]
    for i in reversed(range(n - 1)):
        d[i] -= d[i + 1] * c[i] / b[i + 1]
    return [d[i] / b[i] for i in range(n)]
