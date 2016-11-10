/**
 * Created by antonkov on 6/9/14.
 */
public class PairNodes implements Comparable<PairNodes> {
    Node a, b;

    PairNodes(Node a, Node b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int compareTo(PairNodes pairNodes) {
        if (!a.equals(pairNodes.a))
            return a.compareTo(pairNodes.a);
        return b.compareTo(pairNodes.b);
    }
}
