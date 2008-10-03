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

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/**
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class MysqlDB extends DB {

    private static final MysqlDB INSTANCE = new MysqlDB();
    private static MiniConnectionPoolManager poolMgr;

    /**
     * Creates a new instance of MysqlDB
     */
    private MysqlDB(){
        MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
        ds.setDatabaseName(Settings.get("DB_NAME"));
        ds.setServerName(Settings.get("DB_URL"));
        ds.setPort(Integer.valueOf(Settings.get("DB_PORT")) );
        ds.setUser(Settings.get("DB_USER"));
        ds.setPassword(Settings.get("DB_PASS"));
        ds.setAutoReconnect(true);
        ds.setAutoReconnectForConnectionPools(true);
        ds.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        poolMgr = new MiniConnectionPoolManager(ds, 1000);
    }

    public static MysqlDB getInstance() {
        return INSTANCE;
    }

    @Override
    public Connection getConn() throws SQLException {
        return poolMgr.getConnection();
    }

    @Override
    public void closeConn(Connection conn) throws SQLException {
        conn.close();
    }
}