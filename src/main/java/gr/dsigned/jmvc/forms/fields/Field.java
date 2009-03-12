/*
 *  Field.java
 * 
 *  Copyright (C) 2008 
 *  Nikos Kastamoulas <nikosk@dsigned.gr>
 *  Vas Chryssikou <vchrys@gmail.com>
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

import gr.dsigned.jmvc.libraries.HTMLInputFilter;
import gr.dsigned.jmvc.types.Tuple2;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static gr.dsigned.jmvc.libraries.Localization.get;
/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class Field {

    public enum Rule {

        /**
         * Signifies that the field cannot be empty
         * o(REQUIRED, "true")
         */
        REQUIRED,
        /**
         * Max length of field (only applies to text type fields)
         * o(MAX_LENGTH, "45")
         */
        MAX_LENGTH,
        MIN_LENGTH,
        DOMAIN,
        EMAIL,
        NUMERIC,
        ALPHA,
        ALPHANUM,
        DATE,
        PASSED_CHARS_ONLY,
        DATE_NOT_REQUIRED,
        ALLOWED_EXTENSIONS,
        MAX_FILE_SIZE,
        DEFAULT_NOT_ALLOWED,
        EQUALS,
        PROFILE_NAME,
        CAPTCHA,
        EITHER,
        /**
         * Automatically clean input of this field 
         * from html. Second param is a comma-separated list of allowed HTML tags
         */
        XSS_CLEAN,
        ALPHANUM_WITH_SPACES,
        PASSWORD
    }
    protected String fieldName;
    protected String label;
    protected String value;
    protected ArrayList<Tuple2<Rule, String>> rules = new ArrayList<Tuple2<Rule, String>>();
    protected boolean validates;
    protected ArrayList<String> errors = new ArrayList<String>();
    
    public static final String GREEK_ALPHA_PUNCTUATION= "[0-9a-zA-Zα-ωΑ-Ωάέήίόώύ;.?\\-_ ]*";
    
    /**
     * Create a new field with initial name and value.
     * No field label. 
     * @param fieldName the name='' of the field
     * @param value
     * @param rules
     */
    public Field(String fieldName, String value, Tuple2<Rule, String>... rules) {
        this.fieldName = fieldName;
        this.value = value;
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }

    /**
     * Create a new field with initial name and value.
     * No field label. 
     * @param fieldName the name='' of the field
     * @param value
     * @param rules
     */
    public Field(String fieldName, Tuple2<Rule, String>... rules) {
        this.fieldName = fieldName;
        this.value = "";
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }

    public Field(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        this.label = labelName;
        this.fieldName = fieldName;
        this.value = value;
        for (Tuple2<Rule, String> t : rules) {
            this.rules.add(t);
        }
    }

    public boolean isRequired() {
        boolean required = false;
        for (Tuple2<Rule, String> r : rules) {
            if (r._1 == Rule.REQUIRED) {
                required = true;
                break;
            }
        }
        return required;
    }

    public boolean validates() {
        validates = true;
        Pattern p = null;
        Matcher m = null;
        for (Tuple2<Rule, String> r : rules) {
            String label = "";
            if (getLabelName() != null && getLabelName().length() != 0) {
                label = getLabelName();
            } else {
                label = getFieldName();
            }
            switch (r._1) {
                case REQUIRED:
                    if (getValue().isEmpty()) {
                        addError(get("The field ") + label + get(" is required."));
                        validates = false;
                    }
                    break;
                case MAX_LENGTH:
                    if (!getValue().isEmpty() && getValue().length() > Integer.parseInt(r._2)) {
                        addError(get("The field ") + label + get("is too long."));
                        validates = false;
                    }
                    break;
                case MIN_LENGTH:
                    if (!getValue().isEmpty() && getValue().length() < Integer.parseInt(r._2)) {
                        addError(get("The field ") + label + get(" is too short."));
                        validates = false;
                    }
                    break;
                case EMAIL:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("[0-9a-zA-Z]+(\\.{0,1}[0-9a-zA-Z\\+\\-_]+)*@[0-9a-zA-Z\\-]+(\\.{1}[a-zA-Z]{2,6})+");
                        m = p.matcher(getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" is not a valid email."));
                            validates = false;
                        }
                    }
                    break;
                case DOMAIN:
                    if (!getValue().isEmpty()) {
                        String domain = "http://" + this.getValue();
                        p = Pattern.compile("(http):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
                        m = p.matcher(domain);
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" is not a valid domain."));
                            validates = false;
                        }
                    }
                    break;
                case NUMERIC:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("\\d*");
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" should consist of numbers."));
                            validates = false;
                        }
                    }
                    break;
                case ALPHA:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("[a-zA-Zα-ωΑ-Ωάέήίόώύ]*");
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" should consist of letters."));
                            validates = false;
                        }
                    }
                    break;
                case PASSWORD:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("[a-zA-Z0-9._-]*");
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" latin characters, numbers and dot, dash and underscore."));
                            validates = false;
                        }
                    }
                    break;
                case ALPHANUM:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("\\w*");
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" should consist of numbers and letters."));
                            validates = false;
                        }
                    }
                    break;
                case ALPHANUM_WITH_SPACES:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("[0-9a-zA-Zα-ωΑ-Ωάέήίόώύ ]*");
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" should consist of numbers, letters and spaces."));
                            validates = false;
                        }
                    }
                    break;
                case PASSED_CHARS_ONLY:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile(r._2);
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" should consist of " + r._2));
                            validates = false;
                        }
                    }
                    break;
                case PROFILE_NAME:
                    if (!getValue().isEmpty()) {
                        p = Pattern.compile("[a-zA-Z0-9]{1,}[a-zA-Z0-9-_]*");
                        m = p.matcher(this.getValue());
                        if (!m.matches()) {
                            addError(get("The field ") + label + get(" should consists characters of numbers, letters (a-z,A-Z) or '-' '_'. Cannot start with '-' or '_'"));
                            validates = false;
                        }
                    }
                    break;
                case EQUALS:
                    if (!getValue().isEmpty() && !this.getValue().equals(r._2)) {
                        addError(get("The field ") + label + get(" does not match with the above field."));
                        validates = false;
                    }
                    break;
                case EITHER:
                    if (getValue().isEmpty() && r._2.isEmpty()) {
                        addError(get("Both fields cannot be empty."));
                        validates = false;
                    }
                    break;
                case DATE:
                    if (!this.getValue().isEmpty()) {
                        SimpleDateFormat dtFormatter = new SimpleDateFormat(r._2);
                        dtFormatter.setLenient(false);
                        try {
                            dtFormatter.parse(this.getValue());
                        } catch (ParseException ex) {
                            validates = false;
                            addError(get("The field ") + label + get(" is not of the correct date format: ") + r._2);
                        }
                    } else {
                        validates = false;
                        addError(get("The field ") + label + get(" is not of the correct date format ") + r._2);
                    }
                    break;
                case DATE_NOT_REQUIRED:
                    if (!this.getValue().isEmpty()) {
                        SimpleDateFormat dtFormatter = new SimpleDateFormat(r._2);
                        dtFormatter.setLenient(false);
                        try {
                            dtFormatter.parse(this.getValue());
                        } catch (ParseException ex) {
                            validates = false;
                            addError(get("The field ") + label + get(" is not of the correct date format: ") + r._2);
                        }
                    }
                    break;
                case XSS_CLEAN:
                    String[] tags = r._2.split(",");
                    HTMLInputFilter filter = new HTMLInputFilter(true);
                    for(String tag : tags){
                        filter.setVAllowed(tag);
                    }
                    setValue(filter.filter(getValue()).trim());
                    break;
            }
        }
        return validates;
    }

    protected void addError(String str) {
        errors.add(str);
    }

    /**
     * Builds a list of error messages for the field
     * Caution: No messages are displayed when rendering the field
     * when this is called. You need to validate the field again to display errors
     * @return Div list of error messages
     */
    public String renderErrors() {
        String out = "";
        if (errors.size() != 0) {
            out = String.format("<div class='error'> %1$s </div>", getErrors());
        }
        errors.clear();
        return out;
    }

    public String getErrors() {
        String out = "";
        for (String s : errors) {
            out += "<span>" + s + " </span>";
        }
        return out;
    }

    public ArrayList<String> getErrorList() {
        return errors;
    }

    public String renderLabel() {
        if (getLabelName() != null && getLabelName().length() != 0) {
            return String.format("<label for='id_%1$s'>%2$s</label>", getFieldName(), getLabelName());
        } else {
            return "";
        }
    }

    public String renderField() {
        return ""; // Only children of this class render themselves
    }

    public String getLabelName() {
        return label;
    }

    public void setLabelName(String labelName) {
        this.label = labelName;
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
        this.value = value.trim();
    }

    public void addErrorMessage(String msg) {
        addError(msg);
    }
}
