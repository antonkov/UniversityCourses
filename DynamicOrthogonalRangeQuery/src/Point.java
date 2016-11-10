public class Point {
	int x, y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	int xyCompare(Point p) {
		if (x != p.x) {
			return Integer.compare(x, p.x);
		}
		return Integer.compare(y, p.y);
	}
	
	int yxCompare(Point p) {
		if (y != p.y) {
			return Integer.compare(y, p.y);
		}
		return Integer.compare(x, p.x);
	}
}