/*
 *  ButtonField.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import gr.dsigned.jmvc.forms.Field;
import gr.dsigned.jmvc.forms.Field.Rule;
import gr.dsigned.jmvc.types.Tuple2;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class ButtonField extends Field {

    String template = "<input type='submit'>";

    public ButtonField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        //return String.format("<label for='id_%1$s'>%1$s</label><input type='text' name='%1$s' id='%2$s' value='%3$s'/>%4$s %n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
        return template;
    }
}
