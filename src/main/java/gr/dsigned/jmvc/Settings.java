/*
 *  Settings.java
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
package gr.dsigned.jmvc;

import gr.dsigned.jmvc.framework.Jmvc;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Settings {

    private static long fileTimeStamp = 0;
    private static long nextCheck = 0;
    private static long recheckInterval = 60000;
    private static Properties properties = null;
    private static File file;
    private static int count = 0;

    Settings() {
        
    }

    public static String get(String property) {
        String value = "";
        if (file == null) {
            init();
        }
        try {
            boolean checkForChanges = false;
            if (nextCheck - new Date().getTime() < 0) {
                checkForChanges = file.lastModified() > fileTimeStamp;                
                nextCheck = new Date().getTime() + recheckInterval;
                count = count + 1;
            }
            if (properties == null || checkForChanges) {
                fileTimeStamp = file.lastModified();
                properties = new Properties();
                
                properties.clear();
                properties.load(new FileInputStream(file));
                value = properties.getProperty(property);                
            } else {
                properties = new Properties();
                properties.load(new File(new FileInputStream(new URI(Settings.class.getResource("settings.properties").toString()))));
                value = properties.getProperty(property);
            }
        } catch (Exception e) {
            Jmvc.logError(e.toString());
        }
        return value;
    }

    
}
