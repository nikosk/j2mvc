/*
 *  ResetButton.java
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

import static gr.dsigned.jmvc.framework.Renderer.*;

 /**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */

public class ResetButton extends Field{
    
    private String redirectTo = "";
    

    /**
     * 
     * @param fieldName
     * @param value
     * @param rules
     */
    public ResetButton(String value) {
        super("reset", value);
    }

    @Override
    public String renderField() {
        return renderAsCancel();
    }

    public String renderAsCancel(){
        String out = "";
        if(redirectTo.isEmpty()){
            out = "<a href='javascript:history.go(-1)' class='cancel'>"+getValue()+"</a>";
        } else {
            out = "<a href='" + redirectTo + "' class='cancel'>"+getValue()+"</a>";
        }        
        return div(out);
    }
            
    @Override
    public String renderLabel() {
        return "";
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }
    
    
}
