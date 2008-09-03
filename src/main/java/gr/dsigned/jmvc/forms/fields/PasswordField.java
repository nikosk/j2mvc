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
package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.types.Tuple2;

 /**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class PasswordField extends Field {

    private boolean disabled;
    private boolean readonly;
    
    String template = "<input type='password' name='%1$s' id='%2$s' value='%3$s'/>%n";

    /**
     * 
     * @param labelName
     * @param fieldName
     * @param value
     * @param disabled
     * @param readonly
     * @param rules
     */
    public PasswordField(String labelName, String fieldName, String value, boolean disabled, boolean readonly, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
        setDisabled(disabled);
        setReadonly(readonly);
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='password' name='%1$s' id='%2$s' value='%3$s' %4$s %5$s />%n", getFieldName(), "id_" + getFieldName(), getValue(), ((isDisabled())? " disabled " : ""), ((isReadonly())? " readonly " : ""), getErrors());
    }
    
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

}
