/*
 *  J3mvcAppInitListener
 *
 *  Copyright (C) Jan 31, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class J3mvcAppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {        
        if (arg0.getServletContext().getInitParameter("DBCACHE").equals("TRUE")) {
            Cache dbCache = new Cache("dbCache", 500, false, false, 3600, 3600);
            CacheManager.getInstance().addCache(dbCache);
            dbCache = CacheManager.getInstance().getCache("dbCache");
            arg0.getServletContext().setAttribute("dbCache", dbCache);
        }
        if (arg0.getServletContext().getInitParameter("PAGECACHE").equals("TRUE")) {
            Cache pageCache = new Cache("pageCache", 2, true, false, 3600, 3600);
            pageCache.getCacheConfiguration().setMaxElementsOnDisk(500);
            CacheManager.getInstance().addCache(pageCache);
            pageCache = CacheManager.getInstance().getCache("pageCache");
            arg0.getServletContext().setAttribute("pageCache", pageCache);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        arg0.getServletContext().removeAttribute("dbCache");
        System.out.println(CacheManager.getInstance().getStatus());
        CacheManager.getInstance().shutdown();
        System.out.println(CacheManager.getInstance().getStatus());
    }
}
