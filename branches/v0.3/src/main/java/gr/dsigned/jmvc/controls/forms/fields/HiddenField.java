/*
 *  HiddenField.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
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
 *
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class HiddenField extends Field{
    
    /**
     * Construcrtor for a hidden field that will not need the post value of the field
     * 
     * @param fieldName
     * @param value (the value that we assign to the input's value attribute)
     * @param rules
     */
    public HiddenField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, rules);
        this.value = value ;
        isRequired();
    }

    @Override
    public String renderField() {
        return String.format("<input type='hidden' name='%1$s' id='%2$s' value='%3$s' />", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
        //return template;
    }

    @Override
    public String renderLabel() {
        return "";// this field has no label
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
