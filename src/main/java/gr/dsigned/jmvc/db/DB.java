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

import gr.dsigned.jmvc.db.Bean;
import gr.dsigned.jmvc.framework.Utils;
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
public class DB {

    protected Connection conn;

    public Connection getConn() throws SQLException {
        return conn;
    }

    public void closeConn() throws SQLException {
        conn.close();
    }

    public ResultSet executePreparedStatement(String sql, Bean values) throws SQLException {
        PreparedStatement pstmt = getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        if (values != null) {
            int parameterIndex = 1;
            for (String s : values.keySet()) {
                if (values.get(s) == null || values.get(s).equalsIgnoreCase("null")) {
                    pstmt.setNull(parameterIndex, Types.NULL);
                } else {
                    pstmt.setObject(parameterIndex, values.get(s));
                }
                parameterIndex++;
            }
        }
        pstmt.executeUpdate();
        closeConn();
        return pstmt.getGeneratedKeys();
    }

    public ResultSet executePreparedStatement(String sql) throws SQLException {
        return executePreparedStatement(sql, null);
    }

    /**
     * Executes the given query and returns the result as a ArrayList<LinkedHashMap>
     * @param sql The SQL query to execute.
     * @return ArrayList of LinkedHashMap<String,String> Each ArrayList entry is a row.
     * @throws SQLException 
     */
    public ArrayList<Bean> executeQuery(String sql) throws SQLException {
        ArrayList<Bean> result = new ArrayList<Bean>();
        int resultIndex = 0;
        PreparedStatement pstmt = getConn().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ResultSet rs = pstmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String columnName = ""; // this string is used in the loop so we create it now once instead of every cycle.
        while (rs.next()) {
            Bean row = new Bean();
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
        closeConn();
        return result;
    }

    /**
     * Looks up if a table exists
     * @param table The name of the table
     * @return True if it exists, false if not
     * @throws SQLException 
     */
    public boolean tableExists(String table) throws SQLException {
        try {
            DatabaseMetaData dbm = getConn().getMetaData();
            ResultSet tables = dbm.getTables(null, null, table, null);
            if (tables.next()) {
                closeConn();
                return true;
            } else {
                closeConn();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            closeConn();
        }
    }

    /**
     * Execute query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    public ArrayList<Bean> get(QuerySet qs) throws SQLException {
        return executeQuery(qs.compileSelect());
    }

    /**
     * Count returned rows of a querySet
     * @param qs
     * @return
     * @throws java.sql.SQLException
     */
    public int count(QuerySet qs) throws SQLException {
        ArrayList<Bean> a = executeQuery(qs.compileCount());
        if (a.size() > 0) {
            return Integer.parseInt(a.get(0).get("count"));
        } else {
            throw new SQLException("Failed while counting");
        }
    }

    /**
     * Inserts a row in the given table. 
     * @param table The table name to insert.
     * @param values LinkedHashMap of key => value strings
     * @return returns the id of the record that got created
     * @throws SQLException 
     */
    public ResultSet insertRow(String table, Bean values) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table).append("(");
        for (String s : values.keySet()) {
            sb.append(s).append(",");
        }
        sb.deleteCharAt(sb.length() - 1); //remove last ','
        sb.append(") VALUES (");
        for (int i = 0; i < values.size(); i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return executePreparedStatement(sb.toString(), values);
    }

    /**
     * Performs a "SELECT * FROM" query
     * @param table the name of the table
     * @return LinkedHashMap of the resultset
     * @throws SQLException 
     */
    public ArrayList<Bean> query(String table) throws SQLException {
        String sql = "SELECT * FROM " + table;
        return executeQuery(sql);
    }

    /**
     * Performs a "SELECT cols FROM table" query
     * @param table name of the table
     * @param cols columns to return separate with comma
     * @return LinkedHashMap of the resultset
     * @throws SQLException 
     */
    public ArrayList<Bean> query(String table, String cols) throws SQLException {
        String sql = "SELECT " + cols + " FROM " + table;
        return executeQuery(sql);
    }

    /**
     * Performs a "SELECT cols FROM table WHERE criteria" query 
     * @param table name of the table
     * @param cols columns to return separate with comma
     * @param criteria Hashmap where key is the column and value is the criterion
     * aHashMap.put("id >","1");
     * equals is used when no operator is used in the key.
     * @return LinkedHashMap of the resultset
     * @throws SQLException 
     */
    public ArrayList<Bean> query(String table, String cols, Bean criteria) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(cols.isEmpty() ? "*" : cols).append(" FROM ").append(table).append(" WHERE ");

        for (String key : criteria.keySet()) {
            sb.append(key);
            if (!Utils.hasOperator(key)) {
                sb.append(" = ");
            }
            sb.append(Utils.escape(criteria.get(key)));
            sb.append(" AND ");
        }
        sb.delete(sb.length() - 5, sb.length()); //delete last " AND "
        return executeQuery(sb.toString());
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
            executePreparedStatement(sql);
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
        System.out.println(sb.toString());
        executePreparedStatement(sb.toString());
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
            executePreparedStatement(sql);
        }
    }

    /**
     * Looks up the table meta data and tries to deduce 
     * the table definition. This might not work for all dbs
     * Primarily used to instrument Models to sync them selfs to the db.
     * 
     * @param table the table name to define.
     * @return Bean
     * @throws SQLException
     */
    public Bean tableDef(String table) throws SQLException {
        try {

            DatabaseMetaData dbm = getConn().getMetaData(); // Get database meta data  
            ResultSet pk = dbm.getPrimaryKeys(null, null, table); // get list of primary keys
            String primaryKeyColumnName = ""; // store the name of the column that is the pk for later reference            
            while (pk.next()) {
                primaryKeyColumnName = pk.getString("COLUMN_NAME");
            }
            ///// GET COLUMN DEFS
            ResultSet columns = dbm.getColumns(null, null, table, null); // Get the column meta data.
            Bean definition = new Bean(); // To store the definition. Key = (table) column name, Value = column definition in "attribute:value" pairs (| separated).
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
                closeConn();
                return definition;
            }
        /// @todo close resultsets
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConn();
        }
        return null;
    }

   
}
