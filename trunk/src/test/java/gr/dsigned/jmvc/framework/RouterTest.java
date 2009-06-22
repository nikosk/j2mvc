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
    public void testGetControllerName() {
        Router r = new Router();
        r.addControllerClass(SampleController.class);
        Assert.assertEquals(r.getControllerName("samplecontroller"),"Samplecontroller");
        Assert.assertEquals(r.getControllerName("controllertest"),"Samplecontroller");
        Assert.assertEquals(r.getControllerName("controllertest/methodtest"),"Samplecontroller");
        Assert.assertEquals(r.getControllerName("controllertest/methodtest"),"Samplecontroller");
    }

    @Test
    public void testGetControllerName_String() {
    }
}