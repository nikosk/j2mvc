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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.regex.Pattern;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Router {
    private Pattern extractControllerMethodPattern = Pattern.compile(".*\\((\\d)\\).*");
    private LinkedHashMap<String,String> entries = new SortedMap() {}<String, String>();

    public Router(){
        entries.put("/admin/*/", "AdminController");
        entries.put("/admin/articles/edit/", "AdminController.edit");
        entries.put("/article/preview/", "AdminController");
        ArrayList<String> entryKeys = new ArrayList(entries.keySet());
        Collections.sort(entryKeys, new StringLengthComparator());
        entries.
        System.out.println("");
    }
    
    public String getControllerName(String requestPath) {
      return "";
    }
    
    private class StringLengthComparator implements Comparator{
    	
        @Override
    	public int compare(Object t1, Object t2){            
    		int BEFORE = -1;
    		int EQUAL = 0;
    		int AFTER = 1;
    		if(t1.toString().length() > t2.toString().length()){
    			return BEFORE;
    		} else if(t1.toString().length() < t2.toString().length()){
    			return AFTER;
    		} else {
    			return EQUAL;
    		}
    	}

        @Override
        public boolean equals(Object obj) {
            return this.toString().length() == obj.toString().length();
        }
    	
    }
}
