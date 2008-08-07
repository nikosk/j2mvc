/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.framework.Jmvc;
import java.util.ArrayList;
import junit.framework.*;

/**
 *
 * @author Alkis Kafkis <a.kafkis@phiresoft.com>
 */
public class TestIssues extends TestCase {

    public void testCrudIssue() throws Exception {
        
        Issue issueModel = Jmvc.loadModel("Issue");

        ArrayList<Bean> initIssues = issueModel.getIssuesBySiteId("1");
        String issueId = issueModel.insertIssue("1", "testIssue", "testIssue desc");
        assertEquals(initIssues.size() + 1, issueModel.getIssuesBySiteId("1").size());
        
        issueModel.updateIssue(issueId, "new label", "new desc");
        assertEquals("new label", issueModel.getById(issueId).get("label"));
        assertEquals("new desc", issueModel.getById(issueId).get("description"));
        
        issueModel.delete(issueId);
        assertEquals(initIssues.size(), issueModel.getIssuesBySiteId("1").size());
    }
}
