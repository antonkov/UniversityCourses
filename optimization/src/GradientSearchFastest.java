import java.util.ArrayList;
import java.util.List;

/**
 * Created by Borys Minaiev on 10.03.2015.
 */
public class GradientSearchFastest {
    final static double eps = 1e-7;
    final static int MAX_ITER_COUNT = 1000;
    final static double MAX_STEP = 10;

    public static List<Point> searchMinimum(final Function2D f) {
        Point start = new Point(f.getStartX(), f.getStartY());
        ArrayList<Point> res = new ArrayList<Point>();
        res.add(start);
        int iters = 0;
        int queries = 1;
        while (true) {
            final Point cur = res.get(res.size() - 1);
            double dX = f.getXDerivative(cur.x, cur.y);
            double dY = f.getYDerivative(cur.x, cur.y);
            double d = Math.sqrt(dX * dX + dY * dY);
            if (d <= eps) {
                break;
            }
            final double realDX = dX / d;
            final double realDY = dY / d;

            Function f1d = new Function() {
                double maxX = -1;

                @Override
                public double getLeftX() {
                    return 0;
                }

                @Override
                public double getRightX() {
                    if (maxX >= 0) {
                        return maxX;
                    }
                    double left = 0, right = MAX_STEP;
                    for (int it = 0; it < 60; it++) {
                        double mid = (left + right) / 2;
                        Point p = new Point(cur.x - mid * realDX, cur.y - mid * realDY);
                        if (p.x < f.getLeftX() || p.x > f.getRightX() || p.y < f.getLeftY() || p.y > f.getRightY()) {
                            right = mid;
                        } else {
                            left = mid;
                        }
                    }
                    maxX = (left + right) / 2;
                    return maxX;
                }

                @Override
                public double getValue(double x) {
                    Point p = new Point(cur.x - x * realDX, cur.y - x * realDY);
                    return f.getValue(p.x, p.y);
                }
            };
            int[] queriesInGoldenRatioSearch = new int[1];
            double usePoint = GoldenRatio.findMinimum(f1d, eps,  queriesInGoldenRatioSearch, false);
            queries += queriesInGoldenRatioSearch[0];
            Point nextPoint = new Point(cur.x - dX * usePoint / d, cur.y - dY * usePoint / d);
            res.add(nextPoint);
            double dist = dist(cur, nextPoint);
            iters++;
            if (iters >= MAX_ITER_COUNT) {
                break;
            }
            if (dist < eps) {
                break;
            }
        }
        System.out.println("ITERATIONS COUNT = " + iters + ", asked function value = " + queries);
        return res;
    }

    private static double dist(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
