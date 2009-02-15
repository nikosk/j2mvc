/*
 *  Logs.java
 *
 *  Copyright (C) Feb 5, 2009 Nikos Kastamoulas <nikosk@dsigned.gr>
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Nikos Kastamoulas <nikosk@dsigned.gr>
 */
public class Logs {

    private static final Logger infoLogger = Logger.getLogger("Info");
    private static final Logger debugLogger = Logger.getLogger("Debug");
    private static final Logger errorLogger = Logger.getLogger("Error");

    public static void logInfo(Object msg) {
        infoLogger.info(msg);
    }

    public static void logDebug(Object msg) {
        debugLogger.debug(msg);
    }

    public static void logError(Exception exception) {
        errorLogger.error(exception.getMessage(), exception);
    }

    public static void logError(String msg) {
        errorLogger.error(msg);
    }
}
