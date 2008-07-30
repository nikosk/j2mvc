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

import static gr.dsigned.jmvc.forms.Field.Rule.*;
import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.forms.NewForms;
import gr.dsigned.jmvc.forms.fields.CharField;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.models.Issue;
import gr.dsigned.jmvc.models.Site;
import static gr.dsigned.jmvc.types.operators.*;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Forms extends Controller {

    public Forms() throws Exception {
    }

    public void index() throws Exception {
       $.response.sendRedirect("/forms/show_form");
    }

    public void show_form() throws Exception {
        NewForms f = new NewForms();

//        f.addField("label", FormElems.INPUT_TEXT, "required", "max_length:255");
//        f.addField("email", FormElems.HIDDEN, "required");
//        f.addField("domain", FormElems.TEXTAREA);
//        f.addField("redirect_to", FormElems.HIDDEN, "numeric");
        
        f.setFields( 
                new CharField("label",$.input.post("label"),o(REQUIRED,"true")),
                new CharField("email",$.input.post("email"),o(MAX_LENGTH,"255"), o(MIN_LENGTH,"123")),
                new CharField("textbox", "" , o(EMAIL,"true"))
        );

        PageData data = new PageData();
        data.put("form", f.build());
        $.loadView("testing_forms", data);
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
            data.put("action", "/sites/edit_site/" + id);
            data.put("label", site.get("label"));
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form", data);
        } else {
            siteModel.updateSite($.input.post("label"), id);
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
            data.put("action", "/sites/add_task/" + id);
            data.put("description", "");
            data.put("redirect_to", "/" + $.input.segment(0));
            $.loadView("list_form_add_task", data);
        } else {
            issue.insertIssue(id, $.input.post("label"), $.input.post("description"));
            $.response.sendRedirect($.input.post("redirect_to"));
        }
    }
}
