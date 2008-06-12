/**
 * 15 Μαρ 2008, gr.dsigned.jmvc.models 
 * Category.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
package gr.dsigned.jmvc.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import gr.dsigned.jmvc.db.Model;

/**
 * 
 */
public class Category extends Model {
	
	public Category() throws Exception{
		this.tableName=  "categories";		
	}
	
	public ArrayList<LinkedHashMap<String,String>> getCategories() throws SQLException{
		db.from(this.tableName);
		return db.get();
	}
}
