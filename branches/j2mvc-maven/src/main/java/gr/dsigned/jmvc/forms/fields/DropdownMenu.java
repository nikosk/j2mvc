/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.types.Tuple2;
import java.util.ArrayList;

/**
 *
 * @author USER
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
