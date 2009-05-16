/*
 *  Articles.java
 * 
 *  Copyright (C) 2008 Vas Chryssikou <vchrys@gmail.com>
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

import gr.dsigned.jmvc.controls.forms.NewForms;
import gr.dsigned.jmvc.controls.forms.fields.CharField;
import gr.dsigned.jmvc.controls.forms.fields.DropdownMenu;
import gr.dsigned.jmvc.controls.forms.fields.HiddenField;
import gr.dsigned.jmvc.controls.forms.fields.SubmitButton;
import gr.dsigned.jmvc.controls.forms.fields.TextareaField;

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.libraries.Pagination;
import gr.dsigned.jmvc.models.Category;
import gr.dsigned.jmvc.models.User;
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.renderers.BlogRenderer;
import gr.dsigned.jmvc.types.Hmap;
import java.util.ArrayList;
import static gr.dsigned.jmvc.controls.forms.fields.Field.Rule.*;
import static gr.dsigned.jmvc.types.operators.*;

/**
 *
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class Articles extends Controller {

    Article article = $.loadModel("Article");

    public Articles() throws Exception {
    }

    public void index() throws Exception {
        PageData data = new PageData();
        String state = $.input.segment(2);
        if (state != null && state.equalsIgnoreCase("notauthorized")) {
            data.put("post_data", "Invalid Username or Password");
        } else {
            data.put("post_data", "");
        }
        $.loadView("index", data);

    }

    public void login() throws Exception {
        String username = $.input.post("id");
        String pass = $.input.post("pass");
        User user = $.loadModel("User");
        Hmap al = user.auth(username, pass);
        if (al != null) {
            $.session.set("userId", al.get("id"));
            $.session.set("loggedin", "true");
            $.response.sendRedirect("show_articles/news");
        } else {
            $.response.sendRedirect("/articles/index/notauthorized");
        }
    }

    public void show_articles() throws Exception {
        // Only for logged users
        // Our page data
        PageData data = new PageData();
        // Load the models we'll need
        Category cat = $.loadModel("Category");
        // Load the renderer we'll need
        BlogRenderer renderer = $.loadRenderer("BlogRenderer");
        // Lookup segment 2 (the article id)
        String category = $.input.segment(2);

        // Lookup uri segment 3 (the current page/offset in
        // the results)
        String page = $.input.segment(3);
        // Lets see if the current page is correct
        int offset = 0;
        if (!page.isEmpty()) {
            offset = Integer.parseInt(page);
        // If we have too many results then we need to
        // paginate them.
        }
        Pagination p = new Pagination();
        p.setBaseUrl("/articles/show_articles/" + category);
        p.setTotalRows(article.countArticlesByCatName(category));
        // Now we are ready to get some data (limit to 10
        // articles with offset page number * 10)
        ArrayList<Hmap> posts = article.getArticlesByCatName(category, p.getPerPage(), p.getPerPage() * offset);
        p.setTotalRows(posts.size());
        int i = (p.getPerPage() * offset) + 1;

        data.put("category", "-" + category);
        data.put("link_create", "<a href='/articles/show_form'>Create Article</a><br/>");
        data.put("data", renderer.renderArticleTitlesWithDelete(posts, i, category));
        // Build our menu of categories
        data.put("menu", renderer.buildMenu("articles", "show_articles", cat.getCategories()));
        data.put("item_links", p.createLinks(offset, Pagination.PagingType.ITEM));
        data.put("search_links", "");
        data.put("item_links", p.createLinks(offset, Pagination.PagingType.ITEM));
        data.put("search_links", p.createLinks(offset, Pagination.PagingType.SEARCH));
        data.put("styles", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/admin_styles.css\" >\r\n");
        $.loadView("index", data);
    }

    public void show_form() throws Exception {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd;
        Category cat = $.loadModel("Category");
        Hmap Hmaps = cat.getCategoryIdNamePair();
        f.setFields(
                dd = new DropdownMenu("Article Category", "category", $.input.post("category"), Hmaps, o(REQUIRED, "true")),
                new CharField("Title", "title", $.input.post("title"), o(REQUIRED, "true")),
                new CharField("Real Title", "real_title", $.input.post("real_title")),
                new CharField("Sub Title", "sub_title", $.input.post("sub_title")),
                new CharField("Lead In", "lead_in", $.input.post("lead_in")),
                new TextareaField("Content", "content", "5", "20", $.input.post("content"), o(REQUIRED, "true"), o(MAX_LENGTH, "20000"), o(MIN_LENGTH, "10")),
                new SubmitButton("submit_button", ""));

        if ($.input.getRequest().getMethod().equalsIgnoreCase("post") && f.isValid()) {
            Article art = $.loadModel("Article");
            Hmap bArt = f.getFormData();
            bArt.put("userId", $.session.data("userId"));
            art.insertArticle(bArt);
           Hmap cats = cat.getById($.input.post("category"));
            $.response.sendRedirect("/articles/show_articles/" + cats.get("name"));
        } else {
            String form = "<form  action='/articles/show_form' method='post'>";
            form += f.renderControl();
            form += "</form>";
            data.put("title", "Create Article");
            data.put("form", form);
            $.loadView("testing_forms", data);
        }
    }

    public void edit_form() throws Exception {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd;

        Hmap articleAL = null;
        Hmap bArt = null;

        String id = $.input.segment(2);
        if (id == null || id.length() == 0) {
            id = $.input.post("id");
            bArt = new Hmap();
            articleAL = new Hmap();
            bArt.put("title", $.input.post("title"));
            bArt.put("real_title", $.input.post("real_title"));
            bArt.put("sub_title", $.input.post("sub_title"));
            bArt.put("lead_in", $.input.post("lead_in"));
            bArt.put("content", $.input.post("content"));
            bArt.put("category", $.input.post("category"));
        } else {
            articleAL = article.getArticleById(id);
        }

        Category cat = $.loadModel("Category");
        Hmap Hmaps = cat.getCategoryIdNamePair();

        f.setFields(
                new HiddenField("id", id, o(REQUIRED, "true")),
                dd = new DropdownMenu("Category", "category", "", Hmaps, o(REQUIRED, "true")),
                new CharField("Title", "title", ""),
                new CharField("Real Title", "real_title", ""),
                new CharField("Sub Title", "sub_title", ""),
                new CharField("Lead In", "lead_in", ""),
                new TextareaField("Content", "content", "5", "20", "", o(REQUIRED, "true"), o(MAX_LENGTH, "20000"), o(MIN_LENGTH, "100")),
                new SubmitButton("submit_button"));
        if ($.input.getRequest().getMethod().equalsIgnoreCase("post") && f.isValid()) {
            article.editArticle(id, $.input.post("category"), $.input.post("title"), $.input.post("real_title"), $.input.post("sub_title"), $.input.post("lead_in"), $.input.post("content"));
            Hmap cats = cat.getCategoryById($.input.post("category"));
            $.response.sendRedirect("/articles/show_articles/" + cats.get("name"));
        } else {
            String form = "<form  action='/articles/edit_form' method='post'>";
            form += f.renderControl();
            form += "</form>";
            data.put("title", "Edit Article");
            data.put("form", form);
            $.loadView("testing_forms", data);
        }
    }

    public void delete_article() throws Exception {
        String cat = $.input.segment(2);
        String id = $.input.segment(3);
        article.deleteById(id);
        $.response.sendRedirect("/articles/show_articles/" + cat);
    }
}
