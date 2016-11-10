package com.dbobrov.num_methods;

public interface Solver {
    void solve(double[][] matrix, double[] free, double[] solution, double epsilon, int maxIterations);
}
