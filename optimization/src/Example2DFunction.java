/**
 * Created by Borys Minaiev on 10.03.2015.
 */
public class Example2DFunction implements Function2D {
    @Override
    public double getLeftX() {
        return 0;
    }

    @Override
    public double getRightX() {
        return 1;
    }

    @Override
    public double getLeftY() {
        return 0;
    }

    @Override
    public double getRightY() {
        return 1;
    }

    // z = x^4 + y ^ 4 - 5(x*y - x^2*y^2)
    @Override
    public double getValue(double x, double y) {
        return Math.pow(x, 4) + Math.pow(y, 4)  - 5 * (x * y - x * x * y * y);
    }

    @Override
    public double getStartX() {
        return 0.1;
    }

    @Override
    public double getStartY() {
        return 0.2;
    }

    @Override
    public double getXDerivative(double x, double y) {
        return 4 * Math.pow(x, 3) - 5 * (y - 2 * x * y * y);
    }

    @Override
    public double getYDerivative(double x, double y) {
        return 4 * Math.pow(y, 3) - 5 * (x - 2 * y * x * x);
    }
}
