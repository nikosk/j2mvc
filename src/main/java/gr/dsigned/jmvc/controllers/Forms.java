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

import gr.dsigned.jmvc.types.Hmap;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.forms.NewForms;
import gr.dsigned.jmvc.forms.fields.SubmitButton;
import gr.dsigned.jmvc.forms.fields.DropdownMenu;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.models.Issue;
import gr.dsigned.jmvc.models.Site;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 * @author VChrys <vchrys@gmail.com>
 */
public class Forms extends Controller {

    public Forms() throws Exception {
    }

    public void index() throws Exception {
       $.response.sendRedirect("/forms/show_form");
    }

    public void show_form() throws Exception {
        PageData data = new PageData();
        NewForms f = new NewForms();
        DropdownMenu dd ;
        f.setFields( 
                //dd = new DropdownMenu("Category","category",$.input.post("category"),o(REQUIRED,"true")),
                //new CharField("Date","date",$.input.post("date"),o(DATE,"yyyy-MM-dd")),
               // new CharField("Real Title", "real_title",$.input.post("real_title")),
                //new CharField("Sub Title", "sub_title",$.input.post("sub_title")),
                //new CharField("Lead In","lead_in",$.input.post("lead_in")),
                //new TextareaField("Content","content", "5", "20",$.input.post("content"), /*o(MAX_LENGTH,"4"), o(MIN_LENGTH,"2"), o(REQUIRED,"true")*/o(NUMERIC,"123")),
//                new PasswordField("password",$.input.post("password"),o(REQUIRED,"true") ,o(MAX_LENGTH,"255"), o(MIN_LENGTH,"123")),
//                new CharField("email",$.input.post("email"),o(REQUIRED,"true") ,o(MAX_LENGTH,"255"), o(EMAIL,"123")),
                //new FileField("Upload Image", "image",new File("/"),false),
//                new RadioButton("Mr", "gender","1",$.input.post("gender"),"checked",o(REQUIRED,"true") ),
//                new RadioButton("Mrs","gender","2",$.input.post("gender"),"" ),
//                new Checkbox("Terms & Conditions","terms","1",$.input.post("terms"),"",o(REQUIRED,"true")),
                new SubmitButton("submit_button")
                //new ButtonField("button", "sth"),
                //new ResetButtonField("reset_button", "")
        );
       // dd.addOption(new DropdownOption("sths", "sths" ,"")) ;
      //  dd.addOption(new DropdownOption("sthelse", "sthelse" , "selected"));
       // dd.addOption(new DropdownOption("ok", "ok" , ""));
                
        if($.input.getRequest().getMethod().equalsIgnoreCase("post") && f.isValid()){
            data.put("form", "success");
        } else {
            String form = "<form action='/forms/show_form' method='post' enctype='multipart/form-data'>";
            form += f.build();
            form += "</form>";
            data.put("form", form);
            data.put("title", "Form Testing");
        }
        
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
            Hmap site = siteModel.getById(id);
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
