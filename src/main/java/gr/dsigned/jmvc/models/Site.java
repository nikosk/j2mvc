package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.db.Model;

import gr.dsigned.jmvc.db.QuerySet;
import gr.dsigned.jmvc.db.QuerySet.Operand;
import gr.dsigned.jmvc.db.QuerySet.OrderBy;
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
    
    public ArrayList<Hmap> getSites() throws Exception {
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
        qs.where("id", id, Operand.EQUALS);
        db.update(qs);        
    }

    public void deleteTest(String id) throws Exception {
        deleteById(id);
    }

    public LinkedHashMap insertTestQuerySets() throws Exception {
        return db.tableDef("sites");
    }
}