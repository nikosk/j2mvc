package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.Bean;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author USER
 */
public class Site extends Model {

    public Site() throws Exception {
        this.tableName = "sites";
    }

    public ArrayList<Bean> getSites() throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from("sites");
        qs.orderBy("label", "DESC");
        return db.getList(qs);
    }

    public void insertSite(String label) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.insert("label", label);
        qs.insert("status", "1");
        db.insert(qs);
    }

    public void updateSite(String label) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.update("label", label);
        db.update(qs);        
    }

    public void insertTest(String id) throws SQLException {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.insert("id", id);
        qs.insert("label", id);
        qs.insert("status", "1");
        db.insert(qs);                
    }

    public void deleteTest(String id) throws SQLException {
        this.delete(id);
    }

    public LinkedHashMap insertTestQuerySets() throws SQLException {
        return db.tableDef("sites");
    }
}