/**
 * Created by Borys Minaiev on 10.03.2015.
 */
public interface Function2D {
    double getLeftX();
    double getRightX();
    double getLeftY();
    double getRightY();
    double getValue(double x, double y);

    double getStartX();
    double getStartY();
    double getXDerivative(double x, double y);
    double getYDerivative(double x, double y);
}
