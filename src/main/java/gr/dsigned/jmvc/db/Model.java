/*
 *  Model.java
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
package gr.dsigned.jmvc.db;

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.types.Tuple2;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Model {

    protected String tableName = "";
    protected DB db;
    
    /**
     * Creates a new instance of Model
     */
    public Model() {
        init();
    }

    public Model(String name) {
        this.tableName = name;
        init();
    }

    private void init() {
        if (!Settings.get("DATABASE_TYPE").equalsIgnoreCase("none")) {
            if (Settings.get("DATABASE_TYPE").equalsIgnoreCase("mysql")) {
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
    public Hmap getById(String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("id", id, QuerySet.LogicOperands.EQUAL);
        return db.getObject(qs);
    }

    /**
     * Deletes a bean from the db
     * @todo Do something in case we didn't find any data to load
     * @param id The id of the row to delete
     * @return 
     * @throws SQLException
     */
    public int deleteById(String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.delete(tableName);
        qs.where("id", id, QuerySet.LogicOperands.EQUAL);
        qs.limit(1);
        return db.delete(qs);
    }

    /**
     * Inserts a bean into the db
     * @param bean The bean to insert
     * @return
     * @throws SQLException
     */
    public String insert(Hmap bean) throws Exception {
        QuerySet qs = new QuerySet();
        qs.insert(tableName, bean);
        return db.insert(qs);
    }    
    /**
     * Retrieves a bean from the db using an established transaction
     * @todo Do something in case we didn't find any data to load
     * @param id The id of the row to load
     * @return
     * @throws SQLException
     */
    public Hmap getById(String id, MysqlDBTrans dbTrans) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.where("id", id, QuerySet.LogicOperands.EQUAL);
        return dbTrans.getObject(qs);
    }

    /**
     * Deletes a bean from the db using an established transaction
     * @todo Do something in case we didn't find any data to load
     * @param id The id of the row to delete
     * @return
     * @throws SQLException
     */
    public int deleteById(String id, MysqlDBTrans dbTrans) throws Exception {
        QuerySet qs = new QuerySet();
        qs.delete(tableName);
        qs.where("id", id, QuerySet.LogicOperands.EQUAL);
        qs.limit(1);
        return dbTrans.delete(qs);
    }

    /**
     * Inserts a bean into the db using an established transaction
     * @param bean The bean to insert
     * @return 
     * @throws SQLException
     */
    public String insert(Hmap bean, MysqlDBTrans dbTrans) throws Exception {
        QuerySet qs = new QuerySet();
        qs.insert(tableName, bean);
        return dbTrans.insert(qs);
    }    
}