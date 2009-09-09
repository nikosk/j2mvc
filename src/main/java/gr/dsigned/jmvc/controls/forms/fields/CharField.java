/*
 *  CharField.java
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

import gr.dsigned.jmvc.types.Tuple2;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class CharField extends Field {

    private boolean disabled = false;
    private boolean readonly = false;
    private String maxlength = "" ;
    private String onclick ="";
    private String classStyle="txt";
    
    String template = "<input type='text' name='%1$s' id='%2$s' value='%3$s'/>";
    
    /**
     * 
     * @param labelName
     * @param fieldName
     * @param value (value from post)
     * @param rules
     */
    public CharField(String labelName, String fieldName, String value,  Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<input class='%9$s' type='text' name='%1$s' id='%2$s' value='%3$s' onclick ='%8$s' %4$s %5$s %6$s />", getFieldName(), "id_" + getFieldName(), getValue(), isDisabled(), isReadonly(), getMaxlength(), getErrors(), getOnclick(), getClassStyle());
    }
    
    public String isDisabled() {
        String out = "" ;
        if(disabled){
            out = " disabled " ;
        }else{
            out = "" ;
        }
        return out;
    }

    public void setDisabled() {
        this.disabled = true;
    }

    public String isReadonly() {
        String out = "" ;
        if(readonly){
            out = " readonly " ;
        }else{
            out = "" ;
        }
        return out;
    }

    public void setReadonly() {
        this.readonly = true;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = "maxlength = '" + maxlength + "'";
    }

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

    public String getClassStyle() {
        return classStyle;
    }

    public CharField classStyle(String style){
        this.classStyle=style;
        return this; 
    }
}
