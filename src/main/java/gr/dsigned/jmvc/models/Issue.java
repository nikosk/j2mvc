/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import java.util.ArrayList;
import static gr.dsigned.jmvc.db.Model.Operands;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Issue extends Model {

    public Issue() throws Exception {
        tableName = "issues";
    }
    
    public ArrayList<Bean> getIssuesBySiteId(String siteId) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("issues");
        qs.where("site_id",siteId, Operands.EQUAL.toString());
        qs.orderBy(OrderBy.DESC, "id");
        return db.getList(qs);
    }

    public String insertIssue(String siteId, String label, String description) throws Exception {
        QuerySet qs = new QuerySet();
        qs.set("site_id", siteId);
        qs.set("label", label);
        qs.set("description", description);
        qs.insert(tableName);
        return db.insert(qs);
    }
    
    public void updateIssue(String id, String label, String description) throws Exception {
        QuerySet qs = new QuerySet();
        qs.set("label", label);
        qs.set("description", description);
        qs.where("id", id, Operands.EQUAL.toString());
        qs.update(tableName);
        db.update(qs);
    }
}