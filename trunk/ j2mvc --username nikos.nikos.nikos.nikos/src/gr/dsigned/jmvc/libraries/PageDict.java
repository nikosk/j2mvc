/**
 * 12 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * PageDict.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Settings;

import java.util.LinkedHashMap;

/**
 * 
 */
public class PageDict extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5521489114124146406L;

	public PageDict() {
		put("title", "My title");
		put("keywords", "jmvc, framework, java, mvc");
		put("description", "Java mvc framework inspired by Code Igniter,Ruby on Rails and Django");
		put("charset", Settings.DEFAULT_ENCODING);
		/* Styles */
		put("styles-yui-all", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/reset.css\" >\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/grids.css\">"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/fonts.css\">");
		put("styles-yui-all-min", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/reset-fonts-grids.css\">");
		put("styles-yui-reset", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/reset.css\" >\r\n");
		put("styles-yui-grids", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/grids.css\">");
		put("styles-yui-fonts", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/yui/fonts.css\">");
		/* mootools */
		put("mootools", "<script type=\"text/javascript\" src=\"/js/mootools-release-1.11.js\"></script>");
		put("mootools-no-comp", "<script type=\"text/javascript\" src=\"/js/mootools-release-1.11-no-comp.js\"></script>");
	}
}
