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

import gr.dsigned.jmvc.Bean;
import gr.dsigned.jmvc.framework.Jmvc;
import java.util.ArrayList;
import junit.framework.*;

/**
 *
 * @author Alkis Kafkis <a.kafkis@phiresoft.com>
 */
public class TestSites extends TestCase
{
    
    public void testSites()
    {
        int answer = 2;
        assertEquals((1 + 1), answer);
    }

    public void testSimpleTest() throws Exception
    {
        Site siteModel = Jmvc.loadModel("Site");

        ArrayList<Bean> siteFetched = siteModel.getSites();
        int initialSitesCounter = siteFetched.size();
        Bean site = siteModel.insertSite("112");
        assertEquals(initialSitesCounter + 1, siteModel.getSites().size());
        siteModel.delete(site.get("id"));
        assertEquals(initialSitesCounter, siteModel.getSites().size());
    }

    public void testBeanInsertion() throws Exception
    {
        Site siteModel = Jmvc.loadModel("Site");

        int initialSitesCounter = siteModel.getSites().size();
        Bean newSite = new Bean();
        newSite.put("label", "Site Label");
        newSite.put("status", "45");
        newSite = siteModel.insert(newSite);
        assertEquals(initialSitesCounter + 1, siteModel.getSites().size());
        
        siteModel.delete(newSite.get("id"));
        assertEquals(initialSitesCounter, siteModel.getSites().size());
    }
    
}