/*
 *  DB.java
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

import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.types.Hmap;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public abstract class DB {
    
    public abstract Connection getConn() throws SQLException;
    public abstract void closeConn(Connection conn) throws SQLException;

    public ResultSet executeUpdate(String sql, ArrayList<String> values) throws SQLException {
        Jmvc.logDebug("[DB:executeUpdate] " + " sql: " + sql + " values: " + values);
        Connection conn = getConn();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        if (values != null) {
            int parameterIndex = 1;
            for (String s : values) {
                if (s == null || s.equalsIgnoreCase("null")) {
                    pstmt.setNull(parameterIndex, Types.NULL);
                } else {
                    pstmt.setObject(parameterIndex, s);
                }
                parameterIndex++;
            }
        }
        pstmt.executeUpdate();
        closeConn(conn);
        return pstmt.getGeneratedKeys();
    }

    public ResultSet executeUpdate(String sql) throws SQLException {
        return executeUpdate(sql, null);
    }

    /**
     * Executes the given query and returns the result as a ArrayList<LinkedHashMap>
     * @param sql The SQL query to execute.
     * @param values 
     * @return ArrayList of LinkedHashMap<String,String> Each ArrayList entry is a row.
     * @throws SQLException 
     */
    public Hmap executeQueryForObject(String sql, ArrayList<String> values) throws SQLException {
        Jmvc.logDebug("[DB:executeQueryForObject] " + " sql: " + sql + " values: " + values);        
        Hmap result = null;
        Connection conn = getConn();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        if (values != null) {
            int parameterIndex = 1;
            for (String s : values) {
                if (s == null || s.equalsIgnoreCase("null")) {
                    pstmt.setNull(parameterIndex, Types.NULL);
                } else {
                    pstmt.setObject(parameterIndex, s);
                }
                parameterIndex++;
            }
        }
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String columnName = ""; // this string is used in the loop so we create it now once instead of every cycle.
        if (rs.next()) {
            result = new Hmap();
            for (int i = 1; i <= columnCount; i++) {
                String val = rs.getString(i);
                if (val == null) {
                    val = "";
                }
                columnName = rsmd.getColumnName(i);
                if (result.containsKey(columnName)) {
                    result.put(rsmd.getTableName(i) + "." + columnName, val);
                } else {
                    result.put(columnName, val);
                }
            }
        }
        closeConn(conn);
        return result;
    }

    /**
     * Executes the given query and returns the result as a ArrayList<LinkedHashMap>
     * @param sql The SQL query to execute.
     * @param values 
     * @return ArrayList of LinkedHashMap<String,String> Each ArrayList entry is a row.
     * @throws SQLException 
     */
    public ArrayList<Hmap> executeQueryForList(String sql, ArrayList<String> values) throws SQLException {
        Jmvc.logDebug("[DB:executeQueryForList] " + "sql: " + sql + " values: " + values);
        ArrayList<Hmap> result = new ArrayList<Hmap>();
        int resultIndex = 0;
        Connection conn = getConn();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        if (values != null) {
            int parameterIndex = 1;
            for (String s : values) {
                if (s == null || s.equalsIgnoreCase("null")) {
                    pstmt.setNull(parameterIndex, Types.NULL);
                } else {
                    pstmt.setObject(parameterIndex, s);
                }
                parameterIndex++;
            }
        }
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String columnName = ""; // this string is used in the loop so we create it now once instead of every cycle.
        while (rs.next()) {
            Hmap row = new Hmap();
            for (int i = 1; i <= columnCount; i++) {
                String val = rs.getString(i);
                if (val == null) {
                    val = "";
                }
                columnName = rsmd.getColumnName(i);
                if (row.containsKey(columnName)) {
                    row.put(rsmd.getTableName(i) + "." + columnName, val);
                } else {
                    row.put(columnName, val);
                }
            }
            result.add(row);
            resultIndex++;
        }
        closeConn(conn);
        return result;
    }

    /**
     * Looks up if a table exists
     * @param table The name of the table
     * @return True if it exists, false if not
     * @throws SQLException 
     */
    public boolean tableExists(String table) throws SQLException {
        Connection conn = getConn();
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, table, null);
            if (tables.next()) {
                closeConn(conn);
                return true;
            } else {
                closeConn(conn);
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            closeConn(conn);
        }
    }

    /**
     * Execute query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    public ArrayList<Hmap> getList(QuerySet qs) throws SQLException {
        return executeQueryForList(qs.compileSelect(), qs.getData());
    }

    /**
     * Execute query set
     * @param qs 
     * @return Hmap
     * @throws SQLException 
     */
    public Hmap getObject(QuerySet qs) throws SQLException {
        return executeQueryForObject(qs.compileSelect(), qs.getData());
    }

    /**
     * Execute an update query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    public ResultSet update(QuerySet qs) throws SQLException {
        return executeUpdate(qs.compileUpdate(), qs.getData());
    }

    /**
     * Count returned rows of a querySet
     * @param qs
     * @return
     * @throws java.sql.SQLException
     */
    public int count(QuerySet qs) throws SQLException {
        Hmap a = executeQueryForObject(qs.compileCount(), qs.getData());
        if (a.size() > 0) {
            return Integer.parseInt(a.get("count"));
        } else {
            throw new SQLException("Failed while counting");
        }
    }

    /**
     * Inserts a row in the given table. 
     * @param qs the queryset with the sql template and the data to insert
     * @return returns the the auto-generated key as a string
     * @throws SQLException 
     */
    public String insert(QuerySet qs) throws SQLException {
        ResultSet rs = executeUpdate(qs.compileInsert(), qs.getData());
        if (rs.next()) {
            return  rs.getString(1);
        } else {
            Jmvc.logDebug("DB class: insert method : We just did an insert and return a null id back.");
            throw new SQLException("No id was returned");            
        }
    }

    /**
     * Drops a table
     *
     * @param table table name to dropTable.
     * @throws SQLException 
     */
    public void dropTable(String table) throws SQLException {
        if (tableExists(table)) {
            String sql = "DROP TABLE " + table;
            executeUpdate(sql);
        }
    }

    /**
     * reads the structure and creates the table. (Do not use yet - NOT READY)
     * @param table name of the table to create
     * @throws SQLException 
     */
    public void create(String table) throws SQLException {
        dropTable(table);

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(table).append(" (id int(10) unsigned NOT NULL auto_increment,");
        sb.append(" PRIMARY KEY (id))").append(" ENGINE=InnoDB ").append(" DEFAULT CHARSET=utf8 ");
        Jmvc.logDebug("[DB:create] " + sb.toString());
        executeUpdate(sb.toString());
    }

    /**
     * Deletes a row from a table
     * @param table
     * @param id
     * @throws java.sql.SQLException
     */
    public void delete(String table, String id) throws SQLException {
        if (!id.isEmpty()) {
            String sql = "DELETE FROM " + table + " WHERE id=" + id;
            executeUpdate(sql);
        }
    }

    /**
     * Looks up the table meta data and tries to deduce 
     * the table definition. This might not work for all dbs
     * Primarily used to instrument Models to sync them selfs to the db.
     * 
     * @param table the table name to define.
     * @return Hmap
     * @throws SQLException
     */
    public Hmap tableDef(String table) throws SQLException {
        Connection conn = getConn();
        try {
            DatabaseMetaData dbm = conn.getMetaData(); // Get database meta data  
            ResultSet pk = dbm.getPrimaryKeys(null, null, table); // get list of primary keys
            String primaryKeyColumnName = ""; // store the name of the column that is the pk for later reference            
            while (pk.next()) {
                primaryKeyColumnName = pk.getString("COLUMN_NAME");
            }
            ///// GET COLUMN DEFS
            ResultSet columns = dbm.getColumns(null, null, table, null); // Get the column meta data.
            Hmap definition = new Hmap(); // To store the definition. Key = (table) column name, Value = column definition in "attribute:value" pairs (| separated).
            // Iterate through the column definitions and build each column.
            while (columns.next()) {
                String attr = "";
                int type = columns.getInt("DATA_TYPE");
                switch (type) {
                    case Types.INTEGER:
                        attr = "type:int" + "|" +
                                "length:" + columns.getString("COLUMN_SIZE") + "|" +
                                "canBeNull:" + (columns.getString("NULLABLE").equalsIgnoreCase("1") ? "true" : "false");
                        break;
                    case Types.BIT:
                    case Types.TINYINT:
                        attr = "type:tinyint" + "|" +
                                "canBeNull:" + (columns.getString("NULLABLE").equalsIgnoreCase("1") ? "true" : "false");
                        break;
                    case Types.VARCHAR:
                        attr = "type:varchar" + "|" +
                                "length:" + columns.getString("COLUMN_SIZE") + "|" +
                                "canBeNull:" + (columns.getString("NULLABLE").equalsIgnoreCase("1") ? "true" : "false");
                        break;
                    case Types.LONGNVARCHAR:
                        attr = "type:text" + "|" +
                                "canBeNull:" + (columns.getString("NULLABLE").equalsIgnoreCase("1") ? "true" : "false");
                        break;
                    case Types.TIMESTAMP:
                        attr = "type:timestamp" + "|" +
                                "canBeNull:" + (columns.getString("NULLABLE").equalsIgnoreCase("1") ? "true" : "false");
                        break;
                }
                if (columns.getString("COLUMN_NAME").equals(primaryKeyColumnName)) {
                    attr += "|key:true";
                }
                definition.put(columns.getString("COLUMN_NAME"), attr);
                closeConn(conn);
                return definition;
            }
        /// @todo close resultsets
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConn(conn);
        }
        return null;
    }
}
