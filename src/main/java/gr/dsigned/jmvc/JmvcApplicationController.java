/*
 *  JmvcApplicationController.java
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
package gr.dsigned.jmvc;

import gr.dsigned.jmvc.annotations.Implementation;
import gr.dsigned.jmvc.exceptions.CustomHttpException.HttpErrors;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Router;

import gr.dsigned.jmvc.framework.Template;
import gr.dsigned.jmvc.listeners.JmvcInitializationListener;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    private Router router;

    @Override
    public void init() throws ServletException {
        router = (Router) getServletContext().getAttribute("router");
    }

    /**
     * This is the entry point of your application. Given the URI of the request
     * it loads the appropriate controller and passes to it the appropriate
     * environment variables (The GET, POST and Context objects).
     *      
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
        EntityManager em = JmvcInitializationListener.entityManager();
        request.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        String path = request.getRequestURI();
        try {
            Class c = router.getControllerClassByReqURI(path);
            Method m = router.getMethodClassByReqURI(path);
            Controller o = (Controller) c.newInstance();
            o.$.setEnvironment(request, response, this.getServletContext());
            request.setAttribute("controller_name", router.getControllerName(path) + "_page");
            Class[] paramClasses = m.getParameterTypes();
            Annotation[][] paramAnnotations = m.getParameterAnnotations();
            Object[] paramInst = new Object[paramClasses.length];
            for (int i = 0; i < paramClasses.length; i++) {
                if (paramAnnotations[i][0] instanceof Implementation) {
                    Implementation a = (Implementation) paramAnnotations[i][0];
                    Class param = a.value();
                    paramInst[i] = param.getDeclaredConstructor(EntityManager.class).newInstance(em);                    
                }
            }
            Template t = (Template) m.invoke(o, paramInst);
            if (t != null) {
                request.setAttribute("template", t);
                request.getRequestDispatcher("/views/" + t.getViewname() + ".jsp").forward(request, response);
            }
        } catch (Exception e) {
            final EntityTransaction transaction = em.getTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Jmvc.logError(e);
            Jmvc.loadErrorPage(e, response, this.getServletContext(), HttpErrors.E500);
        } finally {
            em.close();
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
