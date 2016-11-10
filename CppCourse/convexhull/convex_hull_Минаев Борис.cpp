#include "point.cpp"
#include <vector>
#include <algorithm>

using namespace std;

class convex_hull_builder {
private: 
	static long long dist(point p1, point p2, point p3) {
		long long x1 = p2.x - p1.x;
		long long y1 = p2.y - p1.y;
		long long x2 = p3.x - p1.x;
		long long y2 = p3.y - p1.y;
		return -(x1 * y2 - x2 * y1);
	}

	static bool clockwise(point p1, point p2, point p3) {
		return dist(p1, p2, p3) > 0;
	}

	static void quick_hull(vector <point> &points, vector <int> &hull, int left, int right, vector <int> &s) {
		if (s.size() == 0) {
			hull.push_back(right);
			return;
		}	
		int top = s[0];
		for (int i = 0; i < s.size(); i++) {
			long long d1 = dist(points[left], points[s[i]], points[right]);
			long long d2 = dist(points[left], points[top], points[right]);
			if (d2 < d1 || (d1 == d2 && clockwise(points[left], points[s[i]], points[top])))
				top = s[i];
		}
		vector <int> s1;
		for (int i = 0; i < s.size(); i++) {
			if (s[i] != left && s[i] != right && s[i] != top) {
				if (clockwise(points[left], points[s[i]], points[top])) s1.push_back(s[i]);
			}
		}
		quick_hull(points, hull, left, top, s1);
		vector <int> s2;
		for (int i = 0; i < s.size(); i++) {
			if (s[i] != left && s[i] != right && s[i] != top) {
				if (clockwise(points[top], points[s[i]], points[right])) s2.push_back(s[i]);
			}
		}
		quick_hull(points, hull, top, right, s2);
	}
public:
	static vector <point> convex_hull(vector <point> points) {
		int left = 0, right = 0;
		for (int i = 0; i < points.size(); i++) {
			if (points[i] < points[left]) left = i;
			if (points[right] < points[i]) right = i; 
		}
		vector <int> upper;
		vector <int> lower;
		for (int i = 0; i < points.size(); i++) {
			if (i != left && i != right) {
				if (clockwise(points[left], points[i], points[right])) upper.push_back(i);
				if (clockwise(points[right], points[i], points[left])) lower.push_back(i);
			}
		}
		vector <int> hull;
		quick_hull(points, hull, left, right, upper);
		quick_hull(points, hull, right, left, lower);
		vector <point> result;
		for (int i = 0; i < hull.size(); i++)
			result.push_back(points[hull[i]]);
		reverse(result.begin(), result.end());
		return result;
	}

};