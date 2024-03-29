/*
 *  Post.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
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


import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.db.QuerySet;
import gr.dsigned.jmvc.db.QuerySet.OrderBy;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Post extends gr.dsigned.jmvc.db.Model {

    public Post() throws Exception {
        this.tableName = "posts";
    }

    public ArrayList<Hmap> getLatestPosts(int numberToFetch) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("posts");
        qs.orderBy(OrderBy.DESC, "pub_date");
        qs.limit(numberToFetch);
        return db.getList(qs);
    }

    public ArrayList<Hmap> getPostsInInterval(Date from, Date to) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("posts");
        qs.orderBy(OrderBy.DESC, "pub_date");
        return db.getList(qs);
    }
}

        