/*
 *  Session.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.helpers;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Library;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * 09 Μαρ 2008, gr.dsigned.jmvc.libraries
 * 
 * A wrapper for HttpSession that adds the functionality of
 * adding attributes for 1 request only (Flash data).
 * 
 * Session.java
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vassilki Chryssikou <vchrys@gmail.com>
 */
public class Session extends Library implements HttpSession {

    private HttpServletRequest request;
    private HttpSession session;
    public HashMap<String, Object> permHM = new HashMap<String, Object>();
    public HashMap<String, Object> tempHM = new HashMap<String, Object>();

    public Session(HttpServletRequest req) {
        session = req.getSession(false);
        this.request = req;
        if (session != null) {
            Enumeration enames = session.getAttributeNames();
            while (enames.hasMoreElements()) {
                String name = (String) enames.nextElement();
                String value = "" + session.getAttribute(name);
                if (name.substring(0, 2).equals("t_")) {
                    tempHM.put(name.substring(2), value);
                    session.removeAttribute(name);
                } else {
                    permHM.put(name, value);
                }
            }
        }
    }

    /**
     * Method that gets a paramName and returns its value from the appropriate hashmap
     * @param paramName
     * @return (string) value of the parameter given 
     */
    @Deprecated
    public String data(String paramName) {
        String out = "";
        if (tempHM.containsKey(paramName)) {
            out = tempHM.get(paramName).toString();
        } else if(permHM.containsKey(paramName)){
            out = permHM.get(paramName).toString();
        }
        return (out == null) ? "" : out;
    }

    public Object get(String paramName) {        
        if (tempHM.containsKey(paramName)) {
            return tempHM.get(paramName);
        } else {
            return permHM.get(paramName);
        }        
    }

    /**
     * Method that sets a permanant attribute to the session and to the appropriate hashmap.
     * If the session is already created
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        if (this.session == null) {
            session = this.request.getSession(true);
            String expiry = Settings.get("SESSION_EXPIRY");
            if (!expiry.isEmpty()) {
                session.setMaxInactiveInterval(Integer.parseInt(expiry));
            }
        }
        session.setAttribute(key, value);
        permHM.put(key, value);
    }

    /**
     * Method that sets a temporary attribute to the session and to the appropriate hashmap
     * @param key
     * @param value
     */
    public void setTemp(String key, String value) {
        if (this.session == null) {
            session = this.request.getSession(true);
            String expiry = Settings.get("SESSION_EXPIRY");
            if (!expiry.isEmpty()) {
                session.setMaxInactiveInterval(Integer.parseInt(expiry));
            }
        }
        session.setAttribute("t_" + key, value);
        tempHM.put(key, value);
    }

    /**
     * invalidates session and initialise parametres
     */
    @Override
    public void invalidate() {
        if (session != null) {
            session.invalidate();
            session = null;
        }
        permHM.clear();
        tempHM.clear();
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    @Override
    @Deprecated
    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException("Deprecated");
    }

    /**
     * Overrides the default behaviour to return
     * either permanent data or temporary.
     * @param name
     * @return
     */
    @Override
    public Object getAttribute(String name) {
        return this.data(name);
    }

    @Override
    @Deprecated
    public Object getValue(String name) {
        throw new UnsupportedOperationException("Deprecated");
    }

    @Override
    public Enumeration getAttributeNames() {
        return Collections.enumeration(this.permHM.keySet());
    }

    @Override
    @Deprecated
    public String[] getValueNames() {
        throw new UnsupportedOperationException("Deprecated");
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.set(name, value);
    }

    @Override
    @Deprecated
    public void putValue(String name, Object value) {
        throw new UnsupportedOperationException("Deprecated");
    }

    @Override
    public void removeAttribute(String name) {
        if (this.permHM.containsKey(name)) {
            this.permHM.remove(name);
        } else {
            this.session.removeAttribute(name);
        }
    }

    @Override
    @Deprecated
    public void removeValue(String name) {
        throw new UnsupportedOperationException("Deprecated");
    }

    @Override
    public boolean isNew() {
        return this.session.isNew();
    }
}
