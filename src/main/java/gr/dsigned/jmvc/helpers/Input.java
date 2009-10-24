/*
 *  Input.java
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

import gr.dsigned.jmvc.types.Hmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;

import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.util.*;
import javax.servlet.http.HttpServletRequest;
import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * This class will at some point contain methods to sanitize 
 * data coming in and out of the application. 
 * @author Nikosk <nikosk@dsigned.gr>
 * @author VChrys <vchrys@gmail.com>
 */
public class Input extends Library {

    private HttpServletRequest request;
    private ServletContext context;
    private Hmap postParams;
    private Hmap getParams;

    public Input(HttpServletRequest req, ServletContext cont) throws IOException, FileUploadException{
        postParams = new Hmap();
        getParams = new Hmap();
        this.context = cont;
        this.setRequest(req);
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Takes the current request object and retrieves any data that might be
     * useful in a Controller. This is the place where we sanitize any data
     * coming in.
     * 
     * @param req
     *            The request object.
     */
    public void setRequest(HttpServletRequest req) throws IOException, FileUploadException{
        if (req != null) { // check for null or tests fail
            this.request = req; // Set the local request

            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) { // Multipart means there is a file being uploaded
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    if (item.isFormField()) {
                        postParams.put(name, Streams.asString(stream, "UTF-8"));
                    } else {
                        if (item.getName() != null && item.getName().length() != 0) {
                            File tmpDir = new File(context.getRealPath("/") + "tmp/");

                            String tmpfileName = String.valueOf(new Date().getTime() + "." + getExtension(item.getName()));
                            File tmpFile = new File(context.getRealPath("/") + "tmp/" + tmpfileName);
                            tmpDir.mkdirs();
                            tmpFile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(tmpFile);
                            int c;
                            while ((c = stream.read()) != -1) {
                                fos.write(c);
                            }
                            postParams.put(name, context.getRealPath("/") + "tmp/" + tmpfileName);
                            fos.close();
                            stream.close();
                        }
                    }
                }
            } else {
                Map<String, String[]> lhm = req.getParameterMap(); // Get the params
                for (String key : lhm.keySet()) { // Get the name of each param
                    String[] val = lhm.get(key); // For each name retrieve the value
                    String value = ""; // init the string that will receive our param
                    // value
                    if (val.length > 1) { // If multi-value param
                        for (int i = 0; i < val.length; i++) { // For each value in multi-value param
                            value += val[i];
                            value += (i + 1 != val.length) ? "," : ""; // Add it to the string as comma separated values
                        }
                    } else {
                        value = val[0];
                    }
                    if (req.getMethod().equalsIgnoreCase("post")) { // If params from post
                        postParams.put(key, value); // add them to the post array
                    } else {
                        getParams.put(key, value); // else add them to the get array
                    }
                }
            }
        }
    }

    /**
     * Returns Nth part of the request URL.
     * e.g.: /0/1/2/3
     * @param index
     * @return the URL part
     * @throws java.lang.Exception
     */
    public String segment(int index) throws Exception {
        String path = this.request.getRequestURI();
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        if (pathParts.size() == 0) {
            return null;
        } else if (pathParts.size() > 0 && pathParts.size() > index) {
            return pathParts.get(index);
        } else {
            return "";
        }
    }
    
    /**
     * Returns Nth part of the path.
     * e.g.: /0/1/2/3
     * @param index
     * @return the URL part
     * @throws java.lang.Exception
     */
    public static String segment(String path, int index){
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        if (pathParts.size() > 0 && pathParts.size() > index) {
            return pathParts.get(index);
        } else {
            return "";
        }
    }

    /**
     * If you use parameters in your URI you can retrieve 
     * a parameter with this method. Warning: If your 
     * URI has an odd number of parts this will return empty string 
     * for the last parameter.
     * 
     * Example:
     * URI : domain.com/controller/method/display/list/perpage/10/sort/descending 
     * <code>
     * $.input.segment("display");
     * </code>
     * should return "list";
     * @param key The key you use in the URL
     * @return 
     * @throws java.lang.Exception
     */
    public String segment(String key) throws Exception {
        String path = this.request.getRequestURI();
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        String value = "";
        for (int i = 0; i < pathParts.size() - 1; i += 2) {
            if (pathParts.get(i).equals(key)) {
                value = (i + 1 < pathParts.size()) ? pathParts.get(i + 1) : "";
            }
        }
        return value;
    }

    /**
     * Returns the value that matches the parameter name. Return null if the
     * parameter does not exist.
     * 
     * @param paramName
     *            The parameter name
     * @return The parameter value
     */
    public String post(String paramName) {
        return (this.postParams.get(paramName) != null) ? this.postParams.get(paramName) : "";
    }

    public Hmap getPostData() {
        return this.postParams;
    }

    public File upload(String paramName) {
        return new File("/");
    }

    public boolean isPost() {
        return this.request.getMethod().equalsIgnoreCase("post");
    }

    public boolean isGet() {
        return this.request.getMethod().equalsIgnoreCase("get");
    }
}
