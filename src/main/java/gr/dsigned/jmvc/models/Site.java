
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author USER
 */
public class Site extends Model {
    
    public Site() throws Exception {
        this.tableName = "sites";
    }
    
    public ArrayList<LinkedHashMap<String, String>> getSites() throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("sites");
        //db.join("", "categories.id = articles.category_id", "left");
        //db.where("categories.name = 'basket'");
        qs.orderBy("id", "DESC");
        //db.limit(numberToFetch);
    return db.get(qs);
    } 

    public void insertTest(LinkedHashMap<String, String> hashMap) throws SQLException {
       this.data.put("id", "111");
       this.data.put("url", "");
       this.data.put("", "");
       this.store();
    } 
     public LinkedHashMap insertTestQuerySets() throws SQLException {
      return db.tableDef("sites");
    } 
}