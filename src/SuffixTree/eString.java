package SuffixTree;

/**
 * extended string class, includes termination symbols for GSTs.
 */
public class eString {

    private eChar[] s;
    private String original;
    private int strKey;

    public eString(String str, int strKey) {
        this.original = str;
        this.strKey = strKey;
        this.s = new eChar[str.length()+1];
        for(int i = 0; i < str.length(); i++)
            s[i] = new eChar(str.charAt(i));
        s[str.length()] = new eChar(strKey);
    }

    @Override
    public String toString() {
        return original;
    }

    public eChar charAt(int index) {
        return s[index];
    }

    public int length() {
        return s.length;
    }

    public String subString(int start, int end) {
        return original.substring(start, Math.min(original.length(), end));
    }
}
