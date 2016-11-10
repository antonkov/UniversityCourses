public class XSquared extends LipschitzFunction {
    @Override
    public double getLeftX() {
        return -2;
    }

    @Override
    public double getRightX() {
        return 2;
    }

    @Override
    public double getValue(double x) {
        return x * x;
    }
}
