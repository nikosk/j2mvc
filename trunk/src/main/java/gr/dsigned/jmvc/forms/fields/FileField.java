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
    File f = null;

    public FileField(String labelName, String fieldName, File uploadFile, Tuple2<Rule, String>... rules) {
        super(labelName, "file_" + fieldName, "", rules);
        this.f = uploadFile;
    }

    @Override
    public String renderField() {
        return String.format("<input class='text' type='file' name='%1$s' id='%2$s' value='%3$s'/>%n", getFieldName(), "id_" + getFieldName(), getValue(), getErrors());
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
                    String[] allowedExtensions = r._2.split("|");
                    String fileExtension = getExtension(f.getAbsolutePath());
                    for (String ext : allowedExtensions) {
                        if (ext.trim().equalsIgnoreCase(fileExtension)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        validates = false;
                    }
                    break;
                case MAX_FILE_SIZE:
                    long allowedSize = Integer.parseInt(r._2);
                    long fileSize = f.length();
                    if(allowedSize < fileSize){
                        validates = false;
                    }
                    break;
            }
        }
        return validates;
    }
}
