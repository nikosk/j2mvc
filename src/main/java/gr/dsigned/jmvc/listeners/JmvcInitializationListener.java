/*
 *  JmvcInitializationListener.java
 *
 *  Copyright (C) Mar 15, 2009 Nikosk <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.listeners;

import gr.dsigned.jmvc.Settings;
import gr.dsigned.jmvc.framework.Jmvc;
import gr.dsigned.jmvc.routing.Router;
import gr.dsigned.jmvc.types.Hmap;
import java.io.File;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class JmvcInitializationListener implements ServletContextListener {

    private static final String PERSISTENCE_UNIT = "persistence";
    private static EntityManagerFactory emf;

    /**
     * In the init method we find all the controller classes and use reflection
     * to create a map of Class objects. This happens to avoid using reflection
     * on every request. Instead for every request we only need to lookup the class name
     * and instantiate an object from that.
     * The same applies for Method objects.
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        String rootPath = ctx.getRealPath("WEB-INF");
        Router r = new Router();
        String controllerPath = rootPath + File.separator + "classes" + File.separator + Settings.get("SYSTEM_PACKAGE").replace(".", File.separator) + File.separator + "controllers";
        File controllerDirectory = new File(controllerPath);
        if (controllerDirectory.isDirectory()) {
            for (File f : controllerDirectory.listFiles()) {
                if (f.isFile()) {
                    try {
                        String className = f.getName().substring(0, f.getName().lastIndexOf("."));
                        Class theClass = Class.forName(Settings.get("SYSTEM_PACKAGE") + ".controllers." + className);
                        r.addController(theClass);
                    } catch (ClassNotFoundException ex) {
                        Jmvc.logError(ex);
                    }
                }
            }
        }
        Hmap templates = new Hmap();
        File templateDirectory = new File(ctx.getRealPath("") + File.separator + "views");
        if (templateDirectory.isDirectory()) {
            for (File f : templateDirectory.listFiles()) {
                if (f.isFile() && FilenameUtils.isExtension(f.getName(), "jsp")) {
                    templates.put(FilenameUtils.getBaseName(f.getName()), FilenameUtils.getBaseName(f.getName()));
                }
            }
        }
        ctx.setAttribute("router", r);
        ctx.setAttribute("templates", templates);
        emf = createEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        emf.close();
    }

    /**
     *
     * @return a new {@link EntityManager}
     *
     * @see EntityManagerFactory#createEntityManager()
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static EntityManagerFactory entityManagerFactory() {
        return emf;
    }

    /*
     * @return An {@link EntityManagerFactory} to create an {@link EntityManager}
     *
     * @see {@link EntityManagerFactory#createEntityManager()};
     */
    private EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }
}
