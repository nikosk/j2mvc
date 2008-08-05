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
public class DropdownOption {
    
    String template = "<option value='%1$s'>%2$s</option>%n";
    protected String fieldName;
    protected String value;
    protected String selected;
    //protected sellected ;
    
    public DropdownOption(String fieldName, String value, String selected) {
        this.fieldName = fieldName ;
        this.value = value ;
        this.selected = selected ;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
 
//    public String renderOption() {
//        return String.format("<option value='%1$s'>%1$s</option>%n", getFieldName(), getErrors());
//    }

}
