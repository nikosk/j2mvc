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
package gr.dsigned.jmvc.servlets;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.JMVCGuiceModule;
import gr.dsigned.jmvc.framework.Router;
import gr.dsigned.jmvc.framework.interfaces.Action;
import gr.dsigned.jmvc.framework.interfaces.RedirectAction;
import gr.dsigned.jmvc.framework.interfaces.ViewAction;
import gr.dsigned.jmvc.listeners.JmvcInitializationListener;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transaction;

import com.google.inject.Guice;
import com.google.inject.Injector;

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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (debug) {
			request.setAttribute("begin_time", System.nanoTime());
		} // keep time for debug purposes.
		EntityManager em = JmvcInitializationListener.getEntityManager();
        try {
            Class c = Class.forName("gr.dsigned.guice.controllers.Controller");
            Injector injector = Guice.createInjector(new JMVCGuiceModule(em, request, response));
            Controller cont = injector.getInstance(Controller.class);
            Method m = cont.getClass().getMethod(request.getParameter("controller") == null ? "index" : request.getParameter("controller"));
            Action action = (Action) m.invoke(cont);
            if (action instanceof ViewAction) {
                ViewAction va = (ViewAction) action;
                request.setAttribute("model", va.getData());
                request.getRequestDispatcher(va.getTemplateName() + ".jsp").forward(request, response);
            } else if (action instanceof RedirectAction) {
                response.sendRedirect(((RedirectAction) action).getRedirectURL());
            }
        } catch (Exception ex) {
        	EntityTransaction et = em.getTransaction();
        	if(et.isActive()){
        		et.rollback();
        	}
            request.setAttribute("java.lang.Exception", ex);
            request.getRequestDispatcher(Settings.get("ERROR_PAGE")).forward(request, response);
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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
