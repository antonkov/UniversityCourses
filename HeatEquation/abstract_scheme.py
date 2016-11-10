import abc


class AbstractScheme:
    __metaclass__ = abc.ABCMeta

    def __init__(self, init_temper, s, r):
        self.temp = init_temper[:]
        self.s = s
        self.r = r

    @abc.abstractmethod
    def do_iteration(self, left_temper, right_temper):
        pass