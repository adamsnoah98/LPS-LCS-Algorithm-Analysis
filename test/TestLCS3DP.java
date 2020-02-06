import Algos.LCS3;
import org.junit.Assert;
import org.junit.Test;

public class TestLCS3DP {

    private boolean isSubstring(String sub, String par) {
        for (int i = 0; i <= par.length() - sub.length(); i++)
            if (par.substring(i, i + sub.length()).equals(sub))
                return true;
        return false;
    }

    private void loop(String[] firsts, String[] seconds, String[] thirds, int[] lens) {
        String s;
        for(int i = 0; i < firsts.length; i++) {
            s = LCS3.dp(firsts[i], seconds[i], thirds[i]);
            Assert.assertEquals(lens[i], s.length());
            Assert.assertTrue(isSubstring(s, firsts[i]));
            Assert.assertTrue(isSubstring(s, seconds[i]));
            Assert.assertTrue(isSubstring(s, thirds[i]));
        }
    }

    @Test
    public void empty() {
        String[] f = new String[] {"", "", "string", "xyz"};
        String[] s = new String[] {"", "string", "", "abc"};
        String [] t = new String[] {"", "", "string", "123"};
        int[] lens = new int[] {0, 0, 0, 0};
        loop(f, s, t, lens);
    }

    @Test
    public void singleChar() {
        String[] f = new String[] {"s", "t", "string", "xyz"};
        String[] s = new String[] {"s", "string", "n", "abx"};
        String [] t = new String[] {"s", "t", "string", "xxx"};
        int[] lens = new int[] {1, 1, 1, 1};
        loop(f, s, t, lens);
    }

    @Test
    public void full() {
        String[] f = new String[] {"substring", "56share789", "strstrstr"};
        String[] s = new String[] {"str", "3share4", "a substring"};
        String [] t = new String[] {"string", "01share2", "only two full substrings"};
        int[] lens = new int[] {3, 5, 3};
        loop(f, s, t, lens);
    }
}
