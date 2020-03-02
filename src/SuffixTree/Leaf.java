package SuffixTree;

public class Leaf extends Node {

    int suffixIndex;

    Leaf(Inner parent, int strKey, int start, int suffixIndex) {
        this.tree = parent.tree;
        this.parent = parent;
        this.strKey = strKey;
        this.start = start;
        this.suffixIndex = suffixIndex;
        this.id = tree.getAndIncSize();
        this.lastVisitedby = strKey;
    }

    @Override
    int getLen() {
        return tree.getLeafEnd(strKey) - start;
    }

    @Override
    int getDepth() {
        return parent.depth + getLen();
    }

    @Override
    int getEnd() {
        return tree.getLeafEnd(strKey);
    }

    public Node walk() {
        return this.parent;
    }
}
