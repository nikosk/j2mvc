package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.db.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
/**
*
* @author Nikosk <nikosk@dsigned.gr>
*/
public class User extends Model {
	
	public User() throws Exception{
		this.tableName = "users";
	}
	
	public boolean auth(String username, String password) throws Exception{
		// TODO Fix db to escape values properly
		db.where("username = '" + username + "'");
		db.where("password = '" + password + "'");
		db.from(this.tableName);
		ArrayList<LinkedHashMap<String,String>> al = db.get();
		return (al.size()>0) ? true:false;
	}
}
