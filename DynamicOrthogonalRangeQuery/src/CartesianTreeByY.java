import java.util.ArrayList;
import java.util.Random;

public class CartesianTreeByY implements BST1D {
	class Tree {
		Point key;
		int prior;
		Tree l, r;

		Tree(Point key) {
			this.key = key;
			this.prior = rng.nextInt();
		}
	}

	final boolean DEBUG = true;
	Tree root;
	Random rng = new Random(19);

	CartesianTreeByY() {
	}

	// build Cartesian Tree in O(n), list of points should be sorted according
	// to compare in (y, x) order.
	CartesianTreeByY(ArrayList<Point> pts) {
		root = build(pts, 0, pts.size());
	}

	private Tree build(ArrayList<Point> pts, int l, int r) {
		// Constructing with all nearest smaller values algorithm
		// described in Efficient construction
		// https://en.wikipedia.org/wiki/Cartesian_tree
		ArrayList<Tree> ts = new ArrayList<>();
		for (Point p : pts) {
			ts.add(new Tree(p));
		}
		Tree root = null;
		Tree[] st = new Tree[ts.size()];
		int sp = 0;
		for (int i = 0; i < ts.size(); i++) {
			Tree t = ts.get(i);
			if (root == null || t.prior > root.prior) {
				root = t;
			}
			while (sp != 0 && st[sp - 1].prior < t.prior) {
				t.l = st[--sp];
			}
			st[sp++] = t;
		}

		sp = 0;
		for (int i = ts.size() - 1; i >= 0; i--) {
			Tree t = ts.get(i);
			while (sp != 0 && st[sp - 1].prior <= t.prior) {
				t.r = st[--sp];
			}
			st[sp++] = t;
		}

		if (DEBUG) {
			// Check correctness
			ArrayList<Tree> checkTs = new ArrayList<>();
			dfs(root, checkTs);
			if (checkTs.size() != ts.size()) {
				throw new AssertionError();
			}
			for (int i = 0; i < ts.size(); i++) {
				Tree a = checkTs.get(i), b = ts.get(i);
				if (a.key.yxCompare(b.key) != 0 || a.prior != b.prior) {
					throw new AssertionError();
				}
			}
		}

		return root;
	}

	private void dfs(Tree v, ArrayList<Tree> ts) {
		if (v == null) {
			return;
		}
		dfs(v.l, ts);
		ts.add(v);
		dfs(v.r, ts);
	}

	private Tree merge(Tree tl, Tree tr) {
		if (tl == null) {
			return tr;
		}
		if (tr == null) {
			return tl;
		}
		if (tl.prior <= tr.prior) {
			tl.r = merge(tl.r, tr);
			return tl;
		} else {
			tr.l = merge(tl, tr.l);
			return tr;
		}
	}

	private Tree[] split(Tree t, Point key) {
		if (t == null) {
			return new Tree[] { null, null };
		}
		if (t.key.yxCompare(key) < 0) {
			Tree[] ts = split(t.r, key);
			t.r = ts[0];
			return new Tree[] { t, ts[1] };
		} else {
			Tree[] ts = split(t.l, key);
			t.l = ts[1];
			return new Tree[] { ts[0], t };
		}
	}

	// erases one copy of point p from tree if it exists.
	@Override
	public void erase(Point p) {
		root = erase(root, p);
	}

	private Tree erase(Tree t, Point key) {
		if (t == null) {
			return t;
		}
		int comp = t.key.yxCompare(key);
		if (comp == 0) {
			return merge(t.l, t.r);
		} else {
			if (comp < 0) {
				t.r = erase(t.r, key);
			} else {
				t.l = erase(t.l, key);
			}
			return t;
		}
	}

	// inserts point p to tree, if it already exists there will be one more copy.
	@Override
	public void insert(Point p) {
		root = insert(root, new Tree(p));
	}

	private Tree insert(Tree t, Tree item) {
		if (t == null) {
			return item;
		}
		if (item.prior > t.prior) {
			Tree[] ts = split(t, item.key);
			item.l = ts[0];
			item.r = ts[1];
			return item;
		} else {
			if (item.key.yxCompare(t.key) < 0) {
				t.l = insert(t.l, item);
			} else {
				t.r = insert(t.r, item);
			}
			return t;
		}
	}
	
	// returns all points in interval [yl, yr).
	@Override
	public ArrayList<Point> getPoints(int yl, int yr) {
		ArrayList<Point> result = new ArrayList<>();
		Point pyl = new Point(Integer.MIN_VALUE, yl);
		Point pyr = new Point(Integer.MIN_VALUE, yr);
		getPoints(root, pyl, pyr, result);
		return result;
	}
	
	// work in linear time
	@Override
	public ArrayList<Point> getAllPoints() {
		ArrayList<Point> res = new ArrayList<>();
		getAllPoints(root, res);
		return res;
	}
	
	private void getAllPoints(Tree t, ArrayList<Point> pts) {
		if (t == null) {
			return;
		}
		getAllPoints(t.l, pts);
		pts.add(t.key);
		getAllPoints(t.r, pts);
	}
	
	private void getPoints(Tree t, Point l, Point r, ArrayList<Point> pts) {
		if (t == null) {
			return;
		}
		int compl = t.key.yxCompare(l);
		int compr = t.key.yxCompare(r);
		if (compl >= 0) {
			getPoints(t.l, l, r, pts);
		}
		if (compl >= 0 && compr < 0) {
			pts.add(t.key);
		}
		if (compr < 0) {
			getPoints(t.r, l, r, pts);
		}
	}
}
