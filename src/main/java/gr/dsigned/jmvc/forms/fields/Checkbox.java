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
public class Checkbox extends Field {

    String template = "<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s />%n";

    public Checkbox(String labelName, String fieldName, String inputValue, String value, String checked, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, inputValue, value, checked, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s />%n", getFieldName(), "id_" + getFieldName(), getInputValue(), getChecked(), getErrors());
    }

}
