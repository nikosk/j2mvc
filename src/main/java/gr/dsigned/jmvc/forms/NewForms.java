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
    public boolean isValid(){
        boolean valid = true;
        for(Field f : fields){
            if(!f.validates()){
                valid = false;
            }
        }
        return valid;
    }
}
