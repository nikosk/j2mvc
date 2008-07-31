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

    public Bean insertIssue(String siteId, String label, String description) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.insert("site_id", siteId);
        qs.insert("label", label);
        qs.insert("description", description);
        return db.insert(qs);
    }
    
    public void updateIssue(String id, String label, String description) throws Exception {
        QuerySet qs = new QuerySet();
        qs.table(tableName);
        qs.update("label", label);
        qs.update("description", description);
        qs.where("id", id, Operands.EQUAL.toString());
        db.update(qs);
    }
}