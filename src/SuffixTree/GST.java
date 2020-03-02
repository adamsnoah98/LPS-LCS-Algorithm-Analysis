package SuffixTree;

/**
 * A generalized suffix tree implementation for arbitrarily many ascii strings
 */
public class GST {

    private Inner root, dummy = new Inner();
    private eString[] ps;
    private int[] leafEnds;
    private int size = 0, csk; //node count, current strKey
    private RMQ lcaQueryer;


    //////////////////////////// Construction /////////////////////////////////

    public GST(String... strs) {
        root = new Inner(this);
        ps = new eString[strs.length];
        leafEnds = new int[strs.length];
        for(int i = 0; i < strs.length; i++) {
            ps[i] = new eString(strs[i], i);
            include(i);
        }
    }

    /**
     * Implementation of Ukkonen's algorithm
     * @param strKey
     */
    private void include(int strKey) {
        csk = strKey;
        resetAP();
        eString s = ps[strKey];
        int imps = 1, si = 0, next;
        for(int i = 0; i < s.length(); i++) {
            leafEnds[strKey]++;
            next = i - imps;
            while(imps-- > 0 && !checkNext(s.charAt(i)))
                branch(++next, si++);
            imps += 2;
            linkTODO = null;
        }
    }

    /////////////////////////// Queries on Tree ///////////////////////////////

    /**
     * @return A substring of maximal length shared by all parent strings
     */
    public String getLCSS() {
        Node deepest = root.getDeepestShared(ps.length);
        return ps[deepest.strKey].subString(deepest.getEnd() - deepest.getDepth(), deepest.getEnd());
    }

    /**
     * This is a Linear time(*) query of the longest palindromic substring.
     *
     * @return A palindromic substring of maximal length shared by both parent strings
     *
     * An assertion error will be thrown if the tree is not of exactly 2 strings or if the GST
     * has not been preprocessed for LPS. (It is only to be used
     * in the case where ps[0] == ps[1].reverse() but this is time consuming)
     */
    public String getLPS(){
        assert ps.length == 2 && lcaQueryer != null;
        Node best = root, temp;
        int l = ps[0].length();
        for(int i = 0; i < l; i++) {
            temp = lcaQueryer.LCA(i, - i + 1);
            best = temp.depth > best.depth ? temp : best;
        }
        return ps[best.strKey].subString(best.getEnd() - best.getDepth(), best.getEnd());
    }

    /**
     * Linear time preprocessing of this tree for O(1) LCA retrieval;
     *
     * Processing based on algorithm presented by Bender & Farach-Colton (2000)
     * @return this
     */
    public GST LPSPreprocess() {
        assert ps.length == 2;
        lcaQueryer = new RMQ(root, size);
        return this;
    }

    //////////////////////////// Active Point Fields //////////////////////////////

    private Inner n, linkTODO;
    private Node e;
    private int l;

    /////////////////////////// Active Point Methods //////////////////////////////

    private void resetAP() {
        setN(root);
        e = dummy;
        l = 0;
    }

    /**
     * @param c
     * @return whether c is a next character in the current active path
     */
    private boolean checkNext(eChar c) {
        boolean result;
        if(l == 0) {
            if (result = n.children.containsKey(c))
                e = n.children.get(c);
        } else {
            result = e.charOnEdge(l).equals(c);
        } if(result) {
            l++;
            if(linkTODO != null) linkTODO.sl = n;
        } if(l == e.getLen()) {
            l = 0;
            setN(e);
            e = dummy;
        }
        return result;
    }

    /**
     * Add a new suffix to the tree at active point
     * @param start new Leaf param
     * @param suffixIndex new Leaf param
     */
    private void branch(int start, int suffixIndex) {
        Inner i;
        if(l > 0 ) {
            i = e.branch(l, csk, start + l + n.depth, suffixIndex);
            e = i;
        } else
            i = n.addLeaf(csk, start + l + n.depth, suffixIndex);
        if(linkTODO != null) linkTODO.sl = i;
        if(i != root) linkTODO = i;
        update();
    }

    /**
     * Update the active point to the next suffix according to Ukkonen's algorithm
     */
    private void update() {
        if(l == 0 && n == root) return;
        Node oldEdge = e;
        if(n.sl == null) { //includes n==root case
            setN(root);
            e = n.children.get(oldEdge.charFmSuf(1));
            l--;
        } else {
            setN(n.sl);
            if(l>0)
                e = n.children.get(oldEdge.charOnEdge(0));
        } while(l >= e.getLen() && e != dummy) {
            setN(e);
            l -= e.getLen();
            e = n.children.get(oldEdge.charFmSuf(n.depth + 1));
        }
    }

    /** assigns newNode to be the active node,
     *  records which strings have used n as an active node
     */
    private void setN(Node newNode) {
        //safe cast as the active point depth can never reach a leaf
        this.n =(Inner) newNode;
        if(n.lastVisitedby != csk) {
            n.lastVisitedby = csk;
            n.shareCount++;
        }
    }

    /////////////////////////// Node accessor methods ///////////////////////////

    public eString getString(int strKey) {
        return ps[strKey];
    }

    public int getLeafEnd(int strKey) {
        return leafEnds[strKey];
    }

    public int getAndIncSize() {
        return size++;
    }
}

