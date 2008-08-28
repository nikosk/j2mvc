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

import gr.dsigned.jmvc.forms.fields.Field;
import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.framework.Library;
import java.util.ArrayList;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class NewForms extends Library {

    private ArrayList<Field> fields = new ArrayList<Field>();
    private ArrayList errors = new ArrayList();
    private Hmap hmap = new Hmap();
    
    public String submitBtn = "";
    public String resetBtn = "";

    public String build() {
        return buildAsTable();
    }

    private String buildAsTable(){
        StringBuilder sb = new StringBuilder("<table>");
        for(Field f : fields){
            sb.append("<tr>");
            sb.append("<td>");
            if(f.isRequired()){
                sb.append("<span style='color: #FF0000;'>*&nbsp;</span>");
            }
            sb.append(f.renderLabel());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(f.renderField());
            sb.append(f.renderErrors());
            sb.append("</td>");
            sb.append("</tr>");
        }
        if(!submitBtn.equals("") || !resetBtn.equals("")){
            sb.append("<tr>").append("<td>&nbsp;</td><td>").append(submitBtn).append(resetBtn).append("</td>").append("</tr>");
        }  
        sb.append("</table>");
        return sb.toString();
    }

    public String buildAsUList(){
        StringBuilder sb = new StringBuilder("<ul>");
        for(Field f : fields){
            sb.append("<li>");
            sb.append(f.renderLabel());
            sb.append("</li>");
            sb.append("<li>");
            sb.append(f.renderField());
            sb.append(f.renderErrors());
            sb.append("</li>");
        }
        
        if(!submitBtn.equals("") || !resetBtn.equals("")){
            sb.append("<li>").append(submitBtn).append(resetBtn).append("</li>");
        }  

        sb.append("</ul>");
        return sb.toString();
    }

    public void setFields(Field... fields) {
        Hmap b = this.hmap ;
        if(b.isEmpty()){
            for (Field f : fields) {
                this.fields.add(f); 
            }
        }else{
            for (Field f : fields) {
                f.setValue(b.get(f.getFieldName()));
                this.fields.add(f);
            }
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
    
    public Hmap getFormData(){
        Hmap hmap = new Hmap() ;
        for(Field f : fields){
            hmap.put(f.getFieldName(), f.getValue()) ;
        } 
        return hmap;
    }
    
    public ArrayList getFields(){
        return fields;
    }

    public Hmap getHmap() {
        return hmap;
    }

    public void setBean(Hmap hmap) {
        this.hmap = hmap;
    }

    public ArrayList getErrors() {
        return errors;
    }

    public void setErrors(ArrayList errors) {
        this.errors = errors;
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
    
    public void setSubmitBtn(String submit){
        this.submitBtn = submit;
    }

    public void setResetBtn(String reset){
        this.resetBtn = reset;
    }    
}
