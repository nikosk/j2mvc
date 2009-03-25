/*
 *  JcmsApplicationInitListener.java
 *
 *  Copyright (C) Mar 25, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

package gr.dsigned.jcms.listeners;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Jmvc;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.jcr.Repository;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.jackrabbit.core.TransientRepository;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class JcmsApplicationInitListener  implements ServletContextListener {
@Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String rootPath = ctx.getRealPath("WEB-INF");
        String controllerPath = rootPath + File.separator + "classes" + File.separator + Settings.get("SYSTEM_PACKAGE").replace(".", File.separator) + File.separator + "controllers";
        File controllerDirectory = new File(controllerPath);
        LinkedHashMap<String, Class> controllerClasses = new LinkedHashMap<String, Class>();
        if (controllerDirectory.isDirectory()) {
            for (File f : controllerDirectory.listFiles()) {
                if (f.isFile()) {
                    try {
                        String className = f.getName().substring(0, f.getName().lastIndexOf("."));
                        controllerClasses.put(className, Class.forName(Settings.get("SYSTEM_PACKAGE")+".controllers."+className));
                    } catch (ClassNotFoundException ex) {
                        Jmvc.logError(ex);
                    }
                }
            }
        }
        ctx.setAttribute("controllerClasses", controllerClasses);
        try {
            Repository repo = new TransientRepository("\\jcr\\repo\\repository.xml", "\\jcr\\repo");
            ctx.setAttribute("repo", repo);
        } catch (IOException ex) {
            Jmvc.logError(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Jcms shutting down.");
    }
}
