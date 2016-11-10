import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class StupidDynamicORQ {
	TreeMap<Point, Integer> ptsCnt;

	public StupidDynamicORQ(ArrayList<Point> pts) {
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

	public void insert(Point p) {
		if (!ptsCnt.containsKey(p)) {
			ptsCnt.put(p, 0);
		}
		ptsCnt.put(p, ptsCnt.get(p) + 1);
	}

	public void erase(Point p) {
		int cnt = 0;
		if (ptsCnt.containsKey(p)) {
			cnt = ptsCnt.get(p) - 1;
		}
		cnt = Math.max(cnt, 0);
		ptsCnt.put(p, cnt);
	}

	public ArrayList<Point> getPoints(Point l, Point r) {
		ArrayList<Point> res = new ArrayList<>();
		for (Point p : ptsCnt.keySet()) {
			int cnt = ptsCnt.get(p);
			if (l.x <= p.x && l.y <= p.y && p.x < r.x && p.y < r.y) {
				for (int i = 0; i < cnt; i++) {
					res.add(p);
				}
			}
		}
		return res;
	}
}
