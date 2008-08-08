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
public class FileField extends Field{
    
    String template = "<input type='file' name='%1$s' id='%2$s' value='%3$s'/>%n";

    public FileField(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='file' name='%1$s' id='%2$s' value='%3$s'/>%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
    }

}
