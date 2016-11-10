public class SinFunction extends LipschitzFunction {
    @Override
    public double getLeftX() {
        return -10;
    }

    @Override
    public double getRightX() {
        return 10;
    }

    @Override
    public double getValue(double x) {
        return Math.sin(x);
    }
}