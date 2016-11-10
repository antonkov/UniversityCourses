import random
import numpy as np
from matplotlib import pyplot as plt
from matplotlib import animation
from implicit_upstream import ImplicitUpstream
from explicit_downstream import ExplicitDownstream
from implicit_downstream import ImplicitDownstream
from explicit_upstream import ExplicitUpstream
from leapfrog import Leapfrog
from matplotlib.widgets import RadioButtons, Slider

x0, x1 = 0, 0.5
t0, t1 = -2, 2

fig = plt.figure()
fig.canvas.set_window_title('Heat equation')
ax = plt.axes(xlim=(x0, x1), ylim=(t0, t1))
line, = ax.plot([], [], lw=2)

# init_temp = [-2 + (0.4 * (i // 2)) for i in range(21)]
init_temp = [random.uniform(-1.5, 1.5) for i in range(101)]
# init_temp = [0.2, 0.3, 0.5, 0.4, 1, 0.1]
animation_interval = 50
s = 0.1
r = 0.1
method_scheme = None
plot_animation = None


def radio_on_click(label):
    global method_scheme
    global plot_animation
    global line

    def animate(i):
        if method_scheme is None:
            return line
        x = np.linspace(x0, x1, len(init_temp))
        y = method_scheme.do_iteration(init_temp[0], init_temp[-1])
        line.set_data(x, y)
        return line

    def init():
        line.set_data([], [])
        return line

    d = {"Explicit Downstream": ExplicitDownstream,
         "Explicit Upstream": ExplicitUpstream,
         "Implicit Downstream": ImplicitDownstream,
         "Implicit Upstream": ImplicitUpstream,
         "Leapfrog": Leapfrog}
    if plot_animation is None:
        plot_animation = animation.FuncAnimation(fig, animate, init_func=init, interval=animation_interval, blit=False)

    method_scheme = d[label](init_temp, s, r)
    plt.draw()

rax = plt.axes([0.05, 0.7, 0.15, 0.15])
radio = RadioButtons(rax, ("Explicit Downstream", "Explicit Upstream", "Implicit Downstream",
                           "Implicit Upstream", "Leapfrog"))


def make_temp_sliders(index, ax, label, valmin, valmax):
    def slider_on_change(val):
        init_temp[index] = val
    t_slider = Slider(ax, label, valmin, valmax, valinit = init_temp[index])
    t_slider.on_changed(slider_on_change)
    return t_slider

left_t_slider = make_temp_sliders(0, plt.axes([0.05, 0.5, 0.15, 0.03]), "Left temp", -2, 2)
right_t_slider = make_temp_sliders(-1, plt.axes([0.05, 0.6, 0.15, 0.03]), "Right temp", -2, 2)

radio.on_clicked(radio_on_click)
plt.subplots_adjust(left=0.3)


def change_r(val):
    global r
    r = val


def change_s(val):
    global s
    s = val

axs = plt.axes([0.05, 0.4, 0.15, 0.03])
axr = plt.axes([0.05, 0.3, 0.15, 0.03])

r_slide = Slider(axs, 'R', 0, 2)
s_slide = Slider(axr, 'S', 0, 2)

r_slide.on_changed(change_r)
s_slide.on_changed(change_s)

radio.on_clicked(radio_on_click)
plt.subplots_adjust(left=0.3)
plt.show()
