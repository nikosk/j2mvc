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
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.renderers.BlogRenderer;

import java.util.ArrayList;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Home extends Controller {

	public Home() throws Exception{
		
	}
    public  void index() throws Exception{    	
        Article article = $.loadModel("Article"); // Load model
        BlogRenderer renderer = $.loadRenderer("BlogRenderer"); //Load renderer
        Hmap data = new Hmap(); // We'll pass this to the template
        ArrayList<Hmap> posts = article.getLatestPosts(10); // Get data from model
        String output = "";
        for (Hmap lhm : posts) {
            output = output + renderer.renderPost(lhm); // Render each post
        }
        data.put("head", output); // Include the output for parsing 
        //data.put("menu", renderer.getMenu());
        $.loadView("blog_frontpage", data);
    }
}
