import SuffixTree.GST;
import org.junit.Test;
import org.junit.Assert;

public class TestGST {

    private String wrapper(String... strs) {
        return new GST(strs).getLCSS();
    }

    @Test
    public void testEmpty() {
        Assert.assertEquals("", wrapper(""));
    }

    @Test
    public void testSingles() {
        Assert.assertEquals("x", wrapper("x"));
        Assert.assertEquals("banana", wrapper("banana"));
        Assert.assertEquals("1231201234", wrapper("1231201234"));
        Assert.assertEquals("CDCDCCCDC", wrapper("CDCDCCCDC"));
        Assert.assertEquals("bbbbbaaaaa", wrapper("bbbbbaaaaa"));
    }

    @Test
    public void testPairs() {
        Assert.assertEquals("bana", wrapper("banana", "tobana"));
        Assert.assertEquals("anana", wrapper("banana", "ananab"));
        Assert.assertEquals("DCCC", wrapper("CCAEADCCCA", "CDCDCCCDC"));
        Assert.assertEquals("ba", wrapper("bbbbaaaa", "ba"));
    }

    @Test
    public void testMulti() {
        Assert.assertEquals("ba",
                wrapper("banana", "tobana", "cuba"));
        Assert.assertEquals("ba",
                wrapper("xxxbaxxbaxx", "ba", "bbbbaaaa"));
    }
}
