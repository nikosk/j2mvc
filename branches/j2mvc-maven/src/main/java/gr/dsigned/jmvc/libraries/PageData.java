/*
 *  PageData.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Settings;

import java.util.LinkedHashMap;

/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * PageData.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class PageData extends LinkedHashMap<String, String> {

    /**
     * 
     */
    private static final long serialVersionUID = 5521489114124146406L;

    public PageData() {
        put("title", "My title");
        put("keywords", "jmvc, framework, java, mvc");
        put("description", "Java mvc framework inspired by Code Igniter,Ruby on Rails and Django");
        put("charset", Settings.DEFAULT_ENCODING);
        /* Styles */
        put("styles-yui-all", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/reset.css\" >\r\n" + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/grids.css\">" + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/fonts.css\">");
        put("styles-yui-all-min", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/reset-fonts-grids.css\">");
        put("styles-yui-reset", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/reset.css\" >\r\n");
        put("styles-yui-grids", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/grids.css\">");
        put("styles-yui-fonts", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/fonts.css\">");
        put("styles-typography", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/typography.css\">");
        put("styles-forms", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/forms.css\">");
        /* mootools */
        put("mootools", "<script type=\"text/javascript\" src=\"/js/mootools-1.2-core-yc.js\"></script>");
        put("mootools-more", "<script type=\"text/javascript\" src=\"/js/mootools-1.2-more.js\"></script>");
    }
}
