/*
 *  PasswordField.java
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
public class PasswordField extends Field {

    private boolean disabled = false ;
    private boolean readonly = false ;
    
    String template = "<input type='password' name='%1$s' id='%2$s' value='%3$s'/>%n";

    /**
     * 
     * @param labelName
     * @param fieldName
     * @param value
     * @param rules
     */
    public PasswordField(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='password' name='%1$s' id='%2$s' value='%3$s' %4$s %5$s />%n", getFieldName(), "id_" + getFieldName(), getValue(), isDisabled(), isReadonly(), getErrors());
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
