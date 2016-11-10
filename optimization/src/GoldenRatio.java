/**
 * Created by Borys Minaiev on 08.03.2015.
 */
public class GoldenRatio {

    // x + ? + x = 1
    // ? = 1 - 2x
    // (1 - x)^2 / 1 = x
    // 1 - 3x + x^2 = 0;
    // x1,2 = (3+-sqrt(5)) / 2
    // x = (3 - sqrt(5)) / 2

    public static double findMinimum(final Function f, double stopEpsilon, int[] queries, boolean showInfo) {
        if (showInfo)
            System.out.println("GoldenRatio:");
        double xFrom = f.getLeftX(), xTo = f.getRightX();
        final double golden = (3 - Math.sqrt(5)) / 2.0;
        double posLeft = xFrom + (xTo - xFrom) * golden;
        double posRight = xTo - (xTo - xFrom) * golden;
        double fLeft = f.getValue(posLeft);
        double fRight = f.getValue(posRight);
        double lastSegmentSize = Double.NaN;
        queries[0] = 2;
        if (showInfo)
            System.out.println("step | xLeft | xRight | length | length_div");
        int step = 0;
        while (xTo - xFrom > stopEpsilon) {
            if (showInfo)
                System.out.printf("%04d  %.8f %.8f %.8f %.8f\n", step, xFrom, xTo, (xTo - xFrom), Double.isNaN(lastSegmentSize) ? 0 : (xTo - xFrom) / lastSegmentSize);
            lastSegmentSize = xTo - xFrom;
            if (fLeft < fRight) {
                xTo = posRight;
                fRight = fLeft;
                posLeft = xFrom + (xTo - xFrom) * golden;
                posRight = xTo - (xTo - xFrom) * golden;
                fLeft = f.getValue(posLeft);
            } else {
                xFrom = posLeft;
                fLeft = fRight;
                posLeft = xFrom + (xTo - xFrom) * golden;
                posRight = xTo - (xTo - xFrom) * golden;
                fRight = f.getValue(posRight);
            }
            queries[0]++;
            step++;
        }
        if (showInfo)
            System.out.println("calculated function value " + queries[0] + " times");
        return (xFrom + xTo) / 2;
    }
}
