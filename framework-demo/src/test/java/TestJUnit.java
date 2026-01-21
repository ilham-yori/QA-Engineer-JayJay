import org.ilhamyp.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJUnit {

    @Test
    public void testMinus(){
        float result = Calculator.minus(2, 2);
        Assertions.assertEquals(0, result);
    }

    @Test
    public void testMinusNotNull(){
        float result = Calculator.minus(2, 2);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testPlus(){
        float result = Calculator.plus(2, 2);
        Assertions.assertEquals(4, result);
    }

    @Test
    public void testPlusNotNull(){
        float result = Calculator.plus(2, 2);
        Assertions.assertNotNull(result);
    }
}
