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
public class TextareaField extends Field {
    
    protected String cols;
    protected String rows;

    String template = "<textarea name='%1$s' id='%2$s' rows='%3$s' cols='%4$s'>%5$s</textarea>%n";

    public TextareaField(String fieldName, String rows, String cols, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<textarea name='%1$s' id='%2$s' rows='%3$s' cols='%4$s'>%5$s</textarea>%n", getFieldName(), "id_" + getFieldName(), getRows(), getCols(), getValue(), getErrors());
    }
        
    public String getCols() {
        return cols;
    }

    public void setCols(String cols) {
        this.cols = cols;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}
