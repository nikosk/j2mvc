/*
 *  Jmvc.java
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
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.exceptions.HttpException.HttpErrors;
import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.db.DB;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.helpers.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;

/**
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Jmvc {

    private static final Logger infoLogger = Logger.getLogger("Info");
    private static final Logger debugLogger = Logger.getLogger("Debug");
    private static final Logger errorLogger = Logger.getLogger("Error");
    
    private boolean debug = Settings.get("DEBUG").equals("TRUE");
    private boolean showDebugLog = Settings.get("DEBUG_LOG").equals("TRUE");
    private static ArrayList<String> dbDebug = null;

    private HttpServletRequest request;
    private ServletContext context;
    private HttpServletResponse response;

    public static HashMap<String, View> parsedViews = null;
    /*
     * These are the default auto-loaded libraries To load
     * others use Jmvc.loadLibrary()
     */
    public DB db;
    public JMVCRequest input;
    private Session session;

    public Jmvc() {
    }
    public Jmvc(HttpServletRequest req, HttpServletResponse resp, ServletContext cont) throws Exception {
        setEnvironment(req, resp, cont);
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Session getSession() {
        return session;
    }

    /**
     * Returns the request we are handling currently
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Sets the environment environment
     * @param req
     * @param resp
     * @param cont
     */
    private void setEnvironment(HttpServletRequest req, HttpServletResponse resp, ServletContext cont) throws IOException, FileUploadException {
        request = req;
        response = resp;
        context = cont;
        input = new JMVCRequest(request);
        session = new Session(req);
        req.setAttribute("sessionObj", session);
    }

    /**
     * Loads a template and replaces tags with the variables
     * stored in the HashMap. The key is used to find the
     * tag in the template.
     * This method is deprecated in favor of use of JSPs.
     * Users of this method should use a Template return
     * in controller methods.
     * 
     * @param view_name
     *            The name of the template (the path and
     *            extension is added automatically)
     * @param data
     *            LinkedHashMap with the key being the name
     *            of the tag to be replaced and the value
     *            the replacement.
     */
    @Deprecated
    public void loadView(String view_name, LinkedHashMap<String, String> data) throws Exception {
        if (debug) {
            request.setAttribute("debugLog", buildDebugOutput());
        }        
        request.getRequestDispatcher("/views/" + view_name + ".jsp").forward(request, response);
    }

    /**
     * Displays the default error page
     * @param e The exception that caused the error
     * @param response Servlet response to write to
     * @param cont Servlet context to load the template
     */
    public static void loadErrorPage(Exception e, HttpServletResponse response, ServletContext cont, HttpErrors er) {
        try {

            String template = "";
            String errorTemplate = "";

            switch (er) {
                case E404:
                    response.setStatus(404);
                    errorTemplate = "404.html";
                    break;
                case E500:
                    response.setStatus(500);
                    errorTemplate = "500.html";
                    break;
                case CUSTOM:
                    errorTemplate = "500.html";
                    response.setStatus(500);
                    break;
            }

            template = Jmvc.readWithStringBuilder(cont.getRealPath("/") + "error_pages" + File.separator + errorTemplate);
            response.setContentType("text/html");
            response.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
            PrintWriter out = response.getWriter();
            if (Settings.get("DEBUG").equals("TRUE")) {
                String message = e.toString();
                String html = "<h1>Something went wrong: </h1>";
                html += "<pre>" + message + "</pre>";
                StackTraceElement traceElements[] = e.getStackTrace();
                html += "<h2>Stacktrace: </h2>";
                html += "<pre>";
                for (StackTraceElement elem : traceElements) {
                    html += elem.getClassName() + ": " + elem.getMethodName() + " on line:" + elem.getLineNumber() + "\n";
                }
                html += "</pre>";

                for (Throwable c = e.getCause(); c != null; c = c.getCause()) {
                    html += "<h2>Exception caused by: </h2>";
                    html += "<pre>" + c.getMessage() + "</pre>";
                    html += "<pre>";
                    StackTraceElement[] causeTraceElements = c.getStackTrace();
                    for (StackTraceElement elem : causeTraceElements) {
                        html += elem.getClassName() + ": " + elem.getMethodName() + " on line:" + elem.getLineNumber() + "\n";
                    }
                    html += "</pre>";
                }
                if (dbDebug != null) {
                    html += "<div style='margin:10px auto;padding:5px;width:50%;font:10px/1.2em Arial; background-color:#eee;border:1px dotted #d00;'>";
                    html += "<div style='background-color:#000;color:#fff'> Database queries ran: " + "</div>";
                    html += dbDebug.size();
                    html += "</div>";
                    for (String s : dbDebug) {
                        html += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + "</div>";
                    }
                    html += "</div>";
                }
                template = template.replace("<!--%error%-->", html);
            }
            out.println(template);
            out.flush();
        } catch (Exception exc) {
            logError(exc);
        }
    }

    @SuppressWarnings("unused")
    private static LinkedHashMap<String, String> getParamsMap(String path) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        ArrayList<String> uriParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        for (int i = 0; i < uriParts.size(); i++) {
            if (i + 1 < uriParts.size()) {
                params.put(uriParts.get(i), uriParts.get(i + 1));
            } else {
                params.put(uriParts.get(i), "");
            }
        }
        return params;
    }

    @SuppressWarnings("unused")
    private static ArrayList<String> getParamsArray(String path) {
        ArrayList<String> params = new ArrayList<String>(Arrays.asList(path.split("/")));
        return params;
    }

    /**
     * Reads the file from disk and returns the content as a
     * string. Used to load templates.
     * 
     * @param fileName
     *            The name of the file to be read.
     * @return Returns the contents of the file.
     * @throws java.io.IOException
     */
    static String readWithStringBuilder(String fileName) throws IOException {
        Reader in = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result.append(line).append("\n");
        }
        br.close();
        in.close();
        return result.toString();
    }

    /**
     * Loads a model from J2mvc.
     * 
     * @param <T> 
     * @param modelName
     *            The name of the model you need to load
     * @return the model
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    @SuppressWarnings("unchecked")
    public static <T extends Model> T loadSystemModel(String modelName) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName("gr.dsigned.jmvc.models." + modelName);
        T m = c.newInstance();
        return m;
    }

    /**
     * Loads a model from SYSTEM_PACKAGE (the application package).
     * See settings.properties for setting the SYSTEM_PACKAGE.
     * @param <T> 
     * @param modelName
     *            The name of the model you need to load
     * @return the model
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    @SuppressWarnings("unchecked")
    public static <T extends Model> T loadModel(String modelName) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName(Settings.get("SYSTEM_PACKAGE") + ".models." + modelName);
        T m = c.newInstance();
        return m;
    }

    /**
     * Loads the Renderer from J2mvc.
     * 
     * @param <T> 
     * @param rendererName The name of the Renderer you need to load
     * @return the Renderer
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Renderer> T loadSystemRenderer(String rendererName) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName("gr.dsigned.jmvc.renderers." + rendererName);
        T m = c.newInstance();
        return m;
    }

    /**
     * Loads the Renderer with the name passed in.
     * 
     * @param <T> 
     * @param rendererName The name of the Renderer you need to load
     * @return the Renderer
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Renderer> T loadRenderer(String rendererName) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName(Settings.get("SYSTEM_PACKAGE") + ".renderers." + rendererName);
        T m = c.newInstance();
        return m;
    }

    /**
     * Loads the requested library.
     * 
     * @param <T> 
     * @param libraryName The name of the library
     * @return The library object you named.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Library> T loadLibrary(String libraryName) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<T> c = (Class<T>) Class.forName("gr.dsigned.jmvc.libraries." + libraryName);
        T m = c.newInstance();
        return m;
    }

    public static void logInfo(String msg) {
        infoLogger.info(msg);
    }

    public static void logDebug(String msg) {
        debugLogger.debug(msg);
    }

    public static void logError(String msg) {
        errorLogger.error(msg);
    }

    public static void logError(Exception e) {
        errorLogger.error(e.getMessage(), e);
    }

    /**
     * Used by db to log query info. 
     * CAUTION: This is not thread safe. Only use it in development.
     * @param s
     */
    public static void dbDebug(String s) {
        if (dbDebug == null) {
            dbDebug = new ArrayList<String>();
        }
        dbDebug.add(s);
    }

    /**
     * Append debug info
     * @param debug
     */
    public void setDebug(boolean debugMode) {
        debug = debugMode;
    }

    private String buildDebugOutput() throws Exception {
        long now = System.nanoTime();
        long then = (Long) request.getAttribute("begin_time");
        String debugInfo = "<div style='margin:10px auto;padding:5px;width:50%;font:11px/1.2em Arial; background-color:#eee;border:1px dotted #d00;'>";
        debugInfo += "<div><b>Request processed in " + ((double) (now - then) / 1000000) + " milliseconds.</b></div>";
        debugInfo += "<div style='background-color:#000;color:#fff'><b>Database queries ran: " + dbDebug.size() + "</b></div>";
//        for (String s : dbDebug) {
//            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + "</div>";
//        }
        if (this.session != null) {
            debugInfo += (session.permHM.size() > 0) ? "<div style='background-color:#000;color:#fff'><b>Permanent session data</b></div>" : "";
            for (String s : session.permHM.keySet()) {
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + ": " + session.permHM.get(s) + "</div>";
            }
            debugInfo += (session.tempHM.size() > 0) ? "<div style='background-color:#000;color:#fff'><b>Flash session data</b></div>" : "";
            for (String s : session.tempHM.keySet()) {
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + ": " + session.tempHM.get(s) + "</div>";
            }
        }
        if (db.getCache() != null) {
            NumberFormat numForm = NumberFormat.getNumberInstance(Locale.ENGLISH);
            numForm.setGroupingUsed(false);
            numForm.setMinimumFractionDigits(0);
            numForm.setMaximumFractionDigits(4);
            long totalQueries = db.getCache().getStatistics().getCacheHits() + db.getCache().getStatistics().getCacheMisses();
            double percentage = ((double) db.getCache().getStatistics().getCacheHits() / totalQueries) * 100;
            debugInfo += "<div style='background-color:#000;color:#fff'><b>DB CACHE: " + db.getCache().getName() + "</b></div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache objects: " + db.getCache().getStatistics().getObjectCount() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache hits: " + db.getCache().getStatistics().getCacheHits() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache misses: " + db.getCache().getStatistics().getCacheMisses() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache in-memory hits: " + db.getCache().getStatistics().getInMemoryHits() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache disk hits: " + db.getCache().getStatistics().getOnDiskHits() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>In memory size: " + db.getCache().calculateInMemorySize() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Memory store size: " + db.getCache().getMemoryStoreSize() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache eficiency: " + numForm.format(percentage) + "%</div>";
            debugInfo += "<div style='background-color:#000;color:#fff'>Cached queries</div>";
            debugInfo += (db.getCache().getKeys().size() > 1) ? "<div style='height:200px;overflow:auto;'>" : "<div>";
            for (Object o : db.getCache().getKeys()) {
                String s = (String) o + ", ";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + "</div>";
            }
            debugInfo += "</div>";
        }
        debugInfo += "</div>";
        return debugInfo;
    }
}
