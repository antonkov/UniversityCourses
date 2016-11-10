# encoding: utf-8

import bisect
import random
import time
from math import fabs

import matplotlib.pyplot as plt


class Predicate:
    def prepare(self):
        pass

    def __call__(self, value):
        pass


class IterationPredicate(Predicate):
    def __init__(self, iterations):
        self.iterations = iterations
        self.cur = 0

    def prepare(self):
        self.cur = 0

    def __call__(self, value):
        self.cur += 1
        return self.cur > self.iterations


class PrecisionPredicate(Predicate):
    def __init__(self, true_value, precision):
        self.true_value = true_value
        self.precision = precision
        self.max_iterations = 100

    def prepare(self):
        self.max_iterations = 100

    def __call__(self, value):
        self.max_iterations -= 1
        return fabs(self.true_value - value) < self.precision or self.max_iterations <= 0


class StagnationPredicate(Predicate):
    STAGNATION_ITERATIONS = 5

    def __init__(self):
        self.prev = None
        self.iter_remaining = self.STAGNATION_ITERATIONS
        self.max_iterations = 100

    def prepare(self):
        self.prev = None
        self.iter_remaining = self.STAGNATION_ITERATIONS
        self.max_iterations = 100

    def __call__(self, value):
        if value == self.prev:
            self.iter_remaining -= 1
        else:
            self.iter_remaining = self.STAGNATION_ITERATIONS
        self.prev = value
        self.max_iterations -= 1
        return self.iter_remaining <= 0 or self.max_iterations <= 0


def get_probabilities(population_size):
    def get_probability(i, pop_size):
        return (1.5 - i / (pop_size - 1)) / pop_size

    if population_size == 1:
        return [1]

    probabilities = [get_probability(0, population_size)]
    for j in range(1, population_size):
        probabilities.append(probabilities[-1] + get_probability(j, population_size))
    probabilities[-1] = 1

    return probabilities


def get_index(probabilities):
    prob = random.random()
    return bisect.bisect(probabilities, prob)


def measure(iterations, func, expected_value):
    total_time = 0
    populations_needed = 0
    mistake = 0
    for _ in range(iterations):
        t = time.time()
        populations = func()
        total_time += time.time() - t
        populations_needed += len(populations)
        mistake += fabs(populations[-1][0].value - expected_value)

    return total_time / iterations, populations_needed / iterations, mistake / iterations


def test(create_algorithm, population_size_default, c_prob_default, m_prob_default, iterations, expected_value, color, label):
    points = []
    print("Populations...")
    for population_size in range(5, 101, 5):
        algorithm = create_algorithm(population_size, c_prob_default, m_prob_default)
        points.append(measure(iterations, algorithm.run, expected_value))
        print(population_size, end=' ', flush=True)
    print()

    x = [i for i in range(5, 101, 5)]
    y = [points[i][0] * 1000 for i in range(len(x))]
    plt.figure('population_time')
    plt.title(u'Зависимость времени от популяции')
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    plt.figure('population_pop')
    plt.title(u'Зависимость популяции от популяции')
    y = [points[i][1] for i in range(len(x))]
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    plt.figure('population_err')
    plt.title(u'Зависимость ошибки от популяции')
    y = [points[i][2] for i in range(len(x))]
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    points = []
    print("Crossover...")
    for cross_prob in range(11):
        algorithm = create_algorithm(population_size_default, cross_prob / 10, m_prob_default)
        points.append(measure(iterations, algorithm.run, expected_value))
        print(cross_prob / 10, end=' ', flush=True)
    print()

    x = [i / 10 for i in range(11)]
    y = [points[i][0] * 1000 for i in range(len(x))]
    plt.figure('cross_time')
    plt.title('Зависимость времени от вероятности кроссинговера')
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    plt.figure('cross_pop')
    plt.title('Зависимость популяции от вероятности кроссинговера')
    y = [points[i][1] for i in range(len(x))]
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    plt.figure('cross_err')
    plt.title('Зависимость ошибки от вероятности кроссинговера')
    y = [points[i][2] for i in range(len(x))]
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    points = []
    print("Mutation...")
    for mut_prob in range(11):
        algorithm = create_algorithm(population_size_default, c_prob_default, mut_prob / 10)
        points.append(measure(iterations, algorithm.run, expected_value))
        print(mut_prob / 10, end=' ', flush=True)
    print()

    x = [i / 10 for i in range(11)]
    y = [points[i][0] * 1000 for i in range(len(x))]
    plt.figure('mut_time')
    plt.title('Зависимость времени от вероятности мутации')
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    plt.figure('mut_pop')
    plt.title('Зависимость популяции от вероятности мутации')
    y = [points[i][1] for i in range(len(x))]
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()

    plt.figure('mut_err')
    plt.title('Зависимость ошибки от вероятности мутации')
    y = [points[i][2] for i in range(len(x))]
    plt.plot(x, y, 'k', color=color, label=label)
    plt.legend()