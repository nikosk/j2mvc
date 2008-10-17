/*
 *  Category.java
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

import gr.dsigned.jmvc.types.Hmap;
import java.sql.SQLException;
import java.util.ArrayList;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.Operand;
import gr.dsigned.jmvc.db.QuerySet;

/**
 * 15 Μαρ 2008, gr.dsigned.jmvc.models 
 * Category.java
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class Category extends Model {

    public Category() throws Exception {
        this.tableName = "categories";
    }

    public ArrayList<Hmap> getCategories() throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        return db.getList(qs);
    }

    public Hmap getCategoryIdNamePair() throws Exception {
        Hmap data = new Hmap();
        QuerySet qs = new QuerySet();
        qs.select("id", "display_name");
        qs.from(this.tableName);
        for (Hmap hm : db.getList(qs)) {
            data.put(hm.get("id"), hm.get("display_name"));
        }
        return data;
    }

    public ArrayList<Hmap> getCategoryById(String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        qs.where("id", id, Operand.EQUAL);
        return db.getList(qs);
    }
}
