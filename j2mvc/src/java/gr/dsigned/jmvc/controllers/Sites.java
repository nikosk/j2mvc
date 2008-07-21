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
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.models.Site;
import gr.dsigned.jmvc.renderers.BlogRenderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Sites extends Controller {

	public Sites() throws Exception{
		
	}
    public  void index() throws Exception{    	
        Site site = $.loadModel("Site"); // Load model
        $.input.post("email");
$.loadLibrary("TableRenderer");

        //        BlogRenderer renderer = $.loadRenderer("BlogRenderer"); //Load renderer
        LinkedHashMap<String,String> data = new LinkedHashMap<String,String>(); // We'll pass this to the template
//        ArrayList<LinkedHashMap<String,String>> posts = site.getSites(); // Get data from model
//        String output = "";
//        for (LinkedHashMap<String,String> lhm : posts) {
//            output = output + renderer.renderSitePost(lhm); // Render each post
//        }
//        data.put("head", output); // Include the output for parsing 
//        //data.put("menu", renderer.getMenu());
        String g = "";
        LinkedHashMap<String, String> map = site.insertTestQuerySets();
        for(String s:map.keySet())
        {
            
            g+= s + " " + map.get(s);
            
        }
        data.put("head", g);
        $.loadView("blog_frontpage", data);
    }
}
