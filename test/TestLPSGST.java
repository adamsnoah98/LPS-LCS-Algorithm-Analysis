import Algos.LPS;
import org.junit.Assert;

public class TestLPSGST extends LPSFrameWork {

    public TestLPSGST() {
        try {
            m = LPS.class.getDeclaredMethod("st", String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }
}
