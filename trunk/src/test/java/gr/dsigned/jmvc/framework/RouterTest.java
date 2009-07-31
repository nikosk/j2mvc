/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class RouterTest {

    public RouterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetController() {
        Router r = new Router();
        r.addControllerClass(TestController.class);

        Assert.assertEquals(r.getControllerName("testcontroller"), "Testcontroller");
        Assert.assertEquals(r.getControllerName("testcontroller"), "Testcontroller");
        Assert.assertEquals(r.getControllerName("controllertest/methodtest"), "Testcontroller");
        Assert.assertEquals(r.getControllerName("controllertest/methodtest"), "Testcontroller");

        Assert.assertEquals(r.getControllerClassByReqURI("controllertest/methodtest"), TestController.class);
        Assert.assertEquals(r.getControllerClassByReqURI("testcontroller/methodtest"), TestController.class);
        Assert.assertEquals(r.getControllerClassByReqURI("testcontroller/index"), TestController.class);
        Assert.assertEquals(r.getControllerClassByReqURI("controllertest/index"), TestController.class);

        Assert.assertEquals(r.getMethodName("controllertest/index"), "index");
        Assert.assertEquals(r.getMethodName("testcontroller/index"), "index");
        Assert.assertEquals(r.getMethodName("controllertest/methodtest"), "index");
        Assert.assertEquals(r.getMethodName("testcontroller/methodtest"), "index");
        
        try {
            Assert.assertEquals(r.getMethodClassByReqURI("controllertest/index"), TestController.class.getMethod("index", new Class[0]));
            Assert.assertEquals(r.getMethodClassByReqURI("testcontroller/index"), TestController.class.getMethod("index", new Class[0]));
            Assert.assertEquals(r.getMethodClassByReqURI("controllertest/methodtest"), TestController.class.getMethod("index", new Class[0]));
            Assert.assertEquals(r.getMethodClassByReqURI("testcontroller/methodtest"), TestController.class.getMethod("index", new Class[0]));
        } catch (Exception ex) {
            //Do nothing
            Assert.fail(ex.getMessage());
        }

    }

    @Test
    public void testGetControllerName_String() {
    }
}
