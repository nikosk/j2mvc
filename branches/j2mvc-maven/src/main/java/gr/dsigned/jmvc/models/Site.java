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
        return db.get(qs);
    }

    public void insertSite(String label) throws Exception {
        this.data.put("label", label);
        this.data.put("status", "1");
        this.store();
    }

    public void updateSite(String label) throws Exception {
        this.data.put("label", label);
        this.store();
    }

    public void insertTest(String id) throws SQLException {
        this.data.put("id", id);
        this.data.put("label", id);
        this.data.put("status", id);
        this.store();
    }

    public void deleteTest(String id) throws SQLException {
        this.delete(id);
    }

    public LinkedHashMap insertTestQuerySets() throws SQLException {
        return db.tableDef("sites");
    }
}