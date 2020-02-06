package Algos;

import DataGen.Trial;
import SuffixTree.GST;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Class with multiple solutions to the longest common substring problem
 *
 * (Space Complexities only consider working space)
 */
public class LCS {

    /**
     * Naive Implementation
     *
     * Space: O(1)
     *
     * All comparisons on done on the input string
     *
     * Time: O(nmL)
     * - where L is the length of the longest common substring
     *
     * For each pair of starting indices (n*m), check the length of their shared substring (O(L)).
     * This dominates the O(n+m) output computation.
     *
     * @param s1 first target String
     * @param s2 second target String
     * @return A common substring of maximal length
     */
    public static String naive(String s1, String s2) {
        int bestI = 0, l, bestL = 0;
        for(int i = 0; i < s1.length(); i++)
            for(int j = 0; j < s2.length(); j++) {
                for(l = 0; i + l < s1.length() &&
                        j + l < s2.length() &&
                        s1.charAt(i+l) == s2.charAt(j+l); l++);
                if(l > bestL) {
                    bestI = i; bestL = l;
                }
            }
        return s1.substring(bestI, bestI + bestL);
    }

    /**
     * Dynamic Programming
     *
     * Space: O(min(n,m))
     *
     * Two arrays of length min(n,m) alongside some O(1) counters.
     *
     * Time: O(nm) worst case, O(nm/L) best (nontrivial) case
     *
     * Iterating over each string alignment, the algorithm attempts (given the current candidate
     * solution length l), to only check every l'th character for each alignment, and only fully compares the
     * neighborhood only if one of these probes finds a match. In the best case, the first candidate is optimal
     * and no other significant matches are found, in which case only O(mn/L) additional checks are made.
     *
     * @param s1 first target String
     * @param s2 second target String
     * @return A common substring of maximal length
     */
    public static String dpSkip(String s1, String s2) {
        if(s1.length() == 0 || s2.length() == 0)
            return "";

        int min, savedi, l, maxL = 0, maxI = 0;

        for(int i = 0; i < s1.length(); i++) { //rounds
            min = 0;
            //iterate through (skip counting)
            for(int j = 0; j < s2.length() && i < s1.length(); j += Math.max(1,maxL-1)) {
                if(s1.charAt(i) == s2.charAt(j)) {
                    l = 1;
                    //check behind
                    while (0 <= i - l && min <= j - l && s1.charAt(i - l) == s2.charAt(j - l))
                        l++;
                    savedi = i-l+1;
                    i++;
                    j++;
                    //check ahead
                    while (i < s1.length() && j < s2.length() && s1.charAt(i++) == s2.charAt(j++))
                        l++;
                    min = j;
                    if (l > maxL) {
                        maxL = l;
                        maxI = savedi;
                    }
                }
            }
        }
        return s1.substring(maxI, maxI + maxL);
    }

    /**
     * Space: O(min(n,m))
     *
     * Uses one array of such length, and some tracking numbers
     *
     * Time: O(nm)
     *
     * Iterating O(max(n,m)) times over a O(min(n,m)) array doing O(1) work at each step.
     *
     * @param s1 first target String
     * @param s2 second target String
     * @return a common substring of maximal length
     */
    public static String dpStd(String s1, String s2) {
        if(s1.length() == 0 || s2.length() == 0)
            return "";

        String tempMin;
        if(s1.length() > s2.length()) { //s1 = min & s2 = max
            tempMin = s2;
            s2 = s1;
            s1 = tempMin;
        }

        int[] a = new int[s1.length()];
        int[] saved = new int[2];
        int i, j, maxI = 0, maxL = 0;

        for(i = 0; i < s1.length(); i++) //base case init
            if(s1.charAt(i) == s2.charAt(0)) {
                a[i] = 1; maxL = 1; maxI = i;
            }
        for(j = 1; j < s2.length(); j++) {
            saved[0] = a[0];
            a[0] = s1.charAt(0) == s2.charAt(j) ? 1 : 0;
            if (a[0] > maxL) {
                maxL = 1; maxI = 0;
            }
            for (i = 1; i < s1.length(); i++) { //main DP
                saved[i%2] = a[i];
                a[i] = s1.charAt(i) == s2.charAt(j) ? saved[(i+1)%2] + 1 : 0;
                if (a[i] > maxL) {
                    maxL = a[i];
                    maxI = i - maxL + 1;
                }
            }
        }
        return s1.substring(maxI, maxI + maxL);
    }

    /**
     * Suffix Tree Implementation
     * Space: O(n+m)
     * A compressed suffix tree has O(1) sized edges, and contains at most 2(n+m) nodes
     * Assuming a constant alphabet (ascii), these nodes are also O(1) space^, though more precisely
     * are O(A) where A is the number of distinct characters in the tree
     *
     * Time: O(n+m)
     *
     * (Simplified) adding each string to the suffix tree is linear in the length of that string.
     * There are at most 2(n+m) phases, as the number of implicit suffixes can be incremented only n + m times,
     * and must be either incremented or decremented each iteration. Each iteration takes O(1) time.
     *
     * After construction, the nodes are already prepared to find the deepest shared node in a simple linear
     * DFS.
     *
     * @param s1 first target String
     * @param s2 second target String
     * @return A common substring of maximal length
     */
    public static String st(String s1, String s2) {
        return new GST(s1, s2).getLCSS();
    }

    public static void main(String[] args) {
        Method[] methods = LCS.class.getDeclaredMethods();
        methods = Arrays.stream(methods).filter(m -> m.getReturnType().equals(String.class)).toArray(Method[]::new);
        new Trial("lcs", methods, 2, 10, 12, 18).run();
    }

}
