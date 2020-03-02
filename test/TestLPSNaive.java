import Algos.LPS;
import org.junit.Assert;

public class TestLPSNaive extends LPSFrameWork {

    public TestLPSNaive() {
        try {
            m = LPS.class.getDeclaredMethod("naive", String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }
}
