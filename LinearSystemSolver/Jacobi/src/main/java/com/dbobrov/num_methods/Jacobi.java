package com.dbobrov.num_methods;

import java.io.*;
import java.util.StringTokenizer;

public class Jacobi {
    private static final String FILENAME_BASE = "jacobi";

    private static final String INPUT_FILE = "input";
    private static final String OUTPUT_FILE = "out_" + FILENAME_BASE;
    private static final String LOG_FILE = "log_" + FILENAME_BASE;


    public static void main(String... args) throws IOException {
        int exitCode = 0;
        try (Log log = new Log(LOG_FILE)) {
            try (Reader in = new Reader(INPUT_FILE)) {
                in.nextToken();
                int size = in.nextInt();
                double[][] matrix = new double[size][size];
                double[] free = new double[size];
                double[] solution = new double[size];

                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        matrix[i][j] = in.nextDouble();
                    }
                    free[i] = in.nextDouble();
                }

                in.nextToken();

                double epsilon = in.nextDouble();

                in.nextToken();

                int maxIterations = in.nextInt();

                in.nextToken();
                
                for (int i = 0; i < size; i++) {
                    solution[i] = in.nextDouble();
                }


                Solver solver = new JacobiSolver(log);
                solver.solve(matrix, free, solution, epsilon, maxIterations);


                try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(solution[0]);
                    for (int i = 1; i < size; i++) {
                        builder.append(" ").append(solution[i]);
                    }
                    writer.write(builder.toString());
                }
            } catch (Exception e) {
                log.e("Error in program execution", e);
                exitCode = 1;
            }
        }
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }


    private static class Reader implements AutoCloseable {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public Reader(String fileName) throws FileNotFoundException {
            reader = new BufferedReader(new FileReader(fileName));
        }

        public String nextToken() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                String line = reader.readLine();
                if (line == null) return null;
                tokenizer = new StringTokenizer(line);
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(nextToken());
        }

        public double nextDouble() throws IOException {
            return Double.parseDouble(nextToken());
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }
}
