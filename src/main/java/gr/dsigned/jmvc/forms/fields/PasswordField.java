/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.types.Tuple2;

/**
 *
 * @author USER
 */
public class PasswordField extends Field {

    String template = "<input type='password' name='%1$s' id='%2$s' value='%3$s'/>%n";

    public PasswordField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='password' name='%1$s' id='%2$s' value='%3$s'/>%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
    }
}
