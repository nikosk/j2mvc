/*
 *  Template.java
 *
 *  Copyright (C) May 10, 2009 Nikosk <nikosk@dsigned.gr>
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

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Template {

    protected String viewname;
    protected Object data;

    public Template() {
    }

    public Template(String viewname) {
        this.viewname = viewname;
    }

    public String getViewname() {
        return viewname;
    }

    public void setViewname(String viewname) {
        this.viewname = viewname;
    }

    public Object getData() {
        return data;
    }

    /**
     * Legacy method. Takes a map
     * and puts all its entries in this instance
     * using the key "data"
     * @param data usually a pagedata object
     */
    public void setData(Object data) {
        this.data = data;
    }

    
}
