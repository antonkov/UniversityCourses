import org.jfree.ui.RefineryUtilities;

/**
 * Created by antonkov on 3/8/2015.
 */
public class Main {
    public static void main(String[] args) {

        Dichotomy.findMinimum(new AntonsFunction(), 1e-9, new int[]{0}, true);
        /*double accuracy = 2;
        for (int minusLog = -1; minusLog <= 30; minusLog++) {
            int[] queries = new int[1];
            Fibonacci.findMinimum(new AntonsFunction(), accuracy, queries, false);
            System.out.println(queries[0]);
            accuracy /= 2;
        }*/
        //Fibonacci.findMinimum(new AntonsFunction(), 1e-9, new int[]{0}, true);
/*        final double accuracy = 1e-9;
        System.out.println("min = " + Dichotomy.findMinimum(new AntonsFunction(), accuracy, new int[]{0}, true));
        System.out.println("min = " + Fibonacci.findMinimum(new AntonsFunction(), accuracy, new int[]{0}, true));*/
        //PolygonalMethodPlot pmp = new PolygonalMethodPlot("optimization", "PolygonalMethod", new SinFunction(), "x", "y");
        //drawPlots(pmp);
    }

    private static void drawPlots(AbstractPlot... plots) {
        for (AbstractPlot plot : plots) {
            plot.pack();
            RefineryUtilities.centerFrameOnScreen(plot);
            plot.setVisible(true);
        }
    }
}
