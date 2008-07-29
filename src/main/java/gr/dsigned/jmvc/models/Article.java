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

import gr.dsigned.jmvc.db.Bean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;

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
        qs.where("categories.name = 'basket'");
        qs.orderBy("published", "DESC");
        qs.limit(numberToFetch);
        return db.get(qs);
    }

    public ArrayList<Bean> getPostsInInterval(Date from, Date to) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.orderBy("published", "DESC");
        return db.get(qs);
    }

    public ArrayList<Bean> getArticlesByCat(String cat, int limit, int offset) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
        qs.orderBy("published", "DESC");
        qs.limit(offset, limit);
        qs.join("categories", "Articles.category_id = Categories.id", "left");
        qs.where("Categories.name = '" + cat + "'");
        return db.get(qs);
    }

    public int countArticlesByCat(String cat) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("articles");
       // qs.orderBy("published", "DESC");
        qs.join("categories", "Articles.category_id = Categories.id", "left");
        qs.where("Categories.name = '" + cat + "'");
        return db.count(qs);
    }
}