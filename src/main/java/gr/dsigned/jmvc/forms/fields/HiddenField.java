/*
 *  HiddenField.java
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

import gr.dsigned.jmvc.types.Tuple2;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class HiddenField extends Field{
    String template = "<input type='submit'>";

    public HiddenField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='hidden' name='%1$s' id='id_%2$s' />%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
        //return template;
    }


    @Override
    public String renderLabel() {
        return "";// this field has no label
    }

}
