package gr.dsigned.jmvc.controllers;

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.PageDict;
import gr.dsigned.jmvc.libraries.Pagination;
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.models.Category;
import gr.dsigned.jmvc.models.User;
import gr.dsigned.jmvc.renderers.BlogRenderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Admin extends Controller {

    BlogRenderer renderer = $.loadRenderer("BlogRenderer");
    Article article = $.loadModel("Article");

    public Admin() throws Exception {
    }

    public void index() throws Exception {
        $.loadView("admin/index", null);
    }

    public void login() throws Exception {
        String username = $.input.post("id");
        String pass = $.input.post("pass");
        User user = $.loadModel("User");
        if (user.auth(username, pass)) {
            $.session.set("loggedin", "true");
            $.response.sendRedirect("show_articles");
        } else {
            throw new Exception("Not authorized");
        }
    }

    @SuppressWarnings("serial")
    public void control_panel() throws Exception {
        if ($.session.data("loggedin").equals("true")) {
            $.loadView("admin/control_panel", new PageDict() {

                {
                    put("post_data", "control panel");
                }
            });
        } else {
            $.response.sendRedirect("/admin");
        }
    }

    public void show_articles() throws Exception {
        // Only for logged users
        // if ($.session.data("loggedin").equals("true")) {
        // Our page data
        PageDict data = new PageDict();
        // Load the models we'll need
        Article article = $.loadModel("Article");
        Category cat = $.loadModel("Category");
        // Load the renderer we'll need
        BlogRenderer renderer = $.loadRenderer("BlogRenderer");
        // Lookup segment 2 (the category name)
        String category = $.input.segment(2);
        // Lookup uri segment 3 (the current page/offset in
        // the results)
        String page = $.input.segment(3);
        // Lets see if the current page is correct
        int offset = 0;
        if (page != null) offset = Integer.parseInt(page);
        // If we have too many results then we need to
        // paginate them.
        Pagination p = new Pagination();
        p.baseUrl = "/admin/show_articles/" + category;
        p.perPage = 15;
        p.totalRows = article.countArticlesByCat(category);
        // Now we are ready to get some data (limit to 10
        // articles with offset page number * 10)
        ArrayList<LinkedHashMap<String, String>> posts = article.getArticlesByCat(category, p.perPage, p.perPage
                * offset);
        // Render db results to html
        String output = "";
        int i = (p.perPage * offset) + 1;
        for (LinkedHashMap<String, String> lhm : posts) {
            output += "<b>" + i + "</b>" + renderer.renderArticleTitles(lhm);
            i++;
        }
        data.put("data", output);
        // Build our menu of categories
        data.put("menu", renderer.buildMenu("admin", "show_articles", cat.getCategories()));
        data.put("page_links", p.createLinks(offset));
        data.put("styles", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/admin_styles.css\" >\r\n");
        $.loadView("admin/control_panel", data);
        // } else {
        // $.response.sendRedirect("/admin");
        // }
    }

    public void edit_article() throws Exception {
        // Only for logged users
        if ($.session.data("loggedin").equals("true")) {
            // Our page data
            PageDict data = new PageDict();
            // Lookup segment 2 (the article id)
            String article_id = $.input.segment(2);
            article.load(article_id);
            $.loadView("admin/control_panel", data);
        } else {
            $.response.sendRedirect("/admin");
        }
    }
}
