/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.controllers;


import gr.dsigned.jmvc.forms.NewForms;
import gr.dsigned.jmvc.forms.fields.CharField;
import gr.dsigned.jmvc.forms.fields.DropdownMenu;
import gr.dsigned.jmvc.forms.fields.DropdownOption;
import gr.dsigned.jmvc.forms.fields.HiddenField;
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
        String state = $.input.segment(2);
        if(state!=null && state.equalsIgnoreCase("notauthorized")){
            data.put("post_data", "Invalid Username or Password");
        }else{
            data.put("post_data", "");
        }
        $.loadView("admin/index", data);
                       
    }
    
    public void login() throws Exception {
        String username = $.input.post("id");
        String pass = $.input.post("pass");
        User user = $.loadModel("User");
        ArrayList<Bean> al = user.auth(username, pass) ;
        if (al != null) {
            $.session.set("userId", al.get(0).get("id"));
            $.session.set("loggedin", "true");
            $.response.sendRedirect("show_articles/news");
        } else {
            $.response.sendRedirect("/articles/index/notauthorized");
        }
    }
    
     public void show_articles() throws Exception {
        // Only for logged users
        if ($.session.data("loggedin").equals("true")) {
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
            output += renderer.renderArticleTitlesWithDelete(lhm,i,category);
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
        $.request.getSession().invalidate();
        $.response.sendRedirect("/articles");
     }
    }
   public void show_form() throws Exception {
       if ($.session.data("loggedin").equals("true")) {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd ;
        
        Category cat = $.loadModel("Category");
        ArrayList<Bean> beans = cat.getCategories() ;
        f.setFields( 
                dd = new DropdownMenu("Article Category","category",$.input.post("category"),o(REQUIRED,"true")),
                new CharField("Title", "title",$.input.post("title")),
                new CharField("Real Title", "real_title",$.input.post("real_title")),
                new CharField("Sub Title", "sub_title",$.input.post("sub_title")),
                new CharField("Lead In", "lead_in",$.input.post("lead_in")),
                new TextareaField("Content", "content", "5", "20",$.input.post("content"),o(REQUIRED,"true"),o(MAX_LENGTH,"20000"), o(MIN_LENGTH,"100") ),
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
            art.insertArticle($.session.data("userId"), $.input.post("category"), $.input.post("title"), $.input.post("real_title"), $.input.post("sub_title"), $.input.post("lead_in"), $.input.post("content"));
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
        } else {
        $.request.getSession().invalidate();
        $.response.sendRedirect("/articles");
     }
    }
   
   public void edit_form() throws Exception {
       if ($.session.data("loggedin").equals("true")) {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd ;
        
        ArrayList<Bean> articleAL = null ;
        Bean bArt = null ;
        
        String id = $.input.segment(2);
        if(id == null || id.length() == 0){
            String test = $.input.post("category");
            id = $.input.post("id") ;
            bArt = new Bean() ;
            articleAL = new ArrayList<Bean>() ;
            bArt.put("title", $.input.post("title"));
            bArt.put("real_title", $.input.post("real_title"));
            bArt.put("sub_title", $.input.post("sub_title"));
            bArt.put("lead_in", $.input.post("lead_in"));
            bArt.put("content", $.input.post("content"));
            bArt.put("category_id", $.input.post("category"));
            articleAL.add(bArt);
        }else{
            articleAL = article.getArticleById(id) ;
        }
                    
        Category cat = $.loadModel("Category");
        ArrayList<Bean> beans = cat.getCategories() ;
        f.setFields( 
                new HiddenField("id", id, o(REQUIRED,"true")),
                dd = new DropdownMenu("Category", "category",articleAL.get(0).get("category_id"),o(REQUIRED,"true")),
                new CharField("Title", "title",articleAL.get(0).get("title")),
                new CharField("Real Title", "real_title",articleAL.get(0).get("real_title")),
                new CharField("Sub Title", "sub_title",articleAL.get(0).get("sub_title")),
                new CharField("Lead In", "lead_in",articleAL.get(0).get("lead_in")),
                new TextareaField("Content", "content", "5", "20",articleAL.get(0).get("content"),o(REQUIRED,"true"),o(MAX_LENGTH,"20000"), o(MIN_LENGTH,"100") ),
                new SubmitButtonField("submit_button", "")
        );
        for(Bean b : beans){
            if(b.get("id").equalsIgnoreCase(articleAL.get(0).get("category_id"))){
                dd.addOption(new DropdownOption(b.get("display_name"), b.get("id") ,"selected")) ;
            }else{
                dd.addOption(new DropdownOption(b.get("display_name"), b.get("id") ,"")) ;
            }
        }    
                
        if($.input.getRequest().getMethod().equalsIgnoreCase("post") && f.isValid()){
            article.editArticle(id,$.input.post("category"), $.input.post("title"), $.input.post("real_title"), $.input.post("sub_title"), $.input.post("lead_in"), $.input.post("content"));
            ArrayList<Bean> cats = cat.getCategoryById($.input.post("category")) ;
            $.response.sendRedirect("/articles/show_articles/"+cats.get(0).get("name"));
        } else {
            String form = "<form  action='/articles/edit_form' method='post'>";
            form += f.build();
            form += "</form>";
            data.put("title", "Edit Article");
            data.put("form", form);
           $.loadView("testing_forms", data);
        }
      } else {
        $.request.getSession().invalidate();
        $.response.sendRedirect("/articles");
     }
        
    }
    public void delete_article() throws Exception {
        if ($.session.data("loggedin").equals("true")) {
            String cat = $.input.segment(2); 
            String id = $.input.segment(3); 
            article.deleteArticle(id) ;
            
            $.response.sendRedirect("/articles/show_articles/"+cat);
            
        } else {
            $.request.getSession().invalidate();
            $.response.sendRedirect("/articles");
        }     
    }
 
    
    

}
