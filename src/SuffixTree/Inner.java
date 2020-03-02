package SuffixTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Inner extends Node {

    int end;
    Inner sl; //suffixLink
    Map<eChar, Node> children = new HashMap<>();
    Iterator<Node> iter;

    /**Root constructor*/
    Inner(GST tree) {
        this.tree = tree;
        this.parent = this;
        this.depth = 0;
        this.id = tree.getAndIncSize();
        this.lastVisitedby = strKey;
    }

    /**Dummy node*/
    Inner() {
        this.strKey = -1;
        this.start = 1;
        this.end = -2;
    }

    /**branch constructor*/
    Inner(Node caller, int depth, int visitingStrKey) {
        this.tree = caller.tree;
        this.parent = caller.parent;
        this.strKey = caller.strKey;
        this.start = caller.start;
        this.end = caller.start + depth;
        this.depth = getLen() + parent.depth;
        this.id = tree.getAndIncSize();
        this.shareCount = caller.shareCount;
        if(caller.lastVisitedby != visitingStrKey) shareCount++;
        this.lastVisitedby = visitingStrKey;
    }

    Inner addLeaf(int strKey, int start, int suffixIndex) {
        Leaf l = new Leaf(this, strKey, start, suffixIndex);
        children.put(tree.getString(strKey).charAt(start), l);
        return this;
    }

    Node getDeepestShared(int count) {
        if(shareCount < count)
            return null;
        Node max = this, temp;
        for(Node n: children.values()) {
            if(n instanceof Leaf) {
                if (n.shareCount >= count && n.getDepth() > max.getDepth())
                    max = n;
            } else if((temp = ((Inner) n).getDeepestShared(count)) != null &&
                    temp.getDepth() > max.getDepth())
                    max = temp;
        }
        return max;
    }

    Node getDeepestPalindrome(int length, int[] firsts, Node[] flatTree) {

        return null;
    }

    public Node walk() {
        if(iter == null)
            iter = children.values().iterator();
        if(iter.hasNext())
            return iter.next();
        else
            return this.parent;
    }

    @Override
    int getLen() {
        return end - start;
    }

    @Override
    int getDepth() {
        return depth;
    }

    @Override
    int getEnd() {
        return end;
    }
}
