/*
 *  Parser.java
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
package gr.dsigned.jmvc.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements tags in the template
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Parser {
    /* finds <% site_url('/admin/login') %> */
    private static Pattern urlPattern = Pattern.compile("<% *site_url *\\([\'\"](.*)[\'\"] *\\) *%>");
    /* finds <%anchor("link/link","title")%> */
    private static Pattern anchorPatternShort = Pattern.compile("<% *anchor *\\( *\"([\\w?=&\\./]*)\" *, *\"(.*)\" *\\) *%>");
    /* find <%anchor("link/link", "title", "Extra attributes") %> */
    private static Pattern anchorPattern = Pattern.compile("<% *anchor *\\( *\"([\\w?=&\\./]*)\" *, *\"(.*)\" *, *\"(.*)\" *\\) *%>");

    public static String parse(String template) {
        if (template.indexOf("anchor") > 0) { // Fail fast to save cycles
            template = anchor(template);
        }
        if (template.indexOf("site_url") > 0) { // Fail fast to save cycles
            template = siteUrl(template);
        }
        if (template.indexOf("form_action") > 0) {
            template = formAction(template);
        }
        return template;
    }

    public static String siteUrl(String template) {
        HashMap<String, String> urls = new HashMap<String, String>();
        Matcher m = urlPattern.matcher(template);
        while (m.find()) {
            urls.put(m.group(0), m.group(1));
        }
        for (String key : urls.keySet()) {
            while (template.indexOf(key) > -1) {
                template = template.replace(key, urls.get(key));
            }
        }
        return template;
    }

    /**
     * 
     * @param template
     * @return
     */
    public static String anchor(String template) {

        HashMap<String, List<String>> anchors = new HashMap<String, List<String>>();
        HashMap<String, List<String>> shortAnchors = new HashMap<String, List<String>>();
        Matcher m = anchorPattern.matcher(template); // Set the matcher to
        // find the long pattern
        while (m.find()) { // Now find them
            ArrayList<String> value = new ArrayList<String>();
            value.add(m.group(1));
            value.add(m.group(2));
            value.add(m.group(3));
            anchors.put(m.group(0), value);
        }
        // Set the matcher to find the short pattern
        m = anchorPatternShort.matcher(template);
        while (m.find()) {
            ArrayList<String> value = new ArrayList<String>();
            value.add(m.group(1));
            value.add(m.group(2));
            shortAnchors.put(m.group(0), value);
        }
        for (String a : anchors.keySet()) {
            while (template.indexOf(a) > -1) {
                String attributes = (anchors.get(a).size() > 2) ? (String) anchors.get(a).get(2) : "";
                String replacement = Renderer.anchor((String) anchors.get(a).get(0), (String) anchors.get(a).get(1), attributes);
                template = template.replace(a, replacement);
            }
        }
        for (String a : shortAnchors.keySet()) {
            while (template.indexOf(a) > -1) {
                String replacement = Renderer.anchor((String) shortAnchors.get(a).get(0), (String) shortAnchors.get(a).get(1), "");
                template = template.replace(a, replacement);
            }
        }
        return template;
    }

    public static String formAction(String template) {
        String out = "";

        return out;
    }
}
