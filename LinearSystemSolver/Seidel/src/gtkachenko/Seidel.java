package gtkachenko;


import gtkachenko.utils.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Seidel {
    public static final String INPUT_FILE_NAME = "input";
    public static final String OUTPUT_FILE_NAME = "out_seidel";
    public static final String LOG_FILE_NAME = "log_seidel";

    /**
     * @param log - where to write log; if (log == null) nothing will happen
     */
    public static CustomVector solveLinearSystem(LinearSystem linearSystem, double epsilon, int maxIteration, CustomVector initApproximation, Writer log) throws IOException {
        int size = linearSystem.getMatrix().getSize();
        Matrix b1 = new Matrix(size);
        Matrix b2 = new Matrix(size);
        Matrix c = new Matrix(size);

        double[] d = new double[size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    c.setItem(i, j, 0);
                    continue;
                }

                c.setItem(i, j, -linearSystem.getMatrix().getItem(i, j) / linearSystem.getMatrix().getItem(i, i));
                if (i < j) {
                    b2.setItem(i, j, c.getItem(i, j));
                } else {
                    b1.setItem(i, j, c.getItem(i, j));
                }
            }
            d[i] = linearSystem.getFree().getItem(i) / linearSystem.getMatrix().getItem(i, i);
        }

        int currentState = 0;
        CustomVector[] vectors = new CustomVector[2];
        vectors[0] = initApproximation;
        vectors[1] = new CustomVector(size);
        double b1Norm = b1.norm();
        double b2Norm = b2.norm();
        double cNorm = c.norm();

        int curIteration = maxIteration;
        for (int iteration = 0; iteration < maxIteration; iteration++) {
            System.out.println(vectors[currentState]);
            for (int i = 0; i < size; i++) {
                double currentValue = d[i];
                for (int j = 0; j < i; j++) {
                    currentValue += c.getItem(i, j) * vectors[1 - currentState].getItem(j);
                }
                for (int j = i + 1; j < size; j++) {
                    currentValue += c.getItem(i, j) * vectors[currentState].getItem(j);
                }

                vectors[1 - currentState].setItem(i, currentValue);
            }

            currentState = 1 - currentState;
            if (needToStop(vectors[0], vectors[1], cNorm, b2Norm, epsilon)) {
                curIteration = iteration;
                break;
            }
        }
        writeToLog(log, curIteration, b1Norm + b2Norm);
        return vectors[currentState];
    }

    private static boolean needToStop(CustomVector a, CustomVector b, double norm1, double norm2, double epsilon) {
        return a.subtract(b).length() <= (1 - norm1) / norm2 * epsilon;

    }

    private static void writeToLog(Writer out, int iteration, double sum) throws IOException {
        if (out == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Iteration: " + iteration + "\n");
        sb.append("|C1|+|C2|=" + sum + "\n");
        out.write(sb.toString() + "\n");
    }

    public static void main(String[] args) throws IOException {
        IOUtils.FastScanner in = new IOUtils.FastScanner(new File(INPUT_FILE_NAME));
        Writer out = new FileWriter(new File(OUTPUT_FILE_NAME));
        Writer outLog = new FileWriter(new File(LOG_FILE_NAME));

        while (true) {
            Object[] current = IOUtils.nextTest(in);
            if (current == null) {
                break;
            }
            out.write(solveLinearSystem((LinearSystem) current[0], (Double) current[1], (Integer) current[2], (CustomVector) current[3], outLog) + "\n");
        }

        out.close();
        outLog.close();
    }

}