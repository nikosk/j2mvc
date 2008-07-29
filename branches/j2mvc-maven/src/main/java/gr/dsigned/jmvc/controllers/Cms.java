/*
 *  Cms.java
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

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Cms extends Controller{
    
    public Cms() throws Exception{
        
    }
    
    public void index(){
      // Which URI did they hit ?
      // Read the configuration for this URI 
      // If conf is not set try and execute a component
      // If conf is set execute conf settings
      // $.loadView(conf.template, data);
    }
}
