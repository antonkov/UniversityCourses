import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Main {
	Random rng = new Random(19);

	boolean equalLists(ArrayList<Point> l0, ArrayList<Point> l1) {
		if (l0.size() != l1.size()) {
			return false;
		}
		for (int i = 0; i < l0.size(); i++) {
			Point p0 = l0.get(i);
			Point p1 = l1.get(i);
			if (p0.yxCompare(p1) != 0) {
				return false;
			}
		}
		return true;
	}
	
	boolean equalListsWithoutOrder(ArrayList<Point> l0, ArrayList<Point> l1) {
		Comparator<Point> comp = new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return o1.xyCompare(o2);
			}
		};
		Collections.sort(l0, comp);
		Collections.sort(l1, comp);
		return equalLists(l0, l1);
	}

	void print(ArrayList<Point> pts) {
		System.out.println(pts.size());
		for (Point p : pts) {
			System.out.println(p.x + " " + p.y);
		}
	}

	Point randPoint(int maxCoord) {
		return new Point(rng.nextInt(maxCoord), rng.nextInt(maxCoord));
	}
	
	void test1D() {
		int countTests = 1000;
		int countPoints = 100;
		int countOperations = 100;
		int countQueries = 100;
		double eraseInsertRate = 0.5;
		int maxCoord = 10;
		int test = 0;
		while (test++ < countTests) {
			ArrayList<Point> pts = new ArrayList<>();
			for (int i = 0; i < countPoints; i++) {
				pts.add(randPoint(maxCoord));
			}
			Collections.sort(pts, new Comparator<Point>() {
				@Override
				public int compare(Point o1, Point o2) {
					return o1.yxCompare(o2);
				}
			});
			BST1D fastTree = new CartesianTreeByY(pts);
			BST1D stupidTree = new StupidBST1D(pts);
			for (int i = 0; i < countOperations; i++) {
				if (rng.nextDouble() < eraseInsertRate && pts.size() != 0) {
					int idx = rng.nextInt(pts.size());
					fastTree.erase(pts.get(idx));
					stupidTree.erase(pts.get(idx));
					pts.remove(idx);
				} else {
					Point p = randPoint(maxCoord);
					pts.add(p);
					fastTree.insert(p);
					stupidTree.insert(p);
				}
			}
			for (int i = 0; i < countQueries; i++) {
				int yl = rng.nextInt(maxCoord + 10), yr = rng.nextInt(maxCoord + 10);
				if (yr < yl) {
					int t = yl;
					yl = yr;
					yr = t;
				}
				ArrayList<Point> ans = fastTree.getPoints(yl, yr);
				ArrayList<Point> rightAns = stupidTree.getPoints(yl, yr);
				if (!equalLists(ans, rightAns)) {
					System.out.println(countTests + " " + countQueries);
					System.out.println(yl + " " + yr);
					print(ans);
					System.out.println("===========");
					print(rightAns);
					throw new AssertionError();
				}
			}
		}
	}

	void timeTest() {
		long start = System.currentTimeMillis();
		int countPoints = 100 * 1000;
		int maxCoord = 1000;
		int countOperations = 100 * 1000;
		int countQueries = 1 * 1000;
		double eraseInsertRate = 0.5;
		ArrayList<Point> pts = new ArrayList<>();
		for (int i = 0; i < countPoints; i++) {
			pts.add(randPoint(maxCoord));
		}
		DynamicORQ tree = new DynamicORQ(pts);
		for (int i = 0; i < countOperations; i++) {
			if (rng.nextDouble() < eraseInsertRate && pts.size() != 0) {
				int idx = rng.nextInt(pts.size());
				tree.erase(pts.get(idx));
				pts.remove(idx);
			} else {
				Point p = randPoint(maxCoord);
				pts.add(p);
				tree.insert(p);
			}
		}
		for (int i = 0; i < countQueries; i++) {
			int x1 = rng.nextInt(maxCoord + 10), x2 = rng.nextInt(maxCoord + 10);
			int y1 = rng.nextInt(maxCoord + 10), y2 = rng.nextInt(maxCoord + 10);
			Point l = new Point(Math.min(x1, x2), Math.min(y1, y2));
			Point r = new Point(Math.max(x1, x2), Math.max(y1, y2));
			tree.getPoints(l, r);
		}
		System.out.println("Time: " + (System.currentTimeMillis() - start));
	}
	
	void test2D() {
		int countTests = 1000;
		int countPoints = 100;
		int countOperations = 100;
		int countQueries = 100;
		double eraseInsertRate = 0.5;
		int maxCoord = 100;
		int test = 0;
		while (test++ < countTests) {
			ArrayList<Point> pts = new ArrayList<>();
			for (int i = 0; i < countPoints; i++) {
				pts.add(randPoint(maxCoord));
			}
			DynamicORQ fastTree = new DynamicORQ(pts);
			StupidDynamicORQ stupidTree = new StupidDynamicORQ(pts);
			for (int i = 0; i < countOperations; i++) {
				if (rng.nextDouble() < eraseInsertRate && pts.size() != 0) {
					int idx = rng.nextInt(pts.size());
					fastTree.erase(pts.get(idx));
					stupidTree.erase(pts.get(idx));
					pts.remove(idx);
				} else {
					Point p = randPoint(maxCoord);
					pts.add(p);
					fastTree.insert(p);
					stupidTree.insert(p);
				}
			}
			for (int i = 0; i < countQueries; i++) {
				int x1 = rng.nextInt(maxCoord + 10), x2 = rng.nextInt(maxCoord + 10);
				int y1 = rng.nextInt(maxCoord + 10), y2 = rng.nextInt(maxCoord + 10);
				Point l = new Point(Math.min(x1, x2), Math.min(y1, y2));
				Point r = new Point(Math.max(x1, x2), Math.max(y1, y2));
				ArrayList<Point> ans = fastTree.getPoints(l, r);
				ArrayList<Point> rightAns = stupidTree.getPoints(l, r);
				if (!equalListsWithoutOrder(ans, rightAns)) {
					System.out.println(test + " " + i);
					for (Point p : pts) {
						System.out.println(p.x + " " + p.y);
					}
					System.out.println("(" + l.x + "," + l.y + ") " + "(" + r.x + "," + r.y + ")");
					print(ans);
					System.out.println("===========");
					print(rightAns);
					throw new AssertionError();
				}
			}
		}
	}

	void run() {
		timeTest();
		//test1D();
		//test2D();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
