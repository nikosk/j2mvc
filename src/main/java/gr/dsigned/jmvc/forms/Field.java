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
package gr.dsigned.jmvc.forms;

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
    protected ArrayList<Tuple2<Rule, String>> rules = new ArrayList<Tuple2<Rule, String>>();
    protected boolean validates;
    protected ArrayList<String> errors = new ArrayList<String>();

    public Field(String fieldName, String value, Tuple2<Rule, String>... rules) {
        this.fieldName = fieldName;
        this.value = value;
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }
    
    public Field(String fieldName, Tuple2<Rule, String>... rules) {
        this.fieldName = fieldName;
        this.value = "";
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }
    public boolean validates() {
        for (Tuple2<Rule, String> r : rules) {
            switch (r._1) {
                case REQUIRED:
                    if (this.value.isEmpty()) {
                        errors.add(fieldName + " is required.");
                        validates = false;
                    }
                    break;
                case MAX_LENGTH:
                    if (this.value.length() < r._2.length()) {
                        errors.add(fieldName + " too long.");
                        validates = false;
                    }
                    break;
                case MIN_LENGTH:
                    if (this.value.length() < r._2.length()) {
                        errors.add(fieldName + " too short.");
                        validates = false;
                    }
                    break;
                case EMAIL:
                    Pattern p = Pattern.compile("[0-9a-zA-Z]+(\\.{0,1}[0-9a-zA-Z\\+\\-_]+)*@[0-9a-zA-Z\\-]+(\\.{1}[a-zA-Z]{2,6})+");
                    Matcher m = p.matcher(value);
                    if (m.matches()) {
                        errors.add(fieldName + " is not a valid domain.");
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }

    public String getErrors() {
        String out = "";
        for (String s : errors) {
            out += "<span>" + s + "</span>";
        }
        return out;
    }

    public  String renderField(){
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
}
