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

import gr.dsigned.jmvc.Settings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Model {

    public enum operands {
        GREATER_THAN {
            @Override
            public String toString() {
                return ">";
            }
        },
        LESS_THAN {
            @Override
            public String toString() {
                return "<";
            }
        },
        GREATER_THAN_OR_EQUAL {
            @Override
            public String toString() {
                return ">=";
            }
        },
        LESS_THAN_OR_EQUAL {
            @Override
            public String toString() {
                return ">=";
            }
        },}
    public String tableName = "";
    public DB db;
    protected LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
    protected LinkedHashMap<String, String> modelDefinition = new LinkedHashMap<String, String>();
    
    /**
     * Creates a new instance of Model
     */
    public Model() throws Exception{
        init();
    }

    public Model(String name)throws Exception {
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
     */
    public void load(String id) throws SQLException{
        Hashtable<String, String> ht = new Hashtable<String, String>();
        ArrayList<LinkedHashMap<String,String>> results = new ArrayList<LinkedHashMap<String,String>>();
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
    public void load(String table, String id) throws SQLException{
        Hashtable<String, String> ht = new Hashtable<String, String>();
        ArrayList<LinkedHashMap<String,String>> results = new ArrayList<LinkedHashMap<String,String>>();
        ht.put("id", id);
        results = db.query(table, "*", ht);
        for (LinkedHashMap<String, String> lhm : results) {
            for (String s : lhm.keySet()) {
                data.put(s, lhm.get(s));
            }
        }
    }

    /**
     * Stores the data currently stored in the model.
     * @param table The table name to use when storing.
     * @todo Is the table name really needed ?
     */
    public void store(String table)throws SQLException {
        String genID = "";
        ResultSet rs = db.insertRow(table, data);
        while (rs.next()) {
            genID = rs.getString(1);
        }
        load(table, genID);
    }

    /**
     * Store the current Model object to the db.
     * @TODO Check for required fields and fail if such a field is missing.
     */
    public void store() throws SQLException {
        String genID = "";
        ResultSet rs = db.insertRow(tableName, data);
        try {
            while (rs.next()) {
                genID = rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        this.load(genID);
    }

    /***************************************************************************
     *  QuerySets
     ***************************************************************************/
    /**
     * Loads the first row that matches the passed in criteria
     * @param criteria Hashtable where key is a column name and value is the criterion.
     */
    public void filter(Hashtable<String,String> criteria) throws SQLException{
        ArrayList<LinkedHashMap<String,String>> results = new ArrayList<LinkedHashMap<String,String>>();
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
    public void filter(String table, Hashtable<String,String> criteria) throws SQLException{
        ArrayList<LinkedHashMap<String,String>> results = new ArrayList<LinkedHashMap<String,String>>();
        results = db.query(table, "*", criteria);
        for (LinkedHashMap<String, String> lhm : results) {
            for (String s : lhm.keySet()) {
                data.put(s, lhm.get(s));
            }
        }
    }
}
