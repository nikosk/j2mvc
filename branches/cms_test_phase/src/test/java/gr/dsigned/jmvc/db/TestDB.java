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
package gr.dsigned.jmvc.db;

import gr.dsigned.jmvc.framework.Jmvc;
import junit.framework.*;

/**
 *
 * @author Alkis Kafkis <a.kafkis@phiresoft.com>
 */
public class TestDB extends TestCase {

    public void testSites() {
        int answer = 2;
        assertEquals((1 + 1), answer);
    }

    public void testCreateDropTable() throws Exception {
        Jmvc framework = Jmvc.getInstance();
        
        if (!framework.db.tableExists("tttt")) {
            framework.db.create("tttt");
        }
        assertTrue(framework.db.tableExists("tttt"));
        framework.db.dropTable("tttt");
        assertFalse(framework.db.tableExists("tttt"));
    }
}