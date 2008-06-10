package gr.dsigned.jmvc.renderers;

import gr.dsigned.jmvc.framework.Renderer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class BlogRenderer extends Renderer {

	private boolean	showAuthor		= true;
	private boolean	withReadMore	= true;

	public String renderPost(LinkedHashMap<String, String> rawPost) throws Exception {
		String out = "<div class='blog_post'>" + "\n";
		out += "\t<a href='edit_article/'"+rawPost.get("id") +">" + rawPost.get("title") + "</a>" + "\n";
		out += (showAuthor) ? "\t<span class='pub_date'>" + rawPost.get("published") + "</span>" + "\n" : "";
		out += "\t<div class='post_body'>";
		if (withReadMore && rawPost.get("content").length() > 200) {
			out += "\n\t<div class='lead_in'>" + rawPost.get("content").substring(0, 199) + "...</div>" + "\n";
		} else {
			out += "\n\t<div class='post_text'>" + rawPost.get("content") + "</div>" + "\n";
		}
		out += "\t</div>" + "\n";
		out += "</div>" + "\n";
		return out;
	}
	/**
	 * Renders the article titles in the admin page. Be careful because it the 
	 * data contains fields from the categories table also. In case of a field name clash
	 * use [table_name].[field_name] notation.
	 * @param rawPost ResultData
	 * @return Html: the html block with a article title and a link to the article's edit page.
	 * @throws Exception In case something goes wrong the exception bubbles up and is displayed on an error page.
	 */
	public String renderArticleTitles(LinkedHashMap<String, String> rawPost) throws Exception {
		String out = "<div class='blog_post'>" + "\n";
		out += "<a href='/admin/edit_article/'"+rawPost.get("id") +">" + rawPost.get("title") + "</a>" + "\n";
		out += (showAuthor) ? "<span class='pub_date'>" + rawPost.get("published") + "</span>" + "\n" : "";		
		out += "</div>" + "\n";		
		return out;
	}
	public String renderMenu(LinkedHashMap<String, String> menuElements) throws Exception {
		String out = "<ul>";
		for (String title : menuElements.keySet()) {
			out += String.format("<li>%1$s</li>", anchor(menuElements.get(title), title, "class='menu_link'"));
		}
		
		return out;
	}

	public String buildMenu(String controller , String method, ArrayList<LinkedHashMap<String, String>> rawCats) throws Exception {
		String out = "<ul>";
		for(LinkedHashMap<String,String> row : rawCats){
			out += "\n" + "<a href='/"+controller+ "/"+method+ "/" + row.get("name")+"' >"  +  row.get("display_name")+ "</a>";
		}
		out += "</ul>";
		return out;
	}
}
