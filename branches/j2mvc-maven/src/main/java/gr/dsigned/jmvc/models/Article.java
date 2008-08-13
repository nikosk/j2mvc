/*
 *  Article.java
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
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Bean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import java.sql.Timestamp;

/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.models 
 * Article.java
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.gr>
 */
public class Article extends Model {

    public Article() throws Exception {
        this.tableName = "articles";
    }

    public ArrayList<Bean> getLatestPosts(int numberToFetch) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.join("categories", "categories.id = articles.category_id", "left");
        qs.where("categories.name","basket", Operands.EQUAL.toString());
        qs.orderBy(OrderBy.DESC, "published");
        qs.limit(numberToFetch);
        return db.getList(qs);
    }
    
    public ArrayList<Bean> getPostsInInterval(Date from, Date to) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.orderBy(OrderBy.DESC, "published");
        return db.getList(qs);
    }

    public ArrayList<Bean> getArticlesByCat(String cat, int limit, int offset) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.join("categories", "Articles.category_id = Categories.id","inner");
        qs.where("Categories.name", cat, Operands.EQUAL.toString());
        qs.orderBy(OrderBy.DESC, "published");
        qs.limit(offset, limit);
        return db.getList(qs);
    }               

    public int countArticlesByCat(String cat) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.join("categories", "Articles.category_id = Categories.id", "left");
        qs.where("Categories.name", cat, Operands.EQUAL.toString());
        qs.orderBy(OrderBy.DESC, "published");
        return db.count(qs);
    }
    
    public String insertArticle(Bean bean) throws Exception {
        QuerySet qs = new QuerySet();
        qs.set("title", bean.get("title"));
        qs.set("real_title", bean.get("real_title"));
        qs.set("sub_title", bean.get("sub_title"));
        qs.set("lead_in", bean.get("lead_in"));
        qs.set("content", bean.get("content"));
        qs.set("category_id", bean.get("category_id"));
        qs.set("published", ""+new Timestamp(new java.util.Date().getTime()));
        qs.set("user_id", bean.get("userId"));
        qs.insert(tableName);
        return db.insert(qs);
    }

    public ArrayList<Bean> getArticleById(String id) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.where("id",id, Operands.EQUAL.toString());
        return db.getList(qs);
    }
    
    public void editArticle(String id, String category_id, String title, String real_title, String sub_title, String lead_in, String content) throws Exception {
        QuerySet qs = new QuerySet();
        qs.set("title", title);
        qs.set("real_title", real_title);
        qs.set("sub_title", sub_title);
        qs.set("lead_in", lead_in);
        qs.set("content", content);
        qs.set("category_id", category_id);
        qs.set("updated", ""+new Timestamp(new java.util.Date().getTime()));
        qs.where("id", id, Operands.EQUAL.toString());
        qs.update(tableName);
        db.update(qs);
    }
    
    public void deleteArticle(String id) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.where("id", id, Operands.EQUAL.toString());
        db.delete(tableName, id);
    }

}