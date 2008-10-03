/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.forms.fields;
import com.phiresoft.social.services.CaptchaServiceSingleton;
import gr.dsigned.jmvc.types.Tuple2;
/**
 *
 * @author user
 */
public class CaptchaField extends Field {
    
    private String captchaSource = "";
    private String captchaId = "";
        
    public CaptchaField(String fieldName, String value, Tuple2<Rule, String>... rules) {
        super(fieldName, value, rules);
        setCaptchaSource("/jcaptcha");
    }
    
    @Override
    public String renderLabel() {
        return String.format("<img alt='' id='id_%1$s' src='%2$s' />",getFieldName(),getCaptchaSource());
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='text' name='%1$s' id='%2$s' value='%3$s' />%n", getFieldName(), "id_" + getFieldName(), getValue(),  getErrors());
    }
    
    public String getCaptchaSource() {
        return captchaSource;
    }

    public void setCaptchaSource(String captchaSource) {
        this.captchaSource = captchaSource;
    }
    
    @Override
    public boolean validates() {
        validates = true;
        for (Tuple2<Rule, String> r : rules) {
            switch (r._1) {
                case CAPTCHA:
                    if (this.getValue().isEmpty()) {
                        addError(getFieldName() + " is required.");
                        validates = false;
                    }else{
                        Boolean isCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(getCaptchaId(),
                        this.getValue());
                        if(!isCorrect){
                            validates = false;
                            addError(getFieldName() + " is wrong");
                        }
                    }
            }
                    break;
        }
        return validates;
    }

    public String getCaptchaId() {
        return captchaId;
    }

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }   
}
