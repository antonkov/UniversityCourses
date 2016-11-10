/**
 * Created by Borys Minaiev on 08.03.2015.
 */
public class UniformSearch {
    public static double findMinimum(final Function f, double stopEpsilon, int[] queries, boolean showInfo) {
        if (showInfo)
            System.out.println("UniformSearch:");
        double xFrom = f.getLeftX(), xTo = f.getRightX();
        double len = xTo - xFrom;
        int n = (int) Math.ceil(len / stopEpsilon);
        queries[0] = n + 1;
        double bestX = xFrom;
        double bestValue = f.getValue(xFrom);
        for (int i = 1; i <= n; i++) {
            double xi = xFrom + i * len / n;
            double curValue = f.getValue(xi);
            if (curValue < bestValue) {
                bestValue = curValue;
                bestX = xi;
            }
        }

        if (showInfo)
            System.out.println("calculated function value " + queries[0] + " times");
        return bestX;
    }
}
