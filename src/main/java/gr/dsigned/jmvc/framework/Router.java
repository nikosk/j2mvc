/*
 *  Router.java
 *
 *  Copyright (C) Feb 4, 2009 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.annotations.ControllerURLAlias;
import gr.dsigned.jmvc.annotations.MethodURLAlias;
import gr.dsigned.jmvc.types.Tuple2;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import static gr.dsigned.jmvc.types.operators.*;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Router {

    private SortedMap<String, Tuple2<String, String>> routeEntries = new TreeMap<String, Tuple2<String, String>>(new StringLengthComparator());
    private LinkedHashMap<String, LinkedHashMap<String, Method>> methodCache = new LinkedHashMap<String, LinkedHashMap<String, Method>>();
    private LinkedHashMap<String, Class> controllerClassesCache = new LinkedHashMap<String, Class>();

    public Router() {
    }

    public LinkedHashMap<String, Class> getControllerClassesCache() {
        return controllerClassesCache;
    }

    public String getControllerName(String requestURI) {
        for (String route : routeEntries.keySet()) {
            String cleanRoute = route.lastIndexOf("/") + 1 == route.length() ? route.substring(0, route.length() - 1) : route;
            String trimmedRequestURL = requestURI.length() > cleanRoute.length() ? requestURI.substring(0, cleanRoute.length()) : requestURI;
            if (cleanRoute.length() > 0 && cleanRoute.equalsIgnoreCase(trimmedRequestURL)) {
                return capitalize(routeEntries.get(route)._1);
            }
        }
        return getController(requestURI);
    }

    public String getMethodName(String requestURI) {
        for (String route : routeEntries.keySet()) {
            String cleanRoute = route.lastIndexOf("/") + 1 == route.length() ? route.substring(0, route.length() - 1) : route;
            String trimmedRequestURL = requestURI.length() > cleanRoute.length() ? requestURI.substring(0, cleanRoute.length()) : requestURI;
            if (cleanRoute.equalsIgnoreCase(trimmedRequestURL)) {
                if (routeEntries.get(route)._2 != null) {
                    return routeEntries.get(route)._2;
                }
            }
        }
        return getMethod(requestURI);
    }

    /**
     * Given a request URI returns a controller class
     * from the cache.
     * @param requestURI
     * @return
     */
    public Class getControllerClassByReqURI(String requestURI) {
        Class controller = controllerClassesCache.get(getControllerName(requestURI));
        if (controller == null) {
            return controllerClassesCache.get(capitalize(Settings.get("DEFAULT_CONTROLLER")));
        }
        return controller;
    }

    /**
     * Given a request URI returns a method class
     * from the cache.
     * @param requestURI
     * @return
     */
    public Method getMethodClassByReqURI(String requestURI) {
        if (methodCache.get(getControllerName(requestURI)) == null) {
            return methodCache.get(capitalize(Settings.get("DEFAULT_CONTROLLER"))).get("index");
        }
        return methodCache.get(getControllerName(requestURI)).get(getMethodName(requestURI));
    }

    /**
     * Adds a route to this router. The routeURI is the
     * URI that will be mapped to the controller named
     * by the 2nd parameter.
     * @param routURI  i.e.: "/articles/march/2009/"
     * @param controllerName i.e.: ""
     */
    public void addRoute(String routeURI, String controllerName, String methodName) {
        routeEntries.put(routeURI, o(controllerName, methodName));
    }

    /**
     * Add a controller Class object in the cache.
     * @param controllerClass
     */
    public void addControllerClass(Class controllerClass) {
        String className = controllerClass.getSimpleName(); // Get Original Class Name
        String classNameKey = controllerClass.getSimpleName().toLowerCase(); // Get Name lower cased to be used a map key
        controllerClassesCache.put(className, controllerClass); // Cache the Class object
        String classAlias = "";
        if (controllerClass.isAnnotationPresent(ControllerURLAlias.class)) {
            ControllerURLAlias annotation = (ControllerURLAlias) controllerClass.getAnnotation(ControllerURLAlias.class);
            classAlias = annotation.value().toLowerCase();
            addRoute(classAlias + "/", className, null); // Add this route to the default controller method
        }
        for (Method m : controllerClass.getDeclaredMethods()) {
            if (m.getModifiers() == java.lang.reflect.Modifier.PUBLIC) { // We only need public methods
                addMethodClass(className, m);
                if (m.isAnnotationPresent(MethodURLAlias.class)) {
                    String methodAlias = m.getAnnotation(MethodURLAlias.class).value().toLowerCase();
                    if (!classAlias.isEmpty()) {
                        addRoute(classAlias + "/" + methodAlias, className, m.getName());                    
                    }
                    addRoute(classNameKey + "/" + methodAlias, className, m.getName());
                }
                addRoute(classNameKey + "/" + m.getName().toLowerCase(), className, m.getName());
            }
        }
    }

    private void addMethodClass(String className, Method method) {
        if (methodCache.get(className) == null) {
            methodCache.put(className, new LinkedHashMap<String, Method>());
        }
        methodCache.get(className).put(method.getName(), method);
    }

    /**
     * Given a URI it returns the controller part and the method part. Used by
     * Adapter to call the appropriate Controller and method.
     *
     * @param path
     *            HttpServletRequest.getRequestURI()
     * @return
     */
    private static String getController(String path) {
        String controllerName;
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        if (pathParts.size() == 0) {
            controllerName = Settings.get("DEFAULT_CONTROLLER");
        } else {
            controllerName = pathParts.get(0);
        }
        return capitalize(controllerName);  //<-- CMS only
    }

    /**
     * extracts the method name from a url
     * @param path HttpServletRequest.getRequestURI()
     * @return
     */
    public static String getMethod(String path) {
        String methodName;
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        if (pathParts.size() == 0) {
            methodName = "index";
        } else {
            if (pathParts.size() == 1) {
                methodName = "index";
            } else {
                methodName = pathParts.get(1);
            }
        }
        return methodName;
    }

    public static String capitalize(String s) {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String k : this.routeEntries.keySet()) {
            sb.append("Key: ").append(k).append(" ->").append(routeEntries.get(k)._1).append(" - ").append(routeEntries.get(k)._2).append("\n");
        }
        return sb.toString();
    }

    /**
     * Comparator used for the TreeMap. Compares Strings by length
     * and if and only the lengths are equal compares them lecically.
     */
    private class StringLengthComparator implements Comparator {

        @Override
        public int compare(Object t1, Object t2) {
            int BEFORE = -1;
            int AFTER = 1;
            if (t1.toString().length() > t2.toString().length()) {
                return BEFORE;
            } else if (t1.toString().length() < t2.toString().length()) {
                return AFTER;
            } else {
                return t1.toString().compareTo(t2.toString());
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this.toString().length() == obj.toString().length()) {
                return (this.toString().compareTo(obj.toString()) == 0) ? true : false;
            } else {
                return this.toString().length() == obj.toString().length();
            }
        }
    }
}
