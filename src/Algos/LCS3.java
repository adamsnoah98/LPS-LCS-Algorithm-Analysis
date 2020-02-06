package Algos;

import DataGen.Trial;
import SuffixTree.GST;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Compares Dynamic Programming and suffix trees on the extend case of LCS
 * with 3 strings
 */
public class LCS3 {

    /**
     * Space: O(min(|s1|, |s2|, |s3|)
     *
     * Time: O(|s1||s2||s3|)
     *
     * For analogous reasoning to LCS.dpStd()
     *
     * @param s1
     * @param s2
     * @param s3
     * @return a shared string of maximal length
     */
    public static String dp(String s1, String s2, String s3) {
        if(s1.length() == 0 || s2.length() == 0 || s3.length() == 0)
            return "";

        String[] strs = new String[] {s1, s2, s3};
        Arrays.sort(strs);
        s1 = strs[0]; s2 = strs[1]; s3 = strs[2];

        int[] a = new int[s1.length()];
        int[] saved = new int[2];
        int i, j, k, savedk, maxI = 0, maxL = 0;

        for(savedk = 0; savedk < s3.length(); savedk++) {
            for (i = 0; i < s1.length(); i++) //base case init
                if (s1.charAt(i) == s2.charAt(0) &&
                        s2.charAt(0) == s3.charAt(savedk)) {
                    a[i] = 1;
                    maxL = 1;
                    maxI = i;
                }
            for (j = 1; j < s2.length() && savedk + 1 < s3.length(); j++) {
                k = savedk + 1;
                saved[0] = a[0];
                a[0] = s1.charAt(0) == s2.charAt(j) &&
                        s2.charAt(j) == s3.charAt(k) ? 1 : 0;
                if (a[0] > maxL) {
                    maxL = 1;
                    maxI = 0;
                }
                for (i = 1; i < s1.length() && k < s3.length(); i++) { //main DP
                    saved[i % 2] = a[i];
                    a[i] = s1.charAt(i) == s2.charAt(j) &&
                            s2.charAt(j) == s3.charAt(k) ?
                            saved[(i + 1) % 2] + 1 : 0;
                    if (a[i] > maxL) {
                        maxL = a[i];
                        maxI = i - maxL + 1;
                    }
                    k++;
                }
            }
        }
        return s1.substring(maxI, maxI + maxL);
    }

    /**
     * Space: O(|s1|+|s2|+|s3|)
     *
     * Time: O(|s1|+|s2|+|s3|)
     *
     * For reasoning analogous to LCS.st()
     *
     * @param s1
     * @param s2
     * @param s3
     * @return a shared substring of maximal length
     */
    public static String st(String s1, String s2, String s3) {
        return new GST(s1, s2, s3).getLCSS();
    }

    //////////////////////////////// RUNTIME TESTING ////////////////////////////////////

    public static void main() {
        Method[] methods = LCS3.class.getDeclaredMethods();
        methods = Arrays.stream(methods).filter(m -> m.getReturnType().equals(String.class)).toArray(Method[]::new);
        new Trial("lcs3", methods, 2, 10, 6, 12).run();
    }

}
