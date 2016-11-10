package com.dbobrov.num_methods;

import java.util.Arrays;

import static java.lang.Math.abs;

public class JacobiSolver implements Solver {
    private final Log log;

    public JacobiSolver(Log log) {
        this.log = log;
    }

    @Override
    public void solve(double[][] matrix, double[] free, double[] solution, double epsilon, int maxIterations) {
        double[] temp = new double[matrix.length];
        double norm;

        log.d("Starting iterations with initial solution " + Arrays.toString(solution));
        int iterationCount = 0;
        do {
            norm = iteration(matrix, free, solution, temp);
            log.d("Iteration " + iterationCount + ". Norm:" + norm + ". Solution: " + Arrays.toString(solution));
            iterationCount++;
        } while (norm > epsilon && iterationCount < maxIterations);

        if (iterationCount == maxIterations) {
            log.e("Maximum number of iterations exceeded, do not trust the result");
        }
    }

    private double iteration(double[][] matrix, double[] free, double[] solution, double[] temp) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            temp[i] = free[i];
            for (int g = 0; g < n; g++) {
                if (g != i) {
                    temp[i] -= matrix[i][g] * solution[g];
                }
            }
            temp[i] /= matrix[i][i];
        }
        double norm = abs(solution[0] - temp[0]);
        for (int i = 0; i < n; i++) {
            double t = abs(solution[i] - temp[i]);
            if (t > norm) {
                norm = t;
            }
            solution[i] = temp[i];
        }

        return norm;
    }
}