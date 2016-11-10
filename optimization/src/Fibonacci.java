import java.util.ArrayList;

/**
 * Created by Borys Minaiev on 08.03.2015.
 */
public class Fibonacci {
    // eps * F_n+2 >= b - a
    public static double findMinimum(final Function f, double stopEpsilon, int[] queries, boolean showInfo) {
        if (showInfo) {
            System.out.println("Fibonacci:");
        }
        double xFrom = f.getLeftX(), xTo = f.getRightX();
        ArrayList<Long> fib = new ArrayList<Long>();
        fib.add(1L);
        fib.add(1L);
        while (true) {
            long sum = fib.get(fib.size() - 1) + fib.get(fib.size() - 2);
            fib.add(sum);
            if (sum > 1e18) {
                throw new AssertionError("too much...");
            }
            if (sum * stopEpsilon >= xTo - xFrom) {
                break;
            }
        }
        System.out.println("eps * F_n+2 >= b - a ------------ n = " + (fib.size() - 2));
        double maxFib = fib.get(fib.size() - 1), maxFibM1 = fib.get(fib.size() - 2), maxFibM2 = fib.get(fib.size() - 3);
        double xLeft = xFrom + (xTo - xFrom) * maxFibM2 / maxFib;
        double xRight =  xFrom + (xTo - xFrom) * maxFibM1 / maxFib;
        double yLeft = f.getValue(xLeft), yRight = f.getValue(xRight);
        queries[0] = 2;
        double lastSegmentSize = Double.NaN;
        if (showInfo) {
            System.out.println("step | xLeft | xRight | length | length_div");
        }
        int step = 0;
        for (int curFib = fib.size() - 1; curFib >= 2; curFib--) {
            step++;
            if (showInfo)
                System.out.printf("%04d  %.8f %.8f %.8f %.8f\n", step, xFrom, xTo, (xTo - xFrom), Double.isNaN(lastSegmentSize) ? 0 : (xTo - xFrom) / lastSegmentSize);
            lastSegmentSize = xTo - xFrom;
            if (yLeft > yRight) {
                xFrom = xLeft;
                if (curFib != 2) {
                    double add = (xTo - xFrom) * fib.get(curFib - 3) * 1.0 / fib.get(curFib - 1);
                    xLeft = xLeft + add;
                    xRight = xTo - add;
                    yLeft = yRight;
                    yRight = f.getValue(xRight);
                }
            } else {
                xTo = xRight;
                if (curFib != 2) {
                    double add = (xTo - xFrom) * fib.get(curFib - 3) * 1.0 / fib.get(curFib - 1);
                    xLeft = xFrom + add;
                    xRight = xTo - add;
                    yRight = yLeft;
                    yLeft = f.getValue(xLeft);
                }
            }
            if (xLeft < xFrom)
                throw new AssertionError();
            if (xRight < xLeft)
                throw new AssertionError(xLeft + " " + xRight + " " + (xRight - xLeft));
            if (xTo < xRight)
                throw new AssertionError();
            queries[0]++;
            if (xFrom > xTo ) {
                throw new AssertionError();
            }
        }
        if (showInfo) {
            System.out.println("calculated function value " + queries[0] + " times");
        }
        return (xFrom + xTo) / 2;
    }
}
