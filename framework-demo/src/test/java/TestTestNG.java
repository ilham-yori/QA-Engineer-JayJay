import org.ilhamyp.Calculator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestTestNG {

    @Test
    public void testPlus(){
        float result = Calculator.plus(2, 2);
        Assert.assertEquals(result, 4.0f);
    }

    @Test
    public void testSubs(){
        float result = Calculator.distribution(2, 2);
        Assert.assertEquals(result, 1);
    }

    @Test
    public void testMinus(){
        float result = Calculator.minus(2, 2);
        Assert.assertEquals(result, 0);
    }

    @Test
    public void testMultiply(){
        float result = Calculator.multiply(2, 2);
        Assert.assertEquals(result, 4);
    }


}
