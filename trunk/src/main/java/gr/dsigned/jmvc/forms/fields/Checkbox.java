/*
 *  Checkbox.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <vchrys@gmail.com>
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
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class Checkbox extends Field {

    private boolean checked = false;
    private boolean disabled = false;
    private String inputValue;
    String template = "<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s />%n";

    /**
     * 
     * @param labelName
     * @param fieldName
     * @param inputValue (the value we assign to the input value attribute)
     * @param value (the value we are going to get from the post:: if checked we are going to get the inputValue else an empty string)
     * @param rules
     */
    public Checkbox(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s %5$s />%n", getFieldName(), "id_" + getFieldName(), getValue(),  isChecked(), isDisabled(), getErrors());
    }

    public String isChecked() {
        String out = "";
        if (getValue() != null && getValue().equalsIgnoreCase("1") || checked) {
            out = " checked ";
        }
        return out;
    }

    public void setChecked() {
        this.checked = true;
    }

    public String isDisabled() {
        String out = "";
        if (disabled) {
            out = " disabled ";
        }
        else {
            out = "";
        }
        return out;
    }

    public void setDisabled() {
        this.disabled = true;
    }

    @Override
    public String getValue() {
        String out = "0";
        if (!value.isEmpty()) {
            out = "1";
        }
        return out;
    }
    
    
    @Override
    public boolean validates() {
        validates = true;
        for (Tuple2<Rule, String> r : rules) {
            switch (r._1) {
                case REQUIRED:
                    if (getValue().equals("0")) {
                        addError(getLabelName() + " is required.");
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }
}
