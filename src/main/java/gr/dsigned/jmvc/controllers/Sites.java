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

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Renderer;
import gr.dsigned.jmvc.libraries.PageDict;
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
        PageDict data = new PageDict();
        Site site = $.loadModel("Site"); // Load model
        Issue issue = $.loadModel("Issue");
        Renderer lr = $.loadRenderer("ListRenderer");
        LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>> issues = new LinkedHashMap<String,ArrayList<LinkedHashMap<String,String>>>();
        LinkedHashMap<String, String> sitesNissues = new LinkedHashMap<String, String>();
        ArrayList<LinkedHashMap<String, String>> sites = site.getSites();  
        for(LinkedHashMap<String, String> s : sites){
            issues.put(s.get("label"), issue.getBySiteId(s.get("id")));
        }
        data.put("label", "");        
        data.put("content", lr.runMethod("renderLists", sites, issues));
        $.loadView("list", data);
    }

    public void add_site() throws Exception {
        PageDict data = new PageDict();
        Site site = $.loadModel("Site"); // Load model
        Renderer lr = $.loadRenderer("ListRenderer");
        ArrayList<LinkedHashMap<String, String>> sites = site.getSites();
        if ($.input.post("label").isEmpty()) {
            data.put("label", "");
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
        PageDict data = new PageDict();
        Site site = $.loadModel("Site"); // Load model        
        Renderer lr = $.loadRenderer("ListRenderer");
        ArrayList<LinkedHashMap<String, String>> sites = site.getSites();
        if ($.input.post("label").isEmpty()) {
            site.load(id);
            data.put("label", site.data.get("label"));
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form", data);
        } else {
            site.updateSite($.input.post("label"));
            $.response.sendRedirect($.input.post("redirect_to"));
        }
    }

    public void add_task() throws Exception {
        String id = $.input.segment(2);
        PageDict data = new PageDict();
        Site site = $.loadModel("Site"); // Load model  
        Issue issue = $.loadModel("Issue"); // Load model  
        if ($.input.post("label").isEmpty()) {
            data.put("id", id);
            data.put("label", "");
            data.put("description", "");
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form_add_task", data);
        } else {
            issue.insertIssue(id, $.input.post("label"), $.input.post("description"));
            $.response.sendRedirect($.input.post("redirect_to"));
        }
    }
}
