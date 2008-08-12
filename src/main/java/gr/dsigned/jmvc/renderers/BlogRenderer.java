/*
 *  BlogRenderer.java
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

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.framework.Renderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class BlogRenderer extends Renderer {

    private boolean showAuthor = true;
    private boolean withReadMore = true;

    public String renderPost(LinkedHashMap<String, String> rawPost) throws Exception {
        String out = "<div class='blog_post'>" + "\n";
        out += "\t<a href='edit_article/'" + rawPost.get("id") + ">" + rawPost.get("title") + "</a>" + "\n";
        out += (showAuthor) ? "\t<span class='pub_date'>" + rawPost.get("published") + "</span>" + "\n" : "";
        out += "\t<div class='post_body'>";
        if (withReadMore && rawPost.get("content").length() > 200) {
            out += "\n\t<div class='lead_in'>" + rawPost.get("content").substring(0, 199) + "...</div>" + "\n";
        } else {
            out += "\n\t<div class='post_text'>" + rawPost.get("content") + "</div>" + "\n";
        }
        out += "\t</div>" + "\n";
        out += "</div>" + "\n";
        return out;
    }

    public String renderSitePost(LinkedHashMap<String, String> rawPost) throws Exception {
        String out = "<div class='blog_post'>" + "\n";
        out += "\t<a href='edit_article/'" + rawPost.get("id") + ">" + rawPost.get("url") + "</a>" + "\n";
        out += (showAuthor) ? "\t<span class='pub_date'>" + rawPost.get("adult") + "</span>" + "\n" : "";
        out += "\t<div class='post_body'>";
        if (withReadMore && rawPost.get("frame_behavior").length() > 200) {
            out += "\n\t<div class='lead_in'>" + rawPost.get("click_rate").substring(0, 199) + "...</div>" + "\n";
        } else {
            out += "\n\t<div class='post_text'>" + rawPost.get("status_id") + "</div>" + "\n";
        }
        out += "\t</div>" + "\n";
        out += "</div>" + "\n";
        return out;
    }

    /**
     * Renders the article titles in the admin page. Be careful because it the 
     * data contains fields from the categories table also. In case of a field name clash
     * use [table_name].[field_name] notation.
     * @param rawPost ResultData
     * @return Html: the html block with a article title and a link to the article's edit page.
     * @throws Exception In case something goes wrong the exception bubbles up and is displayed on an error page.
     */
    public String renderArticleTitles(LinkedHashMap<String, String> rawPost) throws Exception {
        String out = "<div class='blog_post'>" + "\n";
        out += "<a href='/articles/edit_form/" + rawPost.get("id") + "'>" + rawPost.get("title") + "</a>" + "\n";
        out += (showAuthor) ? "<span class='pub_date'>" + rawPost.get("published") + "</span>" + "\n" : "";
        out += "</div>" + "\n";
        return out;
    }

//    public String renderArticleTitlesWithDelete(LinkedHashMap<String, String> rawPost, int i, String category) throws Exception {
//        String out = "<br/><div class='blog_post'>" ;
//        out += i + "&nbsp;<a href='/articles/edit_form/" + rawPost.get("id") + "'>" + rawPost.get("title") + "</a>" + "\n";
//        out += (showAuthor) ? "<span class='pub_date'>" + rawPost.get("published") + "</span> <a href='/articles/delete_article/"+category+"/"+rawPost.get("id")+"'><img height='10' width='10' style='padding: 0px 10px;' src='/images/icons/famfam/cancel.png'/>&nbsp;</a>" + "\n" : "";
//        out += "</div>" + "\n";
//        return out;
//    }
     public String renderArticleTitlesWithDelete(ArrayList<Bean> posts, int i, String category) throws Exception {
        StringBuilder sb = new StringBuilder() ;
        for (Bean lhm : posts) {
            sb.append("<br/><div class='blog_post'>") ;
            sb.append(i).append("&nbsp;<a href='/articles/edit_form/").append(lhm.get("id") ).append("'>").append(lhm.get("title")).append("</a>\n");
            if(showAuthor) {
                sb.append("<span class='pub_date'>").append(lhm.get("published")).append("</span> <a href='/articles/delete_article/").append(category).append("/").append(lhm.get("id")).append("'><img height='10' width='10' style='padding: 0px 10px;' src='/images/icons/famfam/cancel.png'/>&nbsp;</a>\n");
            }
            sb.append("</div>\n");
            i++ ;
        }

        return sb.toString();
    }

    public String renderMenu(LinkedHashMap<String, String> menuElements) throws Exception {
        String out = "<ul>";
        for (String title : menuElements.keySet()) {
            out += String.format("<li>%1$s</li>", anchor(menuElements.get(title), title, "class='menu_link'"));
        }

        return out;
    }

//    public String buildMenu(String controller, String method, ArrayList<Bean> rawCats) throws Exception {
//        String out = "<ul>";
//        for (Bean row : rawCats) {
//            out += "\n" + "<a href='/" + controller + "/" + method + "/" + row.get("name") + "' >" + row.get("display_name") + "</a>";
//        }
//        out += "</ul>";
//        return out;
//    }
    
    public String buildMenu(String controller, String method, ArrayList<Bean> rawCats) throws Exception {
        StringBuilder sb = new StringBuilder() ;
        sb.append("<ul>") ;
        for (Bean row : rawCats) {
            sb.append("\n").append("<a href='/").append(controller).append("/").append(method).append("/").append(row.get("name")).append("' >").append(row.get("display_name")).append("</a>");
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
