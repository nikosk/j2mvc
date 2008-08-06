/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.controllers;


import gr.dsigned.jmvc.forms.NewForms;
import gr.dsigned.jmvc.forms.fields.CharField;
import gr.dsigned.jmvc.forms.fields.DropdownMenu;
import gr.dsigned.jmvc.forms.fields.DropdownOption;
import gr.dsigned.jmvc.forms.fields.SubmitButtonField;
import gr.dsigned.jmvc.forms.fields.TextareaField;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.libraries.Pagination;
import gr.dsigned.jmvc.models.Category;
import gr.dsigned.jmvc.models.User;
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.renderers.BlogRenderer;
import gr.dsigned.jmvc.renderers.ListRenderer;
import gr.dsigned.jmvc.types.Bean;
import java.util.ArrayList;
import static gr.dsigned.jmvc.forms.fields.Field.Rule.*;
import static gr.dsigned.jmvc.types.operators.*;
/**
 *
 * @author USER
 */
public class Articles  extends Controller {
    
    Article article = $.loadModel("Article");
        
    public Articles() throws Exception {
    }
    
    public void index() throws Exception {
        PageData data = new PageData();
        data.put("post_data", "");
        $.loadView("admin/index", data);
    }
    
    public void login() throws Exception {
        String username = $.input.post("id");
        String pass = $.input.post("pass");
        User user = $.loadModel("User");
        if (user.auth(username, pass)) {
            $.session.set("loggedin", "true");
            $.response.sendRedirect("show_articles/news");
        } else {
            throw new Exception("Not authorized");
        }
    }
    
     public void show_articles() throws Exception {
        // Only for logged users
        if ($.session.data("loggedin").equals("true")) {
        // Our page data
        PageData data = new PageData();
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
        if (page != null) {
            offset = Integer.parseInt(page);
        // If we have too many results then we need to
        // paginate them.
        }
        Pagination p = new Pagination();
        p.setBaseUrl( "/articles/show_articles/" + category);
        p.setTotalRows(article.countArticlesByCat(category));
        // Now we are ready to get some data (limit to 10
        // articles with offset page number * 10)
        ArrayList<Bean> posts = article.getArticlesByCat(category, p.getPerPage(), p.getPerPage() * offset);
        p.setNoItemsPerQuery(posts.size());
        // Render db results to html
        String output = "";
        int i = (p.getPerPage() * offset) + 1;
        for (Bean lhm : posts) {
            output += "<b>" + i + "</b>" + renderer.renderArticleTitles(lhm);
            i++;
        }
        data.put("category", "-"+category);
        data.put("link_create", "<a href='/articles/show_form'>Create Article</a><br/>");
        data.put("data", output);
        // Build our menu of categories
        data.put("menu", renderer.buildMenu("articles", "show_articles", cat.getCategories()));
        data.put("item_links", p.createPagingLinks(offset,Pagination.PagingType.ITEM));
        data.put("search_links", p.createPagingLinks(offset,Pagination.PagingType.SEARCH));
        data.put("styles", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/admin_styles.css\" >\r\n");
        $.loadView("admin/control_panel_article", data);
     } else {
     $.response.sendRedirect("/articles");
     }
    }
   public void show_form() throws Exception {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd ;
        
        Category cat = $.loadModel("Category");
        ArrayList<Bean> beans = cat.getCategories() ;
        f.setFields( 
                dd = new DropdownMenu("category",$.input.post("category"),o(REQUIRED,"true")),
                new CharField("title",$.input.post("title")),
                new CharField("real_title",$.input.post("real_title")),
                new CharField("sub_title",$.input.post("sub_title")),
                new CharField("lead_in",$.input.post("lead_in")),
                new TextareaField("content", "5", "20",$.input.post("content"),o(REQUIRED,"true"),o(MAX_LENGTH,"20000"), o(MIN_LENGTH,"100") ),
                new SubmitButtonField("submit_button", "")
        );
        int i = 0 ;
        for(Bean b : beans){
            if(i == 0){
                dd.addOption(new DropdownOption(b.get("display_name"), b.get("id") ,"selected")) ;
            }else{
                dd.addOption(new DropdownOption(b.get("display_name"), b.get("id") ,"")) ;
            }
            i++;
        }    
                
        if($.input.getRequest().getMethod().equalsIgnoreCase("post") && f.isValid()){
            Article art = $.loadModel("Article");
            art.insertArticle($.input.post("category"), $.input.post("title"), $.input.post("real_title"), $.input.post("sub_title"), $.input.post("lead_in"), $.input.post("content"));
            ArrayList<Bean> cats = cat.getCategoryById($.input.post("category")) ;
            $.response.sendRedirect("/articles/show_articles/"+cats.get(0).get("name"));
        } else {
            String form = "<form  action='/articles/show_form' method='post'>";
            form += f.build();
            form += "</form>";
            data.put("title", "Create Article");
            data.put("form", form);
           $.loadView("testing_forms", data);
        }
    }
   
   public void edit_form() throws Exception {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd ;
        
        String id = $.input.segment(2);
        Article art = $.loadModel("Article");
        ArrayList<Bean> articleAL = art.getArticleById(id) ;
        Category cat = $.loadModel("Category");
        ArrayList<Bean> beans = cat.getCategories() ;
        f.setFields( 
                dd = new DropdownMenu("category",articleAL.get(0).get("category_id"),o(REQUIRED,"true")),
                new CharField("title",articleAL.get(0).get("title")),
                new CharField("real_title",articleAL.get(0).get("real_title")),
                new CharField("sub_title",articleAL.get(0).get("sub_title")),
                new CharField("lead_in",articleAL.get(0).get("lead_in")),
                new TextareaField("content", "5", "20",articleAL.get(0).get("content"),o(REQUIRED,"true"),o(MAX_LENGTH,"20000"), o(MIN_LENGTH,"100") ),
                new SubmitButtonField("submit_button", "")
        );
        for(Bean b : beans){
            if(b.get("id").equalsIgnoreCase(id)){
                dd.addOption(new DropdownOption(b.get("display_name"), b.get("id") ,"selected")) ;
            }else{
                dd.addOption(new DropdownOption(b.get("display_name"), b.get("id") ,"")) ;
            }
        }    
                
        if($.input.getRequest().getMethod().equalsIgnoreCase("post") && f.isValid()){
            art.insertArticle($.input.post("category"), $.input.post("title"), $.input.post("real_title"), $.input.post("sub_title"), $.input.post("lead_in"), $.input.post("content"));
            ArrayList<Bean> cats = cat.getCategoryById($.input.post("category")) ;
            $.response.sendRedirect("/articles/edit_article/"+id);
        } else {
            String form = "<form  action='/articles/edit_form' method='post'>";
            form += f.build();
            form += "</form>";
            data.put("title", "Edit Article");
            data.put("form", form);
           $.loadView("testing_forms", data);
        }
        
    }
    public void add_article() throws Exception {
       /* PageData data = new PageData();
         site = $.loadModel("Site"); // Load model
        if ($.input.post("label").isEmpty()) {
            data.put("label", "");
            data.put("action", "/sites/add_site/");
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form", data);
        } else {
            site.insertSite($.input.post("label"));
            $.response.sendRedirect($.input.post("redirect_to"));
        }*/
    }
 
    
    

}
