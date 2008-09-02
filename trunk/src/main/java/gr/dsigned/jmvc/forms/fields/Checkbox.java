/*
 *  Checkbox.java
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
public class Checkbox extends Field {

    protected boolean checked;
    
    String template = "<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s />%n";

    public Checkbox(String labelName, String fieldName, String inputValue, String value, boolean checked, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, inputValue, value, rules);
        this.checked = checked ; 
    }

    @Override
    public String renderField() {
        return String.format("<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s />%n", getFieldName(), "id_" + getFieldName(), getInputValue(), ((isChecked())? " checked " : ""), getErrors());
    }
    
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
