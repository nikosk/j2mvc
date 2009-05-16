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
package gr.dsigned.jmvc.controls.forms.fields;

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.types.Tuple2;
import java.util.ArrayList;
import static gr.dsigned.jmvc.libraries.Localization.get;
/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class DropdownMenu extends Field {
    
    private boolean disabled = false;
    private ArrayList<DropdownOption> options = new ArrayList<DropdownOption>();

    private static final String DEFAULT_VALUE = "" ;
    String template = "<select name='%1$s' id='%2$s'>%3$s</select>";
    
    /**
     * Constructor for drop downmenu with default value field
     * 
     * @param labelName
     * @param fieldName
     * @param defaultValue (the value that is going to be selected when rendering the dropdown and will not be counted as correct input ie: Select value)
     * @param selectedValue (the value of the selected option)
     * @param optionValues ((key ie:id, label of option) options to render within the select tags)
     * @param rules
     */
    public DropdownMenu(String labelName, String fieldName, String defaultValue, String selectedValue, Hmap optionValues, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, selectedValue, rules);

        if(selectedValue!=null && selectedValue.length()!=0){
            if(defaultValue!=null && defaultValue.length()!=0){
               addOption(new DropdownOption(defaultValue, DEFAULT_VALUE));
            }
        }else{
            if(defaultValue!=null && defaultValue.length()!=0){
               addOption(new DropdownOption(defaultValue, DEFAULT_VALUE, true));
            }
        }    
        
        for (String o : optionValues.keySet()) {
            if(selectedValue.equalsIgnoreCase(o)){
                addOption(new DropdownOption(optionValues.get(o), o, true));
            }
            else{
                addOption(new DropdownOption(optionValues.get(o), o));
            }
        }
    }
    
    /**
     * Constructor for drop downmenu without default value field
     * 
     * @param labelName
     * @param fieldName
     * @param selectedValue (the value of the selected option)
     * @param optionValues ((key ie:id, label of option) options to render within the select tags)
     * @param rules
     */
    public DropdownMenu(String labelName, String fieldName, String selectedValue, Hmap optionValues, Tuple2<Rule, String>... rules) {
        super(labelName, fieldName, selectedValue, rules);

        for (String o : optionValues.keySet()) {
            if(selectedValue.equalsIgnoreCase(o)){
                addOption(new DropdownOption(optionValues.get(o), o, true));
            }
            else{
                addOption(new DropdownOption(optionValues.get(o), o));
            }
        }
    }
    
    @Override
    public String renderField() {
        return String.format("<select name='%1$s' id='%2$s' %4$s>%3$s</select>", getFieldName(), "id_" + getFieldName(), renderOptions(), isDisabled(), getErrors());
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
        return (getSelectedDropdownOption() == null) ? "" : getSelectedDropdownOption().getValue();
    }

    @Override
    public String getValue() {
        return getSelectedValue();
    }

    @Override
    public void setValue(String value) {
        setSelected(value);
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

    @Override
    public boolean validates() {
        validates = true;
        for (Tuple2<Rule, String> r : rules) {
            
            switch (r._1) {
                case REQUIRED:
                    if (this.getSelectedValue().isEmpty()) {
                        addError(get("The field ") + getLabelName() + get(" is required."));
                        validates = false;
                    }
                    break;
//                case DEFAULT_NOT_ALLOWED:
//                    System.out.println(getSelectedValue());
//                    System.out.println(getValue());
//                    if (this.getSelectedValue().isEmpty() || this.getSelectedValue().equalsIgnoreCase(DEFAULT_VALUE)) {
//                        addError(getLabelName() + " cannot have the default value.");
//                        validates = false;
//                    }
//                    break;
            }
        }
        return validates;
    }
}
