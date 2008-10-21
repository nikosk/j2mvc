/*
 *  QuerySet.java
 * 
 *  Copyright (C) 2008 Nikos Kastamoulas <nikosk@dsigned.gr>
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
package gr.dsigned.jmvc.controllers;

import gr.dsigned.jmvc.framework.Controller;
import gr.dsigned.jmvc.libraries.PageData;
import static gr.dsigned.jmvc.framework.Renderer.*;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Home extends Controller {

    public Home() throws Exception {
    }
    
    public void index() throws Exception{
        $.request.setAttribute("CACHE_PAGE", "TRUE");
        PageData data = new PageData();
        data.append("content", div(h1("jmvc web framework")));        
        $.loadView("index", data);
    }
}
