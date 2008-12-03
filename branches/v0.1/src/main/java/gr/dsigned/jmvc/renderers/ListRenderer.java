/*
 *  ListRenderer.java
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
package gr.dsigned.jmvc.renderers;

import gr.dsigned.jmvc.framework.Renderer;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author nikos
 */
public class ListRenderer extends Renderer {

    public String renderLists(ArrayList<LinkedHashMap<String, String>> sites, LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>> issues) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.anchor("/sites/add_site/", "add site", "id='add_site'"));
        if (sites.size() > 0) {
            sb.append("<ul>");
            for (LinkedHashMap<String, String> s : sites) {
                sb.append("<li>")
                        .append("<a href='/sites/delete_site/")
                                .append(s.get("id"))
                                .append("'><img src='/images/icons/famfam/cancel.png' width='10' height='10' style='padding:0px 10px;'/>")
                        .append("<a href='/sites/add_task/").append(s.get("id"))
                                .append("'><img src='/images/icons/famfam/add.png' width='10' height='10' style='padding:0px 10px;'/></a>")
                                .append("<a href='/sites/edit_site/").append(s.get("id")).append("'>")
                                        .append(s.get("label"))
                                .append("</a>");
                sb.append("<div><ul>");
                for(LinkedHashMap issue : issues.get(s.get("label"))){
                    sb.append("<li>");
                    sb.append("<div>").append(issue.get("label")).append("</div>");
                    sb.append("<div>").append(issue.get("description")).append("</div>");
                    sb.append("</li>");                    
                }
                sb.append("</ul></div>");
                sb.append("</li>");
            }
            sb.append("</ul>");
        }
        return sb.toString();
    }
}