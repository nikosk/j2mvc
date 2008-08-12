/*
 *  DropdownMenu.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <nikosk@dsigned.gr>
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
import java.util.ArrayList;

 /**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class DropdownMenu extends Field {

    String template = "<select name='%1$s' id='%2$s'>%3$s</select>%n";
    protected ArrayList<DropdownOption> options = new ArrayList<DropdownOption> () ;

    public DropdownMenu(String labelName, String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, value, rules);
    }

    @Override
    public String renderField() {
        return String.format("<select name='%1$s' id='%2$s'>%3$s</select>%n", getFieldName(), "id_" + getFieldName(), renderOptions(), getErrors());
    }
    
    public DropdownOption addOption(DropdownOption option)
    {
        //for (DropdownOption option: optionsAL){
            options.add(option) ;
        //}
        return option ;
    }
    
    public ArrayList<DropdownOption> getOption()
    {
        return options ;
    }
    
    public String renderOptions()
    {
        String out = "" ;
        for (DropdownOption option: options){
            out += String.format("<option value='%2$s' %3$s>%1$s</option>%n", option.getFieldName(), option.getValue(), option.getSelected(), getErrors()) ;
        }
        return out ;
    }
}
