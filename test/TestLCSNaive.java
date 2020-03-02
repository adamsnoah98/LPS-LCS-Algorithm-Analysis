import Algos.LCS;
import org.junit.Assert;

public class TestLCSNaive extends LCSFrameWork {

    public TestLCSNaive() {
        try {
            m = LCS.class.getDeclaredMethod("naive", String.class, String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }
}
