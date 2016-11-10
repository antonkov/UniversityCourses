package gtkachenko;

/**
 * User: gtkachenko
 */
public class LinearSystem {
    private Matrix matrix;
    private CustomVector free;

    public LinearSystem(Matrix matrix, CustomVector free) {
        this.matrix = matrix;
        this.free = free;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public CustomVector getFree() {
        return free;
    }

}
