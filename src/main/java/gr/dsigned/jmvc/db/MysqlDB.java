/*
 *  MysqlDB.java
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
import gr.dsigned.jmvc.framework.Jmvc;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class MysqlDB extends DB {

    private static final MysqlDB INSTANCE = new MysqlDB();
   
    DataSource ds;
    Cache cache;
    /**
     * Creates a new instance of MysqlDB
     */
    private MysqlDB() {
        
        Context initCtx;
        try {
            initCtx = new InitialContext();            
            Context envCtx = (Context) initCtx.lookup("java:comp/env");            
            ds = (DataSource) envCtx.lookup("jdbc/"+Settings.get("DB_NAME"));
        } catch (NamingException ex) {
            Jmvc.logError(ex);
        }
        //poolMgr = new MiniConnectionPoolManager(ds, 255, 20);
        if (cacheEnabled) {
            CacheManager singletonManager = CacheManager.create();
            Cache memoryOnlyCache = new Cache("dbCache", 100, false, true, 86400, 86400);
            singletonManager.addCache(memoryOnlyCache);
            cache = singletonManager.getCache("dbCache");
        }
    }

    public static MysqlDB getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConn() throws SQLException {
        return ds.getConnection();//poolMgr.getConnection();
    }

    @Override
    public void closeConn(Connection conn) throws SQLException {
        conn.close();
    }
    
    @Override
    public Cache getCache() throws Exception {
        return cache;
    }
}
