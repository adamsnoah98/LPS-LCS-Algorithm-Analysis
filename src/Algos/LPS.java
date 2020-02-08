package Algos;

import DataGen.Trial;
import SuffixTree.GST;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Class with multiple solutions to the longest palindromic substring problem
 */
public class LPS {

    /**
     * Naive Implementation
     *
     * Space: O(1)
     *
     * All comparisons on done on the input string
     *
     * Time: O(nL^2)
     * - where L is the length of the longest palindrome
     *
     * Check L up to n rounds of substrings, each round having O(n) substrings, compared in O(L) time
     *
     * @param s target String
     * @return A palindromic substring of maximal length
     */
    public static String naive(String s) {
        int foundSubstring = 2; //need two fails in a row to terminate (even vs. odd)
        int bestLeft = 0, bestL = 0, i,left, right;
        for(int l = 1; l <= s.length() && foundSubstring > 0; l++) {
            foundSubstring--;
            for(i = 0; i < s.length() - l + 1; i++) {
                left = i - 1;
                right = i + l;
                while(++left < --right)
                    if (s.charAt(left) != s.charAt(right)) break;
                if (left >= right) {
                    foundSubstring = 2;
                    bestLeft = i;
                    bestL = l;
                }
            }
        }
        return s.substring(bestLeft, bestLeft + bestL);
    }

    /**
     * Dynamic Programming
     *
     * Space: O(1)
     *
     * Storing only a max and some endpoints, comparisons are done on inputs
     *
     * Time: O(P) < O(Ln)
     * - Where P is the number of palindromic substrings
     * For each i in [0, s.length-1] build as long of a palindrome as possible.
     * Trying both even and odd cases gives, 2n iterations each taking O(L) time, totalling
     * O(P) checks.
     *
     * @param s target String
     * @return A palindromic substring of maximal length
     */
    public static String dp(String s) {
        if(s.length() == 0) return "";
        int left, right, bestLeft = 0, bestL = 0; //bestL inclusive right end
        for(int even = 1; even >= 0; even--)
            for(int i = 0; i < s.length() - even; i++) {
                left = i + 1;
                right = i + even - 1;
                do {
                    left--; right++;
                } while (left >= 0 && right < s.length() &&
                        s.charAt(left) == s.charAt(right));
                if (right - left - 2 > bestL) { //adjust for breaking loop
                    bestL = right - left - 2;
                    bestLeft = left + 1;
                }
            }
        return s.substring(bestLeft, bestLeft + bestL + 1);
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
     * @param s target String
     * @return A common substring of maximal length
     */
    public static String st(String s) {
        return new GST(s, new StringBuilder(s).reverse().toString()).getLCSS();
    }

    //////////////////////////////// RUNTIME TESTING ////////////////////////////////////

    public static void main(String[] args) {
        Method[] methods = LPS.class.getDeclaredMethods();
        methods = Arrays.stream(methods).filter(m -> m.getReturnType().equals(String.class)).toArray(Method[]::new);
        new Trial("lps", methods, 2, 51, 13, 23).run();
    }

}

