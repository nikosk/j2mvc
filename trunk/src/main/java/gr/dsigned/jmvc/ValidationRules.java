/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc;

import java.util.LinkedHashMap;

/**
 *
 * @author Christos Peppas <c.peppas@phiresoft.com>
 */
public class ValidationRules extends LinkedHashMap<String, String>{
    
    public ValidationRules(){

    }

    public ValidationRules(boolean required, int minLength, int maxLength){
        put("required", required+"");
        put("minLength", minLength+"");
        put("maxLength", maxLength+"");
    }
    
    public void setRequired(boolean required){
        put("required", required+"");
    }

    public void setMinLength(int minLength){
        put("minLength", minLength+"");
    }

    public void setMaxLength(int maxLength){
        put("maxLength", maxLength+"");
    }
    
    public Boolean isRequired(){
        return Boolean.valueOf(get("required"));
    }

    public Integer getMinLength(){
        return Integer.valueOf(get("minLength"));
    }
    
    public Integer getMaxLength(){
        return Integer.valueOf(get("maxLength"));
    }


}
