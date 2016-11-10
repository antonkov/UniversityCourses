#define BOOST_TEST_MODULE convex_hull_test
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MAIN
#include <boost/test/unit_test.hpp>

#include "point.cpp"
#include "convex_hull.cpp"

#include <algorithm>
#include <cassert>
#include <cstdlib>
#include <vector>
#include <ctime>
#include <string>
#include <utility>
#include <iostream>

using namespace std;

bool same(vector <point> a, vector <point> b) {
	if (a.size() != b.size()) return false;
	sort(a.begin(), a.end());
	sort(b.begin(), b.end());
	for (int i = 0; i < a.size(); i++)
		if (a[i] != b[i]) return false;
	return true;
}

int string_to_int(string s) {
	if (s[0] == '-') return -string_to_int(s.substr(1, s.size()));
	int res = 0;
	int mul = 1;
	for (int i = s.size() - 1; i >= 0; i--) {
		res += (s[i] - '0') * mul;
		mul *= 10;
	}
	return res;
}

vector<point> string_to_vector(string s) {
	// "(-44, 54), (435, 444), (-44, 2321)"
	vector<point> res;
	int last_open = -1, x = 0, y = 0;
	for (int i = 0; i < s.size(); i++) {
		if (s[i] == '(') {
			last_open = i + 1;
		} else {
			if (s[i] == ',') {
				x = string_to_int(s.substr(last_open, i - last_open));
				last_open = i + 2;
			} else {
				if (s[i] == ')') {
					y = string_to_int(s.substr(last_open, i - last_open));
					res.push_back(point(x, y));
				}
			}
		}
	}
	return res;
}

struct angle {
	long long  a, b;
};

bool operator < (const angle & p, const angle & q) {
	if (p.b == 0 && q.b == 0)
		return p.a < q.a;
	return p.a * 1ll * q.b < p.b * 1ll * q.a;
}

long long sq (point & a, point & b, point & c) {
	return a.x*1ll*(b.y-c.y) + b.x*1ll*(c.y-a.y) + c.x*1ll*(a.y-b.y);
}

bool check(vector<point> hull, vector<point> points) {
	// Points need to be sorted in conterclockwise order
	bool errors = false;

	for (int i = 0; i < hull.size(); i++) {
		point p1 = hull[i];
		point p2 = hull[(i + 1) % hull.size()];
		point p3 = hull[(i + 2) % hull.size()];
		long long x1 = p2.x - p1.x;
		long long x2 = p3.x - p1.x;
		long long y1 = p2.y - p1.y;
		long long y2 = p3.y - p1.y;
		if (x1 * y2 == x2 * y1) {
			errors = true;
			cout << "Too much points in convex hull\n";
		}
	}
	
	int n = hull.size();
	int zero_id = 0;
	for (int i = 0; i < n; ++i) {
		if (hull[i].x < hull[zero_id].x || hull[i].x == hull[zero_id].x && hull[i].y < hull[zero_id].y)
			zero_id = i;
	}
	point zero = hull[zero_id];
	rotate (hull.begin(), hull.begin()+zero_id, hull.end());
	hull.erase (hull.begin());
	--n;

	vector<angle> a (n);
	for (int i = 0; i < n; ++i) {
		a[i].a = hull[i].y - zero.y;
		a[i].b = hull[i].x - zero.x;
		if (a[i].a == 0)
			a[i].b = a[i].b < 0 ? -1 : 1;
	}

	for (int i = 0; i < points.size(); i++) {
	  point q = points[i];
	  bool in = false;
		if (q.x >= zero.x)
			if (q.x == zero.x && q.y == zero.y)
				in = true;
			else {
				angle my = { q.y-zero.y, q.x-zero.x };
				if (my.a == 0)
					my.b = my.b < 0 ? -1 : 1;
				vector<angle>::iterator it = upper_bound (a.begin(), a.end(), my);
				if (it == a.end() && my.a * a[n-1].b == a[n-1].a * my.b)
					it = a.end()-1;
				if (it != a.end() && it != a.begin()) {
					int p1 = int (it - a.begin());
					if (sq (hull[p1], hull[p1-1], q) <= 0)
						in = true;
				}
			}
	  if (!in) {
		errors = true;
		cout << "Some points are outside of convex hull\n"; 
	  }

	}

	return errors;
}

bool random_test(int number_test_cases, int max_coord, int number_points) {
	max_coord *= 2;
	bool errors = false;

	for (int test = 0; test < number_test_cases; test++) {
		vector <point> points;
		points.push_back(point(0, 0));
		points.push_back(point(1, 0));
		points.push_back(point(0, 1));
		for (int i = 0; i < number_points; i++) {
			points.push_back(point(rand() % max_coord - max_coord / 2, rand() % max_coord - max_coord / 2));
		}
		vector <point> hull = convex_hull_builder::convex_hull(points);
		if (check(hull, points)) {
			cout << "Some errors here...\n";
			errors = true;
		}
	}

	return !errors;
}

// E-MAXX CHECKING FAIL
BOOST_AUTO_TEST_CASE(EMAXX_TEST_FAIL)
{
	vector<point> task = string_to_vector("(-10, 6), (-9, -6), (-8, -8), (-8, 8), (-7, 9), (-6, -9), (-3, -10), (7, -10), (9, -9), (9, 9)");
	BOOST_CHECK(!check(convex_hull_builder::convex_hull(task), task));
}

// checking for using more than two points on one line of convex hull
BOOST_AUTO_TEST_CASE(VERY_CLOSE_POINTS)
{
	srand(time(0));
	BOOST_CHECK(random_test(10000, 10, 100));
}

// int overflow
BOOST_AUTO_TEST_CASE(INT_OVERFLOW_CHECKING)
{
	BOOST_CHECK(random_test(10, 1000000000, 100));
}

// TL checking on random tests
BOOST_AUTO_TEST_CASE(TL_CHECKING)
{
	BOOST_CHECK(random_test(10, 5000, 100000)); // 10^5 maybe more?
}

// small tests
BOOST_AUTO_TEST_CASE(SMALL_TEST_0)
{
	vector<point> task = string_to_vector("(0, 0), (1, 0), (0, 1)");
	vector<point> answer = string_to_vector("(0, 0), (1, 0), (0, 1)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_1)
{
	vector<point> task = string_to_vector("(0, 0), (0, 2), (1, 2), (1, 4), (0, 4), (0, 8), (1, 6), (-1, 8)");
	vector<point> answer = string_to_vector("(-1, 8), (0, 0), (1, 2), (1, 6), (0, 8)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_2)
{
	vector<point> task = string_to_vector("(0, 0), (2, 2), (3, 1), (4, 2), (4, 0), (2, 0), (1, 1), (3, 3), (2, 3)");
	vector<point> answer = string_to_vector("(0, 0), (4, 0), (4, 2), (3, 3), (2, 3)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_3)
{
	vector<point> task = string_to_vector("(0, 0), (3, 2), (2, 1), (5, 1), (1, 1), (4, 1)");
	vector<point> answer = string_to_vector("(0, 0), (5, 1), (3, 2), (1, 1)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_4)
{
	//with two same points
	vector<point> task = string_to_vector("(0, 0), (2, 1), (3, 4), (5, 4), (5, 2), (6, 7), (6, 7), (8, 7)");
	vector<point> answer = string_to_vector("(0, 0), (5, 2), (8, 7), (6, 7), (3, 4)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_5)
{
	vector<point> task = string_to_vector("(0, 0), (4, 4), (3, 0), (3, 2), (2, 2), (5, 2), (6, 2), (4, 1), (1, 1), (3, 1)");
	vector<point> answer = string_to_vector("(0, 0), (3, 0), (6, 2), (4, 4)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_6)
{
	vector<point> task = string_to_vector("(0, 0), (5, 0), (0, 5), (1, 1)");
	vector<point> answer = string_to_vector("(0, 0), (5, 0), (0, 5)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_7)
{
	vector<point> task = string_to_vector("(0, 0), (0, 2), (2, 0), (2, 2)");
	vector<point> answer = string_to_vector("(0, 0), (0, 2), (2, 0), (2, 2)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_8)
{
	vector<point> task = string_to_vector("(0, 0), (-2, -2), (2, 2), (-2, 2), (2, -2)");
	vector<point> answer = string_to_vector("(-2, -2), (2, 2), (-2, 2), (2, -2)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}

BOOST_AUTO_TEST_CASE(SMALL_TEST_9)
{
	vector<point> task = string_to_vector("(0, 0), (1, 1), (1, 4), (2, 1), (1, 0), (3, 0), (2, 0)");
	vector<point> answer = string_to_vector("(0, 0), (3, 0), (1, 4)");
	vector<point> algorithm_answer = convex_hull_builder::convex_hull(task);
	BOOST_CHECK(same(answer, algorithm_answer)); 
}