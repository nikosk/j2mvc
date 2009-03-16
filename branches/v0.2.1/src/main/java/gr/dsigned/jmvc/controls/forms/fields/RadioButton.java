    /*
 *  DropdownOption.java
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

/**
 *
 * @author VChrys <vchrys@gmail.com>
 */
public class RadioButton {

    private String fieldName;
    private String radioLabel;
    private String value;
    private boolean checked = false;
    private boolean disabled = false;

    /**
     * Contstructor that constructs radioButtons with checked attribute that can be set either true of false. 
     * Mostly used for the case that we want an option to be set to true 
     * 
     * @param fieldName
     * @param radioLabel
     * @param value
     */
    public RadioButton(String fieldName, String radioLabel, String value) {
        this.fieldName = fieldName;
        this.radioLabel = radioLabel;
        this.value = value;
    }
    
    public String getRadioLabel() {
        return radioLabel;
    }

    public void setRadioLabel(String radioLabel) {
        this.radioLabel = radioLabel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void checked() {
        this.checked = true;
    }

    public void unchecked() {
        this.checked = false;
    }

    public String render() {
        return String.format("%3$s<input type='radio' name='%1$s' id='%2$s_%4$s' value='%4$s' %5$s %6$s/>%n", getFieldName(), "id_" + getFieldName(), getRadioLabel(), getValue(), (isChecked()) ? "checked" : "", isDisabled() ? "disabled" : "");
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled() {
        this.disabled = true;
    }
}