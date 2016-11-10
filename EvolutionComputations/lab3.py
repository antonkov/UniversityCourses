from tkinter import *
import random
import numpy as np
from math import sqrt
import commons


# Params
pop_size = 100
generation_cnt = 100
p_cross = 0.9
p_mutation = 0.4
default_scale = 10
plot_shift = 5

# Исследуйте зависимость работы ПГА от значения вероятности
# ОК p_cross
# 0.9 - ошибка 14.227383138265282
# 0.8 - ошибка 13.564366155679295
# 0.7 - ошибка 16.663867163227792
# 0.6 - ошибка 19.11137007949435
# 0.5 - ошибка 16.20231748745698
# 0.4 - ошибка 15.795961567152176
# 0.3 - ошибка 29.575007203133055
# 0.2 - ошибка 18.333687984404946
# 0.1 - ошибка 19.798022918351307
# 0.0 - ошибка 1686.4080722859585

class Point:
    def __init__(self, num, x, y):
        self.num = num
        self.x = x
        self.y = y
        self.normx = x
        self.normy = y

    def __repr__(self):
        return "({} {} {})".format(self.num, self.x, self.y)

    @staticmethod
    def distance(p0, p1):
        return sqrt((p0.x - p1.x) ** 2 + (p0.y - p1.y) ** 2)


class Route:
    def __init__(self, tour, points):
        self.tour = tour
        self.points = points
        self.n = tour.copy()
        prev = tour[-1]
        for i in tour:
            self.n[prev] = i
            prev = i
        self.value = self.length()

    def length(self):
        result = 0.0
        prev = self.tour[-1]
        for id in self.tour:
            p0, p1 = self.points[prev], self.points[id]
            result += Point.distance(p0, p1)
            prev = id
        return result

    @staticmethod
    def generate_random_route(points):
        return Route(np.random.permutation(len(points)), points)

    def __repr__(self):
        return "[" + ', '.join(str(v) for v in self.tour) + "] = " + str(self.length())


class OptimalRouteFinder:
    def __init__(self, points, population_size, generation_number, p_cross, p_mutation):
        self.points = points
        self.population_size = population_size
        self.generation_number = generation_number
        self.p_cross = p_cross
        self.p_mutation = p_mutation
        self.probabilities = commons.get_probabilities(population_size)

    def run(self):
        # Result population
        populations = []

        # Initial population
        population = [Route.generate_random_route(self.points) for _ in range(self.population_size)]
        population.sort(key=lambda it: it.value)
        populations.append(population)

        for i in range(self.generation_number):
            population = self.get_new_population(population)
            populations.append(population)

        return populations

    def get_new_population(self, population):
        next_population = []
        for _ in range(self.population_size * 2):
            if random.random() >= self.p_cross:
                continue
            route1 = population[commons.get_index(self.probabilities)]
            route2 = population[commons.get_index(self.probabilities)]
            cur = random.randrange(len(self.points))
            used_vertices = {cur}
            all_vertices = set(range(len(self.points)))
            cur_route = [cur]

            for i in range(len(self.points) - 1):
                candidates = [route1.n[cur], route2.n[cur]]
                if Point.distance(self.points[cur], self.points[candidates[0]]) \
                        > Point.distance(self.points[cur], self.points[candidates[1]]):
                    candidates[0], candidates[1] = candidates[1], candidates[0]
                if all([v in used_vertices for v in candidates]):
                    candidates = [min(all_vertices - used_vertices,
                                               key=lambda vertex: Point.distance(self.points[vertex], self.points[cur]))]
                for vertex in candidates:
                    if vertex not in used_vertices:
                        cur = vertex
                        used_vertices.add(cur)
                        cur_route.append(cur)
                        break

            if random.random() < self.p_mutation:
                for i in range(random.randrange(3)):
                    index1 = random.randrange(len(self.points))
                    index2 = random.randrange(len(self.points))
                    # index2 = (index1 + random.randrange(-10, 10)) % len(self.points)
                    cur_route[index1], cur_route[index2] = cur_route[index2], cur_route[index1]

            next_population.append(Route(cur_route, self.points))

        return sorted(population + next_population, key=lambda route: route.value)[:self.population_size]


def main():
    # Optimal route
    points = read_points()
    optimal_route = read_optimal_route(points)

    # Our algo route
    algo = OptimalRouteFinder(points, pop_size, generation_cnt, p_cross, p_mutation)
    populations = algo.run()
    draw(optimal_route, populations, points)


def draw(optimal_route, populations_history, points):
    master = Tk()
    print("MY BEST RESULT =", populations_history[-1][0].value, ", optimal length =", optimal_route.value)
    print("error =", populations_history[-1][0].value - optimal_route.value)

    def sel(self):
        w.delete("all")
        info.delete('1.0', END)

        selection = "Value = " + str(slider_value.get())
        my_route = populations_history[slider_value.get()][0]
        info.insert(END, "Optimal = {}\nCurrent = {}\n".format(optimal_route.value, my_route.value))
        draw_points(w, points)
        draw_route(w, optimal_route, fill="red", is_dash=True)
        draw_route(w, my_route, fill="green")

    slider_value = IntVar()
    slider = Scale(master, from_=0, to=generation_cnt, orient=HORIZONTAL, variable=slider_value, command=sel)
    slider.pack()

    info = Text(master, height=2, width=30)
    info.pack()

    w = Canvas(master, width=800, height=800)
    w.pack()
    mainloop()


def read_optimal_route(points):
    ans = []
    with open('Eil76.ans', 'r') as f:
        n = int(f.readline())
        for _ in range(n):
            ans.append(int(f.readline()) - 1)
    return Route(ans, points)


def read_points():
    pts = []
    with open('Eil76.tsp', 'r') as f:
        n = int(f.readline())
        for _ in range(n):
            num, x, y = map(float, f.readline().split(' '))
            pts.append(Point(int(num) - 1, x, y))

    minx, maxx = min([p.x for p in pts]), max([p.x for p in pts])
    miny, maxy = min([p.y for p in pts]), max([p.y for p in pts])
    for p in pts:
        p.normx = p.x - minx + plot_shift
        p.normy = p.y - miny + plot_shift

    return pts


def draw_point(w, x, y, sz=3, text=""):
    w.create_oval(x - sz, y - sz, x + sz, y + sz, fill="red")
    w.create_text(x, y, text=text)


def draw_points(w, pts, scale=default_scale):
    for p in pts:
        draw_point(w, p.normx * scale, p.normy * scale)


def draw_route(w, route, scale=default_scale, fill="red", is_dash=False):
    pts = route.points
    prev = route.tour[-1]
    for id in route.tour:
        if prev != -1:
            p0, p1 = pts[prev], pts[id]
            if (is_dash):
                w.create_line(p0.normx * scale, p0.normy * scale, p1.normx * scale, p1.normy * scale, fill=fill,
                              dash=(4, 4))
            else:
                w.create_line(p0.normx * scale, p0.normy * scale, p1.normx * scale, p1.normy * scale, fill=fill)
        prev = id


if __name__ == '__main__':
    main()