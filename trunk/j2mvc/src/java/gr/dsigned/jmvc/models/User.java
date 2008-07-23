/*
 *  User.java
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

import gr.dsigned.jmvc.db.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class User extends Model {

    public User() throws Exception {
        this.tableName = "users";
    }

    public boolean auth(String username, String password) throws Exception {
        // TODO Fix db to escape values properly
        db.where("username = '" + username + "'");
        db.where("password = '" + password + "'");
        db.from(this.tableName);
        ArrayList<LinkedHashMap<String, String>> al = db.get();
        return (al.size() > 0) ? true : false;
    }
}