/*
 *  Pagination.java
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
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.framework.Library;

/**
 * 16 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * Pagination.java
 * @author Nikosk <nikosk@dsigned.gr>
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
