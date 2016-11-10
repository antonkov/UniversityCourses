/**
 * Created by antonkov on 3/20/2015.
 */
public class MethodOfPotentials {
    int n, m;

    int[] a;
    int[] b;
    int[] u, v;
    int[][] c;

    int[][] x;

    public MethodOfPotentials(int[] inA, int[] inB, int[][] inC) {
        a = new int[inA.length + 1];
        b = new int[inB.length + 1];
        c = new int[a.length][b.length];

        n = inA.length;
        m = inB.length;

        int sumA = 0, sumB = 0;
        for (int i = 0; i < inA.length; i++) {
            for (int j = 0; j < inB.length; j++) {
                c[i][j] = inC[i][j];
            }
        }
        for (int i = 0; i < inA.length; i++) {
            a[i] = inA[i];
            sumA += a[i];
        }
        for (int i = 0; i < inB.length; i++) {
            b[i] = inB[i];
            sumB += b[i];
         }
        if (sumA < sumB) {
            a[a.length - 1] = sumB - sumA;
            n++;
        } else if (sumB < sumA) {
            b[b.length - 1] = sumA - sumB;
            m++;
        }

        x = new int[n][m];

        u = new int[n];
        v = new int[m];
        NorthWestAngle();
        fillPotentials();
        printTable();

        int iter = 0;

        while (!isOptimal()) {
            int minI = 0, minJ = 0;
            int[][] delta = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    delta[i][j] = c[i][j] - u[i] - v[j];
                    if (delta[i][j] < delta[minI][minJ]) {
                        minI = i;
                        minJ = j;
                    }
                    System.out.printf("%4d", delta[i][j]);
                }
                System.out.println();
            }
            System.out.println("minI = " + minI + ", minJ = " + minJ);

            boolean[][] was = new boolean[n][m];
            goVertical(minI, minJ, minI, minJ, was, 1, Integer.MAX_VALUE);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(was[i][j] ? 1 : 0);
                }
                System.out.println();
            }
            fillPotentials();
            printTable();
            iter++;
        }
        System.out.println("countIteration " + iter);
    }

    int goVertical(int curI, int curJ, int stI, int stJ, boolean[][] was, int mul, int best) {
        was[curI][curJ] = true;

        if (mul < 0)
            best = Math.min(best, x[curI][curJ]);

        if (curI != stI && curJ == stJ) {
            // found cycle
        } else {
            for (int i = 0; i < n; i++) {
                if (i == curI)
                    continue;
                if (was[i][curJ])
                    continue;
                if (x[i][curJ] != 0) {
                    best = goHorizontal(i, curJ, stI, stJ, was, mul * -1, best);
                    break;
                }
            }
        }
        x[curI][curJ] += best * (mul < 0 ? -1 : 1);
        return best;
    }

    int goHorizontal(int curI, int curJ, int stI, int stJ, boolean[][] was, int mul, int best) {
        was[curI][curJ] = true;

        if (mul < 0)
            best = Math.min(best, x[curI][curJ]);

        if (curI == stI && curJ != stJ) {
            // found cycle
        } else {
            for (int j = 0; j < m; j++) {
                if (j == curJ)
                    continue;
                if (was[curI][j])
                    continue;
                if (x[curI][j] != 0) {
                    best = goVertical(curI, j, stI, stJ, was, mul * -1, best);
                    break;
                }
            }
        }
        x[curI][curJ] += best * (mul < 0 ? -1 : 1);
        return best;
    }

    void fillU(int uIdx, boolean[] wasU, boolean[] wasV) {
        wasU[uIdx] = true;
        for (int i = 0; i < wasV.length; i++) {
            if (!wasV[i] && x[uIdx][i] != 0) {
                v[i] = c[uIdx][i] - u[uIdx];
                fillV(i, wasU, wasV);
            }
        }
    }

    void fillV(int vIdx, boolean[] wasU, boolean[] wasV) {
        wasV[vIdx] = true;
        for (int i = 0; i < wasU.length; i++) {
            if (!wasU[i] && x[i][vIdx] != 0) {
                u[i] = c[i][vIdx] - v[vIdx];
                fillU(i, wasU, wasV);
            }
        }
    }

    boolean isOptimal() {
        boolean optimal = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int delta = c[i][j] - u[i] - v[j];
                if (delta < 0)
                    optimal = false;
            }
        }
        return optimal;
    }

    public void fillPotentials() {
        boolean[] wasU = new boolean[n], wasV = new boolean[m];
        for (int i = 0; i < n; i++)
            if (!wasU[i]) {
                u[i] = 0;
                fillU(i, wasU, wasV);
            }
        for (int j = 0; j < m; j++)
            if (!wasV[j]) {
                v[j] = 0;
                fillV(j, wasU, wasV);
            }
    }

    public void NorthWestAngle() {
        for (int i = 0; i < n; i++)
            u[i] = a[i];
        for (int i = 0; i < m; i++)
            v[i] = b[i];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                x[i][j] = Math.min(u[i], v[j]);
                u[i] -= x[i][j];
                v[j] -= x[i][j];
            }
        }
    }

    public void printTable() {
        int curCost = 0;
        System.out.printf("---- ");
        for (int i = 0; i < m; i++) {
            System.out.printf("%4d ", b[i]);
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%4d ", a[i]);
            for (int j = 0; j < m; j++) {
                System.out.printf("%4d ", x[i][j]);
                curCost += x[i][j] * c[i][j];
            }
            System.out.println();
        }
        System.out.print("U ---- ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", u[i]);
        }
        System.out.println();
        System.out.print("V ---- ");
        for (int i = 0; i < m; i++) {
            System.out.printf("%d ", v[i]);
        }
        System.out.println();
        System.out.println("Current cost " + curCost);
    }
}
