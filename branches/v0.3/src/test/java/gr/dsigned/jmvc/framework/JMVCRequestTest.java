/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.UploadFileSpec;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import gr.dsigned.jmvc.servlets.JmvcApplicationController;
import java.io.File;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
public class JMVCRequestTest {

    private static JMVCRequest jMVCRequest;
    private static File tmpFile;

    public JMVCRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("JmvcApplicationController", JmvcApplicationController.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request = new GetMethodWebRequest("http://localhost:8080/");
        InvocationContext ic = sc.newInvocation(request);
        jMVCRequest = new JMVCRequest(ic.getRequest());
        tmpFile = new File(System.getProperty("java.io.tmpdir") + "JmvcTestFile.test.txt");
        tmpFile.createNewFile();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        tmpFile.delete();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDoUpload() throws Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("JmvcApplicationController", JmvcApplicationController.class.getName());
        ServletUnitClient sc = sr.newClient();
        PostMethodWebRequest request = new PostMethodWebRequest("http://localhost:8080/");
       
        UploadFileSpec[] spec = new UploadFileSpec[]{new UploadFileSpec(tmpFile)};
        request.setParameter("", "test");
        request.setParameter("aFile",spec);
        
        InvocationContext ic = sc.newInvocation(request);
        assertTrue(ServletFileUpload.isMultipartContent(jMVCRequest));
        jMVCRequest = new JMVCRequest(ic.getRequest());
        assertNotNull(jMVCRequest.getUploadedFile("aFile"));
    }

//    @Test
//    public void testSegment_int() throws Exception {
//        System.out.println("segment");
//        int index = 0;
//        JMVCRequest instance = jMVCRequest;
//        String expResult = "";
//        String result = instance.segment(index);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSegment_String() throws Exception {
//        System.out.println("segment");
//        String key = "";
//        JMVCRequest instance = null;
//        String expResult = "";
//        String result = instance.segment(key);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testPost() {
//        System.out.println("post");
//        String paramName = "";
//        JMVCRequest instance = null;
//        String expResult = "";
//        String result = instance.post(paramName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetPostData() {
//        System.out.println("getPostData");
//        JMVCRequest instance = null;
//        Map expResult = null;
//        Map result = instance.getPostData();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetUploadedFile() {
//        System.out.println("getUploadedFile");
//        String paramName = "";
//        JMVCRequest instance = null;
//        File expResult = null;
//        File result = instance.getUploadedFile(paramName);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testIsPost() {
//        System.out.println("isPost");
//        JMVCRequest instance = null;
//        boolean expResult = false;
//        boolean result = instance.isPost();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testIsGet() {
//        System.out.println("isGet");
//        JMVCRequest instance = null;
//        boolean expResult = false;
//        boolean result = instance.isGet();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
