/*
 *  TextareaField.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <vchrys@gmail.com>
 *
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */

package gr.dsigned.jmvc.controls.forms.fields;

import gr.dsigned.jmvc.types.Tuple2;

/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class TextareaField extends Field {
    
    private String cols;
    private String rows;
    private boolean disabled = false;
    private boolean readonly = false;

    String template = "<textarea name='%1$s' id='%2$s' rows='%3$s' cols='%4$s'>%5$s</textarea>%n";

    /**
     * 
     * @param labelName
     * @param fieldName
     * @param rows
     * @param cols
     * @param value
     * @param rules
     */
    public TextareaField(String labelName, String fieldName, String rows, String cols, String value, Tuple2<Rule, String>... rules) {
        super(labelName,fieldName, value, rules);
        setRows(rows);
        setCols(cols);
    }

    @Override
    public String renderField() {
        return String.format("<textarea name='%1$s' id='%2$s' rows='%3$s' cols='%4$s' %6$s %7$s >%5$s</textarea>%n", getFieldName(), "id_" + getFieldName(), getRows(), getCols(), getValue(), isDisabled(), isReadonly(), getErrors());
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
    
    public String isDisabled() {
        String out = "" ;
        if(disabled){
            out = " disabled " ;
        }else{
            out = "" ;
        }
        return out;
    }

    public void setDisabled() {
        this.disabled = true;
    }

    public String isReadonly() {
        String out = "" ;
        if(readonly){
            out = " readonly " ;
        }else{
            out = "" ;
        }
        return out;
    }

    public void setReadonly() {
        this.readonly = true;
    }
}
