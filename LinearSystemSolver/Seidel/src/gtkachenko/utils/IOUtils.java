package gtkachenko.utils;

import gtkachenko.CustomVector;
import gtkachenko.LinearSystem;
import gtkachenko.Matrix;

import java.io.*;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: gtkachenko
 * Date: 16.03.14
 * Time: 22:56
 */
public class IOUtils {
    public static class FastScanner {
        private BufferedReader br;
        private StringTokenizer st;

        public FastScanner(File file) {
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public String nextLine() {
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void emptyFile(File file) {
        if (file.exists() && !file.isDirectory()) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }

    public static Object[] nextTest(FastScanner in) {
        while (true) {
            String currentLine = in.nextLine();
            if (currentLine == null) {
                return null;
            }
            if (currentLine.isEmpty()) {
                continue;
            }
            int size = in.nextInt();
            Matrix matrix = new Matrix(size);
            CustomVector free = new CustomVector(size);
            LinearSystem linearSystem = new LinearSystem(matrix, free);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size + 1; j++) {
                    if (j < size) {
                        matrix.setItem(i, j, in.nextDouble());
                    } else {
                        free.setItem(i, in.nextDouble());
                    }
                }
            }
            in.nextLine();
            double epsilon = in.nextDouble();
            in.nextLine();
            int maxIteration = in.nextInt();
            in.nextLine();
            CustomVector approximation = new CustomVector(size);
            for (int i = 0; i < size; i++) {
                approximation.setItem(i, in.nextDouble());
            }
            return new Object[]{linearSystem, epsilon, maxIteration, approximation};
        }
    }
}
