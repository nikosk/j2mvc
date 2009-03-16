/*
 *  JmvcApplicationController.java
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

import gr.dsigned.jmvc.exceptions.CustomHttpException;
import gr.dsigned.jmvc.exceptions.CustomHttpException.HttpErrors;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.libraries.Input;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class JmvcApplicationController extends HttpServlet {

    private static boolean debug = Settings.get("DEBUG").equals("TRUE");
    private static LinkedHashMap<String, Class> controllerClasses;

    @Override
    public void init() throws ServletException {
        controllerClasses = (LinkedHashMap<String, Class>) getServletContext().getAttribute("controllerClasses");
    }

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (debug) {
            request.setAttribute("begin_time", System.nanoTime());
        } // keep time for debug purposes.
        request.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        String path = request.getRequestURI();
        try {
            Class c = controllerClasses.get(Input.getController(path).get("controller"));
            Controller o = (Controller) c.newInstance();
            request.setAttribute("controller_name", Input.getController(path).get("controller") + "_page");
            o.$.setEnvironment(request, response, this.getServletContext());
            Method m = c.getMethod(Input.getController(path).get("method"), new Class[0]);
            if (m.getModifiers() == java.lang.reflect.Modifier.PUBLIC) { // We only call public methods
                m.invoke(o, new Object[0]);
            } else {
                NoSuchMethodException e = new NoSuchMethodException("Method found but not callable for URI: " + path);
                Jmvc.logError("[Adapter] " + e.getMessage());
            }
        } catch (InstantiationException ie) {
            /*Something is wrong with the controller. Maybe the controller tried to
            do something weird in its constructor. We should display a 500 error page.*/
            Jmvc.logError("[Adapter] " + ie.getMessage());
            Jmvc.loadErrorPage(ie, response, this.getServletContext(), HttpErrors.E500);
        } catch (IllegalAccessException iae) {
            /* can't load controller. 500 server error.*/
            Jmvc.logError("[Adapter] " + iae.getMessage());
            Jmvc.loadErrorPage(iae, response, this.getServletContext(), HttpErrors.E500);
        } catch (NoSuchMethodException nsme) {
            /* 404 error. Found the controller but no method exists to handle the request.*/
            Jmvc.logError("[Adapter] " + nsme.getMessage());
            Jmvc.loadErrorPage(nsme, response, this.getServletContext(), HttpErrors.E404);
        } catch (InvocationTargetException ite) {
            /* This is the tricky part. This exception could be caused by a
            CustomHttpException thrown delibarately by the controller's invoked method */
            if (ite.getCause() instanceof CustomHttpException) {
                //CustomHttpException means the controller found an error and cannot
                //continue. Show an error page with a message.
                CustomHttpException theCause = (CustomHttpException) ite.getCause();
                Jmvc.logError("[Adapter] " + theCause.getMessage());
                Jmvc.loadErrorPage(theCause, response, this.getServletContext(), theCause.getErrorCode());
            } else {
                // This is an unchecked exception and we need to report it.
                Jmvc.logError(ite);
                Jmvc.loadErrorPage(ite, response, this.getServletContext(), HttpErrors.E500);
            }
        } catch (Exception e) {
            // This is a framework exception. Report it accordingly.
            Jmvc.logError("[Adapter] " + e.getMessage());
            Jmvc.loadErrorPage(e, response, this.getServletContext(), HttpErrors.E500);
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
