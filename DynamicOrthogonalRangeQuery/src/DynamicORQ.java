import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DynamicORQ {
	class Tree {
		Point p;
		BST1D ytree;
		Tree l, r;
		int cnt;

		public Tree(Point p, Tree l, Tree r) {
			super();
			this.p = p;
			this.l = l;
			this.r = r;
			update();
			ytree = createTree(p, l, r);
		}

		void update() {
			cnt = 1 + size(l) + size(r);
		}

		boolean balanced() {
			return size(l) >= size(this) / 4 && size(r) >= size(this) / 4;
		}

		private void getAllPoints(Tree t, ArrayList<Point> pts) {
			if (t == null) {
				return;
			}
			getAllPoints(t.l, pts);
			pts.add(t.p);
			getAllPoints(t.r, pts);
		}
		
		Tree rebuild() {
			ArrayList<Point> pts = new ArrayList<Point>();
			getAllPoints(this, pts);
			return build(pts, 0, pts.size());
		}

		// merging two subtrees in linear time.
		private BST1D createTree(Point p, Tree l, Tree r) {
			ArrayList<Point> ls = (l != null) ? l.ytree.getAllPoints() : new ArrayList<>();
			boolean wasP = false;
			ArrayList<Point> rs = (r != null) ? r.ytree.getAllPoints() : new ArrayList<>();
			int lp = 0, rp = 0;
			ArrayList<Point> result = new ArrayList<>();
			while (lp < ls.size() || !wasP || rp < rs.size()) {
				if (lp < ls.size() && (wasP || ls.get(lp).yxCompare(p) <= 0)
						&& (rp == rs.size() || ls.get(lp).yxCompare(rs.get(rp)) <= 0)) {
					result.add(ls.get(lp++));
				} else if (rp < rs.size() && (wasP || rs.get(rp).yxCompare(p) <= 0)
						&& (lp == ls.size() || rs.get(rp).yxCompare(ls.get(lp)) <= 0)) {
					result.add(rs.get(rp++));
				} else {
					result.add(p);
					wasP = true;
				}
			}
			return new CartesianTreeByY(result);
		}
	}

	private int size(Tree t) {
		return t != null ? t.cnt : 0;
	}

	Tree root;

	public DynamicORQ() {
	}

	public DynamicORQ(ArrayList<Point> pts) {
		Collections.sort(pts, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				return o1.xyCompare(o2);
			}
		});
		root = build(pts, 0, pts.size());
	}

	// building structure in O(n logn) time.
	private Tree build(ArrayList<Point> pts, int tl, int tr) {
		if (tr - tl == 0) {
			return null;
		}
		int tm = (tl + tr) / 2;
		Tree l = build(pts, tl, tm);
		Tree r = build(pts, tm + 1, tr);
		return new Tree(pts.get(tm), l, r);
	}

	public void insert(Point p) {
		root = insert(root, p);
	}

	private Tree insert(Tree t, Point p) {
		if (t == null) {
			return new Tree(p, null, null);
		}
		if (p.xyCompare(t.p) < 0) {
			t.l = insert(t.l, p);
		} else {
			t.r = insert(t.r, p);
		}
		t.ytree.insert(p);
		t.update();
		if (!t.balanced()) {
			return t.rebuild();
		}
		return t;
	}

	public void erase(Point p) {
		root = erase(root, p);
	}

	private Tree erase(Tree t, Point p) {
		if (t == null) {
			return null;
		}
		int comp = p.xyCompare(t.p);
		if (comp == 0) {
			if (t.l == null || t.r == null) {
				if (t.l == null) {
					t = t.r;
				} else {
					t = t.l;
				}
				return t;
			}
			Tree leftmost = t.r;
			while (leftmost.l != null) {
				leftmost = leftmost.l;
			}
			t.r = erase(t.r, leftmost.p);
			t.p = leftmost.p;
		} else {
			if (comp < 0) {
				t.l = erase(t.l, p);
			} else {
				t.r = erase(t.r, p);
			}
		}
		t.ytree.erase(p);
		t.update();
		if (!t.balanced()) {
			return t.rebuild();
		}
		return t;
	}

	public ArrayList<Point> getPoints(Point l, Point r) {
		ArrayList<Point> res = new ArrayList<>();
		getPoints(root, l, r, res);
		return res;
	}

	private boolean inSegX(Point p, Point l, Point r) {
		return l.x <= p.x && p.x < r.x;
	}

	private boolean inRect(Point p, Point l, Point r) {
		return l.x <= p.x && l.y <= p.y && p.x < r.x && p.y < r.y;
	}

	// doesn't guarantee any order to returning points.
	private void getPoints(Tree t, Point l, Point r, ArrayList<Point> pts) {
		while (true) {
			if (t == null) {
				return;
			}
			if (l.x <= t.p.x && t.p.x < r.x) {
				if (inRect(t.p, l, r)) {
					pts.add(t.p);
				}
				// go right
				{
					Tree tr = t.r;
					while (tr != null) {
						if (inSegX(tr.p, l, r)) {
							if (inRect(tr.p, l, r)) {
								pts.add(tr.p);
							}
							if (tr.l != null) {
								pts.addAll(tr.l.ytree.getPoints(l.y, r.y));
							}
							tr = tr.r;
						} else {
							tr = tr.l;
						}
					}
				}
				// go left
				{
					Tree tl = t.l;
					while (tl != null) {
						if (inSegX(tl.p, l, r)) {
							if (inRect(tl.p, l, r)) {
								pts.add(tl.p);
							}
							if (tl.r != null) {
								pts.addAll(tl.r.ytree.getPoints(l.y, r.y));
							}
							tl = tl.l;
						} else {
							tl = tl.r;
						}
					}
				}
				return;
			}
			if (t.p.x >= r.x) {
				t = t.l;
			} else if (t.p.x < l.x) {
				t = t.r;
			}
		}
	}
}
