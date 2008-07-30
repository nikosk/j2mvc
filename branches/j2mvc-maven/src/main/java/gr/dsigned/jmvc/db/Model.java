/*
 *  Model.java
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
package gr.dsigned.jmvc.db;

import gr.dsigned.jmvc.Bean;
import gr.dsigned.jmvc.Settings;
import java.sql.SQLException;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Model {

    public enum Operands {

        EQUAL ("="),
        GREATER_THAN (">"),
        LESS_THAN ("<"),
        GREATER_THAN_OR_EQUAL (">="),
        LESS_THAN_OR_EQUAL (">=");
        
        private final String value;
        Operands(String value) {
            this.value = value;
        }
        @Override
        public String toString() {
            return value;
        }
    }
    public String tableName = "";
    public DB db;
    public Bean data = new Bean();
    public Bean modelDefinition = new Bean();

    /**
     * Creates a new instance of Model
     */
    public Model() throws Exception {
        init();
    }

    public Model(String name) throws Exception {
        this.tableName = name;
        init();
    }

    private void init() throws Exception {
        if (!Settings.DATABASE_TYPE.equalsIgnoreCase("none")) {
            if (Settings.DATABASE_TYPE.equalsIgnoreCase("mysql")) {
                db = MysqlDB.getInstance();
            }
        }
    }

    /**********************************************************************************
     * General DB methods
     **********************************************************************************/
    /**
     * Retrieves a bean from the db
     * @todo Do something in case we didn't find any data to load
     * @param id The id of the row to load
     * @return 
     * @throws SQLException
     */
    public Bean getById(String id) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("id", id, Operands.EQUAL.toString());
        return db.getObject(qs);
    }
    
    /**
     * Inserts a bean into the db
     * @param bean The bean to insert
     * @return 
     * @throws SQLException
     */
    public Bean insert(Bean bean) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.insert(bean);
        return db.insert(qs);
    }

    public void delete(String id) throws SQLException {
        db.delete(tableName, id);
    }
}