/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.annotations.ControllerURLAlias;
import gr.dsigned.jmvc.annotations.MethodURLAlias;
import org.junit.Test;

/**
 *
 * @author nk
 */
@ControllerURLAlias("controllertest")
public class TestController extends Controller {

    @MethodURLAlias("methodtest")
    public void index() {
    }

    @Test
    public void empty() {
    }
}
