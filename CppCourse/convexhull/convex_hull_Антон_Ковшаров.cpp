#include "point.cpp"
#include <vector>
#include <algorithm>

using namespace std;

class convex_hull_builder {
private: 
	static bool clockwise(point p1, point p2, point p3) {
		double x1 = p2.x - p1.x, x2 = p3.x - p1.x;
		double y1 = p2.y - p1.y, y2 = p3.y - p1.y;
		return x1 * y2 - x2 * y1 < 0;
	}

public:
	static vector<point> convex_hull(vector<point> p) {
		sort(p.begin(), p.end());
		vector<point> lower, upper;
		for (int i = 0; i < p.size(); i++) {
			while (upper.size() >= 2 && !clockwise(upper[upper.size() - 2], upper[upper.size() - 1], p[i])) upper.pop_back();
			upper.push_back(p[i]);
		}
		upper.pop_back();
		for (int i = p.size() - 1; i >= 0; i--) {
			while (lower.size() >= 2 && !clockwise(lower[lower.size() - 2], lower[lower.size() - 1], p[i])) lower.pop_back();
			lower.push_back(p[i]);
		}
		for (int i = 0; i < lower.size(); i++) upper.push_back(lower[i]);
		upper.pop_back();
		reverse(upper.begin(), upper.end());
		return upper;
	}
};