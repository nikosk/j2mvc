/*
 *  Admin.java
 *
 *  Copyright (C) Apr 7, 2009 Nikosk <nikosk@dsigned.gr>
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

import gr.dsigned.jmvc.annotations.ControllerURLAlias;
import gr.dsigned.jmvc.annotations.MethodURLAlias;
import gr.dsigned.jmvc.controls.forms.NewForms;
import gr.dsigned.jmvc.controls.forms.fields.CharField;
import gr.dsigned.jmvc.controls.forms.fields.Checkbox;
import gr.dsigned.jmvc.controls.forms.fields.DropdownMenu;
import gr.dsigned.jmvc.controls.forms.fields.EditorField;
import gr.dsigned.jmvc.controls.forms.fields.PasswordField;
import gr.dsigned.jmvc.controls.forms.fields.SubmitButton;
import gr.dsigned.jmvc.controls.forms.fields.TextareaField;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.GreeklishConverter;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.libraries.Pagination;
import gr.dsigned.jmvc.libraries.external.json.JSONObject;
import gr.dsigned.jmvc.libraries.external.json.JSONValue;
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.models.Category;
import gr.dsigned.jmvc.models.Category.CategoryNode;
import gr.dsigned.jmvc.models.Section;
import gr.dsigned.jmvc.models.Section.SectionNode;
import gr.dsigned.jmvc.models.User;
import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.types.Node;
import java.util.ArrayList;
import static gr.dsigned.jmvc.controls.forms.fields.Field.Rule.*;
import static gr.dsigned.jmvc.types.operators.*;
import static gr.dsigned.jmvc.framework.Renderer.*;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
@ControllerURLAlias("/control_panel")
public class Admin extends Controller {

    public Admin() throws Exception {
    }

    public void index() throws Exception {
        String username = $.input.post("username");
        String password = $.input.post("password");
        NewForms form = new NewForms();
        form.setAction($.request.getRequestURI());
        form.setFields(
                new CharField("username", "username", username, o(REQUIRED, "true"), o(MIN_LENGTH, "3"), o(MAX_LENGTH, "45"), o(XSS_CLEAN, "true")),
                new PasswordField("Password", "password", password, o(REQUIRED, "true"), o(MAX_LENGTH, "45"), o(XSS_CLEAN, "true")),
                new SubmitButton("login"));
        if ($.input.isPost() && form.isValid()) {
            Hmap authUser = new User().auth(username, password);
            if (authUser != null) {
                String userId = authUser.get("id");
                $.session.set("id", userId);
                $.session.set("logged", "1");
                $.response.sendRedirect("/control_panel/dashboard");
            } else {
                $.session.setTemp("message", "Invalid username or password");
            }
        }
        PageData data = new PageData();
        data.put("sidebar", "");
        data.put("message", $.session.data("message"));
        data.put("content", form.renderControl());
        $.loadView("index", data);
    }

    public void dashboard() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Category c = new Category();
        PageData data = new PageData();
        data.put("content", "");
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    public void articles() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return;// alway run this 1st for logged areas
        }
        Article a = new Article();
        String catName = $.input.segment(2);
        int offset = $.input.segment(3).isEmpty() ? 0 : Integer.parseInt($.input.segment(3));
        Pagination p = new Pagination();
        p.setBaseUrl("/control_panel/articles/" + catName);
        p.setCurrentPage(offset);
        p.setPerPage(20);
        p.setTotalRows(a.countArticlesByCatName(catName));
        p.setNumLinksToDisplay(5);
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(div(a("edit category", o("href", "/admin/edit_category/" + catName)), o("class", "category_action toolbar")));
        sb.append(div(p.createLinks(offset, Pagination.PagingType.ITEM)));
        sb.append(div(renderArticles(a.getArticlesByCatName($.input.segment(2), p.getPerPage(), offset * p.getPerPage()))));
        sb.append(div(p.createLinks(offset, Pagination.PagingType.SEARCH), o("class", "search_page_links")));
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    @MethodURLAlias("edit_article")
    public void editArticle() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Article a = new Article();
        Category c = new Category();
        String articleId = $.input.segment(2);
        Hmap article = a.getArticleById(articleId);
        NewForms form = new NewForms($.request.getRequestURI());
        form.setFields(new SubmitButton("save article"),
                new DropdownMenu("category", "category_id", article.get("name"), c.getCategoryIdNamePair(), o(REQUIRED, "")),
                new CharField("title", "title", "", o(REQUIRED, "true")),
                new CharField("real_title", "real_title", ""),
                new CharField("sub_title", "sub_title", ""),
                new TextareaField("lead_in", "lead_in", "5", "150", "", o(REQUIRED, "true"), o(XSS_CLEAN, "true")),
                new EditorField("content", "content", "25", "150", "", o(REQUIRED, "true"), o(XSS_CLEAN, "")));
        if ($.input.isGet()) {
            form.setFormData(article);
        } else {
            form.setFormData($.input.getPostData());
            if (form.isValid()) {
                Hmap updatedArticle = form.getFormData();
                updatedArticle.remove("submit_button");
                a.updateArticle(articleId, updatedArticle);
                $.session.setTemp("message", div("Article saved succesfuly", o("class", "success")));
                $.response.sendRedirect($.request.getRequestURI());
            }
        }
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(form.renderControl());
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    @MethodURLAlias("delete_article")
    public void deleteArticle() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Article a = new Article();
        Category c = new Category();
        String articleId = $.input.segment(2);
        String catname = c.getCategoryById(a.getArticleById(articleId).get("category_id")).get("name");
        a.deleteById(articleId);
        $.session.setTemp("message", div("Article deleted succesfuly", o("class", "success")));
        $.response.sendRedirect("/control_panel/articles/" + catname);
    }

    @MethodURLAlias("add_article")
    public void addArticle() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Article a = new Article();
        Category c = new Category();
        NewForms form = new NewForms($.request.getRequestURI());
        form.setId("article_form");
        form.setFields(new SubmitButton("save article"),
                new DropdownMenu("category", "category_id", "", c.getCategoryIdNamePair(), o(REQUIRED, "")),
                new CharField("title", "title", "", o(REQUIRED, "true")),
                new CharField("real_title", "real_title", ""),
                new CharField("sub_title", "sub_title", ""),
                new TextareaField("lead_in", "lead_in", "5", "150", "", o(REQUIRED, "true"), o(XSS_CLEAN, "true")),
                new EditorField("content", "content", "25", "150", "", o(REQUIRED, "true"), o(XSS_CLEAN, "")));
        form.setFormData($.input.getPostData());
        if ($.input.isPost()) {
            if (form.isValid()) {
                Hmap updatedArticle = form.getFormData();
                updatedArticle.remove("submit_button");
                a.insertArticle(updatedArticle);
                $.session.setTemp("message", div("Article saved succesfuly", o("class", "success")));
                $.response.sendRedirect($.request.getRequestURI());
            }
        }
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(form.renderControl());
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    @MethodURLAlias("add_category")
    public void addCategory() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Category c = new Category();
        NewForms form = new NewForms($.request.getRequestURI());
        Hmap catIdNamePairs = new Hmap();
        catIdNamePairs.put("NULL", "No parent");
        catIdNamePairs.putAll(c.getCategoryIdNamePair());
        form.setFields(new SubmitButton("add category"),
                new DropdownMenu("parent category", "parent_id", "", catIdNamePairs, o(REQUIRED, "")),
                new CharField("name", "name", "", o(REQUIRED, "true")),
                new CharField("display_name", "display_name", "", o(REQUIRED, "true")),
                new CharField("priority", "priority", ""),
                new Checkbox("Enabled", "enabled", "0"));
        if ($.input.isPost()) {
            form.setFormData($.input.getPostData());
            if (form.isValid()) {
                Hmap categoryData = form.getFormData();
                categoryData.remove("submit_button");
                c.insert(categoryData);
                $.session.setTemp("message", div("Category saved succesfuly", o("class", "success")));
                $.response.sendRedirect($.request.getRequestURI());
            }
        }
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(form.renderControl());
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    public void edit_category() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Category c = new Category();
        String catName = $.input.segment(2);
        NewForms form = new NewForms($.request.getRequestURI());
        Hmap catIdNamePairs = new Hmap();
        catIdNamePairs.put("NULL", "No parent");
        catIdNamePairs.putAll(c.getCategoryIdNamePair());
        Hmap categoryData = c.getCategoryByName(catName);
        form.setFields(new SubmitButton("edit category"),
                new DropdownMenu("parent category", "parent_id", "", catIdNamePairs, o(REQUIRED, "")),
                new CharField("name", "name", "", o(REQUIRED, "true")),
                new CharField("display_name", "display_name", "", o(REQUIRED, "true")),
                new CharField("priority", "priority", ""),
                new Checkbox("Enabled", "enabled", "0"));
        if ($.input.isPost()) {
            form.setFormData($.input.getPostData());
            if (form.isValid()) {
                Hmap formData = form.getFormData();
                formData.remove("submit_button");
                c.update(categoryData.get("id"), formData);
                $.session.setTemp("message", div("Category saved succesfuly", o("class", "success")));
                $.response.sendRedirect($.request.getRequestURI());
            }
        } else {
            form.setFormData(categoryData);
        }
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(form.renderControl());
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    public void add_section() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Section s = new Section();
        NewForms form = getSectionOptionForm($.input.getPostData());
        form.setAction($.request.getRequestURI());
        if ($.input.isPost()) {
            if (form.isValid()) {
                Hmap sectionData = form.getFormData();
                JSONObject options = new JSONObject();
                options.put("template", sectionData.get("template"));
                options.put("datasource", sectionData.get("datasource"));
                sectionData.remove("submit_button");
                sectionData.remove("template");
                sectionData.remove("datasource");
                sectionData.put("url_name", GreeklishConverter.toGreeklish(sectionData.get("name")).toLowerCase());
                sectionData.put("options", options.toString());
                s.insert(sectionData);
                $.session.setTemp("message", div("Category saved succesfuly", o("class", "success")));
                //$.response.sendRedirect($.request.getRequestURI());
            }
        }
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(form.renderControl());
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }

    public void edit_section() throws Exception {
        if (!isLogged()) {
            $.response.sendRedirect("/admin/");
            return; // alway run this 1st for logged areas
        }
        Section s = new Section();
        String id = $.input.segment(2);
        NewForms form = null;
        if ($.input.isPost()) {
            form = getSectionOptionForm($.input.getPostData());
            form.setAction($.request.getRequestURI());
            if (form.isValid()) {
                Hmap sectionData = form.getFormData();
                JSONObject options = new JSONObject();
                options.put("template", sectionData.get("template"));
                options.put("datasource", sectionData.get("datasource"));
                sectionData.remove("submit_button");
                sectionData.remove("template");
                sectionData.remove("datasource");
                sectionData.put("url_name", GreeklishConverter.toGreeklish(sectionData.get("name")).toLowerCase());
                sectionData.put("options", options.toString());
                s.update(id, sectionData);
                $.session.setTemp("message", div("Section saved succesfuly", o("class", "success")));
                //$.response.sendRedirect($.request.getRequestURI());
            }
        } else {
            form = getSectionOptionForm(s.getById(id));
            form.setAction($.request.getRequestURI());
        }
        PageData data = new PageData();
        StringBuilder sb = new StringBuilder();
        sb.append(form.renderControl());
        data.put("content", sb.toString());
        data.put("message", $.session.data("message"));
        data.put("sidebar", sidebar());
        data.put("right_sidebar", rightSidebar());
        $.loadView("index", data);
    }
    //////////////////////////////////////// Helper functions ////////////////////////////////////////////////

    private String sidebar() throws Exception {

        StringBuilder menuBuilder = new StringBuilder();
        menuBuilder.append(div(renderMainMenu(), o("class", "menu_panel")));
        return menuBuilder.toString();
    }

    private String rightSidebar() throws Exception {
        Category c = new Category();
        Section s = new Section();
        StringBuilder categoryTreeBuider = new StringBuilder();
        StringBuilder sectionTreeBuilder = new StringBuilder();
        StringBuilder menuBuilder = new StringBuilder();
        renderCategories(c.getCategoryGraph(), categoryTreeBuider);
        renderSections(s.getSectionGraph(), sectionTreeBuilder);
        menuBuilder.append(div(div(sectionTreeBuilder.toString(), o("id", "section_menu")), o("class", "menu_panel")));
        menuBuilder.append(div(div(categoryTreeBuider.toString(), o("id", "menu")), o("class", "menu_panel")));
        return menuBuilder.toString();
    }

    private void renderCategories(CategoryNode node, StringBuilder sb) {
        sb.append("<ul>");
        for (Node n : node.getChildren()) {
            CategoryNode cn = (CategoryNode) n;
            sb.append("<li>");
            sb.append(a(cn.getData().get("display_name"), o("href", "/control_panel/articles/" + cn.getName())));
            if (cn.getChildren().size() > 0) {
                renderCategories(cn, sb);
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
    }

    private void renderSections(SectionNode node, StringBuilder sb) {
        sb.append("<ul>");
        for (Node n : node.getChildren()) {
            SectionNode cn = (SectionNode) n;
            sb.append("<li>");
            sb.append(a(cn.getData().get("name"), o("href", "/control_panel/edit_section/" + cn.getData().get("id"))));
            if (cn.getChildren().size() > 0) {
                renderSections(cn, sb);
            }
            sb.append("</li>");
        }
        sb.append("</ul>");

    }

    private String renderArticles(ArrayList<Hmap> articles) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        sb.append("<thead><tr><th>ID</th><th>Title</th><th>Published</th><th>Last Update</th><th>Actions</th></tr></thead>");
        for (Hmap a : articles) {
            sb.append("\n\t<tr>");
            sb.append("\n\t\t<td class='article_id'>");
            sb.append(a.get("id"));
            sb.append("</td>");
            sb.append("\n\t\t<td class='article_title'>");
            sb.append(a(a.get("title"), o("href", "/control_panel/edit_article/" + a.get("id"))));
            sb.append("</td>");
            sb.append("\n\t\t<td class='article_date'>");
            sb.append(a.get("published"));
            sb.append("</td>");
            sb.append("\n\t\t<td class='article_date'>");
            sb.append(a.get("updated"));
            sb.append("</td>");
            sb.append("\n\t\t<td class='article_actions'>");
            sb.append(a(img(o("src", "/static/img/icons/page_delete.png"), o("alt", "delete")), o("href", "/control_panel/delete_article/" + a.get("id"))));
            sb.append("</td>");
            sb.append("\n\t</tr>");
        }
        sb.append("<tr><td></td><td></td><td></td><td></td><td></td></tr>");
        sb.append("</table>\n");
        return sb.toString();
    }

    private String renderMainMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul id='main_menu'>");
        sb.append(li(a("Dashboard", o("href", "/control_panel/dashboard/"))));
        sb.append(li(a("New Article", o("href", "/control_panel/add_article/"))));
        sb.append(li(a("New Category", o("href", "/control_panel/add_category/"))));
        sb.append(li(a("New Section", o("href", "/control_panel/add_section/"))));
        sb.append("</ul>");
        return sb.toString();
    }

    protected boolean isLogged() throws Exception {
        String user = $.session.data("logged");
        if (user.isEmpty()) {
            $.session.setTemp("message", "<div class='error'>Session Expired or User not signed in</div>");
            return false;
        }
        return true;
    }

    protected void isAdmin() throws Exception {
        String admin = $.session.data("admin");
        if (admin.isEmpty()) {
            $.session.setTemp("message", "<div class='error'>Session Expired or user not signed in</div>");
            $.response.sendRedirect("/admin");
            return;
        }
    }

    /**
     * Create a form object for section editing.
     * The method populates the form then checks to
     * see if the data contains an "options" object.
     * If true it parses the object and populates the
     * form with the object content.
     * @param formData (from POST or DB)
     * @return
     * @throws java.lang.Exception
     */
    private NewForms getSectionOptionForm(Hmap formData) throws Exception {
        NewForms f = new NewForms();
        Section s = new Section();
        Category c = new Category();
        Hmap templates = (Hmap) $.context.getAttribute("templates");
        Hmap sectionIdNamePairs = s.getSectionIdNamePair();
        sectionIdNamePairs.put("NULL", "No parent");
        sectionIdNamePairs.putAll(s.getSectionIdNamePair());
        f.setFields(new SubmitButton("save"),
                new DropdownMenu("parent section", "parent_id", formData.get("parent_id") != null ? formData.get("parent_id") : "", sectionIdNamePairs, o(REQUIRED, "")),
                new CharField("name", "name", formData.get("name") != null ? formData.get("name") : "", o(REQUIRED, "true")));
        DropdownMenu template;
        DropdownMenu datasource;
        f.setFields(template = new DropdownMenu("Template", "template", "", templates));
        f.setFields(datasource = new DropdownMenu("Datasource", "datasource", "", c.getCategoryIdNamePair()));
        if (formData.containsKey("options")) {
            JSONObject o = (JSONObject) JSONValue.parse(formData.get("options"));
            template.setSelected(o.get("template") != null ? o.get("template").toString() : "");
            datasource.setSelected(o.get("datasource") != null ? o.get("datasource").toString() : "");
            formData.remove("options");
        }
        f.setFormData(formData);
        return f;
    }
}
