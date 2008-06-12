/**
 * 16 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * Pagination.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.framework.Library;

/**
 * 
 */
public class Pagination extends Library {

	public String	baseUrl	= "";
	public int		totalRows	= 0;
	public int		perPage	= 10;	
	public int		curPage	= 0;


	public String createLinks(int currentPage) {
		String out = "";
		if (totalRows < perPage || perPage == 0) {
			return "";
		}
		int pageCount = Math.round(totalRows / perPage);
		if(pageCount == 0){
			return "";
		}		
		if(!baseUrl.endsWith("/")) baseUrl += "/";
		for (int i = 0; i <= pageCount; i++) {
			if(i != currentPage){
				if(i != 0){
					out += "<a href='" + baseUrl + i + "' >" + (i+1) + "</a> | ";
				} else {
					out += "<a href='" + baseUrl + "' >" + (i+1) + "</a> | ";
				}
			} else {
				out += "<b>"+(i+1)+"</b> | ";
			}
		}
		return out;
	}
	
}
