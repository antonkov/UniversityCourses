import java.util.ArrayList;

public class Production {
    String transSymbol;
    ArrayList<Node> to;

    Production() {
        to = new ArrayList<>();
    }

    Production(ArrayList<Node> to) {
        this.to = to;
    }

    void addNode(Node node) {
        to.add(node);
    }
}
