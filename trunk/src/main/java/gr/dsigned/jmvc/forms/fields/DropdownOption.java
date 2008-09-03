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

package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.types.Tuple2;
 /**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */

public class DropdownOption {
    
    String template = "<option value='%1$s'>%2$s</option>%n";
    private String optionLabel;
    private String value;
    private boolean selected;
    
    /**
     * @param optionLabel
     * @param value
     * @param selected
     */
    public DropdownOption(String optionLabel, String value, boolean selected) {
        this.optionLabel = optionLabel ;
        this.value = value ;
        this.selected = false ;
    }
    /**
     * 
     * @param optionLabel
     * @param value
     */
    public DropdownOption(String optionLabel, String value) {
        this.optionLabel = optionLabel ;
        this.value = value ;
        this.selected = false ;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setIsSelected(boolean selected) {
        this.selected = selected;
    }
    
    public String render(){
        return String.format("<option value='%2$s' %3$s>%1$s</option>%n", getOptionLabel(), getValue(), (isSelected())? " selected " : "");
    }
}
