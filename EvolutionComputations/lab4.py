import functools
import random
import copy
from enum import Enum
import operator

import commons

M_PROB = 0.2
C_PROB = 0.8
POPULATION_SIZE = 50
ITERATIONS = 10
MAX_DEPTH = 3
DIM = 3


class Node:
    def __init__(self, parent):
        self.parent = parent
        self.dep = 0

    def calc(self, vars):
        return 0.0

    def __repr__(self):
        return 'NODE'


class Constant(Node):
    def __init__(self, parent, val):
        Node.__init__(self, parent)
        self.val = val

    def calc(self, vars):
        return self.val

    def __repr__(self):
        return '' + str(self.val)


class Variable(Node):
    def __init__(self, parent, num):
        Node.__init__(self, parent)
        self.num = num

    def calc(self, vars):
        return vars[self.num]

    def __repr__(self):
        return 'x' + str(self.num)


class UnaryFunction(Node):
    def __init__(self, parent, f, f_repr):
        Node.__init__(self, parent)
        self.f = f
        self.f_repr = f_repr
        self.a = None

    def set_children(self, a):
        self.a = a
        self.dep = a.dep + 1

    def calc(self, vars):
        return self.f(self.a.calc(vars))

    def __repr__(self):
        return self.f_repr + '(' + str(self.a) + ')'


class BinaryFunction(Node):
    def __init__(self, parent, f, f_repr):
        Node.__init__(self, parent)
        self.f = f
        self.f_repr = f_repr
        self.a, self.b = None, None

    def set_children(self, a, b):
        self.a = a
        self.b = b
        self.dep = max(a.dep, b.dep) + 1

    def calc(self, vars):
        return self.f(self.a.calc(vars), self.b.calc(vars))

    def __repr__(self):
        return self.f_repr + '(' + str(self.a) + ', ' + str(self.b) + ')'


def smart_pow(x, y):
    inf = 1e3
    res = 1
    for _ in range(y):
        res *= x
        if res > inf:
            return inf
    return res


unary_functions = [(lambda x, y=pw: smart_pow(x, y), 'pow' + str(pw)) for pw in range(2, DIM + 2)] + [(abs, 'abs')]
binary_functions = [(operator.add, 'add')]


class Terminal(Enum):
    constant = 0
    variable = 1


class Function(Enum):
    unary = 0
    binary = 1

VARS = range(1, DIM + 1)
CONSTANTS = range(1, 2)
LEFT, RIGHT = -1, 1
CONTROL_POINTS = []
CONTROL_POINTS_SIZE = 100


def generate_control_points():
    global CONTROL_POINTS
    CONTROL_POINTS = []
    for _ in range(CONTROL_POINTS_SIZE):
        pt = {}
        for var in VARS:
            pt[var] = random.uniform(LEFT, RIGHT)
        CONTROL_POINTS.append(pt)


def full_init(depth):

    def gen(max_dep, cur_dep, parent):
        if max_dep == cur_dep:
            obj = random.choice(list(Terminal))
            if obj is Terminal.constant:
                return Constant(parent, random.choice(CONSTANTS))
            elif obj is Terminal.variable:
                return Variable(parent, random.choice(VARS))
        elif max_dep == cur_dep + 2:
            p = UnaryFunction(parent, abs, "abs")
            p.set_children(gen(max_dep, cur_dep + 1, p))
            return p
        else:
            obj = random.choice(list(Function))
            if obj is Function.unary and max_dep - cur_dep <= 1:
                fun, fun_repr = random.choice(unary_functions)
                p = UnaryFunction(parent, fun, fun_repr)
                p.set_children(gen(max_dep, cur_dep + 1, p))
                return p
            else:
                fun, fun_repr = random.choice(binary_functions)
                p = BinaryFunction(parent, fun, fun_repr)
                p.set_children(gen(max_dep, cur_dep + 1, p), gen(max_dep, cur_dep + 1, p))
                return p
    return gen(depth, 0, None)


def grow_init():
    pass


def f(vars):
    res = 0.0
    for var in VARS:
        res += abs(vars[var]) ** (var + 1)
    return float(res)


class Individual:
    def __init__(self, chromosome):
        self.chromosome = chromosome
        self.value = self.foo(chromosome)

    @staticmethod
    def foo(tree):
        res = 0.0
        for vars in CONTROL_POINTS:
            real = f(vars)
            actual = tree.calc(vars)
            res += ((real - actual) / (1 + abs(real))) ** 2
        res /= len(CONTROL_POINTS)
        return float(res)


class EvolutionaryAlgorithm:
    def __init__(self, population_size, predicate, c_prob, m_prob):
        generate_control_points()
        self.population_size = population_size
        self.predicate = predicate
        self.c_prob = c_prob
        self.m_prob = m_prob
        self.probabilities = commons.get_probabilities(self.population_size)

    def run(self):
        self.predicate.prepare()

        populations = []

        population = [Individual(full_init(MAX_DEPTH))
                      for _ in range(self.population_size)]
        population.sort(key=lambda individual: individual.value)
        populations.append(population)

        global_min = population[0].value

        while not self.predicate(global_min):
            population = self.get_new_population(population)
            populations.append(population)
            global_min = population[0].value

        return populations

    @staticmethod
    def mutation(tree):
        m_prob_internal = 0.1
        ntree = copy.deepcopy(tree)

        def mutate(node):
            if type(node) is Variable or type(node) is Constant:
                return

            if type(node) is UnaryFunction:
                if random.random() < m_prob_internal:
                    node.f, node.f_repr = random.choice(unary_functions)
                mutate(node.a)
            else:
                if random.random() < m_prob_internal:
                    node.f, node.f_repr = random.choice(binary_functions)
                mutate(node.a)
                mutate(node.b)

        mutate(ntree)
        return ntree

    @staticmethod
    def crossover(initc1, initc2):
        #print("-----------------")
       # print(initc1, initc2)
        c1 = copy.deepcopy(initc1)
        c2 = copy.deepcopy(initc2)

        def go(c, trees):
            if c.parent is not None:
                trees.append(c)
            if type(c) is UnaryFunction:
                go(c.a, trees)
            elif type(c) is BinaryFunction:
                for u in [c.a, c.b]:
                    go(u, trees)

        trees1, trees2 = [], []
        go(c1, trees1)
        go(c2, trees2)
        n1 = random.choice(trees1)
        n2_choose = []
        for c in trees2:
            if c.dep == n1.dep:
                n2_choose.append(c)
        n2 = random.choice(n2_choose)
        #print(n1, n2)

        def change(n1, n2):
            p1 = n1.parent
            if type(p1) is UnaryFunction:
                p1.a = n2
            elif type(p1) is BinaryFunction:
                if p1.a is n1:
                    p1.a = n2
                elif p1.b is n1:
                    p1.b = n2

        if (type(n1) is UnaryFunction or type(n1) is BinaryFunction) and type(n1) is type(n2) and random.choice([True, False]):
            n1.f, n2.f = n2.f, n1.f
            n1.f_repr, n2.f_repr = n2.f_repr, n1.f_repr
        else:
            change(n1, n2)
            change(n2, n1)
        #print(c1, c2)
        return [c1, c2]

    def get_new_population(self, population):
        new_population = []

        for _ in range(self.population_size * 2):
            if random.random() < self.c_prob:
                parent_i = population[commons.get_index(self.probabilities)]
                parent_j = population[commons.get_index(self.probabilities)]
                child_chromosomes = self.crossover(parent_i.chromosome, parent_j.chromosome)
                for child_chromosome in child_chromosomes:
                    if random.random() < self.m_prob:
                        child_chromosome = self.mutation(child_chromosome)
                    new_population.append(Individual(child_chromosome))

        return sorted(population + new_population, key=lambda individual: individual.value)[:len(population)]


def find_global_min(dim):
    global_min = 0
    global_chromosome = None
    for _ in range(100):
        algorithm = EvolutionaryAlgorithm(POPULATION_SIZE, commons.StagnationPredicate(), C_PROB, M_PROB, dim)
        populations = algorithm.run()
        if populations[-1][0].value < global_min:
            global_min = populations[-1][0].value
            global_chromosome = populations[-1][0].chromosome

    return global_min, global_chromosome


def draw():
    algorithm = EvolutionaryAlgorithm(POPULATION_SIZE, commons.IterationPredicate(ITERATIONS), C_PROB, M_PROB)
    populations = algorithm.run()
    example = populations[-1][0]
    print(example.chromosome)
    print(example.value)


if __name__ == '__main__':
    draw()