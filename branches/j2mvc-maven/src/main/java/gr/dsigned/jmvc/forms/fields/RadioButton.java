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
public class RadioButton extends Field {

    String template = "<input type='radio' name='%1$s' id='%2$s' value='%3$s' %4$s />%n";

    public RadioButton(String labelName, String fieldName, String value, String checked, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, checked, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='radio' name='%1$s' id='%2$s' value='%3$s' %4$s />%n", getFieldName(), "id_" + getFieldName(), getLabelName(), getChecked(), getErrors());
    }

}
