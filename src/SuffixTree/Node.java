package SuffixTree;


public abstract class Node {

    GST tree;
    Inner parent;
    int id = -1, strKey, start, depth, level;
    //used for finding common substrings
    int shareCount = 1, lastVisitedby = -1;

    abstract int getLen();

    abstract int getDepth();

    abstract int getEnd();

    public abstract Node walk();

    /**Get char at index from suffix up to and including this*/
    eChar charFmSuf(int depth) {
        return tree.getString(strKey).charAt(start - this.parent.depth + depth);
    }

    /**Get char at index relative to (along) this edge*/
    eChar charOnEdge(int index) {
        if(getLen() < 0) return null;
        return tree.getString(strKey).charAt(start + index);
    }

    Inner branch(int depth, int strKey, int start, int suffixIndex) {
        Inner i = new Inner(this, depth, strKey);
        parent.children.put(charOnEdge(0), i);
        this.start += depth;
        this.parent = i;
        i.children.put(charOnEdge(0), this);
        Leaf l = new Leaf(i, strKey, start, suffixIndex);
        i.children.put(l.charOnEdge(0), l);
        return i;
    }

}
