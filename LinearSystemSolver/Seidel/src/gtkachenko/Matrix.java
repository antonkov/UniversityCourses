package gtkachenko;

/**
 * User: gtkachenko
 */
public class Matrix {
    private double[][] data;
    private int size;

    public Matrix(int size) {
        this.size = size;
        data = new double[size][size];
    }

    public Matrix(double[][] data) {
        this.data = data;
        size = data.length;
    }

    public void setItem(int i, int j, double value) {
        data[i][j] = value;
    }

    public int getSize() {
        return size;
    }

    public double getItem(int i, int j) {
        return data[i][j];
    }

    public double norm() {
        double result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result = Math.max(result, Math.abs(data[i][j]));
            }
        }
        return result;
    }

    public CustomVector mul(CustomVector vector) {
        CustomVector result = new CustomVector(size);
        for (int i = 0; i < size; i++) {
            double cur = 0;
            for (int j = 0; j < size; j++) {
                cur += vector.getItem(j) * data[i][j];
            }
            result.setItem(i, cur);
        }
        return result;
    }

    public Matrix getInverse() {
        double[][] e = new double[size][size];
        double[][] clone = new double[size][size];
        for (int i = 0; i < e.length; i++) {
            for (int j = 0; j < e.length; j++) {
                e[i][j] = (i == j ? 1 : 0);
                clone[i][j] = data[i][j];
            }
        }

        for (int index = 0; index < clone.length; index++) {
            if (clone[index][index] == 0.) {
                for (int j = index + 1; j < clone.length; j++) {
                    if (clone[j][index] != 0.) {
                        double[] row = clone[index];
                        clone[index] = clone[j];
                        clone[j] = row;
                        break;
                    }
                }
            }
            double del = clone[index][index];
            for (int j = 0; j < clone.length; j++) {
                clone[index][j] /= del;
                e[index][j] /= del;
            }

            for (int i = 0; i < clone.length; i++) {
                if (index == i) {
                    continue;
                }
                double mul = clone[i][index];
                for (int j = 0; j < clone.length; j++) {
                    clone[i][j] -= (mul * clone[index][j]);
                    e[i][j] -= (mul * e[index][j]);
                }
            }
        }

        return new Matrix(e);
    }

    public Matrix multiply(Matrix a) {
        Matrix ans = new Matrix(size);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (int k = 0; k < size; k++) {
                    ans.data[x][y] += (data[x][k] * a.data[k][y]);
                }
            }
        }
        return ans;
    }
}
