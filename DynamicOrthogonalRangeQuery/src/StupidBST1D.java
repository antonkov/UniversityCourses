import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class StupidBST1D implements BST1D {
	TreeMap<Point, Integer> ptsCnt;

	public StupidBST1D(ArrayList<Point> pts) {
		ptsCnt = new TreeMap<>(new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return o1.yxCompare(o2);
			}
		});
		for (Point p : pts) {
			insert(p);
		}
	}

	@Override
	public void insert(Point p) {
		if (!ptsCnt.containsKey(p)) {
			ptsCnt.put(p, 0);
		}
		ptsCnt.put(p, ptsCnt.get(p) + 1);
	}

	@Override
	public void erase(Point p) {
		int cnt = 0;
		if (ptsCnt.containsKey(p)) {
			cnt = ptsCnt.get(p) - 1;
		}
		cnt = Math.max(cnt, 0);
		ptsCnt.put(p, cnt);
	}

	@Override
	public ArrayList<Point> getPoints(int l, int r) {
		Point yl = new Point(Integer.MIN_VALUE, l);
		Point yr = new Point(Integer.MIN_VALUE, r);
		ArrayList<Point> res = new ArrayList<>();
		for (Point p : ptsCnt.keySet()) {
			int cnt = ptsCnt.get(p);
			if (p.yxCompare(yl) >= 0 && p.yxCompare(yr) < 0) {
				for (int i = 0; i < cnt; i++) {
					res.add(p);
				}
			}
		}
		return res;
	}
	
	@Override
	public ArrayList<Point> getAllPoints() {
		ArrayList<Point> res = new ArrayList<>();
		for (Point p : ptsCnt.keySet()) {
			int cnt = ptsCnt.get(p);
			for (int i = 0; i < cnt; i++) {
				res.add(p);
			}
		}
		return res;
	}
}
