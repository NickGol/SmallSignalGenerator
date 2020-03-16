import SinusGeneratorPackage.SinusGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SinusGeneratorTest {

    private int capacity = 55;
    private SinusGenerator sinusGenerator;

    @Before
    public void load() {
        sinusGenerator = new SinusGenerator(capacity);
    }

    @Test
    public void checkGetterFunction_1() {

        Float[] firstStep = sinusGenerator.getSignalData();
        assertEquals(firstStep.length, capacity);
    }

    @Test
    public void checkGetterFunction_2() {

        Float[] firstStep = sinusGenerator.getSignalData(25);
        assertEquals(firstStep.length, 25);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkGetterFunctionExceptionBigValue() {

        Float[] firstStep = sinusGenerator.getSignalData(capacity+1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkGetterFunctionExceptionNegativeValue() {

        Float[] firstStep = sinusGenerator.getSignalData(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkConstructorExceptionZeroValue() {

        SinusGenerator sinusGenerator = new SinusGenerator(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkConstructorExceptionNegativeValue() {

        SinusGenerator sinusGenerator = new SinusGenerator(-1);
    }
}
