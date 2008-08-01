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
public class ResetButtonField extends Field{
    
    String template = "<input type='reset'>";

    public ResetButtonField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='reset' name='%1$s' id='id_%2$s' />%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
    }

    @Override
    public String renderLabel() {
        return "";
    }
}
