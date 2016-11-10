/**
 * Created by antonkov on 3/20/2015.
 */
public class MainLab2 {
    public static void main(String[] args) {
        int[] a = {30, 40, 20};
        int[] b = {20, 30, 30, 10};
        int[][] c = {{2, 3, 2, 4},
                {3, 2, 5, 1},
                {4, 3, 2, 6}};

        int n = a.length + b.length + 2;
        int st = 0, en = n - 1;
        MinCostMaxFlow algo = new MinCostMaxFlow(n);
        final int infCap = Integer.MAX_VALUE / 2;
        for (int i = 0; i < a.length; i++) {
            algo.addEdge(st, i + 1, a[i], 0);
        }
        for (int i = 0; i < b.length; i++) {
            algo.addEdge(1 + a.length + i, en, b[i], 0);
        }
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[i].length; j++) {
                algo.addEdge(1 + i, 1 + a.length + j, infCap, c[i][j]);
            }
        }
        long[] res = algo.getMinCostMaxFlow(st, en);
        System.out.println("maxFlow = " + res[0] + ", minCost = " + res[1]);

        MethodOfPotentials mp = new MethodOfPotentials(a, b, c);
    }
}
