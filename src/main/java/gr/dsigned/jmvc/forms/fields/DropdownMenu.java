/*
 *  DropdownMenu.java
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

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.types.Tuple2;
import java.util.ArrayList;

/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class DropdownMenu extends Field {

    String template = "<select name='%1$s' id='%2$s'>%3$s</select>%n";
    protected ArrayList<DropdownOption> options = new ArrayList<DropdownOption>();

    public DropdownMenu(String labelName, String fieldName, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, rules);
    }

    public DropdownMenu(String labelName, String fieldName, String defaultValue, Hmap optionValues, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, rules);
        if(defaultValue!=null && defaultValue.length()!=0)
        {
            addOption(new DropdownOption(defaultValue, "0"));
        }    
        for (String o : optionValues.keySet()) {
            addOption(new DropdownOption(optionValues.get(o), o));
        }
    }
    
    @Override
    public String renderField() {
        return String.format("<select name='%1$s' id='%2$s'>%3$s</select>%n", getFieldName(), "id_" + getFieldName(), renderOptions(), getErrors());
    }

    public DropdownOption addOption(DropdownOption option) {
        options.add(option);
        return option;
    }

    public void setOptions(ArrayList<DropdownOption> optionList) {
        for (DropdownOption option : optionList) {
            options.add(option);
        }
    }

    public void setOptions(Hmap optionValues) {
        for (String o : optionValues.keySet()) {
            addOption(new DropdownOption(o, optionValues.get(o)));
        }
    }

    public ArrayList<DropdownOption> getOptions() {
        return options;
    }

    public String renderOptions() {
        StringBuilder sb = new StringBuilder();
        for (DropdownOption option : options) {
            sb.append(option.render());
        }
        return sb.toString();
    }

    public void setSelected(String selectedValue) {
        for (DropdownOption o : options) {
            if (selectedValue.equals(o.getValue())) {
                o.setIsSelected(true);
            } else {
                o.setIsSelected(false);
            }
        }
    }

    private DropdownOption getSelectedDropdownOption() {
        for (DropdownOption o : options) {
            if (o.isSelected()) {
                return o;
            }
        }
        return null;
    }
    public String getSelectedValue() {
        return (getSelectedDropdownOption().getValue() == null) ? "" : getSelectedDropdownOption().getValue();
    }

    @Override
    public String getValue() {
        return getSelectedValue();
    }

    public boolean validates() {
        validates = true;
        for (Tuple2<Rule, String> r : rules) {
            
            switch (r._1) {
                case REQUIRED:
                    if (this.getSelectedValue().isEmpty()) {
                        addError(getLabelName() + " is required.");
                        validates = false;
                    }
                    break;
                case DEFAULT_NOT_ALLOWED:
                    if (this.getSelectedValue().isEmpty() || this.getSelectedValue().equalsIgnoreCase("0")) {
                        addError(getLabelName() + " is required.");
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }
}
