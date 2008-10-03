package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.Operand;
import gr.dsigned.jmvc.db.OrderBy;
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
    
    public ArrayList<Hmap> getSites() throws SQLException {
        QuerySet qs = new QuerySet();
        qs.from(tableName);
        qs.orderBy(OrderBy.DESC, "label");
        return db.getList(qs);
    }

    public String insertSite(String label) throws Exception {
        QuerySet qs = new QuerySet();
        qs.set("label", label);
        qs.set("status", "1");
        qs.insert(tableName);
        return db.insert(qs);
    }

    public void updateSite(String label, String id) throws Exception {
        QuerySet qs = new QuerySet();
        qs.update(tableName);
        qs.set("label", label);
        qs.where("id", id, Operand.EQUAL);
        db.update(qs);        
    }

    public void deleteTest(String id) throws SQLException {
        this.delete(id);
    }

    public LinkedHashMap insertTestQuerySets() throws SQLException {
        return db.tableDef("sites");
    }
}