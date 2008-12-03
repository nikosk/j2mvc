package gr.dsigned.jmvc;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.annotations.NotPublic;
import gr.dsigned.jmvc.exceptions.CustomHttpException;
import gr.dsigned.jmvc.exceptions.CustomHttpException.HttpErrors;
import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.framework.Utils;
import gr.dsigned.jmvc.libraries.Input;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.ehcache.Element;

/**
 *
 * @author nikos
 */
public class Adapter extends HttpServlet {

    private boolean debug = !Settings.get("DEBUG").isEmpty();
    private static final boolean cacheEnabled = Settings.get("CACHE_PAGES").equals("TRUE");

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        if (debug) {
            request.setAttribute("begin_time", System.nanoTime()); // keep request processing start time
        }
        String path = request.getRequestURI();
        String cacheKey = path;
        Element el = null;
        if (cacheEnabled) {            
            el = Jmvc.getCache().get(cacheKey);
        }
        if (el != null) {
            response.getWriter().println((String) el.getObjectValue());
            response.getWriter().println(Jmvc.getDebugInfo());
        } else {
            try {
                Class<Controller> c = (Class<Controller>) Class.forName(Settings.get("SYSTEM_PACKAGE") + ".controllers." + Utils.capitalize(Input.getController(path).get("controller")));
                gr.dsigned.jmvc.framework.Controller o = c.newInstance();
                o.$.setEnvironment(request, response, this.getServletContext());
                Method m = c.getMethod(Input.getController(path).get("method"), new Class[0]);
                if (m.getModifiers() == java.lang.reflect.Modifier.PUBLIC) { // We only call public methods
                    m.invoke(o, new Object[0]);
                } else {
                    Exception e = new Exception("Method not found"); //@TODO Add a new Exception class in JMVC
                    Jmvc.logError("[Adapter] " + e.getMessage());
                    Jmvc.loadErrorPage(e, response, this.getServletContext(), HttpErrors.E404);  // This should return 404
                }
            } catch (ClassNotFoundException e) { // We did not find a controller 
                // Lets try the using the cms requesthandler
                try {
                    // Get the controller
                    Class<Controller> c = (Class<Controller>) Class.forName("gr.dsigned.jmvc.controllers.RequestHandler");
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
                } catch (CustomHttpException che) {
                    Jmvc.logError("Custom Exception thrown.");
                    Jmvc.loadErrorPage(che, response, this.getServletContext(), che.getErrorCode());
                } catch (Exception innerException) {
                    Jmvc.logError("[Adapter] " + e.toString() + " " + innerException.toString());
                    Jmvc.loadErrorPage(innerException, response, this.getServletContext(), HttpErrors.E404);
                }
            } catch (Exception genEx) {
                Jmvc.logError("[Adapter] " + genEx.toString() + " cause: " + genEx.getCause());
                Jmvc.loadErrorPage(genEx, response, this.getServletContext(), HttpErrors.E500);
            }
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
