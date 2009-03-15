/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.controllers;

import gr.dsigned.jmvc.forms.NewForms;
import gr.dsigned.jmvc.forms.fields.CharField;
import gr.dsigned.jmvc.forms.fields.Field.Rule;
import gr.dsigned.jmvc.forms.fields.PasswordField;
import gr.dsigned.jmvc.forms.fields.SubmitButton;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.PageData;
import static gr.dsigned.jmvc.types.operators.*;

/**
 *
 * @author nk
 */
public class Admin extends Controller {

    public Admin() throws Exception {
    }

    public void index() throws Exception {
        NewForms f = new NewForms();
        f.setAction($.request.getRequestURI());

        f.setFields(new CharField("username", "username", $.input.post("username"), o(Rule.REQUIRED, "true"), o(Rule.ALPHANUM, "true")));
        f.setFields(new PasswordField("password", "password", $.input.post("password"), o(Rule.REQUIRED, "true")));
        f.setFields(new SubmitButton("submit", "submit"));
        if ($.input.isPost()) {
            if (f.isValid()) {
                if (true) {                    
                    $.response.sendRedirect("/admin/articles");
                    return;
                } else {
                    f.setErrorMessage("password", "Λάθος κωδικός");
                }
            }
        }        
        $.loadView("index", new PageData());
    }
}
