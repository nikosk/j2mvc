/*
 *  PageData.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * PageData.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class PageData extends LinkedHashMap<String, String> {

    private HashMap<String, ArrayList<String>> pageElements;

    public PageData() {
        pageElements = new HashMap<String, ArrayList<String>>();
    }

    /**
     * Overides LinkedHashMap.put() to implement 
     * inserting to a specified position in the page element
     * @param key
     * @param value
     * @return
     */
    @Override
    public String put(String key, String value) {
        if (pageElements.containsKey(key)) {
            pageElements.get(key).add(value);
        } else {
            ArrayList<String> pageElement = new ArrayList<String>();
            pageElement.add(value);
            pageElements.put(key, pageElement);
        }
        return null;
    }

    /**
     * Insert the specified value in the specified page module at the 
     * specified position.
     * @param key The name of the module to insert to.
     * @param value The value to insert
     * @param position The index of the position to add to 
     */
    public void insert(String key, String value, int position) {
        if (pageElements.containsKey(key) && position >= 0 && pageElements.get(key).size() > position) {
            pageElements.get(key).add(position, value);
        } else {
            put(key, value);
        }
    }

    /**
     * Removes all other values for this page element
     * and inserts the one passed.
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        if (pageElements.containsKey(key)) {
            pageElements.get(key).clear();
            pageElements.get(key).add(value);
        } else {
            put(key, value);
        }
    }

    @Override
    public String get(Object key) {
        StringBuilder sb = new StringBuilder();
        if (pageElements.get(key) != null) {
            for (String s : pageElements.get(key)) {
                sb.append(s);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean containsKey(Object key) {
        return pageElements.containsKey(key);
    }
    
    public HashMap<String, ArrayList<String>> getPageElements() {
        return pageElements;
    }
}
