package gr.dsigned.jmvc.framework.interfaces;

import java.util.LinkedHashMap;
import java.util.Map;

public class View implements ViewAction {

    private String template;

    public View(String template) {
        this.template = template;
    }
    private Map<String, Object> data = new LinkedHashMap<String, Object>();

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String getTemplateName() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
