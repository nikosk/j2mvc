/*
 *  DB.java
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

import gr.dsigned.jmvc.Settings;
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
import java.util.concurrent.ConcurrentHashMap;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public abstract class DB {

    public abstract Connection getConn() throws SQLException;

    public abstract Cache getCache() throws Exception;

    public abstract void closeConn(Connection conn) throws SQLException;
    private static final boolean debug = Settings.get("DEBUG").equals("TRUE");
    protected static final boolean cacheEnabled = Settings.get("CACHE_DB").equals("TRUE");
    protected static final ConcurrentHashMap<String,ArrayList<String>> cacheKeyStore = new ConcurrentHashMap<String, ArrayList<String>>();

    private long endTime = 0;
    private long startTime = 0;

    protected String executeInsert(String sql, ArrayList<String> values) throws SQLException {
        if (debug) {
            Jmvc.logDebug("[DB:executeInsert] " + " sql: " + sql + " values: " + values);
            startTime = System.nanoTime();
        }
        Connection conn = getConn();
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        if (values != null) {
            int parameterIndex = 1;
            for (String s : values) {
                if (s == null || s.trim().equalsIgnoreCase("null")) {
                    pstmt.setNull(parameterIndex, Types.NULL);
                } else {
                    pstmt.setObject(parameterIndex, s);
                }
                parameterIndex++;
            }
        }
        pstmt.executeUpdate();
        ResultSet rsGenKeys = pstmt.getGeneratedKeys();
        String output = null;
        if (rsGenKeys.next()) {
            output = rsGenKeys.getString(1);
        }
        try {
            pstmt.close();
        } catch (Exception ex) {
            // TODO: handle or avoid java.lang.IllegalArgumentException: null source 
            Jmvc.logError("ex:  " + ex);
        }
        closeConn(conn);
        if (debug) {
            endTime = System.nanoTime();
            Jmvc.dbDebug("SQL: " + sql + ". Values: " + values + ". Execution time: " + ((double) (endTime - startTime) / 1000000));
        }
        return output;
    }

    protected int executeUpdate(String sql, ArrayList<String> values) throws SQLException {
        if (debug) {
            Jmvc.logDebug("[DB:executeUpdate] " + " sql: " + sql + " values: " + values);
            startTime = System.nanoTime();
        }
        Connection conn = getConn();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        if (values != null) {
            int parameterIndex = 1;
            for (String s : values) {
                if (s == null || s.trim().equalsIgnoreCase("null")) {
                    pstmt.setNull(parameterIndex, Types.NULL);
                } else {
                    pstmt.setObject(parameterIndex, s);
                }
                parameterIndex++;
            }
        }
        int updatedRows = pstmt.executeUpdate();
        try {
            pstmt.close();
        } catch (Exception ex) {
            // TODO: handle or avoid java.lang.IllegalArgumentException: null source 
            Jmvc.logError("ex:  " + ex);
        }
        closeConn(conn);
        if (debug) {
            endTime = System.nanoTime();
            Jmvc.dbDebug("SQL: " + sql + ". Values: " + values + ". Execution time: " + ((double) (endTime - startTime) / 1000000));
        }
        return updatedRows;
    }

    public int executeUpdate(String sql) throws SQLException {
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
        if (debug) {
            Jmvc.logDebug("[DB:executeQueryForObject] " + " sql: " + sql + " values: " + values);
            startTime = System.nanoTime();
        }
        Hmap result = null;
        int retryCount = 3;
        ResultSet rs;
        ResultSetMetaData rsmd;
        Connection conn;
        do {
            try {
                conn = getConn();
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                if (values != null) {
                    int parameterIndex = 1;
                    for (String s : values) {
                        if (s == null || s.trim().equalsIgnoreCase("null")) {
                            pstmt.setNull(parameterIndex, Types.NULL);
                        } else {
                            pstmt.setObject(parameterIndex, s);
                        }
                        parameterIndex++;
                    }
                }
                rs = pstmt.executeQuery();
                rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String columnName = ""; // this string is used in the loop so we create it now once instead of every cycle.
                if (rs.next()) {
                    result = new Hmap();
                    for (int i = 1; i <= columnCount; i++) {
                        String val = rs.getString(i);
                        if (val == null) {
                            val = "";
                        }
                        columnName = rsmd.getColumnLabel(i);
                        if (result.containsKey(columnName)) {
                            result.put(rsmd.getTableName(i) + "." + columnName, val);
                        } else {
                            result.put(columnName, val);
                        }
                    }
                }
                rs.close();
                pstmt.close();
                closeConn(conn);
                retryCount = 0;
            } catch (SQLException sqlEx) {
                String sqlState = sqlEx.getSQLState();
                if ("08S01".equals(sqlState)) {
                    retryCount--;
                } else {
                    retryCount = 0;
                }
                Jmvc.logError("SQLException : " + sqlEx);
                throw sqlEx;
            } catch (Exception ex) {
                // TODO: handle or avoid java.lang.IllegalArgumentException: null source 
                Jmvc.logError("ex:  " + ex);
                retryCount = 0;
            }
        } while (retryCount > 0);
        if (debug) {
            endTime = System.nanoTime();
            Jmvc.dbDebug("SQL: " + sql + ". Values: " + values + ". Execution time: " + ((double) (endTime - startTime) / 1000000));
        }
        return result;
    }

    /**
     * Executes the given query and returns the result as a HashMap<String,String>
     * @param sql The SQL query to execute.
     * @param values 
     * @return HashMap<String,String>
     * @throws SQLException 
     */
    public Hmap executeQueryForHmap(String sql, ArrayList<String> values) throws SQLException {
        if (debug) {
            Jmvc.logDebug("[DB:executeQueryForObject] " + " sql: " + sql + " values: " + values);
            startTime = System.nanoTime();
        }
        int retryCount = 3;
        ResultSet rs;
        ResultSetMetaData rsmd;
        Connection conn;
        Hmap result = null;
        do {
            try {
                conn = getConn();
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                if (values != null) {
                    int parameterIndex = 1;
                    for (String s : values) {
                        if (s == null || s.trim().equalsIgnoreCase("null")) {
                            pstmt.setNull(parameterIndex, Types.NULL);
                        } else {
                            pstmt.setObject(parameterIndex, s);
                        }
                        parameterIndex++;
                    }
                }
                rs = pstmt.executeQuery();
                rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                result = new Hmap();
                while (rs.next()) {
                    String id = "";
                    String val = "";
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == 1) {
                            id = rs.getString(i);
                        } else {
                            val = rs.getString(i);
                            result.put(id, val);
                        }
                    }
                }
                rs.close();
                pstmt.close();
                closeConn(conn);
                retryCount = 0;
            } catch (SQLException sqlEx) {
                String sqlState = sqlEx.getSQLState();
                if ("08S01".equals(sqlState)) {
                    retryCount--;
                } else {
                    retryCount = 0;
                }
                Jmvc.logError("SQLException : " + sqlEx);
                throw sqlEx;
            } catch (Exception ex) {
                // TODO: handle or avoid java.lang.IllegalArgumentException: null source 
                Jmvc.logError("ex:  " + ex);
                retryCount = 0;
            }
        } while (retryCount > 0);
        if (debug) {
            endTime = System.nanoTime();
            Jmvc.dbDebug("SQL: " + sql + ". Values: " + values + ". Execution time: " + ((double) (endTime - startTime) / 1000000));
        }
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
        if (debug) {
            Jmvc.logDebug("[DB:executeQueryForList] " + "sql: " + sql + " values: " + values);
            startTime = System.nanoTime();
        }
        int retryCount = 3;
        ResultSet rs;
        ResultSetMetaData rsmd;
        Connection conn;
        ArrayList<Hmap> result = new ArrayList<Hmap>();
        do {
            try {
                int resultIndex = 0;
                conn = getConn();
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                if (values != null) {
                    int parameterIndex = 1;
                    for (String s : values) {
                        if (s == null || s.trim().equalsIgnoreCase("null")) {
                            pstmt.setNull(parameterIndex, Types.NULL);
                        } else {
                            pstmt.setObject(parameterIndex, s);
                        }
                        parameterIndex++;
                    }
                }
                rs = pstmt.executeQuery();
                rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String columnName = ""; // this string is used in the loop so we create it now once instead of every cycle.
                while (rs.next()) {
                    Hmap row = new Hmap();
                    for (int i = 1; i <= columnCount; i++) {
                        String val = rs.getString(i);
                        if (val == null) {
                            val = "";
                        }
                        columnName = rsmd.getColumnLabel(i);
                        if (row.containsKey(columnName)) {
                            row.put(rsmd.getTableName(i) + "." + columnName, val);
                        } else {
                            row.put(columnName, val);
                        }
                    }
                    result.add(row);
                    resultIndex++;
                }
                rs.close();
                pstmt.close();
                closeConn(conn);
                retryCount = 0;
            } catch (SQLException sqlEx) {
                String sqlState = sqlEx.getSQLState();
                if ("08S01".equals(sqlState)) {
                    retryCount--;
                } else {
                    retryCount = 0;
                }
                Jmvc.logError("SQLException : " + sqlEx);
                throw sqlEx;
            } catch (Exception ex) {
                // TODO: handle or avoid java.lang.IllegalArgumentException: null source 
                Jmvc.logError("ex:  " + ex);
                retryCount = 0;
            }
        } while (retryCount > 0);
        if (debug) {
            endTime = System.nanoTime();
            Jmvc.dbDebug("SQL: " + sql + ". Values: " + values + ". Execution time: " + ((double) (endTime - startTime) / 1000000));
        }
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

    protected void storeCacheKeys(String key, QuerySet theQuery) throws Exception {
        ArrayList<String> tables = theQuery.getSourceTables();
        ArrayList<String> tableCacheKeys;
        for (String t : tables) {
            tableCacheKeys = cacheKeyStore.get(t);
            if (tableCacheKeys == null) {
                tableCacheKeys = new ArrayList<String>();
            }
            tableCacheKeys.add(key);
            cacheKeyStore.put(t, tableCacheKeys);
        }
    }

    protected void invalidateCachedQueries(QuerySet theQuery) throws Exception {
        ArrayList<String> tableCacheKeys;
        for (String t : theQuery.getUpdatedTables()) {
            tableCacheKeys = cacheKeyStore.get(t);
            if (tableCacheKeys != null) {
                for(int i = 0 ; i<tableCacheKeys.size();i++){
                String r = tableCacheKeys.get(i);
                    getCache().remove(r);
                    //@TODO: We need to remove the entries from tableCacheKeys as well. What happens when you modify the arraylist inside the loop ?
                }
            }
        }
    }

    /**
     * Execute query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    public ArrayList<Hmap> getList(QuerySet qs) throws SQLException, Exception {
        String key = qs.compileSelect() + qs.getData().toString();
        ArrayList<Hmap> result;
        if (cacheEnabled) {
            Element e = getCache().get(key);
            if (e != null) {
                result = (ArrayList<Hmap>) e.getObjectValue();
            } else {
                result = executeQueryForList(qs.compileSelect(), qs.getData());
                Element resultCacheElement = new Element(key, result);
                getCache().put(resultCacheElement);
                storeCacheKeys(key, qs);
            }
        } else {
            result = executeQueryForList(qs.compileSelect(), qs.getData());
        }
        return result;
    }

    /**
     * Execute query set
     * @param qs 
     * @return Hmap
     * @throws SQLException 
     */
    public Hmap getObject(QuerySet qs) throws SQLException, Exception {
        String key = qs.compileSelect() + qs.getData().toString();
        Hmap result;
        if (cacheEnabled) {
            Element e = getCache().get(key);
            if (e != null) {
                result = (Hmap) e.getObjectValue();
            } else {
                result = executeQueryForObject(qs.compileSelect(), qs.getData());
                Element resultCacheElement = new Element(key, result);
                getCache().put(resultCacheElement);
                storeCacheKeys(key, qs);
            }
        } else {
            result = executeQueryForObject(qs.compileSelect(), qs.getData());
        }
        return result;
    }

    /**
     * Execute query set
     * @param qs 
     * @return Hmap
     * @throws SQLException 
     */
    public Hmap getHmap(QuerySet qs) throws SQLException {
        return executeQueryForHmap(qs.compileSelect(), qs.getData());
    }

    /**
     * Execute an update query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    public int update(QuerySet qs) throws SQLException, Exception {
        int i = executeUpdate(qs.compileUpdate(), qs.getData());
        if (cacheEnabled) {
            invalidateCachedQueries(qs);
        }
        return i;
    }

    /**
     * Execute a delete query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    public int delete(QuerySet qs) throws SQLException, Exception {
        int  i = executeUpdate(qs.compileDelete(), qs.getData());
        if (cacheEnabled) {
            invalidateCachedQueries(qs);
        }
        return i;
    }

    /**
     * Count returned rows of a querySet
     * @param qs
     * @return
     * @throws java.sql.SQLException
     */
    public int count(QuerySet qs) throws SQLException, Exception {
       String key = qs.compileCount() + qs.getData().toString();
        Hmap result;
        if (cacheEnabled) {
            Element e = getCache().get(key);
            if (e != null) {
                result = (Hmap) e.getObjectValue();
            } else {
                result = executeQueryForObject(qs.compileCount(), qs.getData());
                Element resultCacheElement = new Element(key, result);
                getCache().put(resultCacheElement);
                storeCacheKeys(key, qs);
            }
        } else {
            result = executeQueryForObject(qs.compileCount(), qs.getData());
        }
        if (result.size() > 0) {
            return Integer.parseInt(result.get("count"));
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
    public String insert(QuerySet qs) throws SQLException, Exception {
        String res =  executeInsert(qs.compileInsert(), qs.getData());
       if (cacheEnabled) {
            invalidateCachedQueries(qs);
        }
        return res;
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
        if (debug) {
            Jmvc.dbDebug("SQL: " + sb.toString());
        }
        executeUpdate(sb.toString());
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
