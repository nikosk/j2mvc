/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

	private static MysqlDB	instance	= null;
	MiniConnectionPoolManager poolMgr;
	
	//private String			url			= buildURL();
	
	/**
	 * Creates a new instance of MysqlDB
	 */
	private MysqlDB() throws Exception {
		init();
	}

	private void init() throws Exception {	
		MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
		ds.setDatabaseName(Settings.DB_NAME);
		ds.setServerName (Settings.DB_URL);
		ds.setPort(Settings.DB_PORT);
		ds.setUser (Settings.DB_USER);
		ds.setPassword (Settings.DB_PASS);
		ds.setAutoReconnect(true);
		ds.setCharacterEncoding(Settings.DEFAULT_ENCODING);		
		poolMgr = new MiniConnectionPoolManager(ds,20);		
	}
	
	public Connection getConn() throws SQLException{
		conn = poolMgr.getConnection();		
    	return conn;    	
    }
    public void closeConn() throws SQLException{
    	conn.close();
    }
    
	public static MysqlDB getInstance() throws Exception {
		if (instance == null) {
			instance = new MysqlDB();
		}		
		return instance;		
	}	
}
