/*
 *  SubmitButton.java
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

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class SubmitButton extends Field {
    private boolean imageType = false;

    /**
     * 
     * @param fieldName
     * @param value
     * @param rules
     */
    public SubmitButton(String value) {
        super("submit_button", value);
    }
   
    public SubmitButton(String value, boolean imageType) {
        super("submit_button", value);
        this.imageType =  imageType;
    }
    
    /**
     * 
     * @param fieldName
     * @param value
     * @param rules
     */
    public SubmitButton(String key, String value) {
        super(key, value);
    }

    @Override
    public String renderField() {
        if(imageType){
            return String.format("<input name='%1$s' type='image' src='/images/icons/famfam/accept.png' class='image' id='%2$s' />%n", getFieldName(), "id_" + getFieldName());
        }
            return String.format("<input name='%1$s' type='submit' value='%3$s' />%n", getFieldName(), "id_" + getFieldName(), getValue());
    }

    @Override
    public String renderLabel() {
        return "";
    }
    
    
}
