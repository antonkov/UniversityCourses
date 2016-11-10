import org.StructureGraphic.v1.DSTreeNode;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by antonkov on 4/5/14.
 */
public class Tree implements DSTreeNode{
    String node;

    List<Tree> children;

    public Tree(String node, Tree... children) {
        this.node = node;
        this.children = Arrays.asList(children);
    }

    public Tree(String node) {
        this.node = node;
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
        if (children != null)
            return children.toArray(new DSTreeNode[0]);
        else
            return new DSTreeNode[0];
    }

    @Override
    public Object DSgetValue() {
        return node;
    }

    @Override
    public Color DSgetColor() {
        if (DSgetValue().equals("epsilon"))
            return Color.blue;
        return DSgetChildren().length == 0 ? Color.red : Color.black;
    }
}
