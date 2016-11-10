/**
 * Created by Borys Minaiev on 10.03.2015.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


class Surface extends JPanel {

    Function2D f = new Example2DFunction();
    final double zStep = 0.1;

    boolean needPaint(double z1, double z2) {
        return Math.round(z1 / zStep) != Math.round(z2 / zStep);
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);

        Dimension size = getSize();
        Insets insets = getInsets();

        int width = size.width - insets.left - insets.right;
        int height = size.height - insets.top - insets.bottom;

        double xFrom = f.getLeftX(), xTo = f.getRightX();
        double yFrom = f.getLeftY(), yTo = f.getRightY();

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                double x = w / (width + 0.) * (xTo - xFrom) + xFrom;
                double y = (height - h) / (height + 0.) * (yTo - yFrom) + yFrom;
                double nextX = (w + 1) / (width + 0.) * (xTo - xFrom) + xFrom;
                double nextY = (height - h - 1) / (height + 0.) * (yTo - yFrom) + yFrom;
                double ff1 = f.getValue(x, y);
                double ff2 = f.getValue(nextX, y);
                double ff3 = f.getValue(x, nextY);
                double f1 = Math.min(Math.min(ff1, ff2), ff3);
                double f2 = Math.max(Math.max(ff1, ff2), ff3);
                if (needPaint(f1, f2)) {
                    g2d.drawLine(w, h, w, h);
                }
            }
        }

        {
            g2d.setColor(Color.RED);
            List<Point> pts = GradientSearchFixedStep.searchMinimum(f, 1e-2);
            drawPoints(pts, xFrom, xTo, yFrom, yTo, width, height, g2d);
        }

        {
            g2d.setColor(Color.GREEN);
            List<Point> pts = GradientSearchFastest.searchMinimum(f);
            drawPoints(pts, xFrom, xTo, yFrom, yTo, width, height, g2d);
        }
    }

    void drawPoints(List<Point> pts, double xFrom, double xTo, double yFrom, double yTo, int width, int height, Graphics2D g2d) {
        for (int i = 0; i + 1 < pts.size(); i++) {
            Point p1 = pts.get(i);
            Point p2 = pts.get(i + 1);
            int x1 = (int) ((p1.x - xFrom) / (xTo - xFrom) * width);
            int y1 = (int) (height - (p1.y - yFrom) / (yTo - yFrom) * height);
            int x2 = (int) ((p2.x - xFrom) / (xTo - xFrom) * width);
            int y2 = (int) (height - (p2.y - yFrom) / (yTo - yFrom) * height);
            g2d.drawLine(x1, y1, x2, y2);
        }
        final int POINT_SIZE = 2;
        for (Point p : pts) {
            int x1 = (int) ((p.x - xFrom) / (xTo - xFrom) * width);
            int y1 = (int) (height - (p.y - yFrom) / (yTo - yFrom) * height);
            g2d.drawLine(x1 - POINT_SIZE, y1 - POINT_SIZE, x1 + POINT_SIZE, y1 + POINT_SIZE);
            g2d.drawLine(x1 - POINT_SIZE, y1 + POINT_SIZE, x1 + POINT_SIZE, y1 - POINT_SIZE);
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}

public class  GradientSearchVisualisation extends JFrame {

    public GradientSearchVisualisation() {

        initUI();
    }

    private void initUI() {

        setTitle("x^4 + y^4 - 5(x * y - x^2 * y^2)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new Surface());

        setSize(600, 600);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                GradientSearchVisualisation ps = new GradientSearchVisualisation();
                ps.setVisible(true);
            }
        });
    }
}