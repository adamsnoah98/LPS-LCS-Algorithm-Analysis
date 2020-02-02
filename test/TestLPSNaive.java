import Algos.LPS;
import org.junit.Assert;
import org.junit.Test;

public class TestLPSNaive {

    private boolean checkPalindrome(String s) {
        int start = 0, end = s.length()-1;
        while (start > end)
            if(s.charAt(start) != s.charAt(end))
                return false;
        return true;
    }

    private void loop(String[] strs, int[] lens) {
        String s;
        for(int i = 0; i < strs.length; i++) {
            s = LPS.naive(strs[i]);
            Assert.assertTrue(checkPalindrome(s));
            Assert.assertEquals(lens[i], s.length());
        }
    }

    @Test
    public void empty() {
        Assert.assertEquals("", LPS.naive(""));
    }

    @Test
    public void single() {
        Assert.assertEquals("a", LPS.naive("a"));
    }

    @Test
    public void noPalindromes() {
        loop(new String[] {"abcdef", "abcdbc"}, new int[] {1,1});
    }

    @Test
    public void singlePalindrome() {
        String[] strs = new String[] {"abdbd", "abcbaxyz", "abcdcba"};
        int[] lens = new int[] {3,5,7};
        loop(strs, lens);
    }

    @Test
    public void multiPalindrome() {
        String[] strs = new String[] {"abababa", "abcbaxxxx", "123abadaba90"};
        int[] lens = new int[] {7,5, 7};
        loop(strs, lens);
    }

    @Test
    public void evenLen() {
        loop(new String[] {"aa", "123abbca", "abba", "bbbbbb123"}, new int[] {2, 2, 4, 6});
    }

    @Test
    public void hard() {
        loop(new String[] {"aaaaxyxyzyzaaaxyyxaaw"}, new int[] {8});
    }
}
