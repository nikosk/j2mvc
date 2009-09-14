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
package gr.dsigned.jmvc.controls.forms.fields;

import gr.dsigned.jmvc.types.Tuple2;
import static gr.dsigned.jmvc.libraries.Localization.get;
/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class Checkbox extends Field {

    private boolean checked = false;
    private boolean disabled = false;
    String template = "<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s />";

    /**
     * 
     * @param labelName
     * @param fieldName
     * @param value (the value we are going to get from the post:: if checked we are going to get the inputValue else an empty string)
     * @param rules
     */
    public Checkbox(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input type='checkbox' name='%1$s' id='%2$s' value='%3$s' %4$s %5$s />", getFieldName(), "id_" + getFieldName(), getValue(),  isChecked(), isDisabled(), getErrors());
    }

    public String isChecked() {
        String out = "";
        if (checked) {
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
        return value;
    }
    
    
    @Override
    public boolean validates() {
        validates = true;
        for (Tuple2<Rule, String> r : rules) {
            switch (r._1) {
                case REQUIRED:
                    if (!checked) {
                        addError(get("The field ") + getLabelName() + get(" is required."));
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }
}
