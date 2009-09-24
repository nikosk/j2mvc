/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.controls.forms.fields;

/**
 *
 * @author empty
 */
public class ButtonTagField extends Field{


    /**
     * @param fieldName
     * @param value
     * @param rules
     */


    public ButtonTagField(String fieldName, String value) {
        super(fieldName, value);
    }

    @Override
    public String renderField() {
        return String.format("<button class='headBtn' type='submit' name='%1$s' id='%2$s'>%3$s</button>", getFieldName(), "id_" + getFieldName(), getValue());
    }

    @Override
    public String renderLabel() {
        return "";
    }

	
}
