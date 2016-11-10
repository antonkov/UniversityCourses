public class Node implements Comparable<Node> {
    String transSymbol;
    String name;

    Node(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Node term) {
        return name.compareTo(term.name);
    }

    @Override
    public String toString() {
        return name;
    }
}