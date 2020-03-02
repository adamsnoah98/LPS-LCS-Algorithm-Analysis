import Algos.LCS;
import org.junit.Assert;

public class TestLCSDPSkip extends LCSFrameWork {

    public TestLCSDPSkip() {
        try {
            m = LCS.class.getDeclaredMethod("dpSkip", String.class, String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }

}
