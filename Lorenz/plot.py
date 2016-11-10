import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import sys

__author__ = 'zumzoom'

def draw_plot(input_file, name):
    x, y, z = [], [], []
    with open(input_file) as f:
        for line in f.readlines():
            xc, yc, zc = [float(it) for it in line.split()]
            x.append(xc)
            y.append(yc)
            z.append(zc)

    print("count: {}".format(len(x)))

    fig = plt.figure()
    fig.suptitle(name, fontsize=20)
    ax = fig.add_subplot(111, projection='3d')
    # ax.scatter(x, y, z, s=2)
    ax.plot(x, y, z)
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')
    plt.show()


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("ERROR: need input file")
        sys.exit(1)
    draw_plot(sys.argv[1], sys.argv[1])
