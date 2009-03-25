/*
 *  QuerySet.java
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
package gr.dsigned.jcms.controllers;

import gr.dsigned.jcms.models.Article;
import gr.dsigned.jcms.models.Category;
import javax.jcr.Repository;



import org.apache.jackrabbit.core.TransientRepository;


import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.libraries.PageData;


import gr.dsigned.jmvc.types.Hmap;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.servlet.http.HttpServletResponse;
import static gr.dsigned.jmvc.framework.Renderer.*;
import static gr.dsigned.jmvc.types.operators.o;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Home extends Controller {

    public static Repository repo;
    public Session s;
    String debug = "";
    long now;
    long then;

    public Home() throws Exception {
//        now = System.nanoTime();
//        repo = (TransientRepository)$.context.getAttribute("repo");
//        then = System.nanoTime();
//        debug += " Repo creation took " + ((double) (then - now) / 1000000) + " secs </br> ";
    }

    public void index() throws Exception {
        Category c = new Category();
        PageData data = new PageData();
        ArrayList<Hmap> cats = c.getCategories();
        StringBuilder sb = new StringBuilder();
        for (Hmap cat : cats) {
            sb.append(div(a("import " + cat.get("name"), o("href", "/Home/install/" + cat.get("name")))));
        }
        sb.append("<hr/><br/><br/>");
        repo = (TransientRepository) $.context.getAttribute("repo");
        Session session = repo.login(new SimpleCredentials("johndoe", "password".toCharArray()));
        try {
            Node catRoot = getCategoryNode(session);
            NodeIterator nodes = catRoot.getNodes();
            while (nodes.hasNext()) {
                Node n = nodes.nextNode();
                sb.append(div(a("show " + n.getName(), o("href", "/Home/show_cat/" + n.getName()))));
            }
        } finally {
            session.logout();
        }
        data.put("content", sb.toString());
        $.loadView("index", data);
    }

    public void install() throws Exception {
        $.response.getWriter().print(getFakeHTML());
        repo = (TransientRepository) $.context.getAttribute("repo");
        s = repo.login(new SimpleCredentials("johndoe", "password".toCharArray()));
        try {
            int offset = 0;
            String catName = $.input.segment(2);
            Node catNode = addCat(catName, s);
            Article articleModel = new Article();
            ArrayList<Hmap> articles = articleModel.getArticlesByCat(catName, 50, offset);
            int i = 1;
            while (articles.size() > 0) {
                for (Hmap article : articles) {
                    if (!getContentNode(s).hasNode(article.get("id"))) {
                        Node articleNode = getContentNode(s).addNode(article.get("id"));
                        articleNode.setProperty("title", article.get("title"));
                        articleNode.setProperty("real_title", article.get("real_title"));
                        articleNode.setProperty("sub_title", article.get("sub_title"));
                        articleNode.setProperty("lead_in", article.get("lead_in"));
                        articleNode.setProperty("content", article.get("content"));
                        articleNode.setProperty("url", article.get("url"));
                        articleNode.setProperty("published", article.get("published"));
                        articleNode.setProperty("updated", article.get("updated"));
                        articleNode.setProperty("category_name", catNode.getName());
                        articleNode.setProperty("categoryUUID", catNode.getUUID());
                        articleNode.setProperty("category_path", catNode.getPath());
                        articleNode.addMixin("mix:referenceable");
                        s.save();
                        i++;
                        Jmvc.logInfo(i + ". added article: " + article.get("title") + " in " + catNode.getPath());
                        $.response.getWriter().print("<br/>" + i + ". added article: " + article.get("title") + " in " + catNode.getPath());
                    } else {
                        Jmvc.logInfo(i + ". Did not add article: " + article.get("title") + " in " + catNode.getPath());
                        $.response.getWriter().print("<br/>" + i + ". Did not add article: " + article.get("title") + " in " + catNode.getPath());
                    }
                }
                $.response.getWriter().flush();
                offset = offset + 50;
                articles = articleModel.getArticlesByCat(catName, 50, offset);
            }
        } finally {
            s.logout();
        }
        $.response.sendRedirect("/Home/index/done");
    }

    private Node getContentNode(Session s) throws Exception {
        Node root = s.getRootNode();
        if (root.hasNode("content")) {
            root = root.getNode("content");
        } else {
            root = root.addNode("content");
        }
        if (root.hasNode("articles")) {
            root = root.getNode("articles");
        } else {
            root = root.addNode("articles");
        }
        return root;
    }

    private Node getCategoryNode(Session s) throws Exception {
        Node root = s.getRootNode();
        if (root.hasNode("categories")) {
            root = root.getNode("categories");
        } else {
            root = root.addNode("categories");
        }
        return root;
    }

    private Node addCat(String catName, Session s) throws Exception {
        Category c = new Category();
        ArrayList<Hmap> cats = new ArrayList<Hmap>();
        Hmap currentCat = c.getCategoryByName(catName);
        cats.add(currentCat);
        while (!currentCat.get("parent_id").isEmpty()) {
            currentCat = c.getCategoryById(currentCat.get("parent_id"));
            cats.add(currentCat);
        }
        Collections.reverse(cats);
        Node root = s.getRootNode();
        Node catNode = root;
        if (catNode.hasNode("categories")) {
            catNode = catNode.getNode("categories");
        } else {
            catNode = catNode.addNode("categories");
        }
        for (Hmap cat : cats) {
            if (catNode.hasNode(cat.get("name"))) {
                catNode = catNode.getNode(cat.get("name"));
            } else {
                catNode = catNode.addNode(cat.get("name"));
            }
            catNode.addMixin("mix:referenceable");
        }
        return catNode;
    }

    public void test() throws Exception {
        repo = (TransientRepository) $.context.getAttribute("repo");
        now = System.nanoTime();
        s = repo.login(new SimpleCredentials("johndoe", "password".toCharArray()));
        then = System.nanoTime();
        debug += " Login took " + ((double) (then - now) / 1000000) + " secs </br> ";
        String str = "";
        try {
            now = System.nanoTime();
            Node root = s.getRootNode();
            // Store content
            Node hello = root.addNode("hello");
            Node world = hello.addNode("world");
            world.setProperty("message", "Hello, World!");
            s.save();
            then = System.nanoTime();
            debug += " Creating content took " + ((double) (then - now) / 1000000) + " secs </br> ";
            // Retrieve content
            now = System.nanoTime();
            Node node = root.getNode("hello/world");
            System.out.println(node.getPath());
            System.out.println(node.getProperty("message").getString());
            then = System.nanoTime();
            debug += " Query took " + ((double) (then - now) / 1000000) + " secs </br> ";

            // Remove content
            now = System.nanoTime();
            root.getNode("hello").remove();
            then = System.nanoTime();
            debug += " Delete took " + ((double) (then - now) / 1000000) + " secs </br> ";

            now = System.nanoTime();
            s.save();
            then = System.nanoTime();
            debug += " Save took " + ((double) (then - now) / 1000000) + " secs </br> ";
            //str = dump(root);
            now = 0;
            then = 0;
        } finally {
            s.logout();
        }
        PageData data = new PageData();
        data.put("content", div(h1("jmvc web framework") + div(debug)));
        $.loadView("index", data);
    }

    public void show() throws Exception {
        repo = (TransientRepository) $.context.getAttribute("repo");
        String catName = $.input.segment(2);
        String subCatName = $.input.segment(3);
        catName = subCatName.isEmpty() ? catName : catName + "/" + subCatName;
        now = System.nanoTime();
        s = repo.login();
        then = System.nanoTime();
        debug += " Login took " + ((double) (then - now) / 1000000) + " secs </br> ";
        try {
            now = System.nanoTime();
            Node root = s.getRootNode();
            root = root.getNode(catName);
            dump(root, $.response);
            then = System.nanoTime();
            debug += " Dump took " + ((double) (then - now) / 1000000) + " secs </br> ";
            now = 0;
            then = 0;
        } finally {
            s.logout();
        }
        PageData data = new PageData();
        data.put("content", div(h1("jmvc web framework") + div(debug)));
        $.loadView("index", data);
    }

    public void showAll() throws Exception {
        repo = (TransientRepository) $.context.getAttribute("repo");
        now = System.nanoTime();
        s = repo.login();
        then = System.nanoTime();
        debug += " Login took " + ((double) (then - now) / 1000000) + " secs </br> ";
        try {
            now = System.nanoTime();
            Node root = s.getRootNode();
            dump(root, $.response);
            then = System.nanoTime();
            debug += " Dump took " + ((double) (then - now) / 1000000) + " secs </br> ";
            now = 0;
            then = 0;
        } finally {
            s.logout();
        }
        PageData data = new PageData();
        data.put("content", div(h1("jmvc web framework") + div(debug)));
        $.loadView("index", data);
    }

    /** Recursively outputs the contents of the given node. */
    private static String dump(Node node) throws RepositoryException {
        StringBuilder sb = new StringBuilder();
        // First output the node path
        sb.append(div(node.getPath()));
        // Skip the virtual (and large!) jcr:system subtree
        if (node.getName().equals("jcr:system")) {
            return sb.toString();
        }

        // Then output the properties
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                // A multi-valued property, print all values
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    //sb.append(div(property.getPath() + " = " + values[i].getString()));
                    sb.append(div(property.getPath()));
                }
            } else {
                // A single-valued property
                //sb.append(div(property.getPath() + " = " + property.getString()));
                sb.append(div(property.getPath()));
            }
        }
        // Finally output all the child nodes recursively
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            sb.append(div(dump(nodes.nextNode())));
        }
        return sb.toString();
    }

    /** Recursively outputs the contents of the given node. */
    private static void dump(Node node, HttpServletResponse r) throws Exception {
        r.setCharacterEncoding("UTF-8");
        Writer w = r.getWriter();
        // First output the node path
        w.write(div(node.getPath(), o("style", "font:bold 12px Arial;color:#000;")));
        // Skip the virtual (and large!) jcr:system subtree
        if (node.getName().equals("jcr:system")) {
            return;
        }
        // Then output the properties
        PropertyIterator properties = node.getProperties();
        while (properties.hasNext()) {
            Property property = properties.nextProperty();
            if (property.getDefinition().isMultiple()) {
                // A multi-valued property, print all values
                Value[] values = property.getValues();
                for (int i = 0; i < values.length; i++) {
                    //w.write(div(property.getPath() + " = " + values[i].getString(), o("style", "font:bold 10px Arial;color:#ccc;")));
                    w.write(div(property.getPath(), o("style", "font:bold 10px Arial;color:#ccc;")));
                }
            } else {
                // A single-valued property
                //w.write(div(property.getPath() + " = " + property.getString(), o("style", "font:bold 10px Arial;color:#ccc;")));
                w.write(div(property.getPath(), o("style", "font:bold 10px Arial;color:#ccc;")));
            }
        }
        w.flush();
        // Finally output all the child nodes recursively
        NodeIterator nodes = node.getNodes();
        while (nodes.hasNext()) {
            dump(nodes.nextNode(), r);
        }
    }

    public void import_cat() throws Exception {
        repo = (TransientRepository) $.context.getAttribute("repo");
        Category c = new Category();
        Article a = new Article();
        String catName = $.input.segment(2);
        ArrayList<Hmap> cats = new ArrayList<Hmap>();
        Hmap currentCat = c.getCategoryByName(catName);
        cats.add(currentCat);
        while (!currentCat.get("parent_id").isEmpty()) {
            currentCat = c.getCategoryById(currentCat.get("parent_id"));
            cats.add(currentCat);
        }
        Collections.reverse(cats);
        $.response.setCharacterEncoding("UTF-8");
        try {
            s = repo.login(new SimpleCredentials("johndoe", "password".toCharArray()));
            Node root = s.getRootNode();
            Node catNode = root.getNode("categories");
            Node contentNode = root.getNode("content");
            for (Hmap cat : cats) {
                if (catNode.hasNode(cat.get("name"))) {
                    catNode = catNode.getNode(cat.get("name"));
                } else {
                    catNode = catNode.addNode(cat.get("name"));
                }
                $.response.getWriter().print(div("adding cat: " + cat.get("name")));
                $.response.flushBuffer();
                ArrayList<Hmap> articles = a.getArticlesByCat(cat.get("name"));
                for (Hmap article : articles) {
                    Node articleNode = catNode.addNode(article.get("id"));
                    articleNode.setProperty("title", article.get("title"));
                    articleNode.setProperty("real_title", article.get("real_title"));
                    articleNode.setProperty("sub_title", article.get("sub_title"));
                    articleNode.setProperty("lead_in", article.get("lead_in"));
                    articleNode.setProperty("content", article.get("content"));
                    articleNode.setProperty("url", article.get("url"));
                    articleNode.setProperty("published", article.get("published"));
                    articleNode.setProperty("updated", article.get("updated"));
                    $.response.getWriter().print(div("added article: " + article.get("title")));
                    s.save();
                    $.response.flushBuffer();
                }
            }
        } finally {
            s.logout();
        }
        showAll();
    }

    private String getFakeHTML() {
        return "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html xmlns='http://www.w3.org/1999/xhtml'><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' /><title>LIVE24.gr: Best 92.6</title></head><body>";
    }
}
