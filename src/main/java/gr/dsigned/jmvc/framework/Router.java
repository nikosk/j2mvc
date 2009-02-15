/*
 *  Router.java
 *
 *  Copyright (C) Feb 4, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Router {

    private static Router instance;
    private static final Properties properties = new Properties();
    private File file;
    private Pattern extractControllerMethodPattern = Pattern.compile(".*\\((\\d)\\).*");

    private Router() throws URISyntaxException, IOException {
        URL res = Router.class.getResource("routes.xml");
        URI uri = new URI(res.toString());
        file = new File(uri);
        properties.loadFromXML(new FileInputStream(file));
        
    }

    public static Router getInstance() throws URISyntaxException, IOException {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public String getControllerName(String requestPath) {
        String out = null;
        for(Object o : properties.keySet()){
            String str = o.toString();
            if(extractControllerMethodPattern.matcher(str).matches()){
                // /?[^/]*/([^/]+).*
                out = "";
                Pattern p = Pattern.compile("/?[^/]*/([^/]+).*");
                Matcher m = p.matcher(str);
                m.find();
                if(m.group(1) != null & !m.group(1).isEmpty()){
                    out = m.group(1);
                }
            }
            Pattern p = Pattern.compile(o.toString(), Pattern.CASE_INSENSITIVE);
        }
        return out;
    }
}
