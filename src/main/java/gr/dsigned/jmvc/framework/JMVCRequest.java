/*
 *  JMVCRequest.java
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

import gr.dsigned.jmvc.framework.Jmvc;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import java.util.Map;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.FilenameUtils;

/**
 * This class will at some point contain methods to sanitize 
 * data coming in and out of the application. 
 * @author Nikosk <nikosk@dsigned.gr>
 * @author VChrys <vchrys@gmail.com>
 */
public class JMVCRequest extends HttpServletRequestWrapper implements HttpServletRequest {

    private LinkedHashMap<String, String> uploadedFilePaths;

    public JMVCRequest(HttpServletRequest request) {
        super(request);
        uploadedFilePaths = new LinkedHashMap<String, String>();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) { // Multipart means there is a file being uploaded
            doUpload();
        }
    }

    protected final void doUpload() {
        // Multipart means there is a file being uploaded
        try {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(this);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                String name = item.getFieldName();
                InputStream stream = item.openStream();
                if (!item.isFormField()) {
                    if (item.getName() != null && item.getName().length() != 0) {
                        File tmpFile = File.createTempFile(FilenameUtils.getBaseName(item.getName()), FilenameUtils.getExtension(item.getName()));
                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        int c;
                        while ((c = stream.read()) != -1) {
                            fos.write(c);
                        }
                        uploadedFilePaths.put(name, tmpFile.getAbsolutePath());
                        fos.close();
                        stream.close();
                    }
                }
            }
        } catch (Exception ex) {
            Jmvc.logError(ex);
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
        String path = this.getRequestURI();
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        while (pathParts.contains("")) {
            pathParts.remove("");
        }
        if (pathParts.size() == 0) {
            return null;
        } else if (pathParts.size() > 0 && pathParts.size() > index) {
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
        String path = this.getRequestURI();
        ArrayList<String> pathParts = new ArrayList<String>(Arrays.asList(path.split("/")));
        while (pathParts.contains("")) {
            pathParts.remove("");
        }
        String value = "";
        for (int i = 0; i < pathParts.size() - 1; i += 2) {
            if (pathParts.get(i).equals(key)) {
                value = (i + 1 < pathParts.size()) ? pathParts.get(i + 1) : "";
            }
        }
        return value;
    }

    /**
     * Returns the value that matches the parameter name. Return empty string if the
     * parameter does not exist.
     * 
     * @param paramName
     *            The parameter name
     * @return The parameter value
     */
    public String post(String paramName) {
        return (this.getParameter(paramName) != null) ? this.getParameter(paramName) : "";
    }

    public Map getPostData() {
        return this.getParameterMap();
    }

    public File getUploadedFile(String paramName) {
        File uploadedFile = null;
        if (uploadedFilePaths.get(paramName) != null) {
            uploadedFile = new File(uploadedFilePaths.get(paramName));
        }
        return uploadedFile;
    }

    public boolean isPost() {
        return this.getMethod().equalsIgnoreCase("post");
    }

    public boolean isGet() {
        return this.getMethod().equalsIgnoreCase("get");
    }
}
