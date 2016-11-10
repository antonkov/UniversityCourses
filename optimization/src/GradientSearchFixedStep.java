import java.util.ArrayList;
import java.util.List;

/**
 * Created by Borys Minaiev on 10.03.2015.
 */
public class GradientSearchFixedStep {
    final static double eps = 1e-7;
    final static int MAX_ITER_COUNT = 100000;

    public static List<Point> searchMinimum(Function2D f, double step) {
        Point start = new Point(f.getStartX(), f.getStartY());
        ArrayList<Point> res = new ArrayList<Point>();
        res.add(start);
        int iters = 0;
        while (true) {
            Point cur = res.get(res.size() - 1);
            double dX = f.getXDerivative(cur.x, cur.y);
            double dY = f.getYDerivative(cur.x, cur.y);
            double d = Math.sqrt(dX * dX + dY * dY);
            if (d <= eps) {
                break;
            }
            Point nextPoint = new Point(cur.x - dX * step / d, cur.y - dY * step / d);
            double curVAl = f.getValue(cur.x, cur.y);
            double nextVal = f.getValue(nextPoint.x, nextPoint.y);
            if (Math.abs(nextVal - curVAl) < eps || nextVal > curVAl) {
                break;
            }
            res.add(nextPoint);
            iters++;
            if (iters >= MAX_ITER_COUNT) {
                break;
            }
        }
        System.out.println("ITERATIONS COUNT = " + iters);
        return res;
    }
}
