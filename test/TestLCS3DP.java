import Algos.LCS3;
import org.junit.Assert;


public class TestLCS3DP extends LCS3FrameWork {

    public TestLCS3DP() {
        try {
            m = LCS3.class.getDeclaredMethod("dp", String.class, String.class, String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }
}
