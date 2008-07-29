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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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
                db = gr.dsigned.jmvc.db.MysqlDB.getInstance();
            }
        }
    }

    /**********************************************************************************
     * General DB methods
     **********************************************************************************/
    /***************************************************************************
     * ORM Methods
     ***************************************************************************/
    /**
     * Loads the data from the db to this models data
     * @todo Do something in case we didn't find any data to load
     * @param id The id of the row to load
     * @throws SQLException 
     */
    public void load(String id) throws SQLException {
        Bean ht = new Bean();
        ArrayList<Bean> results = new ArrayList<Bean>();
        ht.put("id", id);
        results = db.query(tableName, "*", ht);
        for (LinkedHashMap<String, String> lhm : results) {
            for (String s : lhm.keySet()) {
                data.put(s, lhm.get(s));
            }
        }
    }

    /**
     * Loads the data from the db to this models data.
     * @param table The table name to look into.
     * @param id The id of the row to load
     */
    public void load(String table, String id) throws SQLException {
        Bean ht = new Bean();
        ArrayList<Bean> results = new ArrayList<Bean>();
        ht.put("id", id);
        results = db.query(table, "*", ht);
        for (LinkedHashMap<String, String> lhm : results) {
            for (String s : lhm.keySet()) {
                data.put(s, lhm.get(s));
            }
        }
    }

    /**
     * Store the current Model object to the db.
     * @throws SQLException 
     * @TODO Check for required fields and fail if such a field is missing.
     */
    public void store() throws SQLException {
//        String genID = "";
//        ResultSet rs = db.insertRow(tableName, data);
//        try {
//            while (rs.next()) {
//                genID = rs.getString(1);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        this.load(genID);
    }

    /***************************************************************************
     *  QuerySets
     ***************************************************************************/
    /**
     * Loads the first row that matches the passed in criteria
     * @param criteria Hashtable where key is a column name and value is the criterion.
     */
    public void filter(Bean criteria) throws SQLException {
        ArrayList<Bean> results = new ArrayList<Bean>();
        results = db.query(this.tableName, "*", criteria);
        for (LinkedHashMap<String, String> lhm : results) {
            for (String s : lhm.keySet()) {
                data.put(s, lhm.get(s));
            }
        }
    }

    /**
     * Loads the first row that matches the passed in criteria
     * @param table The table name to use.
     * @param criteria Hashtable where key is a column name and value is the criterion.
     */
    public void filter(String table, Bean criteria) throws SQLException {
        ArrayList<Bean> results = new ArrayList<Bean>();
        results = db.query(table, "*", criteria);
        for (LinkedHashMap<String, String> lhm : results) {
            for (String s : lhm.keySet()) {
                data.put(s, lhm.get(s));
            }
        }
    }

    public void delete(String id) throws SQLException {
        db.delete(tableName, id);
    }
}