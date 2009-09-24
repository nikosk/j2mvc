/*
 *  SubmitButton.java
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
package gr.dsigned.jmvc.controls.forms.fields;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class SubmitButton extends Field {
    private boolean imageType = false;
    private String src="";

    /**
     * 
     * @param fieldName
     * @param value
     * @param rules
     */
    public SubmitButton(String value) {
        super("submit_button", value);
    }
   
    public SubmitButton(String value, boolean imageType, String src) {
        super("submit_button", value);
        this.imageType =  imageType;
        this.src = src;
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
            return String.format("<input name='%1$s' type='image' class='submit' id='%2$s' src='%3$s' />", getFieldName(), "id_" + getFieldName(), getSrc());
        }
            return String.format("<input name='%1$s' type='submit' value='%3$s' />", getFieldName(), "id_" + getFieldName(), getValue());
    }

    @Override
    public String renderLabel() {
        return "";
    }

    public String getSrc() {
        return src;
    }
}
