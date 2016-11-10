import java.util.Locale;

/**
 * Created by Borys Minaiev on 08.03.2015.
 */
public class Dichotomy {
    public static double findMinimum(final Function f, double stopEpsilon, int[] queries, boolean showInfo) {
        Locale.setDefault(Locale.US);
        if (showInfo)
            System.out.println("Dichotomy:");
        final double delta = 1e-11;
        double xFrom = f.getLeftX(), xTo = f.getRightX();
        double lastSegmentSize = Double.NaN;
        queries[0] = 0;
        if (showInfo)
            System.out.println("step | xLeft | xRight | length | length_div");
        int step = 0;
        while (xTo - xFrom > stopEpsilon) {
            if (showInfo)
                System.out.printf("%04d  %.8f %.8f %.8f %.8f\n", step, xFrom, xTo, (xTo - xFrom), Double.isNaN(lastSegmentSize) ? 0 : (xTo - xFrom) / lastSegmentSize);
            lastSegmentSize = xTo - xFrom;
            double xMid = (xTo + xFrom) / 2.0;
            double xLeft = xMid - delta, xRight = xMid + delta;
            queries[0] += 2;
            double fLeft = f.getValue(xLeft), fRight = f.getValue(xRight);
            if (fLeft <= fRight) {
                xTo = xRight;
            } else {
                xFrom = xLeft;
            }
            step++;
        }
        if (showInfo) {
            System.out.println("calculated function value " + queries[0] + " times");
        }
        return (xFrom + xTo) / 2;
    }
}
