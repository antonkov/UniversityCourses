import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;

public abstract class AbstractPlot<T> extends ApplicationFrame {
    protected T function;

    public AbstractPlot(String applicationTitle, String chartTitle, T function, String xAxisLabel, String yAxisLabel) {
        super(applicationTitle);
        this.function = function;
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle,
                xAxisLabel,
                yAxisLabel,
                createDatasets()[0],
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(getPreferredSize());
        final XYPlot plot = xylineChart.getXYPlot();
        for (int i = 0; i < createDatasets().length; i++) {
            plot.setDataset(i, createDatasets()[i]);
        }

        XYItemRenderer[] renderers = getRenderers();
        for (int i = 0; i < getRenderers().length; i++) {
            initRendererPaint(i, renderers[i]);
            plot.setRenderer(i, renderers[i]);
        }
        setContentPane(chartPanel);
    }

    public abstract XYDataset[] createDatasets();

    public XYItemRenderer[] getRenderers() {
        return new XYItemRenderer[]{new XYLineAndShapeRenderer()};
    }

    public abstract void initRendererPaint(int index, XYItemRenderer renderer);

    public Dimension getPreferredSize() {
        return new Dimension(700, 400);
    }
}