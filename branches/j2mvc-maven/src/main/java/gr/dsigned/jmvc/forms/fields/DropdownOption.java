/*
 *  DropdownOption.java
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
