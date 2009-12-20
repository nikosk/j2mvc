/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.interfaces.Controller;
import gr.dsigned.jmvc.annotations.ControllerURLAlias;
import gr.dsigned.jmvc.annotations.MethodURLAlias;
import gr.dsigned.jmvc.annotations.NotPublic;
import org.junit.Test;

/**
 *
 * @author nk
 */
@ControllerURLAlias("controllertest")
public class TestController implements Controller {

    @MethodURLAlias("methodtest")
    public void index() {
    }

    @MethodURLAlias("testIndex")
    public void testIndex() {
    }

    @MethodURLAlias("TestIndeX")
    public void TestIndeX() {
    }

    @Test
    @NotPublic
    public void empty() {
    }
}
