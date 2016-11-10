/**
 * Created by Borys Minaiev on 08.03.2015.
 */
public class AntonsFunction extends LipschitzFunction {
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
        return Math.pow(x, 6) +  3 * Math.pow(x, 3) + 8 * x - 8;
    }
}
