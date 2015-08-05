j2mvc tutorial

# Introduction #

This document will guide you through writing a sample application with j2mvc. The application is a simple todo list.

  * You will need:
  * A working copy of Mysql.
  * A database to work on.
  * A copy of j2mvc.

## Step 1: Create todo list table ##

Assuming you have installed Mysql and created a database you will also need to create the tables that hold your data.
First we will create a table to hold your todo lists :

```
CREATE TABLE `todo_lists` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `label` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) DEFAULT CHARSET=utf8 ;
```


## Step 2: Create your model ##

Models help you group your data access code in one coherent piece of code specifically created to handle conceptually related tasks. This piece of code is called a model and serves as an abstraction of the underlying database. Lets create our first model:

```
package gr.dsigned.jmvc.models;

public class TodoList extends Model {
    
    public TodoList() throws Exception {
        this.tableName = "todo_lists";
    }

}
```

As you can see from the above code each of your app's model must reside inside the gr.dsigned.jmvc.models package. Your model must extend gr.dsigned.jmvc.db.Model and the constructor throws an exception.
Be careful to set the name of the table that this model corresponds to because some methods of depend on it.

For the first step we will need to create a method that returns a list of all the todo lists currently stored in the database:

```
public ArrayList<Hmap> getLists() throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("todo_lists");             
        return db.getList(qs);
}
```

First we need to explain what Hmap and QuerySet are. Hmap is a shorthand way of writing LinkedHashMap<String, String>. Hmap extends LinkedHashMap and therefore contains all the methods provided by it.
QuerySet is a class that helps you write SQL statements, we will see how it works later on.

The above method will return an ArrayList of hashmaps containing the values for each column in the result of the query. Each entry in the ArrayList corresponds to one row from the results and each entry of Bean contains the name/value pair of each column in the result.

Remember you must make all your methods throw an Exception so that the framework reports the error.

## Step 3: Create you first controller ##

Controllers contain the logic in your application. Each controller has methods to handle requests and return the response. As with the models, the controllers must reside in the gr.dsigned.jmvc.controllers package :

```
package gr.dsigned.jmvc.controllers;

public class TodoLists extends Controller {

    public TodoLists() throws Exception {
    }

}
```

Now that we have our controller we need to create the default method. When the controller is called without an action this is the method that gets called:

```
public void index() throws Exception {
        PageData data = new PageData();
        $.loadView("list", data);
}
```

We just created an empty controller with a default method that only load a template.
When someone visits the home page of our app we want to show him our todo\_lists. That's where our model from step 2 comes in to play:

```
public void index() throws Exception {
        PageData data = new PageData();
        TodoList todoList = new TodoList(); // Load model
        ArrayList<Bean> todoList = todoList. getLists();  // Get all lists
        $.loadView("list", data);
}
```

By adding just 2 lines we now have an ArrayList of our todo lists. All we need to do now is put the results in HTML and display them. This is where Renderers come in to play. A common task in web applications is the retrieval of data from the database which we render in HTML and send them to the browser. Instead of polluting our controller logic with ugly loops going through the data and appending HTML tags we use a Renderer to  do this job elsewhere. This way our controller is clean and readable and we also have the ability to reuse the rendering code elsewhere.


## Step 4: Create a Renderer for the todo lists ##

Create a class in the package gr.dsigned.jmvc.renderers with the following structure:

```
package gr.dsigned.jmvc.renderers;

public class ListRenderer extends Renderer {

    public String renderLists(ArrayList<Bean> lists) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for(Bean list : lists){
                 sb.append("<li>");
                 sb.append(list.get("label"));
                 sb.append("</li>");                 
        }
        sb.append("</ul>");
        return sb.toString();
    }
}
```

The code above loops though the array and builds an unordered list containing the labels of our todo lists. We also need a link to add a list somewhere so add this line just before the opening <ul> tag.<br>
<pre><code> sb.append(anchor("/sites/add_list/", "add list", "id='add_list'"));<br>
</code></pre>

The anchor is one of the helper methods contained in the Renderer super class and helps you build links to controllers.<br>
<br>
<h2>Step 5: Use the renderer to display the lists</h2>

Now that the renderer is ready we can use it in our controller.<br>
<br>
<pre><code>public void index() throws Exception {<br>
        PageData data = new PageData();<br>
        TodoList todoList = $.loadModel("TodoList"); // Load model<br>
        ArrayList&lt;Bean&gt; todoList = todoList. getLists();  // Get all lists<br>
        ListRenderer lr = new ListRenderer();<br>
        data.put("list", lr. renderLists(todoList));<br>
        $.loadView("list", data);<br>
}<br>
</code></pre>

We now have an instance of the ListRenderer and we pass it the ArrayList which returns the rendered HTML. The final step is to add the html to the PageData object and load the template.<br>
<br>
<h2>Step 6: Create the view</h2>

He have the data, the logic and now we need to create the view. Views in j2mvc are plain HTML files that contain placeholders that will be replaced with the actual data once the view is loaded.<br>
<br>
<pre><code>&lt;html&gt;<br>
    &lt;head&gt;<br>
        &lt;meta http-equiv="Content-Type" content="text/html; charset=utf-8" /&gt;        <br>
        &lt;title&gt;&lt;% title %&gt;&lt;/title&gt;<br>
    &lt;/head&gt;<br>
    &lt;body&gt;<br>
        &lt;div id="doc" class="yui-t7"&gt;<br>
            &lt;div id="hd"&gt;&lt;h1&gt;To do list&lt;/h1&gt;&lt;/div&gt;<br>
            &lt;div id="bd"&gt;<br>
                &lt;div class="yui-g"&gt;<br>
                    &lt;% list %&gt;<br>
                &lt;/div&gt;                <br>
            &lt;/div&gt;<br>
            &lt;div id="ft"&gt;Footer is here.&lt;/div&gt;<br>
        &lt;/div&gt;<br>
    &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

The view contains tags in the form <% variable name %> tha will be replaced by the data with the same name you pass in the PageData object in your controller. In our case the tag <% list %> will be replaced with the HTML we got from our renderer.<br>
<br>
<br>
<h2>Step 7: Add method to add a new list</h2>

Create a new method in your controller :<br>
<br>
<pre><code>public void add_site() throws Exception {<br>
        PageData data = new PageData();<br>
        Site site = new Site(); // Load model<br>
        ArrayList&lt;Hmap&gt; sites = site.getSites();<br>
        if ($.input.post("label").isEmpty()) {<br>
            data.put("label", "");<br>
            data.put("action", "/sites/add_list/");<br>
            data.put("redirect_to", "/" + $.input.segment(0));<br>
            $.loadView("list_form", data);<br>
        } else {<br>
            site.insertSite($.input.post("label"));<br>
            $.response.sendRedirect($.input.post("redirect_to"));<br>
        }<br>
    }<br>
</code></pre>

The above method when accessed normally ( GET ) will display a form to add a new list. When this form is filled and submitted the controller will get the data, insert a new row in the database and redirect to the default controller method.<br>
<br>
<h2>Step 8: Add the appropriate methods in the model</h2>
After we received the data back from the form we need to save them. This is a task that should be handled by our model. Let's add the method that gets the label of the todo list and save it in the database.<br>
<br>
<pre><code>public void insertSite(String label) throws Exception{<br>
        this.data.put("label", label);<br>
        this.store();<br>
}<br>
</code></pre>

<h2>Step 9: Create the form</h2>

Now we need a form for our controller in order to input data and submit it back.<br>
Notice that now the view contains more parameters than the previous.<br>
<br>
<pre><code>&lt;html&gt;<br>
&lt;head&gt;<br>
&lt;meta http-equiv="Content-Type" content="text/html; charset=utf-8" /&gt;<br>
    &lt;/head&gt;<br>
    &lt;body&gt;<br>
        &lt;div id="doc" class="yui-t7"&gt;<br>
            &lt;div id="hd"&gt;&lt;h1&gt;To do list&lt;/h1&gt;&lt;/div&gt;<br>
            &lt;div id="bd"&gt;<br>
                &lt;div class="yui-g"&gt;	 <br>
                    &lt;form action="&lt;%action%&gt;" method="post"&gt;<br>
                        &lt;input type="text" size="10" name="label" value="&lt;%label%&gt;"&gt;&lt;/input&gt;<br>
                        &lt;input type="hidden" value="&lt;%redirect_to%&gt;" name="redirect_to"&gt;&lt;/input&gt;<br>
                        &lt;button type="submit" value="Go" name="go"&gt;Go&lt;/button&gt;<br>
                    &lt;/form&gt;      <br>
                &lt;/div&gt;                <br>
            &lt;/div&gt;<br>
            &lt;div id="ft"&gt;Footer is here.&lt;/div&gt;<br>
        &lt;/div&gt;<br>
    &lt;/body&gt;<br>
&lt;/html&gt;<br>
</code></pre>

<h2>Step 10: Test your app so far</h2>

Run the project (mvn jetty:run) and test adding lists.<br>
Go to http:localhost:8080/ which only contains a link to add more for now. Click on that