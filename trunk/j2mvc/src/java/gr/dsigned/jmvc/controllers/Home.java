/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.dsigned.jmvc.controllers;

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.models.Article;
import gr.dsigned.jmvc.renderers.BlogRenderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
        LinkedHashMap<String,String> data = new LinkedHashMap<String,String>(); // We'll pass this to the template
        ArrayList<LinkedHashMap<String,String>> posts = article.getLatestPosts(10); // Get data from model
        String output = "";
        for (LinkedHashMap<String,String> lhm : posts) {
            output = output + renderer.renderPost(lhm); // Render each post
        }
        data.put("head", output); // Include the output for parsing 
        //data.put("menu", renderer.getMenu());
        $.loadView("blog_frontpage", data);
    }
}
