/*
 *  CMSController.java
 *
 *  Copyright (C) Apr 27, 2009 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.controllers;

import gr.dsigned.j2cmsdb.dto.Template;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.external.json.JSONObject;
import gr.dsigned.jmvc.libraries.external.json.JSONValue;
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.models.Category;
import gr.dsigned.jmvc.models.Category.CategoryNode;
import gr.dsigned.jmvc.models.Images;
import gr.dsigned.jmvc.models.Section;
import gr.dsigned.jmvc.models.Section.SectionNode;
import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.types.Node;
import static gr.dsigned.jmvc.framework.Renderer.*;
import static gr.dsigned.jmvc.types.operators.*;

/**
 * This is a normal j2mvc controller that handles requests
 * for j2cms pages. This controller must determine which modules are loaded and
 * which template to use. 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class CMSController extends Controller {

    public void index() throws Exception {
        Section s = new Section();
        String sectionName = $.input.segment(0) == null ? "Arxikh" : $.input.segment(0);
        Hmap sectionData = s.getSectionByUrlName(sectionName);
        if (sectionData != null) {
            Article articleModel = new Article();
            String id = getSectionOptions(sectionData).get("datasource");
            StringBuilder sb = new StringBuilder();
            for (Hmap article : articleModel.getArticlesByCatId(id, 10, 0)) {
                sb.append(renderArticle(article));
            }
            Template t = new Template();
            t.setBodyCSSClass("default");
            t.setContent(sb.toString());
            t.setSidebar(sidebar());
            t.setJs("js");
            t.setTitle(sectionData.get("name"));
            $.request.setAttribute("template", t);
            $.request.getRequestDispatcher("/views/index.jsp").forward($.request, $.response);
        } else {
            $.response.sendError($.response.SC_NOT_FOUND);
        }
    }

    private Hmap getSectionOptions(Hmap s) {
        JSONObject o = (JSONObject) JSONValue.parse(s.get("options"));
        Hmap data = new Hmap();
        for (Object k : o.keySet()) {
            data.put(k.toString(), o.get(k).toString());
        }
        return data;
    }

    private String renderArticle(Hmap article) throws Exception {
        StringBuilder sb = new StringBuilder();
        Images im = new Images();
        Hmap img = im.getArticleImage(article.get("id"));
        sb.append("<div class='article_item'>");
        sb.append(h1(article.get("title")));
        if (img != null) {
            sb.append(img(o("src", "http://news247.gr/data/" + img.get("location") + "/" + img.get("name"))));
        }
        sb.append(div(article.get("lead_in")));
        sb.append(div(article.get("content")));
        sb.append("</div>");
        return sb.toString();
    }

    private String sidebar() throws Exception {
        Section s = new Section();
        StringBuilder sectionTreeBuilder = new StringBuilder();
        StringBuilder menuBuilder = new StringBuilder();
        renderSections(s.getSectionGraph(), sectionTreeBuilder);
        menuBuilder.append(div(div(sectionTreeBuilder.toString(), o("id", "section_menu")), o("class", "menu_panel")));
        return menuBuilder.toString();
    }

    private void renderSections(SectionNode node, StringBuilder sb) {
        sb.append("<ul>");
        for (Node n : node.getChildren()) {
            SectionNode cn = (SectionNode) n;
            sb.append("<li>");
            sb.append(a(cn.getData().get("name"), o("href", "/" + cn.getData().get("url_name"))));
            if (cn.getChildren().size() > 0) {
                renderSections(cn, sb);
            }
            sb.append("</li>");
        }
        sb.append("</ul>");

    }
}
