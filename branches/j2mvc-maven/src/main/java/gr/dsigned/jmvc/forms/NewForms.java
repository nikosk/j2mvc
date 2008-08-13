/*
 *  NewForms.java
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
package gr.dsigned.jmvc.forms;

import gr.dsigned.jmvc.forms.fields.Field;
import gr.dsigned.jmvc.types.Bean;
import gr.dsigned.jmvc.framework.Library;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

/**
 *  
 * @author Nikosk <nikosk@dsigned.gr>
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class NewForms extends Library {

    private ArrayList<Field> fields = new ArrayList<Field>();
    private Bean errors = new Bean();

    public String build() {
        return buildAsTable();
    }

    private String buildAsTable(){
        StringBuilder sb = new StringBuilder("<table>");
        for(Field f : fields){
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(f.renderLabel());
            sb.append("</td>");
            sb.append("<td>");
            sb.append(f.renderField());
            sb.append(f.renderErrors());
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
    
    public void setFields(Field... fields) {
        for (Field f : fields) {
            this.fields.add(f); 
        }
    }
    public boolean isValid(HttpServletRequest request){
        boolean valid = true;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request) ;
        if(isMultipart){
            try {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                while (iter.hasNext()) {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    if (item.isFormField()) {
                        for(Field f : fields){
                            if(f.getFieldName().equalsIgnoreCase(name)){
                                f.setValue(Streams.asString(stream));
                            }
                        }
                        //System.out.println("Form field " + name + " with value " + Streams.asString(stream) + " detected.");
                    }
                    else {
                        for(Field f : fields){
                            if(f.getFieldName().equalsIgnoreCase(name)){
                                f.setValue(Streams.asString(stream));
                            }
                        }
                        //System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
                        // Process the input stream
                    }
                }
            }
            catch (FileUploadException ex) {
                Logger.getLogger(NewForms.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(NewForms.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for(Field f : fields){
            if(!f.validates()){
                valid = false;
            }
        }

        return valid;
    }
    
    public Bean addFormValuesToBean(){
        Bean bean = new Bean() ;
        for(Field f : fields){
            bean.put(f.getFieldName(), f.getValue()) ;
        } 
        return bean;
    }
}
