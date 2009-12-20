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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * 
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Settings {

    private static final Logger logger = Logger.getLogger(Settings.class);
    private static Properties properties = new Properties();

    private static void init() throws FileNotFoundException, IOException {        
        properties.load(Settings.class.getResourceAsStream("settings.properties"));
    }

    public static String get(String property) {
        if (properties.isEmpty()) {
            try {
                init();
            } catch (Exception ex) {
                logger.log(Priority.FATAL, ex.getMessage(), ex);
            } 
        }
        return properties.getProperty(property);
    }

    public static void reload() {
        File file = new File(Settings.class.getResource("settings.properties").toString());
        try {
            properties.load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            logger.log(Priority.FATAL,e.getMessage(), e);
        } catch (IOException e) {
            logger.log(Priority.FATAL, e.getMessage(), e);
        }
    }
}
