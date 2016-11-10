import java.util.ArrayList;

public interface BST1D {
	void insert(Point p);
	void erase(Point p);
	ArrayList<Point> getPoints(int l, int r);
	ArrayList<Point> getAllPoints();
}