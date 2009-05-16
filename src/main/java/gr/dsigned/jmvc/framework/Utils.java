/*
 *  Utils.java
 * 
 *  Copyright (C) 2008 Nikosk <nikosk@dsigned.gr>
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class Utils {
    
    public static String capitalize(String s) {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    
    /***************************************************************************
    *     DB Utility methods
    ***************************************************************************/
    
    
    /**
     * Given a string it tries to find if the string
     * contains a valid SQL logic operator
     * @param str An SQL String
     * @return true or false
     */
    public static boolean hasOperator(String str) {
        Pattern p = Pattern.compile("[<>=!]|(?i)is null|(?i)is not null");
        Matcher m = p.matcher(str);
        boolean b = m.find();
        return b;
    }

    public static String escape(String str) {
        if (str == null) {
            str = "null";
        }
        if (!isNumeric(str) && !str.equals("null") && !isDate(str) && !str.isEmpty()) {
            str = "'" + str + "'";
        }
        return str;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isDate(String str) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-mm-dd");
        if (str == null) {
            return false;
        }
        try {
            d.parse(str);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
}
