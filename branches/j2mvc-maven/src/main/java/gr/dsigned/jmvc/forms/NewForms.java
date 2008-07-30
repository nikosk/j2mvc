/*
 *  NewForms.java
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
package gr.dsigned.jmvc.forms;

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.framework.Library;
import java.util.ArrayList;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class NewForms extends Library {

    private ArrayList<Field> fields = new ArrayList<Field>();
    private Bean errors = new Bean();

    public String build() {
        String out = "";
        for (Field f : this.fields) {
            out += f.renderField();
        }
        return out;
    }

    public void setFields(Field... fields) {
        for (Field f : fields) {
            this.fields.add(f);
        }
    }

    /**
     * enum type that returns a field html tag with hooks to
     * pass parameters. Params : 1. name: will be used as id
     * as well. Required. 2. value: Pass it to fields that
     * need a value. 3. method: Only pass it to a FORM_OPEN
     * 4. selected: Pass the string selected if the field is
     * an OPTION.
     */
    public enum FormElems {

        INPUT_TEXT("<input type='text' name='%1$s' id='%1$s' value='%2$s'/>%n"),
        INPUT_PASS("%n"),
        TEXTAREA("<textarea name='%1$s' id='%1$s'>%2$s</textarea>%n"),
        HIDDEN("<input type='hidden' name='%1$s' value='%2$s' />%n"),
        CHECKBOX("<input type='checkbox' name='%1$s' value='%2$s' id='%1$s' />%n"),
        BUTTON(""),
        RADIOBUTTON("<input name='%1$s' type='radio' value='%2$s' id='%1$s' />%n"),
        FILE("<input type='file' name='%1$s' id='%1$s' />%n"),
        SELECT("<select name='%1$s' id='%1$s'>%2$s</select>%n"),
        OPTION("<option value='%2$s' %4$s>%3$s</option>%n");
        private final String body;

        FormElems(String body) {
            this.body = body;
        }

        public String toString() {
            return this.body;
        }
    }
}
