/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.Bean;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import java.util.ArrayList;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Issue extends Model {

    public Issue() throws Exception {
        this.tableName = "issues";
    }

    public ArrayList<Bean> getBySiteId(String siteId) throws Exception {
        QuerySet qs = new QuerySet();
        qs.from("issues");
        qs.where("site_id = " + siteId);
        qs.orderBy("id", "ASC");
        return db.get(qs);
    }

    public void insertIssue(String siteId, String label, String description) throws Exception {
        this.data.put("site_id", siteId);
        this.data.put("label", label);
        this.data.put("description", description);
        this.store();
    }
}
