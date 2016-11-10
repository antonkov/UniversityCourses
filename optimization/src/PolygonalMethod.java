import java.util.*;

public class PolygonalMethod {
    private static double getRandomPoint(double left, double right) {
        return left + 0.5 * (right - left);
    }

    public List<Point> calc(LipschitzFunction function, double eps) {
        final TreeSet<Point> pointsOrdY = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.y != o2.y)
                    return Double.compare(o1.y, o2.y);
                return Double.compare(o1.x, o2.x);
            }
        });
        final TreeSet<Point> pointsOrdX = new TreeSet<Point>(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                if (o1.x != o2.x)
                    return Double.compare(o1.x, o2.x);
                return Double.compare(o1.x, o2.x);
            }
        });
        double p = getRandomPoint(function.getLeftX(), function.getRightX());
        pointsOrdY.add(new Point(function.getLeftX(), function.getValue(p) - function.getL() * (p - function.getLeftX())));
        pointsOrdY.add(new Point(p, function.getValue(p)));
        pointsOrdY.add(new Point(function.getRightX(), function.getValue(p) - function.getL() * (function.getRightX() - p)));
        for (Point point : pointsOrdY) {
            pointsOrdX.add(point);
        }
        int cntEvaluations = 3;
        int cntIterations = 0;

        List<TreeSet<Point>> sets = new ArrayList<TreeSet<Point>>();
        sets.add(pointsOrdX);
        sets.add(pointsOrdY);

        while (true) {
            cntEvaluations++;
            cntIterations++;
            Point minPoint = pointsOrdY.first();
            Point next = pointsOrdX.higher(minPoint);
            Point prev = pointsOrdX.lower(minPoint);
            Point newGraphPoint = new Point(minPoint.x, function.getValue(minPoint.x));
            if (Math.abs(newGraphPoint.y - minPoint.y) < eps) {
                break;
            }
            for (TreeSet<Point> set : sets) {
                set.remove(minPoint);
            }
            List<Point> toAdd = new ArrayList<Point>();
            toAdd.add(newGraphPoint);
            if (prev != null) {
                toAdd.add(findIntersectPoint(prev, newGraphPoint, function.getL()));
            }
            if (next != null) {
                toAdd.add(findIntersectPoint(newGraphPoint, next, function.getL()));
            }
            for (TreeSet<Point> set : sets) {
                for (Point addPoint : toAdd) {
                    set.add(addPoint);
                }
            }
        }
        List<Point> pointList = new ArrayList<Point>();
        pointList.addAll(pointsOrdX);
        System.out.println(cntIterations + " " + cntEvaluations);
        System.out.println(pointsOrdY.first().x + " " + pointsOrdY.first().y);
        return pointList;
    }

    private Point findIntersectPoint(Point left, Point right, double l) {
        if (left.y < right.y) {
            double y = left.y;
            double x = right.x - (right.y - y) / l;
            return findMiddlePoint(left, new Point(x, y), l);
        } else {
            double y = right.y;
            double x = left.x + (left.y - y) / l;
            return findMiddlePoint(new Point(x, y), right, l);
        }
    }

    private Point findMiddlePoint(Point left, Point right, double l) {
        return new Point((left.x + right.x) / 2, right.y - (right.x - left.x) / 2 * l);
    }
}