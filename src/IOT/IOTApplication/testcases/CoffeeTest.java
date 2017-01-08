package IOT.IOTApplication.testcases;

import IOT.IOTApplication.coffeemachine.CoffeeMachineService;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by Emily on 1/7/2017.
 * Tests the  basic functionality of the class CoffeeMachineService.
 */
public class CoffeeTest {
    /** The class to be tested. */
    private CoffeeMachineService coffeeMachineService = new CoffeeMachineService(null);
    /** The default delay after which coffee is to be made.*/
    static private final int COFFEE_DELAY = 5000; // 5 seconds

    @Test
    public void testAddCoffeetime() {
        long time = System.currentTimeMillis() + COFFEE_DELAY;
        assertEquals(0,coffeeMachineService.addCoffeeTime(time));
    }

    @Test
    public void testDuplicateCoffetime() {
        long time = System.currentTimeMillis() + COFFEE_DELAY;
        coffeeMachineService.addCoffeeTime(time);
        assertEquals(CoffeeMachineService.ERROR_CMS_INVALID_TIME, coffeeMachineService.addCoffeeTime(time));
    }

    @Test
    public void testPastCoffeetime() {
        long time = System.currentTimeMillis() - COFFEE_DELAY;
        assertEquals(CoffeeMachineService.ERROR_CMS_INVALID_TIME, coffeeMachineService.addCoffeeTime(time));
    }
}
