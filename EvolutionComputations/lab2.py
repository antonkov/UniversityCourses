import functools
import random
from math import exp, pi, cos, sqrt

import matplotlib.pyplot as plt
import numpy as np
from matplotlib import cm
from matplotlib.widgets import Slider
from mpl_toolkits.mplot3d import Axes3D

import commons

LEFT = -600.0
RIGHT = 600.0
M_PROB = 0.2
C_PROB = 0.8
POPULATION_SIZE = 50
ITERATIONS = 10


class Individual:
    def __init__(self, chromosome):
        self.chromosome = chromosome
        self.value = self.foo(chromosome)

    @staticmethod
    def foo(xs):
        s = sum([xi ** 2 for xi in xs]) / 4000
        p = 1
        for i in range(len(xs)):
            p *= cos(xs[i] / sqrt(i + 1))
        res = s - p + 1
        return res


class EvolutionaryAlgorithm:
    def __init__(self, population_size, predicate, c_prob, m_prob, dim):
        self.population_size = population_size
        self.predicate = predicate
        self.c_prob = c_prob
        self.m_prob = m_prob
        self.dim = dim
        self.probabilities = commons.get_probabilities(self.population_size)

    def run(self):
        self.predicate.prepare()

        populations = []
        population = [Individual([random.uniform(LEFT, RIGHT) for _ in range(self.dim)])
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
    def mutation(chromosome):
        ret = chromosome.copy()
        ret[random.randrange(len(chromosome))] = random.uniform(LEFT, RIGHT)
        return ret

    @staticmethod
    def crossover(c1, c2):
        new_c = []
        for i in range(len(c1)):
            min_gene, max_gene = min(c1[i], c2[i]), max(c1[i], c2[i])
            width = (max_gene - min_gene) * 1.2
            min_gene, max_gene = max(LEFT, max_gene - width), min(RIGHT, min_gene + width)
            new_c.append(random.uniform(min_gene, max_gene))
        return new_c

    def get_new_population(self, population):
        new_population = []

        for _ in range(self.population_size * 2):
            if random.random() < self.c_prob:
                parent_i = population[commons.get_index(self.probabilities)]
                parent_j = population[commons.get_index(self.probabilities)]
                child_chromosome = self.crossover(parent_i.chromosome, parent_j.chromosome)
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


def create_algorithm(population_size, c_prob, m_prob, dim):
    return EvolutionaryAlgorithm(population_size, commons.StagnationPredicate(), c_prob, m_prob, dim)


def draw():
    draw_left_bound = -600.0
    draw_right_bound = 600.0
    count_steps = 100
    draw_step = (draw_right_bound - draw_left_bound) / count_steps
    fig = plt.figure('Evolution')
    ax = fig.gca(projection='3d')
    plt.subplots_adjust(left=0.05, right=1, top=0.95, bottom=0.15)
    plt.axis([draw_left_bound, draw_right_bound, draw_left_bound, draw_right_bound])
    plt.hold(True)

    X, Y = np.meshgrid(np.arange(draw_left_bound, RIGHT, draw_step), np.arange(draw_left_bound, draw_right_bound, draw_step))
    Z = np.array([[Individual.foo([x, y]) for x, y in zip(xs, ys)] for xs, ys in zip(X, Y)])
    surf = ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap=cm.coolwarm, linewidth=0, antialiased=False, alpha=0.3)

    algorithm = EvolutionaryAlgorithm(100, commons.StagnationPredicate(), C_PROB, M_PROB, 2)
    populations = algorithm.run()
    X = [individual.chromosome[0] for individual in populations[0]]
    Y = [individual.chromosome[1] for individual in populations[0]]
    Z = [individual.value for individual in populations[0]]
    plot = ax.scatter(X, Y, Z, s=30, c='black')

    axage = plt.axes([0.1, 0.05, 0.8, 0.03])
    sage = Slider(axage, 'Age', 1, len(populations), valinit=1, valfmt='%d')

    def update(val):
        age = int(val) - 1
        plot._offsets3d = ([individual.chromosome[0] for individual in populations[age]],
                           [individual.chromosome[1] for individual in populations[age]],
                           [individual.value for individual in populations[age]])
        fig.canvas.draw_idle()

    fig.colorbar(surf, shrink=0.5, aspect=5, ax=ax)

    sage.on_changed(update)
    plt.show()


if __name__ == '__main__':
    draw()
    print('Test 2 dimensions')
    create_algorithm_2 = functools.partial(create_algorithm, dim=2)
    commons.test(create_algorithm_2, POPULATION_SIZE, C_PROB, M_PROB, ITERATIONS, find_global_min(2)[0], "b", "2D")
    print('Test 3 dimensions')
    create_algorithm_3 = functools.partial(create_algorithm, dim=3)
    commons.test(create_algorithm_3, POPULATION_SIZE, C_PROB, M_PROB, ITERATIONS, find_global_min(3)[0], "r", "3D")
    plt.show()