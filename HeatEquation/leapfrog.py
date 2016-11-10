from abstract_scheme import AbstractScheme
from implicit_downstream import ImplicitUpstream


class Leapfrog(AbstractScheme):
    def __init__(self, init_temper, s, r):
        AbstractScheme.__init__(self, init_temper, s, r)
        helper = ImplicitUpstream(init_temper, s, r)
        self.temp = helper.do_iteration(init_temper[0], init_temper[-1])
        self.prev_temp = init_temper[:]

    def do_iteration(self, left_temper, right_temper):
        new_temper = [None] * len(self.temp)
        new_temper[0], new_temper[-1] = left_temper, right_temper
        for i in range(1, len(new_temper) - 1):
            new_temper[i] = (self.prev_temp[i] - self.s * (self.temp[i+1] - self.temp[i-1]) +
                             2. * self.r * (self.temp[i+1] + self.temp[i-1] - self.prev_temp[i])) /\
                            (1. + 2. * self.r)

        self.prev_temp = self.temp
        self.temp = new_temper

        return self.temp
