/*
 *  Input.java
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
package gr.dsigned.jmvc.libraries;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Library;

import gr.dsigned.jmvc.types.Hmap;
import java.io.File;
import java.io.FileOutputStream;
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
    private Hmap postParams = new Hmap();
    private Hmap getParams = new Hmap();

    public Input(HttpServletRequest req, ServletContext cont) throws Exception {
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
    @SuppressWarnings("unchecked")
    public void setRequest(HttpServletRequest req) throws Exception {
        if (req != null) { // check for null or tests fail
            this.request = req; // Set the local request
            
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    if (item.isFormField()) {
                        postParams.put(name, Streams.asString(stream));
                    } else {
                        if(item.getName()!=null && item.getName().length()!=0){
                            File tmpDir = new File(context.getRealPath("/")+ "tmp/");
                            
                            String tmpfileName = String.valueOf(new Date().getTime()+ "." +getExtension(item.getName()));
                            File tmpFile = new File(context.getRealPath("/") + "tmp/" + tmpfileName);
                            tmpDir.mkdirs();
                            tmpFile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(tmpFile);
                            int c;
                            while ((c = stream.read()) != -1) {
                                fos.write(c);
                                postParams.put(name, context.getRealPath("/") + "tmp/" + tmpfileName) ;
                            }
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
                                for (int i = 0; i < val.length; i++) { // For each value in
                                        // multi-value param
                                        value += val[i];
                                        value += (i + 1 != val.length) ? "," : ""; // Add it to the
                                        // string as
                                        // comma
                                        // separated values
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

    public String segment(int index) throws Exception {
        String path = this.request.getRequestURI();
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        if (pathParts.size() == 0) {
            throw new ArrayIndexOutOfBoundsException("Segment not found");
        } else if (pathParts.size() > 0 && pathParts.size() > index) {
            return pathParts.get(index);
        } else {
            return "";
        }
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
    public Hmap getPostData(){
        return this.postParams;
    }
    public File upload(String paramName) {
        return new File("/");
    }

    public boolean isPost(){
        return this.request.getMethod().equalsIgnoreCase("post");
    }
    
    public boolean isGet(){
        return this.request.getMethod().equalsIgnoreCase("get");
    }
    /**
     * Given a URI it returns the controller part and the method part. Used by
     * Adapter to call the appropriate Controller and method.
     * 
     * @param path
     *            HttpServletRequest.getRequestURI()
     * @return
     */
    public static LinkedHashMap<String, String> getController(String path) {
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        pathParts.remove("");
        if (pathParts.size() == 0) {
            params.put("controller", Settings.get("DEFAULT_CONTROLLER"));
            params.put("method", "index");
        } else {
            if (pathParts.size() == 1) {
                params.put("controller", pathParts.get(0));
                params.put("method", "index");
            } else {
                params.put("controller", pathParts.get(0));
                params.put("method", pathParts.get(1));
            }
        }
        return params;
    }
}
