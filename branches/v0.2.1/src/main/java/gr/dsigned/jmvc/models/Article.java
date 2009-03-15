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

import gr.dsigned.jmvc.db.QuerySet.Join;
import gr.dsigned.jmvc.types.Hmap;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import static gr.dsigned.jmvc.db.QuerySet.LogicOperands;
import static gr.dsigned.jmvc.db.QuerySet.OrderBy;
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

    public ArrayList<Hmap> getLatestPosts(int numberToFetch) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        //qs.join("categories", "categories.id = articles.category_id", Join.LEFT);
        //qs.where("categories.name","basket", LogicOperands.EQUALS);
        qs.orderBy(OrderBy.DESC, "published");
        qs.limit(numberToFetch);
        return db.getList(qs);
    }
    
    public ArrayList<Hmap> getPostsInInterval(Date from, Date to) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.orderBy(OrderBy.DESC, "published");
        return db.getList(qs);
    }

    public ArrayList<Hmap> getArticlesByCat(String cat, int limit, int offset) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.join("categories", "Articles.category_id = Categories.id",Join.INNER);
        qs.where("Categories.name", cat, LogicOperands.EQUAL);
        qs.orderBy(OrderBy.DESC, "published");
        qs.limit(offset, limit);
        return db.getList(qs);
    }               

    public int countArticlesByCat(String cat) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.join("categories", "Articles.category_id = Categories.id", Join.LEFT);
        qs.where("Categories.name", cat, LogicOperands.EQUAL);
        qs.orderBy(OrderBy.DESC, "published");
        return db.count(qs);
    }
    
    public String insertArticle(Hmap bean) throws Exception {
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

    public Hmap getArticleById(String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.where("id",id, LogicOperands.EQUAL);
        return db.getObject(qs);
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
        qs.where("id", id, LogicOperands.EQUAL);
        qs.update(tableName);
        db.update(qs);
    }
    
    public void deleteArticle(String id) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.where("id", id, LogicOperands.EQUAL);
        db.delete(tableName, id);
    }

}