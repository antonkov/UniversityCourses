import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.List;

public class PolygonalMethodPlot extends AbstractPlot<LipschitzFunction> {
    private static final double EPS = 1e-4;
    private static final PolygonalMethod method = new PolygonalMethod();
    private static final Color[] lineColors = {Color.GREEN, Color.RED};

    public PolygonalMethodPlot(String applicationTitle, String chartTitle, LipschitzFunction function, String xAxisLabel, String yAxisLabel) {
        super(applicationTitle, chartTitle, function, xAxisLabel, yAxisLabel);
    }

    @Override
    public XYDataset[] createDatasets() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        double cur = function.getLeftX();
        final XYSeries data = new XYSeries("Function");
        while (cur < function.getRightX()) {
            data.add(cur, function.getValue(cur));
            cur += 0.01;
        }
        dataset.addSeries(data);
        List<Point> result = method.calc(function, EPS);
        final XYSeries dataP = new XYSeries("Approximation");
        for (Point point : result) {
            dataP.add(point.x, point.y);
        }
        dataset.addSeries(dataP);
        return new XYDataset[]{dataset};
    }

    @Override
    public void initRendererPaint(int index, XYItemRenderer renderer) {
        for (int i = 0; i < 2; i++) {
            renderer.setSeriesPaint(i, lineColors[i]);
            if (i != 1) {
                renderer.setSeriesStroke(i, new BasicStroke(2.0f));
                renderer.setSeriesShape(i, new Rectangle(1, 1));
            }
        }
    }
}