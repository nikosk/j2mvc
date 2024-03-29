/*
 *  User.java
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
import gr.dsigned.jmvc.db.Model;

import gr.dsigned.jmvc.db.QuerySet;
import gr.dsigned.jmvc.db.QuerySet.LogicOperands;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class User extends Model {

    public User() throws Exception {
        this.tableName = "users";
    }

    public Hmap auth(String username, String password) throws Exception {
        // TODO Fix db to escape values properly
        QuerySet qs = new QuerySet();
        qs.where("username", username, LogicOperands.EQUAL);
        qs.where("password", password, LogicOperands.EQUAL);
        qs.from(this.tableName);
        Hmap al =db.getObject(qs);
        return al;
    }
}
