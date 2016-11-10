package gtkachenko;

/**
 * User: gtkachenko
 */
public class CustomVector {
    private double[] data;
    private int size;

    public CustomVector(int size) {
        this.size = size;
        data = new double[size];
    }

    public void setItem(int i, double value) {
        data[i] = value;
    }

    public double getItem(int i) {
        return data[i];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (double value : data) {
            stringBuilder.append(value + " ");
        }
        return stringBuilder.toString();
    }

    public CustomVector subtract(CustomVector customVector) {
        CustomVector result = new CustomVector(size);
        for (int i = 0; i < size; i++) {
            result.data[i] = data[i] - customVector.data[i];
        }
        return result;
    }

    public double length() {
        double sum = 0;
        for (double cur : data) {
            sum += cur * cur;
        }
        return Math.sqrt(sum);
    }
}
