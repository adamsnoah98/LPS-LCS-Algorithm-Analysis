import Algos.LPS;
import org.junit.Assert;

public class TestLPSDP extends LPSFrameWork {

    public TestLPSDP() {
        try {
            super.m = LPS.class.getDeclaredMethod("dp", String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }
}
