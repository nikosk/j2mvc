package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Bean;
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
        tableName = "sites";
    }
    
    public ArrayList<Bean> getSites() throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.orderBy("label", "DESC");
        return db.getList(qs);
    }

    public Bean insertSite(String label) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.insert("label", label);
        qs.insert("status", "1");
        return db.insert(qs);
    }

    public void updateSite(String label, String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.update("label", label);
        qs.where("id", id, Operands.EQUAL.toString());
        db.update(qs);        
    }

    public void deleteTest(String id) throws SQLException {
        this.delete(id);
    }

    public LinkedHashMap insertTestQuerySets() throws SQLException {
        return db.tableDef("sites");
    }
}