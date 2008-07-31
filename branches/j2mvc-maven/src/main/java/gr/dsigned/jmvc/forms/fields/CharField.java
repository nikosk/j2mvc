/*
 *  CharField.java
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
public class CharField extends Field {

    String template = "<input type='text' name='%1$s' id='%1$s' value='%2$s'/>%n";

    public CharField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='text' name='%1$s' id='%2$s' value='%3$s'/>%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
    }
    
    
}
