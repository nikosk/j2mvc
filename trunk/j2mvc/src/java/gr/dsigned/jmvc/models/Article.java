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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import gr.dsigned.jmvc.db.Model;

/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.models 
 * Article.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Article extends Model {

    public Article() throws Exception {
        this.tableName = "articles";
    }

    public ArrayList<LinkedHashMap<String, String>> getLatestPosts(int numberToFetch) throws SQLException {
        db.from("articles");
        db.join("categories", "categories.id = articles.category_id", "left");
        db.where("categories.name = 'basket'");
        db.orderBy("published", "DESC");
        db.limit(numberToFetch);
        return db.get();
    }

    public ArrayList<LinkedHashMap<String, String>> getPostsInInterval(Date from, Date to) throws SQLException {
        db.from("articles");
        db.orderBy("published", "DESC");
        return db.get();
    }

    public ArrayList<LinkedHashMap<String, String>> getArticlesByCat(String cat, int limit, int offset) throws SQLException {
        db.from("articles");
        db.orderBy("published", "DESC");
        db.limit(offset, limit);
        db.join("categories", "Articles.category_id = Categories.id", "left");
        db.where("Categories.name = '" + cat + "'");
        return db.get();
    }

    public int countArticlesByCat(String cat) throws SQLException {
        db.from("articles");
        db.orderBy("published", "DESC");
        db.join("categories", "Articles.category_id = Categories.id", "left");
        db.where("Categories.name = '" + cat + "'");
        return db.count();
    }
}
