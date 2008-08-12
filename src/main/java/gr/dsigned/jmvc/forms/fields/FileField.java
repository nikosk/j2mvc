/*
 *  FileField.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <nikosk@dsigned.gr>
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

import gr.dsigned.jmvc.forms.fields.Field.Rule;
import gr.dsigned.jmvc.types.Tuple2;

 /**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class FileField extends Field{
    
    String template = "<input type='file' name='%1$s' id='%2$s' value='%3$s'/>%n";

    public FileField(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='file' name='%1$s' id='%2$s' value='%3$s'/>%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
    }

}