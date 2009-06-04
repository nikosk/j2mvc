/*
 *  ButtonField.java
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

import gr.dsigned.jmvc.controls.forms.fields.Field.Rule;
import gr.dsigned.jmvc.types.Tuple2;

 /**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class ButtonField extends Field{

    /**
     * @param fieldName
     * @param value
     * @param rules
     */
	
	private String onclick;
	
    public ButtonField(String fieldName, String value) {
        super(fieldName, value);
    }

    @Override
    public String renderField() {
        return String.format("<input type='button' name='%1$s' id='%2$s' value='%3$s' onclick ='%4$s'/>", getFieldName(), "id_" + getFieldName(), getValue(), getOnclick());
    }

    @Override
    public String renderLabel() {
        return "";
    }

	public String getOnclick() {
		return onclick==null?getErrors():onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}
    

}
