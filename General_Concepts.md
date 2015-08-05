# Introduction #

The concepts behind j2mvc.

![http://dsigned.gr/MVC.jpg](http://dsigned.gr/MVC.jpg)

# Details #


## URLs ##
Jmvc URLs are an important part of the framework. Instead of having ugly query string in your URLs you have instead plain URLs that are mapped by the framework to parameter that you use in your application.
Example:
`http://jmvc.org/blog/post/value1/value2  `
OR
` http://jmvc.org/blog/post/parameter1/value1/parameter2/value2 `

As you can see each url is mapped to a Controller and a Method. These correspond directly to your Controller class. The above URL for example would be answered by a Java class in the controller package named Blog. The code that would respond to this request would have to live inside a method of the Blog class named post.
With jmvc your URLs are inherently optimized for search engines and

## Controllers ##
Controllers contain the logic behind your application. They accept a request, process data, load a template and return HTML to the browser.  For example when the web application receives a request for `http://domain/blog/post`  it will automatically create an instance of the controller Blog and run the method post() of this controller.

Example:
```
package gr.dsigned.jmvc.controllers;
import gr.dsigned.jmvc.framework.Controller;

public class Home extends Controller {    
    public Home() throws Exception {
    }

    public void index() throws Exception {
        $.loadView("blog_frontpage");
    }
}
```
The above controller contains only the method index(). This method does not do anything but display the template named blog\_frontpage.
Index is a special case because it is called whenever the framework does not find a method name in the URL. I.e.: The URL `http://domain/blog/`  will find the controller Blog and execute the method index().
By default the framework searches for controllers in the package `gr.dsigned.jmvc.controllers` so make sure you create your controllers in that package. Also make sure that methods that you want exposed to visitors are public. If you need helper methods in your controller declare them private show they wont get called by URL.


## Views ##
A view is an HTML file that contains a whole page or a fragment of a page i.e. a header. Views are loaded by controllers and cannot be viewed directly.

Example of a View:
```
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title><% title %></title>
  </head>
  <body>
  this is a view.
  <br/>
          <% content %>
  </body>
</html>
```
Every template may contain placeholders for data. The above template contains 2 placeholders name title and content respectively. To load a template you need to add the line `$.loadView("template_name");` at the very end of your controller method. To pass data to the template create a `LinkedHashMap<String, String>` and put each tag name as the key and the data as the value. In the above example you would need to do something like the following:
```
LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
data.put("title", "This is the page title");
$.loadView("blog_frontpage", data);
```

A template may also contain callbacks to helper methods to make developer life easier.

The following tag creates a link to the controller home and the method blog with a title="title" attribute and a style:

```
<% anchor("/home/blog/","title","style='color:#000;'")%>
```

See also PageDict for an object with predefined values for tags you will almost alway need when loading templates.

## Models ##
Models are classes that represent data in your system. They provide mechanisms to retrieve, store and search your database and they also provide methods to read these data.

A typical model:
```
package gr.dsigned.jmvc.models;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import gr.dsigned.jmvc.db.Model;

public class Category extends Model {
    public Category() throws Exception {
        this.tableName = "categories";
    }
    public ArrayList<LinkedHashMap<String, String>> getCategories() throws SQLException {
        db.from(this.tableName);
        return db.get();
    }
}
```

Models by default reside in the gr.dsigned.jmvc.models package and extend Model.  There are 3 ways to access data from the database:

1. SQL

Using the db object of the model we can pass SQL statements:
```
public ArrayList<LinkedHashMap<String, String>> getLatestPosts() throws SQLException {
      return  db.executeQuery("SELECT * FROM tablename");
}
```
2. Using Active record (for single instances of data)

When accessing a single row we can load the data of the row in the model:
```
 public ArrayList<LinkedHashMap<String, String>> getLatestPosts() throws SQLException {
       return this.db.executeQuery("SELECT * FROM tablename");
}
```
3. Querysets

The most advanced feature is querysets. Querysets are a way to access data with simple java statements.

Example:
```
public ArrayList<LinkedHashMap<String, String>> getLatestPosts(int numberToFetch) throws SQLException {
        db.from("frontend_article");
        db.join("frontend_category", "frontend_category.id = frontend_article.category_id", "left");
        db.where("frontend_category.name = 'basket'");
        db.orderBy("sub_title", "DESC");
        db.limit(numberToFetch);
        return db.get();
}
```

## Renderers ##
Renders are classes that contain methods that receive data and render them in HTML. You can use renderers to create tables from data, lists etc

```
package gr.dsigned.jmvc.renderers;
import gr.dsigned.jmvc.framework.Renderer;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BlogRenderer extends Renderer {
    private boolean showAuthor = true;
    private boolean withReadMore = true; 

    public String renderMenu(LinkedHashMap<String, String> menuElements) throws Exception {
        String out = "<ul>";
        for (String title : menuElements.keySet()) {
            out += String.format("<li>%1$s</li>", anchor(menuElements.get(title), title, "class='menu_link'"));
        }
        return out;
}
```




## Libraries ##
j2mvc contains several libraries to help with repetitive tasks.

  * Input library contains methods to deal with URLs, POST data etc.

  * Pagination library contains methods to build pagination from data.

  * Session contains methods to deal with session management.

To load a library all you need to do is call : `Pagination p = $.loadLibrary("Pagination");`
## Creating Your Own Libraries ##
## Auto-loading Resources ##
## Error Handling ##
## Caching ##
## Security ##