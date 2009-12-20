/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.routing;

import gr.dsigned.jmvc.framework.TestController;
import gr.dsigned.jmvc.interfaces.Controller;
import java.lang.reflect.Method;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nk
 */
public class RouterTest {

    Controller testController;
    Class controllerClass;
    Router instance;
    Method method1;
    Method method2;

    public RouterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        testController = new TestController();
        controllerClass = testController.getClass();
        instance = new Router();
        instance.addController(controllerClass);
        try {
            method1 = TestController.class.getDeclaredMethod("index", new Class[0]);
            method2 = TestController.class.getDeclaredMethod("testIndex", new Class[0]);
        } catch (Exception ex) {
            // do nothing
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetControllerClass() {
        Class expResult = TestController.class;
        Class result = instance.getControllerClass(""); // Test empty request URI
        assertEquals(expResult, result);
        result = instance.getControllerClass("TestController"); // Test case sensitive
        assertEquals(expResult, result);
        result = instance.getControllerClass("TeStCoNtrOLLer"); // Test case insensitive
        assertEquals(expResult, result);
        result = instance.getControllerClass("controllertest"); // Test alias case sensitive
        assertEquals(expResult, result);
        result = instance.getControllerClass("ControLLerTest"); // Test alias case insensitive
        assertEquals(expResult, result);
        result = instance.getControllerClass("TestController/blah/blah"); // Test with long request URI
        assertEquals(expResult, result);
        result = instance.getControllerClass("ControLLerTest/blah/blah"); // Test alias with long request URI
        assertEquals(expResult, result);
        result = instance.getControllerClass("TestController/blah/blah?blah=blah"); // Test with long request URI & parameters
        assertEquals(expResult, result);
        result = instance.getControllerClass("/TestController/blah/blah?blah=blah"); // Test with beginning slash
        assertEquals(expResult, result);
        result = instance.getControllerClass("/TestController/blah/blah/"); // Test with beginning slash and ending slash
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMethodClass() {
        Method expResult = method1;
        Method result = instance.getMethodClass(""); // Test empty URI
        assertEquals(expResult, result);
        result = instance.getMethodClass("TestController"); // Just the Controller (should return index)
        assertEquals(expResult, result);
        result = instance.getMethodClass("TestController/index"); // Controller & method1
        assertEquals(expResult, result);
        result = instance.getMethodClass("TestController/index/"); // Controller & method1 ending slash
        assertEquals(expResult, result);
        result = instance.getMethodClass("/TestController/index"); // Controller & method1 beginning slash
        assertEquals(expResult, result);
        result = instance.getMethodClass("/TestController/index/"); // Controller & method1 beginning & ending slash
        assertEquals(expResult, result);
        result = instance.getMethodClass("/TestController/index/?blah=blah"); // Controller & method1 beginning & ending slash & parameters
        assertEquals(expResult, result);
        expResult = method2;
        result = instance.getMethodClass("TestController/testIndex"); // Controller & method1 with duplicate name (should return the 1st found)
        assertEquals(expResult, result);
        result = instance.getMethodClass("TestController/TESTIndex"); // Controller & method1 with duplicate name case insensitive (should return the 1st found)
        assertEquals(expResult, result);
    }

    @Test
    public void testGetControllerNameFromURI() {
        String requestURI = "TestController";
        String expResult = "testcontroller";
        String result = instance.getControllerNameFromURI(requestURI);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMethodNameFromURI() {
        String requestURI = "TestController/index";
        String expResult = "index";
        String result = instance.getMethodNameFromURI(requestURI);
        assertEquals(expResult, result);
    }
}
