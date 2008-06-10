/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.models 
 * Article.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
package gr.dsigned.jmvc.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import gr.dsigned.jmvc.db.Model;

/**
 * 
 */
public class Article extends Model {
	public Article() throws Exception{
        this.tableName = "articles";
    }
	
	public ArrayList<LinkedHashMap<String,String>> getLatestPosts(int numberToFetch)  throws SQLException{
        db.from("articles");
        db.join("categories", "categories.id = articles.category_id", "left");
        db.where("categories.name = 'basket'");
        db.orderBy("published", "DESC");
        db.limit(numberToFetch);
        return db.get();
    }
	
	
	public ArrayList<LinkedHashMap<String,String>> getPostsInInterval(Date from, Date to) throws SQLException{
        db.from("articles");
        db.orderBy("published", "DESC");
        return db.get();
    }
	
	public ArrayList<LinkedHashMap<String,String>> getArticlesByCat(String cat, int limit, int offset) throws SQLException{
		db.from("articles");
		db.orderBy("published", "DESC");
		db.limit(offset, limit);
		db.join("categories", "Articles.category_id = Categories.id", "left");
		db.where("Categories.name = '" + cat + "'");
		return db.get();
	}
	public int countArticlesByCat(String cat) throws SQLException{
		db.from("articles");
		db.orderBy("published", "DESC");		
		db.join("categories", "Articles.category_id = Categories.id", "left");
		db.where("Categories.name = '" + cat + "'");
		return db.count();
	}
}
