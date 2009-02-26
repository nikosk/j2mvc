/*
 *  Command.java
 *
 *  Copyright (C) Feb 27, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Command {

    private Method action;

    public Command(HttpServletRequest request) {
    }

    public Page execute() {
        return new Page();
    }
}
