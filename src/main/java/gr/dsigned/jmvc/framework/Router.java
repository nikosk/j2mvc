/*
 *  Router.java
 *
 *  Copyright (C) Feb 4, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Router {

    String requestURI;
    private SortedMap<String, String> entries = new TreeMap<String, String>(new StringLengthComparator());

    public Router(HttpServletRequest request) {
        requestURI = request.getRequestURI();
        entries.put("/admin/", "AdminController");
        entries.put("/admin", "AdminController");
        entries.put("/admin/articles/edit/", "AdminController.edit");
        entries.put("/article/preview/", "AdminController");
    }

    protected void setEntries(SortedMap<String, String> entries) {
        this.entries = new TreeMap(new StringLengthComparator());
        this.entries.putAll(entries);
    }

    public String getControllerName() {
        for (String route : entries.keySet()) {
            String cleanRoute = route.lastIndexOf("/") + 1 == route.length() ? route.substring(0, route.length() - 1) : route;
            String trimmedRequestURL = requestURI.length() > cleanRoute.length() ? requestURI.substring(0, cleanRoute.length()) : requestURI;
            if (cleanRoute.contains("*")) {
            } else if (cleanRoute.equalsIgnoreCase(trimmedRequestURL)) {
                return entries.get(route).contains(".") ? entries.get(route).split(".")[0] : entries.get(route);
            }
        }
        return "";
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
