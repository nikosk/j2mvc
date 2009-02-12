package gr.dsigned.jmvc;


import gr.dsigned.jmvc.annotations.NotPublic;
import gr.dsigned.jmvc.exceptions.CustomHttpException;
import gr.dsigned.jmvc.exceptions.CustomHttpException.HttpErrors;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Utils;
import gr.dsigned.jmvc.libraries.Input;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nikos
 */
public class Adapter extends HttpServlet {

    private HttpServletResponse response;
    private HttpServletRequest request;
    private String path;
    private static boolean debug = Settings.get("DEBUG").equals("TRUE");
    private boolean requestCompleted = false;
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        request = req;
        response = resp;
        if (debug) {
            request.setAttribute("begin_time", System.nanoTime());
        } // keep time for debug purposes.
        request.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        path = request.getRequestURI();
        processRequest();
    }

    /**
     * Will try to find a controller class with the name 
     * specified in the url. If the class exists it will 
     * find and execute the method specified in the URI.
     * 
     */
    private void processRequest() {
        try {
            Class<Controller> c = (Class<Controller>) Class.forName(Settings.get("SYSTEM_PACKAGE") + ".controllers." + Utils.capitalize(Input.getController(path).get("controller")));
            gr.dsigned.jmvc.framework.Controller o = c.newInstance();
            o.$.setEnvironment(request, response, this.getServletContext());
            Method m = c.getMethod(Input.getController(path).get("method"), new Class[0]);
            if (m.getModifiers() == java.lang.reflect.Modifier.PUBLIC) { // We only call public methods
                m.invoke(o, new Object[0]);
            } else {
                NoSuchMethodException e = new NoSuchMethodException("Method found but not callable for URI: " + this.path);
                Jmvc.logError("[Adapter] " + e.getMessage());
            }
            // No exceptions. The request has been served. Set the flag accordingly.
            requestCompleted = true;
        } catch (ClassNotFoundException e) {
            /* The class was missing or wrong request. We need to see 
             * if the RequestHandler can handle it so we do nothing.*/
            this.processReroutedRequest();
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
                Jmvc.logError("[Adapter] " + ite.getMessage());
                Jmvc.loadErrorPage(ite, response, this.getServletContext(), HttpErrors.E500);
            }
        } catch (Exception e) {
            // This is a framework exception. Report it accordingly.
            Jmvc.logError("[Adapter] " + e.getMessage());
            Jmvc.loadErrorPage(e, response, this.getServletContext(), HttpErrors.E500);
        }
    }

    /**
     * This method is called only when a standard controller is not found
     * to process the request.
     */
    private void processReroutedRequest() {
        try {
            Class<Controller> c = (Class<Controller>) Class.forName("com.phiresoft.social.controllers.CMSRequestHandler");
            gr.dsigned.jmvc.framework.Controller o = c.newInstance();
            o.$.setEnvironment(request, response, this.getServletContext());
            ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
            Method m = c.getMethod(pathParts.get(1), new Class[0]);
            boolean ut = m.isAnnotationPresent(NotPublic.class);
            if (!ut) {
                m.invoke(o, new Object[0]);
            } else {
                Jmvc.logError("Method call call of NotPublic method");
                throw new java.lang.NoSuchMethodException("That method is not public");
            }
        } catch (ClassNotFoundException e) {
            /* The class was missing or wrong request. We need to see 
             * if the RequestHandler can handle it so we do nothing.*/
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
                Jmvc.logError("[Adapter] " + ite.getMessage());
                Jmvc.loadErrorPage(ite, response, this.getServletContext(), HttpErrors.E500);
            }
        } catch (Exception e) {
            // This is a framework exception. Report it accordingly.
            Jmvc.logError("[Adapter] " + e.getMessage());
            Jmvc.loadErrorPage(e, response, this.getServletContext(), HttpErrors.E500);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
