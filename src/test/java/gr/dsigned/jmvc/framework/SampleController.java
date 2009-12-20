/*
 *  SampleController.java
 *
 *  Copyright (C) Mar 12, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import gr.dsigned.jmvc.interfaces.Controller;
import gr.dsigned.jmvc.annotations.ControllerURLAlias;
import gr.dsigned.jmvc.annotations.MethodURLAlias;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
@ControllerURLAlias("controllertest")
public class SampleController implements Controller {

    
    public SampleController() throws Exception {
    }

    @MethodURLAlias("methodtest")
    public void index() throws Exception {
    }
}
