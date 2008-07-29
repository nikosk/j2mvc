/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.db.QuerySet;
import java.sql.SQLException;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Configuration extends Model{
    public Configuration() throws Exception {
        this.tableName = "configuration";
    }
    /**
     * Retrieves an option from the database. This method is used
     * by the CMS platform to retrieve configuration for 
     * @param optionName
     * @param confName
     * @return
     * @throws java.sql.SQLException
     */
    public String getOption(String optionName, String confName) throws SQLException{
        QuerySet qs = new QuerySet();
        qs.from("configuration");
        qs.where("option_name = " + optionName);
        if(confName != null){ // If no configuration name is given use global
            qs.where("configuration_name = global");
        }
        qs.select("option_name");
        return db.get(qs).get(0).get(optionName);
    }
}
