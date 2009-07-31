/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.controllers.CMSController;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        r.addControllerClass(CMSController.class);

        Assert.assertEquals(r.getControllerName("cmscontroller"), "Cmscontroller");
        Assert.assertEquals(r.getControllerName("cmscontroller"), "Cmscontroller");
        Assert.assertEquals(r.getControllerName("controllertest/methodtest"), "Cmscontroller");
        Assert.assertEquals(r.getControllerName("controllertest/methodtest"), "Cmscontroller");

        Assert.assertEquals(r.getControllerClassByReqURI("controllertest/methodtest"), CMSController.class);
        Assert.assertEquals(r.getControllerClassByReqURI("cmscontroller/methodtest"), CMSController.class);
        Assert.assertEquals(r.getControllerClassByReqURI("cmscontroller/index"), CMSController.class);
        Assert.assertEquals(r.getControllerClassByReqURI("controllertest/index"), CMSController.class);

        Assert.assertEquals(r.getMethodName("controllertest/index"), "index");
        Assert.assertEquals(r.getMethodName("cmscontroller/index"), "index");
        Assert.assertEquals(r.getMethodName("controllertest/methodtest"), "index");
        Assert.assertEquals(r.getMethodName("cmscontroller/methodtest"), "index");
        
        try {
            Assert.assertEquals(r.getMethodClassByReqURI("controllertest/index"), CMSController.class.getMethod("index", new Class[0]));
            Assert.assertEquals(r.getMethodClassByReqURI("cmscontroller/index"), CMSController.class.getMethod("index", new Class[0]));
            Assert.assertEquals(r.getMethodClassByReqURI("controllertest/methodtest"), CMSController.class.getMethod("index", new Class[0]));
            Assert.assertEquals(r.getMethodClassByReqURI("cmscontroller/methodtest"), CMSController.class.getMethod("index", new Class[0]));
        } catch (Exception ex) {
            //Do nothing
            Assert.fail(ex.getMessage());
        }

    }

    @Test
    public void testGetControllerName_String() {
    }
}
