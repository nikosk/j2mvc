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
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Bean;
import gr.dsigned.jmvc.framework.Library;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class NewForms extends Library {

    private Bean fields = new Bean();
    private LinkedHashMap<String, ArrayList<String>> rules = new LinkedHashMap<String, ArrayList<String>>();
    private Bean errors = new Bean();
    private boolean formIsValid = false;

    public NewForms() {
    }

    public void addField(String name, FormElems ft, String... validationRules) {
        fields.put(name, ft.name());
        ArrayList<String> validationRulesList = new ArrayList<String>();
        for (String r : validationRules) {
            validationRulesList.add(r);
        }
        rules.put(name, validationRulesList);
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("<form id='' method='post' action='/sites/show_form/'>");
        sb.append("<span style='color:#FF0000;'>");
        for (String e : errors.keySet()) {
            sb.append(errors.get(e)).append(", ");
        }
        sb.append("</span>");
        for (String fieldName : fields.keySet()) {
            String field = FormElems.valueOf(fields.get(fieldName)).toString();
            sb.append("<div>");
            sb.append("<span>").append(fieldName).append(" ").append("</span>");
            sb.append(renderField(FormElems.valueOf(FormElems.class, fields.get(fieldName)), fieldName, ""));
            sb.append("</div>");
        }
        sb.append("<input type='submit' value='Submit'/>");
        sb.append("</form>");
        return sb.toString();
    }
    
    public String renderField(FormElems ftype, String fname, String data){
        String out = "";
        switch(ftype){
            case INPUT_TEXT:
                out = "<input type='text' name='"+fname+"' id='id_"+fname+"' value='"+data+"'/>";
                break;
            case INPUT_PASS:
                out = "<input type='password' name='"+fname+"' id='id_"+fname+"' value='"+data+"'/>";
                break;
            case HIDDEN:
                out = "<input type='hidden' name='"+fname+"' id='id_"+fname+"' value='"+data+"'/>";
                break;
            case TEXTAREA:
                out = "<textarea name='"+fname+"' id='id_"+fname+"'>"+data+"</textarea>";
                break;
            case FILE:
                out = "<input type='file' name='"+fname+"' id='id_"+fname+"' />";
                break;
            default:
                break;
        }
        return out;
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
