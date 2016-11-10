#ifndef POINT_STRUCTURE
#define POINT_STRUCTURE

struct point {
	long long x, y;
	point(long long x, long long y) : x (x), y(y) {};
	bool operator < (point &p) {
		return (p.x > x || (p.x == x && p.y > y));
	}
	bool operator != (point &p) {
		return (p.x != x || p.y != y);
	}
};

#endif