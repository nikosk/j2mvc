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
        Jmvc framework = Jmvc.getInstance();
        Issue issueModel = framework.loadModel("Issue");

        ArrayList<Bean> initIssues = issueModel.getIssuesBySiteId("1");
        Bean issue = issueModel.insertIssue("1", "testIssue", "testIssue desc");
        assertEquals(initIssues.size() + 1, issueModel.getIssuesBySiteId("1").size());
        
        issueModel.updateIssue(issue.get("id"), "new label", "new desc");
        assertEquals("new label", issueModel.getById(issue.get("id")).get("label"));
        assertEquals("new desc", issueModel.getById(issue.get("id")).get("description"));
        
        issueModel.delete(issue.get("id"));
        assertEquals(initIssues.size(), issueModel.getIssuesBySiteId("1").size());
    }
}
