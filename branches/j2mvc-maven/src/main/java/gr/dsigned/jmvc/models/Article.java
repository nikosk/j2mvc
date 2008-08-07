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
        qs.join("categories", "Articles.category_id = Categories.id", "left");
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
    
    public Bean insertArticle(String userId, String category_id, String title, String real_title, String sub_title, String lead_in, String content) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.insert("title", title);
        qs.insert("real_title", real_title);
        qs.insert("sub_title", sub_title);
        qs.insert("lead_in", lead_in);
        qs.insert("content", content);
        qs.insert("category_id", category_id);
        qs.insert("published", ""+new Timestamp(new java.util.Date().getTime()));
        qs.insert("user_id", userId);
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
        qs.table(tableName);
        qs.update("title", title);
        qs.update("real_title", real_title);
        qs.update("sub_title", sub_title);
        qs.update("lead_in", lead_in);
        qs.update("content", content);
        qs.update("category_id", category_id);
        qs.update("updated", ""+new Timestamp(new java.util.Date().getTime()));
        qs.where("id", id, Operands.EQUAL.toString());
        db.update(qs);
    }
}