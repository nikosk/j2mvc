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

import java.util.Enumeration;
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
    public HashMap<String, String> permHM = new HashMap<String, String>();
    public HashMap<String, String> tempHM = new HashMap<String, String>();

    public Session(HttpServletRequest req) {
        session = req.getSession(false);
        this.request = req;
        if (session != null) {
            Enumeration enames = session.getAttributeNames();
            while (enames.hasMoreElements()) {
                String name = (String) enames.nextElement();
                String value = "" + session.getAttribute(name);
                if (name.substring(0, 2).equals("t_")) {
                    tempHM.put(name, value);
                    session.removeAttribute(name);
                }
                else {
                    permHM.put(name, value);
                }
            }
        }
    }

    public String data(String paramName) {
        String out = "";
        if (tempHM.containsKey("t_"+paramName)) {
            out = tempHM.get("t_" + paramName);
        }
        else {
            out =  permHM.get(paramName);
        }
        return (out == null) ? "" : out ;
    }

    public void set(String key, String value) {
        if(this.session == null){
            session = this.request.getSession(true);
        }
        session.setAttribute(key, value);
        permHM.put(key, value) ;
    }

    public void setTemp(String key, String value) {
        session.setAttribute("t_" + key, value);
         tempHM.put(key, value) ;
    }

    public void setData(HashMap<String, String> data) {
        for (String key : data.keySet()) {
            this.session.setAttribute(key, data.get(key));
        }
    }
}
