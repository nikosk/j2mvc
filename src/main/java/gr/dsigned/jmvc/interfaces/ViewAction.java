/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.interfaces;

import java.util.Map;

/**
 *
 * @author nk
 */
public interface ViewAction extends Action{
    public Map<String, Object> getData();
    public String getTemplateName();
}
