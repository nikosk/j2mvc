/*
 *  Renderer.java
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
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.*;
import java.util.HashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Renderer {

    public static String anchor(String segments, String title, String attributes) {        
        String href = Settings.ROOT_URL;
        href = (!Settings.SUB_DIR.isEmpty()) ? Settings.SUB_DIR + "/" : "";
        href = (segments.startsWith("/") && segments.length() < 2) ? "" : segments;
        if (attributes == null || attributes.isEmpty()) {
            return String.format("<a href='%1$s' title='%2$s'>%2$s</a>", href, title);
        } else {
            return String.format("<a href='%1$s' title='%2$s' %3$s>%2$s</a>", href, title, attributes);
        }
    }

    public static String anchor(String segments, String title, HashMap<String, String> attributes) {
        segments = (segments.startsWith("/") && segments.length() > 2) ? segments.substring(1) : segments;
        String href = Settings.ROOT_URL;
        href = (!Settings.SUB_DIR.isEmpty()) ? Settings.SUB_DIR + "/" : "";
        href = (segments.startsWith("/") && segments.length() < 2) ? "" : segments;
        if (attributes == null) {
            return String.format("<a href='%1$s' title='%2$s'>%2$s</a>", href, title);
        } else {
            String attr = "";
            for (String a : attributes.keySet()) {
                attr += a + "='" + attributes.get(a) + "' ";
            }
            return String.format("<a href='%1$s' title='%2$s' %3$s>%2$s</a>", href, title, attr);
        }
    }

    public static String root_url() {
        return Settings.ROOT_URL;
    }
}
