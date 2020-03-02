import Algos.LPS;
import org.junit.Assert;

public class TestLPSMana extends LPSFrameWork {

    public TestLPSMana() {
        try {
            super.m = LPS.class.getDeclaredMethod("manachers", String.class);
        } catch (NoSuchMethodException e) {
            Assert.fail();
        }
    }

}
