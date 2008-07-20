/*
 *  Session.java
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

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Library;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 09 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * Session.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Session extends Library {
	HttpServletRequest request;
	HttpSession session;
	
	public Session(HttpServletRequest req){
		this.session = req.getSession();
		this.session.setMaxInactiveInterval(Settings.SESSION_EXPIRY);
	}
	
	public String data(String paramName){
		return (session.getAttribute(paramName) == null) ? "" :session.getAttribute(paramName).toString();
	}
	public void set(String key, String value){
		session.setAttribute(key, value);
	}
	public void setData(HashMap<String,String> data){
		for(String key : data.keySet()){
			this.session.setAttribute(key, data.get(key));
		}
	}
}
