/*
 *  Home.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
 * 
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.controllers;

import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.ValidationRules;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Renderer;
import gr.dsigned.jmvc.libraries.Form;
import gr.dsigned.jmvc.libraries.Form.HtmlControl;
import gr.dsigned.jmvc.libraries.Form.ValidationType;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.models.Issue;
import gr.dsigned.jmvc.models.Site;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Sites extends Controller {

    public Sites() throws Exception {
    }

    public void index() throws Exception {
        PageData data = new PageData();
        Site site = $.loadModel("Site"); // Load model
        Issue issue = $.loadModel("Issue");
        Renderer lr = $.loadRenderer("ListRenderer");
        LinkedHashMap<String,ArrayList<Bean>> issues = new LinkedHashMap<String,ArrayList<Bean>>();
        ArrayList<Bean> sites = site.getSites();  
        for(Bean s : sites){
            issues.put(s.get("label"), issue.getIssuesBySiteId(s.get("id")));
        }
        data.put("label", "");        
        data.put("content", lr.runMethod("renderLists", sites, issues));
        $.loadView("list", data);    
    }
    
    public void add_site() throws Exception {
        PageData data = new PageData();
        Site site = $.loadModel("Site"); // Load model
        if ($.input.post("label").isEmpty()) {
            data.put("label", "");
            data.put("action", "/sites/add_site/");
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form", data);
        } else {
            site.insertSite($.input.post("label"));
            $.response.sendRedirect($.input.post("redirect_to"));
        }
    }
    
    public void delete_site() throws Exception {
        String id = $.input.segment(2);
        Site site = $.loadModel("Site"); // Load model
        site.db.delete("sites", id);
        $.response.sendRedirect("/sites");
    }

    public void edit_site() throws Exception {
        String id = $.input.segment(2);
        PageData data = new PageData();
        Site siteModel = $.loadModel("Site"); // Load model        
        if ($.input.post("label").isEmpty()) {
            Bean site = siteModel.getById(id);
            data.put("id", id);
            data.put("action", "/sites/edit_site/"+id);
            data.put("label", site.get("label"));
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form", data);
        } else {
            siteModel.updateSite($.input.post("label"),id);
            $.response.sendRedirect($.input.post("redirect_to"));
        }
    }

    public void add_task() throws Exception {
        String id = $.input.segment(2);
        PageData data = new PageData();
        Issue issue = $.loadModel("Issue"); // Load model  
        if ($.input.post("label").isEmpty()) {
            data.put("id", id);
            data.put("label", "");
            data.put("action", "/sites/add_task/"+id);
            data.put("description", "");
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form_add_task", data);
        } else {
            issue.insertIssue(id, $.input.post("label"), $.input.post("description"));
            $.response.sendRedirect($.input.post("redirect_to"));
        }
    }
}
