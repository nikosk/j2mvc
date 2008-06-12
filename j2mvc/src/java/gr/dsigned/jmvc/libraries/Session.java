/**
 * 09 Μαρ 2008, gr.dsigned.jmvc.libraries 
 * Session.java
 * @author Nikosk <nikosk@dsigned.gr>
 */
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Library;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


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
