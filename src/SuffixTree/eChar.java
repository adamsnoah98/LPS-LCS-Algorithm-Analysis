package SuffixTree;

/**
 * Extended character class with termination characters outside ascii representation
 * of primitive chars
 */
class eChar {
    final char c;
    final int strKey;
    final boolean isChar;

    eChar(char c) {
        isChar = true; this.c = c; strKey = -1;
    }

    eChar(int strKey) {
        isChar = false; this.strKey = strKey; c = '\u0000';
    }

    @Override
    public boolean equals(Object other) {
        eChar o;
        if(other instanceof eChar) {
            o = (eChar) other;
            return this.c == o.c && this.strKey == o.strKey && this.isChar == o.isChar;
        } else if(other instanceof Character)
            return this.isChar && c == (Character) other;
        return false;
    }

    @Override
    public int hashCode() {
        return isChar ? 0x80000000 & c: strKey;
    }

    public String toString() { return  isChar ? Character.toString(c) : "_" + strKey; }
}
