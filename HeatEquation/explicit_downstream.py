from abstract_scheme import AbstractScheme


class ExplicitDownstream(AbstractScheme):
    def do_iteration(self, left_temper, right_temper):
        prev = self.temp[:]
        self.temp[0], self.temp[-1] = left_temper, right_temper
        for i in range(1, len(self.temp) - 1) :
            self.temp[i] = self.r * prev[i - 1] + (1 - 2 * self.r + self.s) * prev[i] + (self.r - self.s) * prev[i + 1];
        return self.temp[:]