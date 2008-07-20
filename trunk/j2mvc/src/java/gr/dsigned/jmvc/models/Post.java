/*
 *  Post.java
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

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Post extends gr.dsigned.jmvc.db.Model {

    public Post() throws Exception {
        this.tableName = "posts";
    }

    public ArrayList<LinkedHashMap<String, String>> getLatestPosts(int numberToFetch) throws SQLException {
        db.from("posts");
        db.orderBy("pub_date", "DESC");
        db.limit(numberToFetch);
        return db.get();
    }

    public ArrayList<LinkedHashMap<String, String>> getPostsInInterval(Date from, Date to) throws SQLException {
        db.from("posts");
        db.orderBy("pub_date", "DESC");
        return db.get();
    }
}

        