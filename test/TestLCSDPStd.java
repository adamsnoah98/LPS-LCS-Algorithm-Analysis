import Algos.LCS;
import org.junit.Assert;

public class TestLCSDPStd extends LCSFrameWork {

    public TestLCSDPStd() {
        try {
            super.m = LCS.class.getDeclaredMethod("dpStd", String.class, String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }
}
