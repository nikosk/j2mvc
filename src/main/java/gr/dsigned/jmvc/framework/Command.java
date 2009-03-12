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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Command {

    Router r;
    String controllerName;
    HttpServletRequest request;
    HttpServletResponse response;

    public Command(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        r = new Router(request);
        controllerName = r.getControllerName();
    }

    public Page execute() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        Class<Controller> controllerClass = (Class<Controller>) Class.forName("gr.dsigned.jmvc.controllers." + controllerName);
        Constructor controllerConstructor = controllerClass.getConstructor(new Class[]{HttpServletRequest.class, HttpServletResponse.class});
        Controller controllerInstance = (Controller) controllerConstructor.newInstance(request, response);
        Method controllerAction = controllerClass.getMethod(r.getControllerName(), new Class[0]);
        if (controllerAction.getModifiers() != java.lang.reflect.Modifier.PRIVATE) { // We only call public methods
            controllerAction.invoke(controllerInstance, new Object[0]);
        }
        return new Page();
    }
}
