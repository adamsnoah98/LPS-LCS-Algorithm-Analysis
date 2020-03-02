package SuffixTree;

/**
 * Basic Constant* (a(n)) time UnionFind data structure for GST.getLPS()
 */
public class DisjointSet<T> {

    int[] sizes;
    T[] items;
    int[] parents;

    public DisjointSet(T[] items) {
        sizes = new int[items.length];
        parents = new int[items.length];
        items = items;
        for(int i = 0; i < parents.length; i++)
            parents[i] = i;
    }

    public void addSet(int index) {
        sizes[index] = 1;
        parents[index] = index;
    }

    public int find(int index) {
        int p, gp;
        while(parents[index] != index) { //path splitting
            p = parents[index];
            gp = parents[p];
            parents[index] = gp;
            parents[p] = parents[gp];
            index = parents[index];
        }
        return index;
    }

    public void merge(int i, int j) {
        int iRoot = find(i), jRoot = find(j);
        if(sizes[iRoot] > sizes[jRoot]) {
            parents[jRoot] = iRoot;
            sizes[iRoot] += sizes[jRoot];
        } else {
            parents[iRoot] = jRoot;
            sizes[jRoot] += sizes[iRoot];
        }
    }

    public T getItem(int index) {
        return items[index];
    }
}
