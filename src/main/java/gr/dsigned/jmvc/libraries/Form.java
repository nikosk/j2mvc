/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.ValidationRules;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Library;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Christos Peppas <c.peppas@phiresoft.com>
 */
public class Form extends Library {
   
    private String id;
    private String name;
    private String action;
    private String className;
    
    public LinkedHashMap<String, HtmlControl> fieldsControls = new LinkedHashMap<String, HtmlControl>();
    public Bean data = new Bean();
    public ArrayList<String> requiredFields = new ArrayList<String>();
    public LinkedHashMap<String, ValidationType> fieldsValidation = new LinkedHashMap<String, ValidationType>();
    public Bean errors = new Bean();
    
    public LinkedHashMap<String, ValidationRules> fieldsRules = new LinkedHashMap<String, ValidationRules>();
         
    
    // "name , type , {required|max_lenth:200}"
    public void setFields(Object... fields){
        for(Object o : fields){
            
        }
    }
    public void addField(String fieldName , HtmlControl control, Object... validation){
        fieldsControls.put(fieldName, control);
        
        for(Object o : validation){
            
        }
    }
    
    public void validateFields(){
        for(String field: fieldsValidation.keySet()){
            ValidationType val = fieldsValidation.get(field);            
            switch(val) {
                case TEXT:
                    Validation.parseString(data.get(field), field, fieldsRules.get(field).getMinLength(), fieldsRules.get(field).getMaxLength(), errors);
                break;
                case TEXT_ALLOW_EMPTY:
                    Validation.parseStringAllowEmpty(data.get(field), field, fieldsRules.get(field).getMinLength(), fieldsRules.get(field).getMaxLength(), errors);
                break;
                case EMAIL:
                    Validation.parseEmail(data.get(field), field, fieldsRules.get(field).getMinLength(), fieldsRules.get(field).getMaxLength(), errors);
                break;
                default:
                    Jmvc.logError("[Form:validateFields] " + "I don't know");
                break;
            }
        }
    }

    public void addField(String fieldName , HtmlControl control,  ValidationType val, String dataValue, ValidationRules rules){
        fieldsControls.put(fieldName, control);
        fieldsValidation.put(fieldName, val);
        data.put(fieldName, dataValue);
       /* if(required){
            requiredFields.add(fieldName);
        }*/
        fieldsRules.put(fieldName,rules);
    }
    
    public String renderForm(){
        StringBuilder sb = new StringBuilder();
        sb.append("<form id='' method='post' action='/sites/show_form/'>");
        sb.append("<span style='color:#FF0000;'>");
        for(String e : errors.keySet()){
            sb.append(errors.get(e)).append(", ");
        }
        sb.append("</span>");
        
        for(String fieldName : fieldsControls.keySet()){            
            HtmlControl htmlControl =  fieldsControls.get(fieldName);//map value
            sb.append("<div>");
            sb.append("<span>").append(fieldName).append(" ").append("</span>");
            sb.append(String.format(htmlControl.html, fieldName, data.get(fieldName) ) );
            /*if(requiredFields.contains(fieldName)){
            sb.append("*");
            }*/
            if(fieldsRules.get(fieldName).isRequired()){
                sb.append("*");
            }
            sb.append("</div>");
        }
        sb.append("<input type='hidden' name='redirect_to' value='/sites'/>");
        sb.append("<input type='submit' value='Submit'/>");
        sb.append("</form>");
        return sb.toString(); 
    }

    
    public enum HtmlControl{
        // Input standar attributes id, class, title, style, dir, lang, xml:lang
        INPUT_TEXT           ("<input id='%1$s' name='%1$s' value='%2$s' type='text' size='%3$s' maxlength='%4$s' />"),
        INPUT_TEXT_RDONLY    ("<input id='%1$s' name='%1$s' value='%2$s' type='text' size='%3$s' readonly/>"),
        INPUT_HIDDEN         ("<input id='%1$s' name='%1$s' value='%2$s' type='hidden'/>"),
        INPUT_PASSWORD       ("<input id='%1$s' name='%1$s' value='%2$s' type='password'/>"),
        INPUT_RADIO          ("<input id='%1$s' name='%1$s' value='%2$s' type='radio'/>"),
        INPUT_RADIO_CHKED    ("<input id='%1$s' name='%1$s' value='%2$s' type='radio' checked/>"),
        INPUT_CHECKBOX       ("<input id='%1$s' name='%1$s' value='%2$s' type='checkbox'/>"),
        INPUT_CHECKBOX_CHKED ("<input id='%1$s' name='%1$s' value='%2$s' type='checkbox' checked/>"),
        INPUT_IMAGE          ("<input id='%1$s' name='%1$s' value='%2$s' type='image' align='%3$s' alt='%4$s'/>"),
        INPUT_SUBMIT         ("<input id='%1$s' name='%1$s' value='%2$s' type='submit'/>"),
        INPUT_RESET          ("<input id='%1$s' name='%1$s' value='%2$s' type='reset'/>"),
        INPUT_BUTTON         ("<input id='%1$s' name='%1$s' value='%2$s' type='button'/>"), 
        INPUT_FILE           ("<input id='%1$s' name='%1$s'              type='file'/>"),
        TEXTAREA(""),
        OPTION_SELECT("");
        
        private final String html;

        HtmlControl(String html) {
            this.html = html;
        }

    }
          

    public enum ValidationType{
        TEXT("min='%1$s'|max='%2$s'"),
        //TEXT_RANGE_VALUES("range=one,two,three"),
        TEXT_ALLOW_EMPTY("min='%1$s'|max='%2$s'"),
        EMAIL("min='%1$s'|max='%2$s'"),
        NO_VALIDATION("");

        private final String rule;
        private int min;
        private int max;
        
        ValidationType(String rule){
            this.rule = rule;
        }
        
        public String getTextRule(){
            return String.format(this.toString(), max, min );
        }
    }
    
}
