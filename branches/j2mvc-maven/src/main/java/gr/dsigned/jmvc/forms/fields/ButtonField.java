/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.forms.fields.Field.Rule;
import gr.dsigned.jmvc.types.Tuple2;

/**
 *
 * @author USER
 */
public class ButtonField extends Field{
    String template = "<input type='button'>";

    public ButtonField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='button' name='%1$s' id='id_%2$s' value='%3$s' />%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
    }

    @Override
    public String renderLabel() {
        return "";
    }

}
