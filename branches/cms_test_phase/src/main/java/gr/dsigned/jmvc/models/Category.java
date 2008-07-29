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

import gr.dsigned.jmvc.db.Bean;
import java.sql.SQLException;
import java.util.ArrayList;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;

/**
 * 15 Μαρ 2008, gr.dsigned.jmvc.models 
 * Category.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Category extends Model {

    public Category() throws Exception {
        this.tableName = "categories";
    }

    public ArrayList<Bean> getCategories() throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from(this.tableName);
        return db.get(qs);
    }
}
