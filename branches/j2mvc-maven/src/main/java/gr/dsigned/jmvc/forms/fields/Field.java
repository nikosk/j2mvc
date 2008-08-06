/*
 *  Field.java
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Field {


    public enum Rule {
        REQUIRED, MAX_LENGTH, MIN_LENGTH, DOMAIN, EMAIL, NUMERIC, ALPHA, ALPHANUM
    }
    protected String fieldName;
    protected String labelName;
    protected String value;
    protected String inputValue;
    protected String checked;
    protected String selected;
    protected ArrayList<Tuple2<Rule, String>> rules = new ArrayList<Tuple2<Rule, String>>();
    protected boolean validates;
    protected ArrayList<String> errors = new ArrayList<String>();
    protected ArrayList<DropdownOption> options = new ArrayList<DropdownOption>();

    /*
     * Used by:
     * 
     * textarea
     * submitButton
     * resetButton
     * passwordField
     * charField
     * buttonField
     * fileField
     * hiddenField
     * dropdownMenu
     */
    public Field(String fieldName, String value, Tuple2<Rule, String>... rules) {
        this.fieldName = fieldName;
        this.value = value;
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }
    
    /*
     * Used by:
     * 
     * Checkbox
     * radioButton
     */
    public Field(String labelName, String fieldName, String inputValue, String value, String checked, Tuple2<Rule, String>... rules) {
        this.labelName = labelName;
        this.fieldName = fieldName;
        this.inputValue = inputValue;
        this.value = value;
        this.checked = checked;
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }

    public boolean validates() {
        validates = true;
        
        for (Tuple2<Rule, String> r : rules) {
            String label = "" ;
            if(getLabelName()!=null && getLabelName().length()!=0){
                label = getLabelName();
            }else{    
                label = getFieldName();
            }
            switch (r._1) {
                case REQUIRED:
                    if (this.value.isEmpty()) {
                        errors.add(label + " is required.");
                        validates = false;
                    }
                    break;
                case MAX_LENGTH:
                    if (this.value.length() < r._2.length()) {
                        errors.add(label + " too long.");
                        validates = false;
                    }
                    break;
                case MIN_LENGTH:
                    if (this.value.length() < r._2.length()) {
                        errors.add(label + " too short.");
                        validates = false;
                    }
                    break;
                case EMAIL:
                    Pattern p = Pattern.compile("[0-9a-zA-Z]+(\\.{0,1}[0-9a-zA-Z\\+\\-_]+)*@[0-9a-zA-Z\\-]+(\\.{1}[a-zA-Z]{2,6})+");
                    Matcher m = p.matcher(value);
                    if (!m.matches()) {
                        errors.add(label + " is not a valid email address.");
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }

    public String renderErrors() {
        if (errors.size() != 0) {
            return String.format("<div class='error'> %1$s </div>", getErrors());
        } else {
            return "";
        }
    }

    public String getErrors() {
        String out = "";
        for (String s : errors) {
            out += "<span>" + s + " </span>";
        }
        return out;
    }

    public String renderLabel() {
        if(getLabelName()!=null && getLabelName().length()!=0){
            return String.format("<label for='id_%1$s'>%1$s</label>", getLabelName());
        }else{    
        return String.format("<label for='id_%1$s'>%1$s</label>", getFieldName());
        }
    }

    public String renderField() {
        return "";
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ArrayList<Tuple2<Rule, String>> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Tuple2<Rule, String>> rules) {
        this.rules = rules;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
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