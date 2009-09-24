/*
 *  MysqlDB.java
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
import java.sql.SQLException;

import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.ehcache.Cache;

/**
 * 
 * @author alkis
 */
public class MysqlDBTrans extends DB {

    Connection conn;
    DataSource ds;
    Cache cache;
    ArrayList<QuerySet> querySets = new ArrayList<QuerySet>();

    public MysqlDBTrans() throws Exception {

        Context initCtx;
        try {
            initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/" + Settings.get("DB_NAME"));
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            cache = MysqlDB.getInstance().getCache();
        } catch (NamingException ex) {
            Jmvc.logError(ex);
        }
    }

    @Override
    public Connection getConn() throws SQLException {
        return conn;
    }

    @Override
    public Cache getCache() throws Exception {
        return cache;
    }

    @Override
    public void closeConn(Connection conn) throws SQLException {
    }

    /**
     * Execute query set with no cache
     * @param qs
     * @return Hmap
     * @throws SQLException
     */
    @Override
    public Hmap getObject(QuerySet qs) throws SQLException, Exception {
        return executeQueryForObject(qs.compileSelect(), qs.getData());
    }

    /**
     * Execute query set
     * @param qs
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException
     */
    @Override
    public ArrayList<Hmap> getList(QuerySet qs) throws SQLException, Exception {
        return executeQueryForList(qs.compileSelect(), qs.getData());
    }

    /**
     * Inserts a row in the given table.
     * @param qs the queryset with the sql template and the data to insert
     * @return returns the the auto-generated key as a string
     * @throws SQLException
     */
    @Override
    public String insert(QuerySet qs) throws SQLException, Exception {
        querySets.add(qs);
        return executeInsert(qs.compileInsert(), qs.getData());
    }

    /**
     * Execute an update query set
     * @param qs
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException
     */
    @Override
    public int update(QuerySet qs) throws SQLException, Exception {
        querySets.add(qs);
        return executeUpdate(qs.compileUpdate(), qs.getData());
    }

    /**
     * Execute a delete query set
     * @param qs 
     * @return ArrayList<LinkedHashMap> of results
     * @throws SQLException 
     */
    @Override
    public int delete(QuerySet qs) throws SQLException, Exception {
        querySets.add(qs);
        return executeUpdate(qs.compileDelete(), qs.getData());
    }

    /*
     *
     **/
    public void commit() throws Exception {
        getConn().commit();
        getConn().close();
        if(Settings.get("CACHE_DB").equals("TRUE")) {
            for (QuerySet qs : querySets) {
                invalidateCachedQueries(qs);
            }
        }
    }

    public void rollback() throws Exception {
        getConn().rollback();
        getConn().close();
    }
}
