/*
 *  NewForms.java
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
package gr.dsigned.jmvc.controls.forms;

import gr.dsigned.jmvc.controls.HTMLControl;
import gr.dsigned.jmvc.controls.forms.fields.Field;
import gr.dsigned.jmvc.controls.forms.fields.HiddenField;
import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.framework.Library;
import java.util.LinkedHashMap;
import static gr.dsigned.jmvc.framework.Renderer.*;
import static gr.dsigned.jmvc.types.operators.*;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class NewForms extends HTMLControl {

    private LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
    private String id;
    private String action;
    private boolean multipart = false;

    public NewForms() {
    }

    public NewForms(String act) {
        this.action = act;
    }

    @Override
    public String renderControl() {
        return String.format("<form id='%4$s' action='%1$s' method='post' %2$s>%3$s</form>", getAction(), enctype(), buildAsUList(), getId());
    }
    public String renderFormTag(){
        return String.format("<form id='%3$s' action='%1$s' method='post' %2$s>", getAction(), enctype(), getId());
    }
    private String buildAsTable() {
        StringBuilder sb = new StringBuilder("<table>");
        sb.append("\n\t");
        for (String name : fields.keySet()) {
            Field f = fields.get(name);
            sb.append("<tr>");
            sb.append("\n\t");
            sb.append("<td>");
            if (f.isRequired()) {
                sb.append("<span style='color: #FF0000;'>*&nbsp;</span>");
            }
            sb.append(f.renderLabel());
            sb.append("</td>");
            sb.append("\n\t");
            sb.append("<td>");
            sb.append(f.renderField());
            sb.append(f.renderErrors());
            sb.append("</td>");
            sb.append("\n");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    public String buildAsUList() {
        StringBuilder listSB = new StringBuilder("\n<ul>");
        StringBuilder hiddenSB = new StringBuilder();
        for (String name : fields.keySet()) {
            Field f = fields.get(name);
            if (f instanceof HiddenField) {
                hiddenSB.append(f.renderField());
            } else {
                listSB.append("\n\t");
                listSB.append("<li>");
                listSB.append(span(f.renderLabel(), o("class", "label_span")));
                listSB.append("</li>");
                listSB.append("\n\t");
                listSB.append("<li>");
                listSB.append(div(f.renderField()));
                listSB.append(f.renderErrors());
                listSB.append("</li>");
            }
        }
        listSB.append("\n");
        listSB.append("</ul>");
        listSB.append(div(hiddenSB.toString()));
        return listSB.toString();
    }

    public void setFields(Field... fields) {
        for (Field f : fields) {
            this.fields.put(f.getFieldName(), f);
        }

    }

    /**
     * Takes an Hmap<fieldName,value> and sets
     * the data to the fields in the list
     * @param data Hmap<fieldName,value>
     */
    public void setFormData(Hmap data) {
        for (String k : data.keySet()) {
            Field f = fields.get(k);
            if (f != null) {
                f.setValue(data.get(k));
            }
        }
    }

    /**
     * Checks to see if the form is valid by iterating the fields.
     * When you render the form before calling isValid 
     * no error messages are shown.
     * @return True if valid
     */
    public boolean isValid() {
        boolean valid = true;
        for (String name : fields.keySet()) {
            Field f = fields.get(name);
            if (!f.validates()) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Get the data.
     * @return Hmap<fieldName,Value>
     */
    public Hmap getFormData() {
        Hmap hmap = new Hmap();
        for (String name : fields.keySet()) {
            Field f = fields.get(name);
            hmap.put(f.getFieldName(), f.getValue());
        }
        return hmap;
    }

    /**
     * Map of the fields in this form in case 
     * you want to process them in the controller.
     * @return
     */
    public LinkedHashMap<String, Field> getFields() {
        return fields;
    }

    /**
     * Map of the fields in this form in case
     * you want to process them in the controller.
     * @return
     */
    public Field getField(String fieldName) {
        return fields.get(fieldName);
    }

    /**
     * Returns a Map of --field name, error message--.
     * Caution: When getErrorMessages is called 
     * no errors will be rendered when you build the form
     * until you called isValid again.
     * @return A list of DIVs with error messages
     */
    public Hmap getErrorMessages() {
        Hmap hmap = new Hmap();
        for (String name : fields.keySet()) {
            Field f = fields.get(name);
            hmap.put(f.getFieldName(), f.renderErrors());
        }
        return hmap;
    }

    /** 
     * Set a custom error message to a field of the form
     * @param fieldName
     * @param errorMessage
     */
    public void setErrorMessage(String fieldName, String errorMessage) {
        Field f = fields.get(fieldName);
        if (f != null) {
            f.addErrorMessage(errorMessage);
        }

    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id == null ? "id_form" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String enctype() {
        String out = "";
        if (multipart) {
            out = "enctype='multipart/form-data'";
        }
        return out;
    }

    /**
     * Call this method for forms that contain file fields
     */
    public void setEnctypeToMultipart() {
        this.multipart = true;
    }
}
