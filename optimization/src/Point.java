/**
 * Created by Borys Minaiev on 10.03.2015.
 */
public class Point implements Comparable<Point> {
    double x, y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point o) {
        if (y != o.y)
            return Double.compare(y, o.y);
        return Double.compare(x, o.x);
    }
}
