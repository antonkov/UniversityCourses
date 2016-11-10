import java.util.*;

public class MinCostMaxFlow {
    public static class Edge {
        int from;
        int to;
        public int flow;
        int cap;
        long cost;
        Edge rev;

        public Edge(int from, int to, int flow, int cap, long cost) {
            this.from = from;
            this.to = to;
            this.flow = flow;
            this.cap = cap;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Edge [from=" + from + ", to=" + to + ", flow=" + flow
                    + ", cap=" + cap + ", cost=" + cost + "]";
        }

    }

    int n;
    ArrayList<Edge>[] edges;

    public MinCostMaxFlow(int n) {
        this.n = n;
        edges = new ArrayList[n];
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new ArrayList<Edge>();
        }
    }

    public Edge addEdge(int from, int to, int cap, long cost) {
        Edge e1 = new Edge(from, to, 0, cap, cost);
        Edge e2 = new Edge(to, from, 0, 0, -cost);
        e1.rev = e2;
        e2.rev = e1;
        edges[from].add(e1);
        edges[to].add(e2);
        return e1;
    }

    public long[] getMinCostMaxFlow(int source, int target) {
        long[] h = new long[n];
        for (boolean changed = true; changed; ) {
            changed = false;
            for (int i = 0; i < n; i++) {
                for (Edge e : edges[i]) {
                    if (e.cap > 0 && h[e.to] > h[e.from] + e.cost) {
                        h[e.to] = h[e.from] + e.cost;
                        changed = true;
                    }
                }
            }
        }
        Edge[] lastEdge = new Edge[n];
        long[] d = new long[n];
        int flow = 0;
        long cost = 0;
        while (true) {
            dijkstra(source, lastEdge, d, h);
            if (d[target] == Long.MAX_VALUE) {
                break;
            }
            int addFlow = Integer.MAX_VALUE;
            for (int v = target; v != source; ) {
                Edge e = lastEdge[v];
                addFlow = Math.min(addFlow, e.cap - e.flow);
                v = e.from;
            }
            cost += (d[target] + h[target] - h[source]) * addFlow;
            flow += addFlow;
            for (int v = target; v != source; ) {
                Edge e = lastEdge[v];
                e.flow += addFlow;
                e.rev.flow -= addFlow;
                v = e.from;
            }
            for (int i = 0; i < n; i++) {
                h[i] += d[i] == Long.MAX_VALUE ? 0 : d[i];
            }
        }
        return new long[]{flow, cost};
    }

    void dijkstra(int source, Edge[] lastEdge, final long[] d, long[] h) {
        TreeSet<Integer> ts = new TreeSet<Integer>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                if (d[o1] != d[o2]) {
                    return d[o1] < d[o2] ? -1 : 1;
                }
                return o1 - o2;
            }
        });
        Arrays.fill(d, Long.MAX_VALUE);
        d[source] = 0;
        ts.add(source);
        while (!ts.isEmpty()) {
            int v = ts.pollFirst();
            for (Edge e : edges[v]) {
                if (e.flow >= e.cap) {
                    continue;
                }
                if (d[e.to] == Long.MAX_VALUE
                        || d[e.to] > d[e.from] + e.cost + h[e.from]
                        - h[e.to]) {
                    if (e.cost + h[e.from] - h[e.to] < 0) {
                        throw new AssertionError();
                    }
                    ts.remove(e.to);
                    d[e.to] = d[e.from] + e.cost + h[e.from] - h[e.to];
                    lastEdge[e.to] = e;
                    ts.add(e.to);
                }
            }
        }
    }
}