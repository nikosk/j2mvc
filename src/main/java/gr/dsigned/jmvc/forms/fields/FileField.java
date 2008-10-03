/*
 *  FileField.java
 *
 *  Copyright (C) 2008 Vas Chryssikou <vchrys@gmail.com>
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
package gr.dsigned.jmvc.forms.fields;

import gr.dsigned.jmvc.forms.fields.Field.Rule;
import gr.dsigned.jmvc.types.Tuple2;
import java.io.File;
import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * @author Vas Chryssikou <vchrys@gmail.com>
 */
public class FileField extends Field {

    String template = "<input type='file' name='%1$s' id='%2$s' value='%3$s'/>%n";
    private boolean disabled = false;
    File f = null;

    /**
     * Constructs a file field
     * 
     * @param label
     * @param fieldName
     * @param uploadFile (a file containing the path taken the value from the form's post ie: new File($.input.post("file_"+fieldname)))
     * @param rules
     */
    public FileField(String labelName, String fieldName, File uploadFile, Tuple2<Rule, String>... rules) {
        super(labelName, "file_" + fieldName, ""+uploadFile, rules);
        this.f = uploadFile;
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='file' name='%1$s' id='%2$s' value='%3$s' %4$s />%n", getFieldName(), "id_" + getFieldName(), getValue(), isDisabled(), getErrors());
    }

    @Override
    public boolean validates() {
        validates = true;
        for (Tuple2<Rule, String> r : rules) {
            switch (r._1) {
                case REQUIRED:
                    if (!f.exists()) {
                        addError(getLabelName() + " is required.");
                        validates = false;
                    }
                    break;
                case ALLOWED_EXTENSIONS:
                    boolean found = false;
                    String[] allowedExtensions = r._2.split("\\|");
                    String fileExtension = getExtension(f.getAbsolutePath());
                    for (String ext : allowedExtensions) {
                        if (ext.trim().equalsIgnoreCase(fileExtension)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        addError(getLabelName() + " is not of the allowed types " + r._2.replace("|", ", ") + ".");
                        validates = false;
                    }
                    break;
                case MAX_FILE_SIZE:
                    long allowedSize = Integer.parseInt(r._2);
                    long fileSize = f.length();
                    if(allowedSize < fileSize){
                        addError(getLabelName() + " exceeds the max file size: "+r._2+".");
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }
    
    public String isDisabled() {
        String out = "" ;
        if(disabled){
            out = " disabled " ;
        }else{
            out = "" ;
        }
        return out;
    }

    public void setDisabled() {
        this.disabled = true;
    }
    
    @Override
    public String getErrors() {
        String out = "";
        for (String s : errors) {
            out +=  s+"," ;
        }
        return out;
    }
}
