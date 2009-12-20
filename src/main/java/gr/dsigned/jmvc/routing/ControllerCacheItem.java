/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.routing;

import gr.dsigned.jmvc.annotations.ControllerURLAlias;
import gr.dsigned.jmvc.annotations.MethodURLAlias;
import gr.dsigned.jmvc.annotations.NotPublic;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper class that contains the Class object
 * of a controller and helper methods to retrieve
 * Method classes of the controller with alias or method name.
 */
public class ControllerCacheItem {

    private Class controllerClass;
    private Map<String, Method> methods;
    private String className;
    private String alias;

    public ControllerCacheItem(Class controllerClass) {
        this.controllerClass = controllerClass;
        this.className = controllerClass.getSimpleName();
        methods = new LinkedHashMap<String, Method>();
        for (Method m : controllerClass.getDeclaredMethods()) {
            if (m.getModifiers() == java.lang.reflect.Modifier.PUBLIC && !m.isAnnotationPresent(NotPublic.class)) { // We only need public methods
                String methodName = m.getName().toLowerCase();
                if (!methods.containsKey(methodName)) {
                    methods.put(methodName, m);
                }
                if (m.isAnnotationPresent(MethodURLAlias.class)) {
                    String methodAlias = m.getAnnotation(MethodURLAlias.class).value().toLowerCase();
                    if (!methods.containsKey(methodAlias)) {
                        methods.put(methodAlias, m);
                    }
                }

            }
        }
        if (this.controllerClass.isAnnotationPresent(ControllerURLAlias.class)) {
            this.alias = ((ControllerURLAlias) this.controllerClass.getAnnotation(ControllerURLAlias.class)).value();
        }
    }

    public Method getMethodByNameOrAlias(String methodName) {
        return methods.get(methodName);
    }

    public Class getControllerClass() {
        return controllerClass;
    }

    public String getAlias() {
        return alias;
    }

    public String getClassName() {
        return className;
    }
}
