/*
 * Copyright (C) 2008 Alkis Kafkis <a.kafkis@phiresoft.com>
 *
 * This module is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version. See http://www.gnu.org/licenses/lgpl.html.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.models;

import gr.dsigned.jmvc.controllers.Sites;
import junit.framework.*;
import gr.dsigned.jmvc.framework.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author Alkis Kafkis <a.kafkis@phiresoft.com>
 */
public class SiteTests extends TestCase
{
    
    public void testSites()
    {
        int answer = 2;
        assertEquals((1 + 1), answer);
    }

    public void testSimpleTest() throws Exception
    {
//        Controller c = new Sites();
//        Site site = c.$.loadModel("Site");
        Jmvc framework = Jmvc.getInstance();        
        Site site = framework.loadModel("Site");

        ArrayList<LinkedHashMap<String, String>> siteFetched = site.getSites();
        int initialSitesCounter = siteFetched.size();
        //site.insertTest();
        siteFetched = site.getSites();
        int finalSitesCounter = siteFetched.size();        
        assertEquals(initialSitesCounter + 1, finalSitesCounter);
    }
    
}