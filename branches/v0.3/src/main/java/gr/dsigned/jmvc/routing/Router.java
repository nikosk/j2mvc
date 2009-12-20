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
package gr.dsigned.jmvc.routing;

import gr.dsigned.jmvc.Settings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Router {

    private Map<String, ControllerCacheItem> controllerEntries = new HashMap<String, ControllerCacheItem>();

    public void addController(Class controllerClass) {
        ControllerCacheItem item = new ControllerCacheItem(controllerClass);
        controllerEntries.put(item.getClassName().toLowerCase(), item);
        controllerEntries.put(item.getAlias(), item);
    }

    public Class getControllerClass(String requestURI) {
        return controllerEntries.get(getControllerNameFromURI(requestURI)).getControllerClass();
    }

    public Method getMethodClass(String requestURI) {
        String controllerClassName = getControllerNameFromURI(requestURI);
        String methodName = getMethodNameFromURI(requestURI);
        return controllerEntries.get(controllerClassName).getMethodByNameOrAlias(methodName);
    }

    final String getControllerNameFromURI(String requestURI) {
        List<String> uriParts = getUriParts(requestURI);
        while (uriParts.contains("")) {
            uriParts.remove("");
        }
        String result = null;
        if (uriParts.size() == 0) {
            result = Settings.get("DEFAULT_CONTROLLER").toLowerCase();
        } else if (uriParts.size() >= 1) {
            result = uriParts.get(0);
        }
        return result;
    }

    final String getMethodNameFromURI(String requestURI) {
        List<String> uriParts = getUriParts(requestURI);
        String result = null;
        if (uriParts.size() <= 1) {
            result = "index";
        } else if (uriParts.size() > 1) {
            result = uriParts.get(1);
        }
        return result;
    }

    private List<String> getUriParts(String requestURI) {
        List<String> uriParts = new ArrayList<String>(Arrays.asList(requestURI.toLowerCase().split("/")));
        while (uriParts.contains("")) {
            uriParts.remove("");
        }
        return uriParts;
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
//    public Class getControllerClass(String requestURI) {
//        for (String route : routeEntries.keySet()) {
//            String cleanRoute = route.lastIndexOf("/") + 1 == route.length() ? route.substring(0, route.length() - 1) : route;
//            String trimmedRequestURL = requestURI.length() > cleanRoute.length() ? requestURI.substring(0, cleanRoute.length()) : requestURI;
//            if (cleanRoute.length() > 0 && cleanRoute.equals(trimmedRequestURL)) {
//                return routeEntries.get(route).getControllerCacheItem().getControllerClass();
//            }
//        }
//        return null;
//    }
//
//    public Method getMethodClass(String requestURI) {
//        for (String route : routeEntries.keySet()) {
//            String cleanRoute = route.lastIndexOf("/") + 1 == route.length() ? route.substring(0, route.length() - 1) : route;
//            String trimmedRequestURL = requestURI.length() > cleanRoute.length() ? requestURI.substring(0, cleanRoute.length()) : requestURI;
//            if (cleanRoute.equals(trimmedRequestURL)) {
//                if (routeEntries.get(route) != null) {
//                    return routeEntries.get(route).getMethod();
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Adds a route to this router. The routeURI is the
//     * URI that will be mapped to the controller named
//     * by the 2nd parameter.
//     * @param routURI  i.e.: "/articles/march/2009/"
//     * @param controllerName i.e.: ""
//     */
//    private void addRoutes(ControllerCacheItem item) {
//        for (RouterItem ri : item.getRouterItems()) {
//            routeEntries.put(ri.getRoute(), ri);
//        }
//    }
//
//    /**
//     * Add a controller Class object in the cache.
//     * @param controllerClass
//     */
//    public void addController(Class controllerClass) {
//        ControllerCacheItem item = new ControllerCacheItem(controllerClass);
//        addRoutes(item);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for (String k : this.routeEntries.keySet()) {
//            sb.append("Key: ").append(k).append(" ->").append(routeEntries.get(k).getControllerName()).append(" - ").append(routeEntries.get(k).getMethodName()).append("\n");
//        }
//        return sb.toString();
//    }
//

