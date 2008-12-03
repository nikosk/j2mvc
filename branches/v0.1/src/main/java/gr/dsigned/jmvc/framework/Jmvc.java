/*
 *  Jmvc.java
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
package gr.dsigned.jmvc.framework;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.db.DB;
import gr.dsigned.jmvc.db.Model;
import gr.dsigned.jmvc.exceptions.CustomHttpException.HttpErrors;
import gr.dsigned.jmvc.libraries.Input;
import gr.dsigned.jmvc.libraries.PageData;
import gr.dsigned.jmvc.libraries.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

/**
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Jmvc {

    private static final Logger infoLogger = Logger.getLogger("Info");
    private static final Logger debugLogger = Logger.getLogger("Debug");
    private static final Logger errorLogger = Logger.getLogger("Error");
    private static boolean debug = Settings.get("DEBUG").equals("TRUE");
    private static final boolean cacheEnabled = Settings.get("CACHE_PAGES").equals("TRUE");
    private static ArrayList<String> dbDebug;
    public HttpServletRequest request;
    public ServletContext context;
    public static LinkedHashMap<String, String> parsedTemplates = new LinkedHashMap<String, String>();
    public HttpServletResponse response;
    public DB db;
    public Input input;
    public Session session;
    public PageData pageData;
    private static CacheManager singletonManager;
    private static Cache cache;

    public Jmvc() throws Exception {
        init();
    }

    private void init() throws Exception {
        input = new Input(null, null);
        if (!Settings.get("DATABASE_TYPE").equalsIgnoreCase("none")) {
            if (Settings.get("DATABASE_TYPE").equalsIgnoreCase("mysql")) {
                db = gr.dsigned.jmvc.db.MysqlDB.getInstance();
            }
        }
        pageData = new PageData();
        if (cacheEnabled) {
            initCache();
        }
        if(debug){
            dbDebug = new ArrayList<String>();
        }
    }

    private static void initCache() {
        if (cache == null) {
            singletonManager = CacheManager.create();
            cache = new Cache("pageCache", 10, true, false, 3600, 3600);
            singletonManager.addCache(cache);
            cache = singletonManager.getCache("pageCache");
        }
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
    public void setEnvironment(HttpServletRequest req, HttpServletResponse resp, ServletContext cont) throws Exception {
        request = req;
        response = resp;
        context = cont;
        input = new Input(request, context);
        if (Settings.AutoLoad.SESSION.loadIt()) {
            session = new Session(req);
        }
        
    }

    /**
     * Loads a template and replaces tags with the variables
     * stored in the HashMap. The key is used to find the
     * tag in the template.
     * 
     * @param view_name
     *            The name of the template (the path and
     *            extension is added automatically)
     * @param data
     *            LinkedHashMap with the key being the name
     *            of the tag to be replaced and the value
     *            the replacement.
     */
    public void loadView(String view_name, LinkedHashMap<String, String> data) throws IOException, Exception {
        String template = "";
        if (parsedTemplates.containsKey(view_name)) {
            template = parsedTemplates.get(view_name);
        } else {
            template = Jmvc.readWithStringBuilder(context.getRealPath("/") + "/views/" + view_name + ".html");
            template = Parser.parse(template);
            parsedTemplates.put(view_name, template);
        }
        if (data != null) {
            boolean dollarFound = false;
            for (String key : data.keySet()) {
                String value = data.get(key);
                dollarFound = value.indexOf("$") != -1 ? true : false;
                if (dollarFound) {
                    value = value.replaceAll("\\$", "!d!");
                }
                template = template.replaceAll("<% ?" + key + " ?%>", value);
                if (dollarFound) {//replace it back
                    template = template.replaceAll("\\!d!", "\\$");
                }
            }
        }
        if (cacheEnabled && request.getAttribute("CACHE_PAGE") != null) {
            String cacheKey = request.getRequestURI();
            Element e = getCache().get(cacheKey);
            if (e == null) {
                Element pageCacheElement = new Element(cacheKey, template);
                getCache().put(pageCacheElement);
            }
        }
        response.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(template);
        if (debug) {
            out.println(buildDebugOutput());
        }
        out.flush();
        out.close();
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
            switch (er) {
                case E404:
                    response.setStatus(404);
                    break;
                case E500:
                    response.setStatus(500);
                    break;
                case CUSTOM:
                    response.setStatus(500);
                    break;
            }
            template = Jmvc.readWithStringBuilder(cont.getRealPath("/") + "error_pages" + File.separator + "404.html");
            PrintWriter out = response.getWriter();
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
            Throwable c = e.getCause();
            if (c != null) {
                String cause = e.getCause().toString();
                html += "<h2>Exception caused by: </h2>";
                html += "<pre>" + cause + "</pre>";
                html += "<pre>";
                StackTraceElement[] causeTraceElements = e.getCause().getStackTrace();
                for (StackTraceElement elem : causeTraceElements) {
                    html += elem.getClassName() + ": " + elem.getMethodName() + " on line:" + elem.getLineNumber() + "\n";
                }
                html += "</pre>";
            }
            template = template.replace("<% error %>", html);
            out.println(template);
            if (debug) {
                out.println(getDebugInfo());
            }
            out.flush();
        } catch (Exception exc) {
            logError("[Jmvc:loadErrorPage] " + exc.getMessage());
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

    /**
     * Used by db to log query info. 
     * CAUTION: This is not thread safe. Only use it in development.
     * @param s
     */
    public static void dbDebug(String s) {
        dbDebug.add(s);
    }

    /**
     * Append debug info
     * @param debug
     */
    public void debug(boolean debug) {
        Jmvc.debug = debug;
    }

    private String buildDebugOutput() {
        long now = System.nanoTime();
        long then = (Long) request.getAttribute("begin_time");
        String debugInfo = "<div style='margin:10px auto;padding:5px;width:50%;font:11px/1.2em Arial; background-color:#eee;border:1px dotted #d00;'>";
        debugInfo += "<div><b>Request processed in " + ((double) (now - then) / 1000000) + " milliseconds.</b></div>";
        debugInfo += "<div style='background-color:#000;color:#fff'><b>Database queries ran: " + dbDebug.size() + "</b></div>";
        for (String s : dbDebug) {
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + "</div>";
        }
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
        if (this.cache != null) {
            debugInfo += "<div style='background-color:#000;color:#fff'><b>Permanent session data</b></div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Total objects: " + cache.getStatistics().getObjectCount() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache hits: " + cache.getStatistics().getCacheHits() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache misses: " + cache.getStatistics().getCacheMisses() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Average get time: " + cache.getStatistics().getAverageGetTime() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Memory size: " + cache.calculateInMemorySize() + "</div>";
            debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Average time: " + cache.getAverageGetTime() + "</div>";
        }
        debugInfo += "</div>";
        return debugInfo;
    }

    public static String getDebugInfo() {
        String debugInfo = "";
        if (debug) {
            debugInfo = "<div style='margin:10px auto;padding:5px;width:50%;font:10px/1.2em Arial; background-color:#eee;border:1px dotted #d00;'>";
            debugInfo += "<div style='background-color:#000;color:#fff'> Database queries ran: " + dbDebug.size() + "</div>";
            for (String s : dbDebug) {
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>" + s + "</div>";
            }
            if (cache != null) {
                debugInfo += "<div style='background-color:#000;color:#fff'><b>Permanent session data</b></div>";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Total objects: " + cache.getStatistics().getObjectCount() + "</div>";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache hits: " + cache.getStatistics().getCacheHits() + "</div>";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Cache misses: " + cache.getStatistics().getCacheMisses() + "</div>";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Average get time: " + cache.getStatistics().getAverageGetTime() + "</div>";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Memory size: " + cache.calculateInMemorySize() + "</div>";
                debugInfo += "<div style='margin:5px 0px;padding:10px;border:1px dotted #999;'>Average time: " + cache.getAverageGetTime() + "</div>";
            }
            debugInfo += "</div>";
        }
        return debugInfo;
    }

    public static Cache getCache() {
        initCache();
        return cache;
    }
}
