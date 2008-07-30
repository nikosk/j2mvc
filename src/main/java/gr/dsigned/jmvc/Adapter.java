/*
 *  Adapter.java
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
package gr.dsigned.jmvc;

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Utils;
import gr.dsigned.jmvc.libraries.Input;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Adapter extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(Adapter.class);

    /**
     * This is the entry point of your application. Given the URI of the request
     * it loads the appropriate controller and passes to it the appropriate
     * environment variables (The GET, POST and Context objects).
     * 
     * @todo the adapter does not load the correct method of the controller yet.
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    @SuppressWarnings("unchecked")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = request.getRequestURI();
            Class<Controller> c = (Class<Controller>) Class.forName("gr.dsigned.jmvc.controllers." + Utils.capitalize(Input.getController(path).get("controller")));
            gr.dsigned.jmvc.framework.Controller o = c.newInstance();
            o.$.setEnv(request, response, this.getServletContext());
            Method m = c.getMethod(Input.getController(path).get("method"), new Class[0]);
            if (m.getModifiers() != java.lang.reflect.Modifier.PRIVATE) { // We only call public methods
                m.invoke(o, new Object[0]);
            } else {
                Exception e = new Exception("Method not found");
                logger.error(e.getMessage());
                Jmvc.loadErrorPage(e, response, this.getServletContext());  // This should return 404
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            Jmvc.loadErrorPage(e, response, this.getServletContext());
        } 
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
