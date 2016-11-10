#include <iostream>
#include <fstream>
#include <vector>
#include <cmath>
#include <stdexcept>

using namespace std;

struct point
{
    double x, y, z;
    point()
    {}

    point(double x, double y, double z)
        : x(x), y(y), z(z)
    {}

    point operator*(double b)
    {
        return point(b * x, b * y, b * z);
    }

    point operator+(point p2)
    {
        return point(x + p2.x, y + p2.y, z + p2.z);
    }

    friend istream & operator>>(istream & in, point & p)
    {
        return in >> p.x >> p.y >> p.z;
    }

    friend ostream & operator<<(ostream & out, point & p)
    {
        return out << p.x << " " << p.y << " " << p.z;
    }
};

double const sigma = 10, b = 8.0 / 3;
point p0;
double r, step;
int count_points;

point euler_next_point(point q)
{
    return q + point(sigma * (q.y - q.x),
                     q.x * (r - q.z) - q.y,
                     q.x * q.y - b * q.z) * step;
}

vector<point> explicit_euler()
{
    vector<point> pts(1, p0);
    while (pts.size() < count_points)
        pts.push_back(euler_next_point(pts.back()));
    return pts;
}

vector<double> gauss(vector<vector<double>> matrix)
{
    double const eps = 1e-13;
    int n = matrix.size();
    for (int i = 0; i < n; i++)
    {
        int pivot = i;
        for (int j = i + 1; j < n; j++)
            if (fabs(matrix[j][i]) > fabs(matrix[pivot][i]))
                pivot = j;
        if (fabs(matrix[pivot][i]) <= eps)
            throw std::runtime_error("0 remains in gauss");
        for (int j = 0; j < n + 1; j++)
            swap(matrix[i][j], matrix[pivot][j]);
        double divide = matrix[i][i];
        for (int j = 0; j < n + 1; j++)
            matrix[i][j] /= divide;
        for (int j = 0; j < n; j++)
            if (i != j)
            {
                double mul = matrix[j][i];
                for (int k = 0; k < n + 1; k++)
                    matrix[j][k] -= matrix[i][k] * mul;
            }
    }
    vector<double> result;
    for (size_t i = 0; i < matrix.size(); ++i)
        result.push_back(matrix[i].back());
    return result;
}

point newtone(point q)
{
    for (size_t i = 0; i < 5; ++i)
    {
        vector<vector<double>> Jacobian_xi =
            {{1 + step * sigma,  -step * sigma,       0        },
             {step * (q.z - r),   1 + step    ,   step * q.x   },
             {   -step * q.y  ,   -step * q.x ,   1 + step * b }};
        point F_xi(euler_next_point(q));
        auto a(Jacobian_xi);
        a[0].push_back(F_xi.x - q.x);
        a[1].push_back(F_xi.y - q.y);
        a[2].push_back(F_xi.z - q.z);
        auto res(gauss(a));
        point delta(res[0], res[1], res[2]);
        q = q + delta;
    }
    return q;
}

vector<point> implicit_euler()
{
    vector<point> pts(1, p0);
    try
    {
        while (pts.size() < count_points)
            pts.push_back(newtone(pts.back()));
    } catch (exception & e)
    {
        cout << "implicit_euler fail: " << e.what() << endl;
    }
    return pts;
}

point eval_f(point p) {
    return point(sigma * (p.y - p.x), p.x * (r -  p.z) - p.y, p.x * p.y - b * p.z);
}

vector<point> runge_kutta() {
    vector<point> pts(1, p0);
    double h = step;
    while (pts.size() < count_points) {
        point p  = pts.back();
        
        point f0 = eval_f(p);
        point yt1 = p + f0 * (h / 2);
        point f1 = eval_f(yt1);
        point yt2 = p + f1 * (h / 2);
        point f2 = eval_f(yt2);
        point yt3 = p + f2 * h;
        point f3 = eval_f(yt3);
        point f = (f0 + f1 * 2.0 + f2 * 2.0 + f3) * (1/6.);

        point new_point = p + f * h;
        pts.push_back(new_point);
    }
    return pts;
}

vector<point> adams() {
    vector<point> pts(1, p0);
    while (pts.size() < 4) {
        pts.push_back(euler_next_point(pts.back()));
    }
    while (pts.size() < count_points) {
        point yi = pts[pts.size() - 1];
        point yim1 = pts[pts.size() - 2], yim2 = pts[pts.size() - 3], yim3 = pts[pts.size() - 4];
        point fi = eval_f(yi), fim1 = eval_f(yim1), fim2 = eval_f(yim2), fim3 = eval_f(yim3);
        point yip1 = yi + (fi * 55 + fim1 * 59 + fim2 * 37 + fim3 * (-9)) * (step / 24.);
        point fip1 = eval_f(yip1);
        point correct = yi + (fip1 * 9 + fi * 19 + fim1 * (-5) + fim2) * (step / 24.);

        pts.push_back(correct);
    }
    return pts;
}

void print(string filename, vector<point> const & pts)
{
    ofstream out(filename);
    for (point p : pts)
        out << p << endl;
}

int main()
{
    ifstream in("input");
    in >> p0 >> r >> count_points >> step;

    print("explicit_euler", explicit_euler());
    print("implicit_euler", implicit_euler());
    print("runge_kutta", runge_kutta());
    print("adams", adams());
}
