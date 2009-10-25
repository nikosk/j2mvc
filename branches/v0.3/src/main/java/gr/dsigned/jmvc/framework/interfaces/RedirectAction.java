/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.framework.interfaces;

/**
 *
 * @author nk
 */
public class RedirectAction implements Action {

    private String redirectURL;

    public RedirectAction(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getRedirectURL() {
        return redirectURL;
    }
}
