import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class LCSFrameWork {

    protected Method m;

    private boolean isSubstring(String sub, String par) {
        for(int i = 0; i <= par.length() - sub.length(); i++)
            if(par.substring(i, i + sub.length()).equals(sub))
                return true;
        return false;
    }

    private void loop(String[] firsts, String[] seconds, int[] lens) {
        if(m == null)
            return;
        String s;
        try {
            for(int i = 0; i < firsts.length; i++) {
                s = (String) m.invoke(null, firsts[i], seconds[i]);
                Assert.assertEquals(lens[i], s.length());
                Assert.assertTrue(isSubstring(s, firsts[i]));
                Assert.assertTrue(isSubstring(s, seconds[i]));
            }
        } catch (Exception e) {
            e.getCause().printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void empty() {
        String[] f = new String[] {"", "", "string", "xyz"};
        String[] s = new String[] {"", "string", "", "abc"};
        int[] lens = new int[] {0, 0, 0, 0};
        loop(f, s, lens);
    }

    @Test
    public void singleChar() {
        String[] f = new String[] {"a", "i", "string", "string", "string"};
        String[] s = new String[] {"a", "string", "i", "s", "g"};
        int[] lens = new int[] {1, 1, 1, 1, 1};
        loop(f, s, lens);
    }

    @Test
    public void singleSub() {
        String[] f = new String[] {"ab", "abc", "xxxxabcd", "abcdw", "nnnlololnnnn"};
        String[] s = new String[] {"ab", "abc", "yyabcd", "abcdzz", "mlololm"};
        int[] lens = new int[] {2, 3, 4, 4, 5};
        loop(f, s, lens);
    }

    @Test
    public void multiSub() {
        String[] f = new String[] {"MatchxxxMore", "FindTheCopy", "Overlappingstrings", "evenOverUneven"};
        String[] s = new String[] {"MoreMatches", "CopyxCopyyCopyz", "pinglapsapps", "UnevenOverlap"};
        int[] lens = new int[] {5, 4, 4, 8};
        loop(f, s, lens);
    }
}
