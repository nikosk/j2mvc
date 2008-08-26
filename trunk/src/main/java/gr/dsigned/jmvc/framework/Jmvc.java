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
import gr.dsigned.jmvc.libraries.Input;
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
import org.apache.log4j.Logger;

/**
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Jmvc {

    private static final Logger infoLogger = Logger.getLogger("Info");
    private static final Logger debugLogger = Logger.getLogger("Debug");
    private static final Logger errorLogger = Logger.getLogger("Error");
    public HttpServletRequest request;
    public ServletContext context;
    public static LinkedHashMap<String, String> parsedTemplates = new LinkedHashMap<String, String>();
    public HttpServletResponse response;
    /*
     * These are the default auto-loaded libraries To load
     * others use Jmvc.loadLibrary()
     */
    public DB db;
    public Input input;
    public Session session;

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
    public void loadView(String view_name, LinkedHashMap<String, String> data) throws IOException {
        String template = "";
        if (parsedTemplates.containsKey(view_name)) {
            template = parsedTemplates.get(view_name);
        } else {
            template = Jmvc.readWithStringBuilder(context.getRealPath("/") + "/views/" + view_name + ".html");
            template = Parser.parse(template);
            parsedTemplates.put(view_name, template);
        }
        if (data != null) {
            for (String key : data.keySet()) {
                template = template.replaceAll("<% ?" + key + " ?%>", data.get(key));
            }
        }
        response.setCharacterEncoding(Settings.get("DEFAULT_ENCODING"));
        PrintWriter out = response.getWriter();
        out.println(template);
        out.flush();
    }

    /**
     * Displays the default error page 
     * @param e The exception that caused the error
     * @param response Servlet response to write to
     * @param cont Servlet context to load the template
     */
    public static void loadErrorPage(Exception e, HttpServletResponse response, ServletContext cont) {
        try {
            String template = "";
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
        Class<T> c = (Class<T>) Class.forName(Settings.get("SYSTEM_PACKAGE") +".renderers." + rendererName);
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
}
