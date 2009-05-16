/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.framework;

import java.util.SortedMap;
import java.util.TreeMap;
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
//        SortedMap<String,String> entries = new TreeMap<String,String>();
//        entries.put("/admin/", "AdminController");
//        entries.put("/admin", "AdminController");
//        entries.put("/admin/articles/edit/", "AdminController.edit");
//        entries.put("/article/preview/", "AdminController");
//        Router r = new Router();
//        r.setEntries(entries);
//        Assert.assertEquals(r.getControllerName("/admin/articles/edit/"),"AdminController.edit");
    }

    @Test
    public void testGetControllerName_String() {
    }

}